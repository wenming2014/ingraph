package ingraph.model.fplan

import ingraph.model.expr._
import ingraph.model.jplan
import ingraph.model.treenodes.{GenericBinaryNode, GenericLeafNode, GenericUnaryNode}
import org.apache.spark.sql.catalyst.expressions.{Attribute, Expression, SortOrder}
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan

trait FNode extends LogicalPlan {
  def jnode: jplan.JNode
  def internalSchema: Seq[ResolvableName]
  override def children: Seq[FNode]
  override def output: Seq[Attribute] = Seq()
}

abstract class LeafFNode extends GenericLeafNode[FNode] with FNode {}
abstract class UnaryFNode extends GenericUnaryNode[FNode] with FNode {
  override def internalSchema: Seq[ResolvableName] = child.internalSchema
}
abstract class BinaryFNode extends GenericBinaryNode[FNode] with FNode {}

trait JoinLike extends BinaryFNode {
  def internalCommon: Seq[ResolvableName] = left.internalSchema.filter(x => right.internalSchema.map(_.resolvedName).contains(x.resolvedName))
  lazy val leftMask:  Seq[Int] = SchemaMapper.schemaToIndices(this, left)
  lazy val rightMask: Seq[Int] = SchemaMapper.schemaToIndices(this, right)
}
trait EquiJoinLike extends JoinLike {
  override def internalSchema: Seq[ResolvableName] =
    left.internalSchema ++ right.internalSchema.filter(x => !left.internalSchema.map(_.resolvedName).contains(x.resolvedName))
}

// leaf nodes
case class GetVertices(requiredProperties: Seq[ResolvableName], jnode: jplan.GetVertices) extends LeafFNode {
  override def internalSchema: Seq[ResolvableName] = jnode.output ++ requiredProperties
}

case class GetEdges(requiredProperties: Seq[ResolvableName], jnode: jplan.GetEdges) extends LeafFNode {
  override def internalSchema: Seq[ResolvableName] = jnode.output ++ requiredProperties
}

case class Dual(jnode: jplan.Dual) extends LeafFNode {
  override def internalSchema = Seq()
}

// unary nodes
case class AllDifferent(jnode: jplan.AllDifferent,
                        child: FNode
                        ) extends UnaryFNode {

}

case class DuplicateElimination(jnode: jplan.DuplicateElimination,
                                child: FNode
                               ) extends UnaryFNode {}

case class Production(jnode: jplan.Production,
                      child: FNode
                     ) extends UnaryFNode {
  override def output = jnode.output
  def outputNames: Iterable[String] = output.map(_.resolvedName.get.resolvedName.replaceAll("#\\d+$", "").replace('$', '.'))
}

case class Projection(requiredProperties: Seq[ResolvableName],
                      jnode: jplan.Projection,
                      child: FNode
                     ) extends UnaryFNode {
  override def internalSchema: Seq[ResolvableName] = jnode.projectList ++ requiredProperties
  lazy val projectionTuple: Seq[Expression] =
    jnode.projectList.map(_.child).map(SchemaMapper.transformExpression(_, child.internalSchema)) ++
      requiredProperties.map(SchemaMapper.transformExpression(_, child.internalSchema))
}

case class Grouping(requiredProperties: Seq[ResolvableName],
                    jnode: jplan.Grouping,
                    child: FNode
                     ) extends UnaryFNode {
  override def internalSchema: Seq[ResolvableName] = jnode.projectList ++ requiredProperties
  lazy val aggregationCriteria: Seq[Expression] =
    jnode.aggregationCriteria.map(SchemaMapper.transformExpression(_, child.internalSchema)) ++
      requiredProperties.map(SchemaMapper.transformExpression(_, child.internalSchema))
  lazy val projectionTuple: Seq[Expression] =
    jnode.projectList.map(_.child).map(SchemaMapper.transformExpression(_, child.internalSchema)) ++
      requiredProperties.map(SchemaMapper.transformExpression(_, child.internalSchema))
}

case class Selection(jnode: jplan.Selection,
                     child: FNode
                    ) extends UnaryFNode {
  lazy val condition: Expression =
    SchemaMapper.transformExpression(jnode.condition, child.internalSchema)
}

case class Unwind(jnode: jplan.Unwind,
                  child: FNode
                 ) extends UnaryFNode {
  override def internalSchema: Seq[ResolvableName] = child.jnode.output ++ Seq(unwindAttribute)
  lazy val unwindAttribute: UnwindAttribute =
    UnwindAttribute(
      SchemaMapper.transformExpression(jnode.unwindAttribute.list, child.internalSchema),
      jnode.unwindAttribute.name,
      jnode.unwindAttribute.resolvedName
    )
}

case class SortAndTop(jnode: jplan.SortAndTop,
                      child: FNode
                     ) extends UnaryFNode {
  lazy val skipExpr:  Option[Expression] = jnode.skipExpr
  lazy val limitExpr: Option[Expression] = jnode.limitExpr
  val order: Seq[SortOrder] = jnode.order.map(SchemaMapper.transformExpression(_, child.internalSchema).asInstanceOf[SortOrder])
}

// binary nodes
case class Union(jnode: jplan.Union,
                 left: FNode,
                 right: FNode) extends BinaryFNode {
  override def internalSchema: Seq[ResolvableName] = left.internalSchema
}

case class AntiJoin(jnode: jplan.AntiJoin,
                    left: FNode,
                    right: FNode
                   ) extends BinaryFNode with JoinLike {
  override def internalSchema: Seq[ResolvableName] = left.internalSchema
}

case class Join(jnode: jplan.Join,
                left: FNode,
                right: FNode
               ) extends BinaryFNode with EquiJoinLike {}

case class TransitiveJoin(jnode: jplan.TransitiveJoin,
                          left: FNode,
                          right: FNode) extends BinaryFNode with EquiJoinLike {
  lazy val edgeList: EdgeListAttribute = jnode.edgeList
}

case class LeftOuterJoin(jnode: jplan.LeftOuterJoin,
                         left: FNode,
                         right: FNode
                        ) extends BinaryFNode with EquiJoinLike {}

case class ThetaLeftOuterJoin(jnode: jplan.ThetaLeftOuterJoin,
                              left: FNode,
                              right: FNode) extends BinaryFNode with EquiJoinLike {
  lazy val condition: Expression =
    SchemaMapper.transformExpression(jnode.condition, left.internalSchema, right.internalSchema, leftMask.size)
}

case class Create(jnode: jplan.Create,
                  child: FNode) extends UnaryFNode {
  lazy val attribute: ResolvableName = jnode.attribute match {
    case VertexAttribute(name, labels, properties, isAnonymous, resolvedName) =>
      val tProperties = properties.map((kv) => kv._1 -> SchemaMapper.transformExpression(kv._2, child.internalSchema))
      VertexAttribute(name, labels, tProperties, isAnonymous, resolvedName)
    case RichEdgeAttribute(src, trg, EdgeAttribute(name, labels, properties, isAnonymous, resolvedName), dir) =>
      val tSrc = SchemaMapper.transformExpression2(src, child.internalSchema)
      val tTrg = SchemaMapper.transformExpression2(trg, child.internalSchema)
      val tEdge = EdgeAttribute(
        name, labels,
        properties.map((kv) => kv._1 -> SchemaMapper.transformExpression(kv._2, child.internalSchema)),
        isAnonymous, resolvedName
      )
      TupleEdgeAttribute(tSrc, tTrg, tEdge, dir)
  }
  override def internalSchema = jnode.output ++ Seq(attribute)
}

case class Delete(jnode: jplan.Delete,
                  child: FNode) extends UnaryFNode {
  lazy val attributes: Seq[TupleIndexLiteralAttribute] =
    jnode.attributes.map(SchemaMapper.transformExpression(_, child.internalSchema).asInstanceOf[TupleIndexLiteralAttribute])
  lazy val detach: Boolean = jnode.detach
}

case class Merge(jnode: jplan.Merge,
                 child: FNode) extends UnaryFNode {}

case class SetNode(jnode: jplan.SetNode,
                   child: FNode) extends UnaryFNode {}

case class Remove(jnode: jplan.Remove,
                  child: FNode) extends UnaryFNode {}


object SchemaMapper {
  def schemaToIndices(node: JoinLike, side: FNode): Seq[Int] = {
    val sideIndices = schemaToMapNames(side)
    node.internalCommon.map(attr => sideIndices(attr.resolvedName.get.resolvedName))
  }

  def schemaToMapNames(n: FNode): Map[String, Int] = {
    n.internalSchema.zipWithIndex.map(f => f._1.resolvedName.get.resolvedName -> f._2).toMap
  }

  def transformExpression(expression: Expression, internalSchema: Seq[ResolvableName]): Expression = {
    expression.transform {
      case a: ResolvableName => TupleIndexLiteralAttribute(internalSchema.map(_.resolvedName).indexOf(a.resolvedName), isVertex = a.isInstanceOf[VertexAttribute])
      case e: Expression => e
    }
  }

  def transformExpression2(expression: Expression, internalSchema: Seq[ResolvableName]): Expression = {
    expression.transform {
      case a: ResolvableName => TupleIndexLiteralAttribute(internalSchema.map(_.name).indexOf(a.name))
      case e: Expression => e
    }
  }

  def transformExpression(expression: Expression,
                          internalSchemaLeft: Seq[ResolvableName],
                          internalSchemaRight: Seq[ResolvableName],
                          maskWidth: Int): Expression = {
    expression.transform {
      case a: ResolvableName => {
        val left = internalSchemaLeft.map(_.resolvedName)
        val right = internalSchemaRight.map(_.resolvedName)
        val resolvedName = a.resolvedName
        if (left.contains(resolvedName)) {
          TupleIndexLiteralAttribute(left.indexOf(resolvedName), Option(Left()))
        } else {
          TupleIndexLiteralAttribute(internalSchemaLeft.size - maskWidth + right.indexOf(resolvedName), Option(Right()))
        }
      }
      case e: Expression => e
    }
  }

}

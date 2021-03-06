package ingraph.sandbox

import ingraph.compiler.FPlanParser
import ingraph.compiler.cypher2gplan.GPlanResolver
import ingraph.compiler.plantransformers.{NPlanToFPlan, GPlanToNPlan}
import ingraph.model.expr._
import ingraph.model.fplan.{FNode, LeafFNode}
import ingraph.model.{fplan, nplan, gplan}
import org.apache.spark.sql.catalyst.expressions.{GreaterThan, Literal}
import org.scalatest.FunSuite

class NPlanToFPlanTest extends FunSuite {

  def getLeafNodes(plan: FNode): Seq[FNode] = {
    if (plan.isInstanceOf[LeafFNode]) return plan :: Nil
    return plan.children.flatMap(x => getLeafNodes(x))
  }

  test("infer schema #1") {
    val v = VertexAttribute("v")
    val gv = gplan.GetVertices(v)
    val de = gplan.DuplicateElimination(gv)

    val qp = de
    val jp = GPlanToNPlan.transform(qp)
    val fp = NPlanToFPlan.transform(jp)

    assert(fp.flatSchema.size == 1)
    assert(fp.children(0).flatSchema.size == 1)
  }

  test("infer schema #2") {
    val vls = VertexLabelSet(Set("Person"), NonEmpty)

    val n = VertexAttribute("n", vls)
    val name = PropertyAttribute("name", n)
    val age = PropertyAttribute("age", n)

    val projectList = Seq(ReturnItem(name))
    val condition = GreaterThan(age, Literal(27))

    val qp = gplan.UnresolvedProjection(
      projectList,
      gplan.Selection(
        condition,
        gplan.GetVertices(n)
      )
    )
    val rqp = GPlanResolver.resolveGPlan(qp)
    val jp = GPlanToNPlan.transform(rqp)
    val fp = NPlanToFPlan.transform(jp)

    assert(fp.flatSchema.size == 1)
    assert(fp.children(0).flatSchema.size == 3)
    assert(fp.children(0).children(0).flatSchema.size == 3)
  }

  test("infer schema #3") {
    val vls = VertexLabelSet(Set("Person"), NonEmpty)

    val n = VertexAttribute("n", vls)
    val name = PropertyAttribute("name", n)
    val projectList = Seq(ReturnItem(name))

    val qp = gplan.UnresolvedProjection(
      projectList,
      gplan.GetVertices(n)
    )
    val rqp = GPlanResolver.resolveGPlan(qp)
    val jp = GPlanToNPlan.transform(rqp)
    val fp = NPlanToFPlan.transform(jp)

    assert(fp.flatSchema.size == 1)
    assert(fp.children(0).flatSchema.size == 2)
  }

  test("infer schema for simple path") {
    val fp = FPlanParser.parse(
      """MATCH (a:A)-[:R1]->(b:B)-[:R2]->(c:C)
        |RETURN a, b, c
        |""".stripMargin)

    assert(getLeafNodes(fp)(0).flatSchema.length == 3)
    assert(getLeafNodes(fp)(1).flatSchema.length == 3)
  }

  test("infer schema for Projection") {
    val vls = VertexLabelSet(Set("Segment"), NonEmpty)

    val segment = VertexAttribute("segment", vls)
    val length = PropertyAttribute("length", segment)

    val projectList = Seq(ReturnItem(segment), ReturnItem(length))

    val qp = gplan.UnresolvedProjection(
      projectList,
      gplan.GetVertices(segment)
    )

    val rqp = GPlanResolver.resolveGPlan(qp)
    val jp = GPlanToNPlan.transform(rqp)
    val fp = NPlanToFPlan.transform(jp)

    assert(fp.flatSchema.size == 2)
    assert(fp.children(0).flatSchema.size == 2)
  }

  test("infer schema for PosLength from Cypher without filtering") {
    val fp = FPlanParser.parse(
      """MATCH (segment:Segment)
        |RETURN segment, segment.length AS length
        |""".stripMargin)
    import fplan._
    fp match {
      case
        Production(_,
          Projection(_, _,
           v: GetVertices
          )) =>
        assert(v.flatSchema.map(_.name) == Seq("segment", "length"))
    }
  }

  test("infer schema for PosLength from Cypher with filtering") {
    val fp = FPlanParser.parse(
      """MATCH (segment:Segment)
        |WHERE segment.length <= 0
        |RETURN segment, segment.length AS length
        |""".stripMargin)
    import fplan._
    fp match {
      case
        Production(_,
          Projection(_, _,
            Selection(_,
              v: GetVertices
            ))) =>
        assert(v.flatSchema.map(_.name) == Seq("segment", "length"))
    }
  }

  test("infer schema for RouteSensor from Cypher") {
    val fp = FPlanParser.parse(
      """MATCH (route:Route)
        |  -[:follows]->(swP:SwitchPosition)
        |  -[:target]->(sw:Switch)
        |  -[:monitoredBy]->(sensor:Sensor)
        |WHERE NOT (route)-[:requires]->(sensor)
        |RETURN route, sensor, swP, sw
        |""".stripMargin)

    val antijoin = fp.children(0).children(0).asInstanceOf[fplan.AntiJoin]
    assert(antijoin.leftMask  == List(0, 6))
    assert(antijoin.rightMask == List(0, 2))
  }

  test("infer schema for RouteSensorPositive from Cypher") {
    val fp = FPlanParser.parse(
      """MATCH (route:Route)
        |  -[:follows]->(swP:SwitchPosition)
        |  -[:target]->(sw:Switch)
        |  -[:monitoredBy]->(sensor:Sensor)
        |RETURN route, sensor, swP, sw
        |""".stripMargin)
    assert(fp.children(0).children(0).flatSchema.size == 7)
    assert(fp.children(0).children(0).children(0).flatSchema.size == 5)
    assert(fp.children(0).children(0).children(1).flatSchema.size == 3)
  }

  test("infer schema for SwitchMonitored") {
    val fp = FPlanParser.parse(
      """MATCH (sw:Switch)
        |WHERE NOT (sw)-[:monitoredBy]->(:Sensor)
        |RETURN sw
        |""".stripMargin)

    assert(getLeafNodes(fp)(0).flatSchema.length == 1)
    assert(getLeafNodes(fp)(1).flatSchema.length == 3)
  }

  test("infer schema for ConnectedSegments") {
    val fp = FPlanParser.parse(
      """MATCH
        |  (sensor:Sensor)<-[mb1:monitoredBy]-(segment1:Segment),
        |  (segment1:Segment)-[ct1:connectsTo]->
        |  (segment2:Segment)-[ct2:connectsTo]->
        |  (segment3:Segment)-[ct3:connectsTo]->
        |  (segment4:Segment)-[ct4:connectsTo]->
        |  (segment5:Segment)-[ct5:connectsTo]->(segment6:Segment),
        |  (segment2:Segment)-[mb2:monitoredBy]->(sensor:Sensor),
        |  (segment3:Segment)-[mb3:monitoredBy]->(sensor:Sensor),
        |  (segment4:Segment)-[mb4:monitoredBy]->(sensor:Sensor),
        |  (segment5:Segment)-[mb5:monitoredBy]->(sensor:Sensor),
        |  (segment6:Segment)-[mb6:monitoredBy]->(sensor:Sensor)
        |RETURN sensor, segment1, segment2, segment3, segment4, segment5, segment6
        |""".stripMargin)

    for (leafNode <- getLeafNodes(fp)) {
      assert(leafNode.flatSchema.length == 3)
    }
  }

  test("infer schema for Cartesian product") {
    val fp = FPlanParser.parse(
      """MATCH (n), (m)
        |RETURN n.value, m.value
      """.stripMargin
    )
    assert(getLeafNodes(fp)(0).flatSchema.length == 2)
    assert(getLeafNodes(fp)(1).flatSchema.length == 2)
  }

}

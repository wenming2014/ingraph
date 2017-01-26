package ingraph.relalg.util

import com.google.common.collect.Iterables
import com.google.common.collect.Lists
import java.util.List
import relalg.AbstractJoinOperator
import relalg.AttributeVariable
import relalg.BinaryOperator
import relalg.NullaryOperator
import relalg.Operator
import relalg.RelalgContainer
import relalg.TernaryOperator
import relalg.UnaryOperator
import relalg.UnionOperator
import relalg.Variable

class TupleInferencer {

  extension RelalgTreeVisitor treeVisitor = new RelalgTreeVisitor
  extension SchemaToMap schemaToMap = new SchemaToMap
  extension VariableExtractor variableExtractor = new VariableExtractor
  var RelalgContainer container

  def addDetailedSchemaInformation(RelalgContainer container) {
    if (container.tupleInferencingCompleted) {
      throw new IllegalStateException("Tuple inferencing on relalg container was already performed")
    } else {
      container.tupleInferencingCompleted = true
    }

    this.container = container
    container.rootExpression.inferDetailedSchema(#[])
    container.rootExpression.traverse([calculateTuples])
    container
  }

  /**
   * inferDetailedSchema
   */
  def dispatch void inferDetailedSchema(NullaryOperator op, List<AttributeVariable> extraVariables) {
    op.defineDetailedSchema(extraVariables)
  }

  def dispatch void inferDetailedSchema(UnaryOperator op, List<AttributeVariable> extraVariables) {
    val newExtraVariables = extractUnaryOperatorExtraVariables(op)
    val inputExtraVariables = union(extraVariables, newExtraVariables)
    
    op.defineDetailedSchema(inputExtraVariables)
    op.input.inferDetailedSchema(inputExtraVariables)
  }

  def dispatch void inferDetailedSchema(BinaryOperator op, List<AttributeVariable> extraVariables) {
    val leftExtraVariables = extraVariables.filter[op.leftInput.schema.contains(it.element)].toList
    val rightExtraVariables = extraVariables.filter[op.rightInput.schema.contains(it.element)].toList

    // remove duplicates as we only need each extra variable once
    // choosing "right\left" over "left\right" is an arbitrary decision - studying, benchmarking and optimizing this is subject to future work
    rightExtraVariables.removeAll(leftExtraVariables)

    val orderedExtraVariables = union(leftExtraVariables, rightExtraVariables)

    op.defineDetailedSchema(orderedExtraVariables)    
    op.leftInput.inferDetailedSchema(leftExtraVariables)
    op.rightInput.inferDetailedSchema(rightExtraVariables)
  }

  def dispatch void inferDetailedSchema(TernaryOperator op, List<AttributeVariable> extraVariables) {
    val leftExtraVariables = extraVariables.filter[op.leftInput.schema.contains(it.element)].toList
    val middleExtraVariables = extraVariables.filter[op.middleInput.schema.contains(it.element)].toList
    val rightExtraVariables = extraVariables.filter[op.rightInput.schema.contains(it.element)].toList

    // remove duplicates as we only need each extra variable once
    // see the related comment in inferDetailedSchema for BinaryOperators
    middleExtraVariables.removeAll(leftExtraVariables)
    
    rightExtraVariables.removeAll(leftExtraVariables)
    rightExtraVariables.removeAll(middleExtraVariables)

    op.defineDetailedSchema(extraVariables)

    op.leftInput.inferDetailedSchema(leftExtraVariables)
    op.middleInput.inferDetailedSchema(middleExtraVariables)
    op.rightInput.inferDetailedSchema(rightExtraVariables)
  }
  
  /**
   * defineSchema
   */
  def void defineDetailedSchema(Operator op, List<? extends Variable> extraVariables) {
    op.extraVariables.addAll(extraVariables)
    
    op.detailedSchema.addAll(op.schema)
    op.detailedSchema.addAll(extraVariables)
  }

  /**
   * shorthand for creating the union of two lists
   */
  def <T> union(List<? extends T> list1, List<? extends T> list2) {
    Lists.newArrayList(Iterables.concat(list1, list2))
  }
  
  /**
   * calculateTuples
   */
  def dispatch void calculateTuples(NullaryOperator op) {
    // do nothing
  }
  
  def dispatch void calculateTuples(UnaryOperator op) {
    // do nothing    
  }
  
  def dispatch void calculateTuples(AbstractJoinOperator op) {
    val leftIndices = op.leftInput.schemaToMap
    val rightIndices = op.rightInput.schemaToMap
    
    op.commonVariables.forEach[ variable |
      op.leftMask.add(leftIndices.get(variable))
      op.rightMask.add(rightIndices.get(variable))
    ]
  }
  
  def dispatch void calculateTuples(UnionOperator op) {
    // do nothing
  }
  
  def dispatch void calculateTuples(TernaryOperator op) {
    // TODO do something
  }  
  
}

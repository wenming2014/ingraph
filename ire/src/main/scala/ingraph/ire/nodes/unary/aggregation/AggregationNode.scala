package ingraph.ire.nodes.unary.aggregation

import ingraph.ire.datatypes.{Tuple, TupleBag}
import ingraph.ire.messages.{ChangeSet, ReteMessage, SingleForwarder}
import ingraph.ire.messages.SingleForwarder
import ingraph.ire.nodes.unary.UnaryNode

import scala.collection.immutable.VectorBuilder
import scala.collection.mutable

class AggregationNode(override val next: (ReteMessage) => Unit,
                      mask: Vector[Tuple => Any], factories: () => Vector[StatefulAggregate],
                      projection: Vector[Int]) extends UnaryNode with SingleForwarder {
  private val keyCount = mutable.Map[Tuple, Int]().withDefault(f => 0)
  private val data = mutable.Map[Tuple, Vector[StatefulAggregate]]().withDefault(f => factories())

  override def onChangeSet(changeSet: ChangeSet): Unit = {
    val oldValues = mutable.Map[Tuple, (Tuple, Int)]()
    for ((key, tuples) <- changeSet.positive.groupBy(t => mask.map(m => m(t)))) {
      val aggregators = data.getOrElseUpdate(key, factories())

      oldValues.getOrElseUpdate(key, (aggregators.map(_.value()), keyCount(key)))
      for (aggregator <- aggregators)
        aggregator.maintainPositive(tuples)
      keyCount(key) += tuples.size
    }
    for ((key, tuples) <- changeSet.negative.groupBy(t => mask.map(m => m(t)))) {
      val aggregators = data.getOrElseUpdate(key, factories())
      oldValues.getOrElseUpdate(key, (aggregators.map(_.value()), keyCount(key)))
      for (aggregator <- aggregators)
        aggregator.maintainNegative(tuples)
      keyCount(key) -= tuples.size
    }

    val positive = new VectorBuilder[Tuple]
    val negative = new VectorBuilder[Tuple]

    for ((key, (oldValues, oldCount)) <- oldValues) {
      val newValues = data(key).map(_.value())
      if (oldValues != newValues || oldCount == 0) {
        if (keyCount(key) != 0)
          positive += key ++ newValues
        if (oldCount != 0)
          negative += key ++ oldValues
      }
    }
    val positiveBag: TupleBag = positive.result().map(t => projection.map(t))
    val negativeBag: TupleBag = negative.result().map(t => projection.map(t))
    forward(ChangeSet(
      positive = positiveBag,
        negative = negativeBag))
  }

  override def onSizeRequest(): Long = ???
}

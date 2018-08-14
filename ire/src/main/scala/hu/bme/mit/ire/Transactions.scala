package hu.bme.mit.ire

import hu.bme.mit.ire.datatypes.Tuple
import hu.bme.mit.ire.messages.ChangeSet

import scala.collection.mutable

trait DataSource extends AutoCloseable {
  override def close(): Unit
  def add(pred: String, tuple: Tuple)
  def remove(pred: String, tuple: Tuple)
}

class DataSourceFactory {
  val subscribers = new mutable.HashMap[String, mutable.MutableList[(ChangeSet) => Unit]]
  //val usedIDs = new mutable.HashSet[Long]
  val idGenerator = new scala.util.Random

  def subscribe(subscriber: Map[String, (ChangeSet) => Unit]) = {
    for ((attribute, func) <- subscriber)
      subscribers.getOrElseUpdate(attribute, mutable.MutableList()) += func
  }

  def newDataSource: BatchDataSource = {
    new BatchDataSource
  }

  class BatchDataSource extends DataSource {
    val positiveChangeSets = mutable.HashMap.empty[String, Vector[Tuple]]
    val negativeChangeSets = mutable.HashMap.empty[String, Vector[Tuple]]

    def close(): Unit = {
      positiveChangeSets.foreach(kv => subscribers(kv._1).foreach(sub => sub(ChangeSet(positive = kv._2))))
      negativeChangeSets.foreach(kv => subscribers(kv._1).foreach(sub => sub(ChangeSet(negative = kv._2))))
      positiveChangeSets.clear()
      negativeChangeSets.clear()
    }

    def add(pred: String, tuple: Tuple): Unit = {
      if (subscribers.contains(pred)) {
        if (!positiveChangeSets.contains(pred))
          positiveChangeSets(pred) = Vector.empty[Tuple]
        positiveChangeSets(pred) +:= tuple
      }
    }

    def remove(pred: String, tuple: Tuple): Unit = {
      // DO NOT call usedIDs.remove(subj), there are enough long values to go around,
      // having to deal with transient IDs is not worth it
      if (subscribers.contains(pred)) {
        if (!negativeChangeSets.contains(pred))
          negativeChangeSets(pred) = Vector.empty[Tuple]
        negativeChangeSets(pred) +:= tuple
      }
    }
  }

}

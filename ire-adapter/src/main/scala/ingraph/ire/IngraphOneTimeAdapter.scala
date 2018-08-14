package ingraph.ire

import hu.bme.mit.ire.datatypes.Tuple
import hu.bme.mit.ire.{PullTupleCreator, DataSourceFactory, TupleCreator}
import ingraph.compiler.FPlanParser

class IngraphOneTimeAdapter (
    val querySpecification: String,
    val queryName: String,
    val indexer: Indexer = new Indexer()
  ) extends AutoCloseable {

  val plan = FPlanParser.parse(querySpecification)
  val engine = EngineFactory.createQueryEngine(plan, indexer)

  val dataSourceFactory = new DataSourceFactory
  dataSourceFactory.subscribe(engine.inputLookup)
  val dataSource = dataSourceFactory.newDataSource
  new PullTupleCreator(
    engine.vertexConverters.values.flatten.toVector.distinct,
    engine.edgeConverters.values.flatten.toVector.distinct,
    indexer, dataSource, LongIdParser
  )

  /**
    * Terminate execution and return results
    */
  def terminate(): Iterable[Tuple] = {
    dataSource.close()
    engine.getResults()
  }

  override def close() {
    engine.shutdown()
  }

}

package ingraph

import ingraph.testrunners.{IngraphTestRunner, Neo4jTestRunner}
import ingraph.tests.LdbcSnbTestCase

object BenchmarkMain {

  def main(args: Array[String]): Unit = {
    val sf = args(0)
    val query = args(1).toInt
    val neo4jDir = if (args.length > 2) {
      Some(args(2))
    } else {
      None
    }

    def csvDir = f"../graphs/ldbc-snb-bi/sf${sf}/"
    def csvPostfix = "_0_0.csv"

    val postId = sf match {
      case "tiny" => 137438953796L
      case   "01" => 412317167461L
      case   "03" => 412316860440L
      case    "1" => 962072674360L
      case    "3" => 3573412790304L
    }
    val forumId = sf match {
      case "tiny" => 274877906944L
      case   "01" => 893353199216L
      case   "03" => 893353197569L
      case    "1" => 1786706395137L
      case    "3" => 3573412790338L
    }
    val removePost =
      s"""MATCH (n:Message:Post {id: ${postId}})
         |DETACH DELETE n
    """.stripMargin
    val removeForum =
      s"""MATCH (n:Forum {id: ${forumId}})
         |DETACH DELETE n
    """.stripMargin

    val tc = new LdbcSnbTestCase("bi", sf, query, f"${csvDir}/", csvPostfix, Seq(removePost, removeForum))

    neo4jDir match {
      case None =>
        val itr = new IngraphTestRunner(tc)
        val ingraphResults = itr.run()
      case Some(_) =>
        val ntr = new Neo4jTestRunner(tc, neo4jDir)
        val neo4jResults = ntr.run()
        ntr.close
    }

//    assert(ingraphResults == neo4jResults)

    // some Akka stuff gets stuck despite my best efforts to shutdown
    // System.exit(0) was not sufficient
    Runtime.getRuntime.halt(0)
  }

}

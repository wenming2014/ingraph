package ingraph.driver.test

import java.io.FileInputStream
import java.util
import java.util.Collections

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Input
import de.javakaffee.kryoserializers.UnmodifiableCollectionsSerializer
import ingraph.driver.data.{IngraphDeltaHandler, PrintDeltaHandler}
import ingraph.driver.ingraph.IngraphDriver
import ingraph.relalg2tex.config.RelalgConverterConfigBuilder
import ingraph.relalg2tex.converters.relalgconverters.Relalg2TexTreeConverter
import org.neo4j.driver.v1.Record
import org.objenesis.strategy.StdInstantiatorStrategy
import org.scalatest.FunSuite
import org.supercsv.prefs.CsvPreference

import scala.collection.JavaConverters._
import scala.io.Source

class LdbcSnbBiDriverTest extends FunSuite {

  def modelPath(entityName: String) = s"../graphs/snb_50/${entityName}_0_0.csv"

  def queryPath(query: Int): String = s"../queries/ldbc-snb-bi/query-$query.cypher"

  def queryResultPath(query: Int): String = queryPath(query).dropRight("cypher".length) + "bin"

  val nodeFilenames: Map[String, List[String]] = Map(
    modelPath("comment") -> List("Message", "Comment"),
    modelPath("forum") -> List("Forum"),
    modelPath("organisation") -> List("Company", "University"),
    modelPath("person") -> List("Person"),
    modelPath("place") -> List("Place"),
    modelPath("post") -> List("Message", "Post"),
    modelPath("tagclass") -> List("TagClass"),
    modelPath("tag") -> List("Tag"))

  val relationshipFilenames: Map[String, String] = Map(
    modelPath("comment_hasCreator_person") -> "hasCreator",
    modelPath("comment_isLocatedIn_place") -> "isLocatedIn",
    modelPath("comment_replyOf_comment") -> "replyOf",
    modelPath("comment_replyOf_post") -> "replyOf",
    modelPath("forum_containerOf_post") -> "containerOf",
    modelPath("forum_hasMember_person") -> "hasMember",
    modelPath("forum_hasModerator_person") -> "hasModerator",
    modelPath("forum_hasTag_tag") -> "hasTag",
    modelPath("person_hasInterest_tag") -> "hasInterest",
    modelPath("person_isLocatedIn_place") -> "isLocatedIn",
    modelPath("person_knows_person") -> "knows",
    modelPath("person_likes_comment") -> "likes",
    modelPath("person_likes_post") -> "likes",
    modelPath("place_isPartOf_place") -> "isPartOf",
    modelPath("post_hasCreator_person") -> "hasCreator",
    modelPath("comment_hasTag_tag") -> "hasTag",
    modelPath("post_hasTag_tag") -> "hasTag",
    modelPath("post_isLocatedIn_place") -> "isLocatedIn",
    modelPath("tagclass_isSubclassOf_tagclass") -> "isSubclassOf",
    modelPath("tag_hasType_tagclass") -> "hasType",
    modelPath("organisation_isLocatedIn_place") -> "isLocatedIn",
    modelPath("person_studyAt_organisation") -> "studyAt",
    modelPath("person_workAt_organisation") -> "workAt"
  )

  val converterConfig = new RelalgConverterConfigBuilder() //
    .setConsoleOutput(false) //
    .setStandaloneDocument(true) //
    .setIncludeCommonVariables(true) //
    .setSchemaIndices(true) //
    .build
  val converter = new Relalg2TexTreeConverter(converterConfig)

  case class TestCase(number: Int)

  Vector(
//        TestCase(2),
        TestCase(3),
        TestCase(4),
        TestCase(5),
        TestCase(6),
        TestCase(7),
        TestCase(9),
        TestCase(12),

//        TestCase(8), // PATH
//        TestCase(14), // PATH
//        TestCase(16), // PATH
//        TestCase(20), // PATH

//        TestCase(13),
//        TestCase(15), // WHERE WITH
//        TestCase(23),
//        TestCase(24),

        null
  ).filter(_ != null) //
    .foreach(
    t => test(s"query-${t.number}-size-1") {
      val queryName = s"ldbc-snb-bi-${t.number}"
      val querySpecification = Source.fromFile(queryPath(t.number)).getLines().mkString("\n")

      val driver = new IngraphDriver()
      val session = driver.session()

      val csvPreference = new CsvPreference.Builder('"', '|', "\n").build()


      val queryHandler = session.registerQuery(queryName, querySpecification)
      var actualChangeSize = 0
      class AssertionHandler(override val keys: Vector[String], size: Int) extends IngraphDeltaHandler {
        override def onChange(positiveRecords: util.List[_ <: Record], negativeRecords: util.List[_ <: Record]): Unit = {
          actualChangeSize = positiveRecords.size()
        }
      }

      val kryo = new Kryo
      kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()))
      kryo.addDefaultSerializer(
          Collections.unmodifiableCollection( Collections.EMPTY_LIST ).getClass,
          classOf[UnmodifiableCollectionsSerializer])

      val javaResults = kryo.readClassAndObject(new Input(new FileInputStream(queryResultPath(t.number))))
        .asInstanceOf[java.util.ArrayList[java.util.Map[String, Any]]]

      import scala.collection.JavaConverters._
      val expectedResultsSize = javaResults.size()
      queryHandler.registerDeltaHandler(new AssertionHandler(queryHandler.adapter.resultNames(), expectedResultsSize))
      queryHandler.readCsv(nodeFilenames.mapValues(_.asJava).asJava, relationshipFilenames.asJava, csvPreference)
      assert(actualChangeSize == expectedResultsSize)
    })
}
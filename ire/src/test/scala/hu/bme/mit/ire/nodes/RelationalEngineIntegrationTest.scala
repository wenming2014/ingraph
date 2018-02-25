package hu.bme.mit.ire.nodes

import akka.actor.{ActorRef, ActorSystem, Props, actorRef2Scala}
import akka.testkit.{ImplicitSender, TestActors, TestKit}
import hu.bme.mit.ire.{TransactionFactory, _}
import hu.bme.mit.ire.datatypes.Tuple
import hu.bme.mit.ire.engine.RelationalEngine
import hu.bme.mit.ire.listeners.{ChangeListener, ConsistentChangeListener}
import hu.bme.mit.ire.messages.ChangeSet
import hu.bme.mit.ire.nodes.unary.{ProductionNode, SelectionNode}
import hu.bme.mit.ire.util.TestUtil._
import org.scalatest._

class RelationalEngineIntegrationTest(_system: ActorSystem) extends TestKit(_system) with ImplicitSender
  with FlatSpecLike with Matchers with BeforeAndAfterAll {

  def this() = this(ActorSystem("MySpec"))

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  class TestQuery1 extends RelationalEngine {
    override val production: ActorRef = system.actorOf(Props(new ProductionNode("TestQuery")))
    override val inputLookup: Map[String, (ChangeSet) => Unit] = Map(
      "testval" -> ((cs: ChangeSet) => forwarder ! cs)
    )
    override val terminator: Terminator = Terminator(Vector(forwarder ! _), production)
    val forwarder = system.actorOf(Props(new SelectionNode(production ! _, a => true)))
  }

  class TestQuery2 extends RelationalEngine {
    override val production: ActorRef = system.actorOf(Props(new ProductionNode("TestQuery", 2)))
    override val inputLookup: Map[String, (ChangeSet) => Unit] = Map(
      "testval" -> ((cs: ChangeSet) => forwarder ! cs),
      "testval2" -> ((cs: ChangeSet) => forwarder2 ! cs)
    )
    override val terminator: Terminator = Terminator(Vector(forwarder ! _, forwarder2 ! _), production)
    val forwarder = system.actorOf(Props(new SelectionNode(production ! _, a => true)))
    val forwarder2 = system.actorOf(Props(new SelectionNode(production ! _, a => true)))
  }

  "multiple queries" should "work" in {
    for (i <- 1 to 1000) {
      val input = new TransactionFactory(10)
      val query = new TestQuery1
      val query2 = new TestQuery2
      input.subscribe(query.inputLookup)
      input.subscribe(query2.inputLookup)
      val tran0 = input.newBatchTransaction()
      tran0.add("testval", tuple(5, 5))
      tran0.add("testval", tuple(5, 6))
      tran0.add("testval2", tuple(5, 7))
      tran0.close()
      val res1 = query.getResults()
      assert(res1.toSet == Set(tuple(5, 5), tuple(5, 6)))
      val res2 = query2.getResults()
      assert(res2.toSet == Set(tuple(5, 5), tuple(5, 6), tuple(5, 7)))
    }
  }

  "integration" should "work" in {
    val input = new TransactionFactory(10)
    val query = new TestQuery1
    input.subscribe(query.inputLookup)
    val tran0 = input.newBatchTransaction()
    tran0.add("testval", tuple(5, 5))
    tran0.add("testval", tuple(5, 6))
    tran0.add("testval", tuple(5, 7))
    tran0.close()
    val res0 = query.getResults()
    assert(res0.toSet == Set(tuple(5, 5), tuple(5, 6), tuple(5, 7)))
    val tran1 = input.newBatchTransaction()
    tran1.remove("testval", tuple(5, 5))
    tran1.remove("testval", tuple(5, 6))
    tran1.add("tesval2", tuple(3, 2))
    tran1.add("testval", tuple(5, 8))
    tran1.close()
    assert(query.getResults().toSet == Set(tuple(5, 7), tuple(5, 8)))
    val beginTime: Long = System.nanoTime()
    (1 to 5000).foreach(i => {
      val tranRemove7 = input.newBatchTransaction()
      tranRemove7.remove("testval", tuple(5, 7))
      tranRemove7.close()
      assert(query.getResults().toSet == Set(tuple(5, 8)))
      val tranRemove8 = input.newBatchTransaction()
      tranRemove8.remove("testval", tuple(5, 8))
      tranRemove8.close()
      assert(query.getResults().toSet == Set())
      val tranAdd78 = input.newBatchTransaction()
      tranAdd78.add("testval", tuple(5, 7))
      tranAdd78.add("testval", tuple(5, 8))
      tranAdd78.close()
    })
    val endTime: Long = System.nanoTime() - beginTime
    val avg = endTime / 500 / 3
  }

  "listeners" should "listen" in {
    (1 to 1000).foreach(i => {
      val echoActor = system.actorOf(TestActors.echoActorProps)
      val input = new TransactionFactory(10)
      val query = new TestQuery1
      input.subscribe(query.inputLookup)
      val tran0 = input.newBatchTransaction()
      tran0.add("testval", tuple(5, 5))
      tran0.close()
      query.getResults()
      query.addListener(new ConsistentChangeListener {
        override def listener(positive: Vector[Tuple], negative: Vector[Tuple]): Unit = {
          echoActor ! ChangeSet(positive, negative)
        }
      })
      expectMsg(ChangeSet(positive = Vector(tuple(5, 5))))
      val tran1 = input.newBatchTransaction()
      tran1.remove("testval", tuple(5, 5))
      tran1.add("testval", tuple(6, 6))
      tran1.close()
      query.getResults()
      expectMsg(ChangeSet(positive = Vector(tuple(6, 6)), negative = Vector(tuple(5, 5))))
    })
  }
}

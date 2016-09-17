package ingraph.relalg2tex.tck

import org.junit.Test

import ingraph.cypher2relalg.RelalgParser
import ingraph.relalg2tex.RelalgTreeSerializer

class LabelsAcceptanceVisualizationTest {

    val RelalgTreeSerializer serializer = new RelalgTreeSerializer
    
    /*
    Scenario: Using `labels()` in return clauses
    And having executed:
      """
      CREATE ()
      """
    */
    @Test
    def void testLabelsAcceptance_10() {
        val container = RelalgParser.parse('''
        MATCH (n)
        RETURN labels(n)
        ''')
        serializer.serialize(container, "LabelsAcceptance_10")
    }

    /*
    Scenario: Removing a label
    And having executed:
      """
      CREATE (:Foo:Bar)
      """
    */
    @Test
    def void testLabelsAcceptance_11() {
        val container = RelalgParser.parse('''
        MATCH (n)
        REMOVE n:Foo
        RETURN labels(n)
        ''')
        serializer.serialize(container, "LabelsAcceptance_11")
    }

    /*
    Scenario: Removing a non-existent label
    And having executed:
      """
      CREATE (:Foo)
      """
    */
    @Test
    def void testLabelsAcceptance_12() {
        val container = RelalgParser.parse('''
        MATCH (n)
        REMOVE n:Bar
        RETURN labels(n)
        ''')
        serializer.serialize(container, "LabelsAcceptance_12")
    }

}

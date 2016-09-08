package ingraph.cypher2relalg.tck

import org.junit.Test

import ingraph.cypher2relalg.RelalgParser

class UnwindAcceptanceTest {
    
    @Test
    def void testUnwindAcceptance_01() {
        RelalgParser.parse('''
        UNWIND [1, 2, 3] AS x
        RETURN x
        ''')
    }
        
    @Test
    def void testUnwindAcceptance_02() {
        RelalgParser.parse('''
        UNWIND range(1, 3) AS x
        RETURN x
        ''')
    }
        
    @Test
    def void testUnwindAcceptance_03() {
        RelalgParser.parse('''
        WITH [1, 2, 3] AS first, [4, 5, 6] AS second
        UNWIND (first + second) AS x
        RETURN x
        ''')
    }
        
    @Test
    def void testUnwindAcceptance_04() {
        RelalgParser.parse('''
        UNWIND RANGE(1, 2) AS row
        WITH collect(row) AS rows
        UNWIND rows AS x
        RETURN x
        ''')
    }
        
    @Test
    def void testUnwindAcceptance_05() {
        RelalgParser.parse('''
        MATCH (row)
        WITH collect(row) AS rows
        UNWIND rows AS node
        RETURN node.id
        ''')
    }
        
    @Test
    def void testUnwindAcceptance_06() {
        RelalgParser.parse('''
        UNWIND $events AS event
        MATCH (y:Year {year: event.year})
        MERGE (e:Event {id: event.id})
        MERGE (y)<-[:IN]-(e)
        RETURN e.id AS x
        ORDER BY x
        ''')
    }
        
    @Test
    def void testUnwindAcceptance_07() {
        RelalgParser.parse('''
        WITH [[1, 2, 3], [4, 5, 6]] AS lol
        UNWIND lol AS x
        UNWIND x AS y
        RETURN y
        ''')
    }
        
    @Test
    def void testUnwindAcceptance_08() {
        RelalgParser.parse('''
        UNWIND [] AS empty
        RETURN empty
        ''')
    }
        
    @Test
    def void testUnwindAcceptance_09() {
        RelalgParser.parse('''
        UNWIND null AS nil
        RETURN nil
        ''')
    }
        
    @Test
    def void testUnwindAcceptance_10() {
        RelalgParser.parse('''
        UNWIND [1, 1, 2, 2, 3, 3, 4, 4, 5, 5] AS duplicate
        RETURN duplicate
        ''')
    }
        
    @Test
    def void testUnwindAcceptance_11() {
        RelalgParser.parse('''
        WITH [1, 2, 3] AS list
        UNWIND list AS x
        RETURN *
        ''')
    }
        
    @Test
    def void testUnwindAcceptance_12() {
        RelalgParser.parse('''
        MATCH (a:S)-[:X]->(b1)
        WITH a, collect(b1) AS bees
        UNWIND bees AS b2
        MATCH (a)-[:Y]->(b2)
        RETURN a, b2
        ''')
    }
        
    @Test
    def void testUnwindAcceptance_13() {
        RelalgParser.parse('''
        WITH [1, 2] AS xs, [3, 4] AS ys, [5, 6] AS zs
        UNWIND xs AS x
        UNWIND ys AS y
        UNWIND zs AS z
        RETURN *
        ''')
    }
        
}
    
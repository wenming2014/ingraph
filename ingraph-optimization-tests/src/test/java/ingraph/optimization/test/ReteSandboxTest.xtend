package ingraph.optimization.test

import ingraph.cypher2relalg.Cypher2Relalg
import org.eclipse.emf.ecore.util.EcoreUtil
import org.junit.Test

class ReteSandboxTest extends Cypher2Relalg2Rete2TexTest {

	override protected directory() {
		return "sandbox"
	}

	@Test
	def void constantFolding() {
		process('test-constant-folding', '''MATCH (n) WHERE 1=1 RETURN n''')
	}

	@Test
	def void undirectedEdges1() {
		process('undirectedEdges-1', '''MATCH (n)-[r1:REL1]->(m)-[r2:REL2]-(o) RETURN n''')
	}

	@Test
	def void undirectedEdges2() {
		process('undirectedEdges-2', '''MATCH (n)-[r1:REL1]->(m)-[r2:REL2]-(o) RETURN n''')
	}

	@Test
	def void query3() {
		process('query3', '''
			// Tag evolution
			MATCH (tag:Tag)<-[:hasTag]-(message:Message)
			WHERE toInt(substring(message.creationDate, 0, 4)) = 2010
			  AND toInt(substring(message.creationDate, 5, 2)) = 11
			WITH tag, count(message) AS countMonth1
			MATCH (tag)<-[:hasTag]-(message:Message)
			WHERE toInt(substring(message.creationDate, 0, 4)) = 2010
			  AND toInt(substring(message.creationDate, 5, 2)) = 12
			WITH tag, countMonth1, count(message) AS countMonth2
			RETURN tag.name, countMonth1, countMonth2, abs(countMonth1-countMonth2) AS diff
			ORDER BY diff desc, tag.name ASC
			LIMIT 100
		''')
	}
	
	@Test
	def void query4() {
	  process('query4', '''
	  	MATCH (:Country)<-[:isPartOf]-(:City)<-[:isLocatedIn]-(person:Person)<-[:hasModerator]-(forum:Forum)-[:containerOf]->(post:Post)-[:hasTag]->(:Tag)-[:hasType]->(:TagClass)
	  	RETURN forum.id, forum.title, forum.creationDate, person.id, count(post) AS count
	  	ORDER BY count DESC, forum.id ASC
	  	LIMIT 20
	  ''')
	}
	
		@Test
	def void queryx() {
		val x = Cypher2Relalg.processString('''
	  	MATCH (semaphore:Semaphore)
	  	RETURN semaphore.signal
	  ''')
	  EcoreUtil.copy(x)
	  
	}
	
	@Test
	def void query6() {
		process('query6', '''
		MATCH (:Tag)<-[:hasTag]-(message:Message)-[:hasCreator]->(person: Person),
		  (message)<-[:likes]-(fan:Person),
		  (message)<-[:replyOf]-(comment:Comment) // TODO add * for transitivity
		WITH person, count(message) AS postCount, count(comment) AS replyCount, count(fan) AS likeCount
		RETURN person.id
		''')
	}

	@Test
	def void query7() {
		process('query7', '''
			MATCH
				(tag:Tag)<-[:hasTag]-(:Message)-[:hasCreator]->(person1:Person)
			MATCH
				(person)<-[:hasCreator]-(message:Message)-[:hasTag]->(tag),
				(message)<-[:likes]-(person2:Person)<-[:hasCreator]-(:Message)<-[:likes]-(person3:Person)
			RETURN
				person1.id,
				count(person3) AS authorityScore
			ORDER BY
				authorityScore DESC,
				person1.name ASC
			LIMIT 100
		''')
	}
	
	@Test
	def void query13() {
		process('query13', '''
			MATCH (:Country)<-[:isLocatedIn]-(message:Message)-[:hasTag]->(tag:Tag)
			WITH
			  toInt(substring(message.creationDate, 0, 4)) AS year,
			  toInt(substring(message.creationDate, 5, 2)) AS month,
			  count(message) AS popularity,
			  tag
			ORDER BY popularity DESC, tag.name ASC
			RETURN year, month, collect([tag.name, popularity]) AS popularTags
			ORDER BY year DESC, month ASC
			LIMIT 100
		''')
	}

	@Test
	def void aggr() {
		process('aggr', '''
			MATCH (n:Node)
			RETURN n, sum(n.age)
		''')
	}


	@Test
	def void query23() {
		process('query23', '''
			MATCH (homeCountry:Country)<-[:isPartOf]-(:City)<-[:isLocatedIn]-(:Person)<-[:hasCreator]-(message:Message)-[:isLocatedIn]->(country:Country)
			WHERE homeCountry <> country
			RETURN count(message) AS messageCount, country.name,  toInt(substring(message.creationDate, 5, 2)) AS month
			ORDER BY messageCount DESC, country.name ASC, month DESC
			LIMIT 100
		''')
	}
	
	@Test
	def void query24() {
		process('query24', '''
			MATCH
			  (:TagClass)<-[:hasType]-(:Tag)<-[:hasTag]-(message:Message)<-[:likes]-(person:Person),
			  (message)-[:isLocatedIn]->(:Country)-[:isPartOf]->(continent:Continent)
			RETURN
			  count(message) AS messageCount,
			  count(person) AS likeCount,
			  toInt(substring(message.creationDate, 0, 4)) AS year,
			  toInt(substring(message.creationDate, 5, 2)) AS month,
			  continent.name
		''')
	}
	
	@Test
	def void sort() {
		process('sort', '''
			MATCH (n)-[:KNOWS]-(m)
			RETURN n, n.name, count(m) AS numberOfFriends
			ORDER BY n.age
			LIMIT 100
		''')
	}
	
	@Test
	def void sort2() {
	  process('sort2', '''
	    MATCH (n: Segment) RETURN n ORDER BY n SKIP 5 LIMIT 10
    ''')
	}
	
	@Test
	def void sortAndTopNode() {
	  process('sort-and-top', '''MATCH (n: Segment) RETURN n ORDER BY n SKIP 5 LIMIT 10''')
	}

}

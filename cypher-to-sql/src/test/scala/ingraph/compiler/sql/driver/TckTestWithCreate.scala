package ingraph.compiler.sql.driver

import ingraph.compiler.sql.driver.TckTestWithCreate.scenarioSet
import ingraph.tck.{TckScenarioSet, TckTestRunner}
import org.scalatest.FunSuite

class TckTestWithCreate extends FunSuite with TckTestRunner with SharedSqlDriver {
  runTckTests(() => new TckAdapter(session), scenarioSet)
}

object TckTestWithCreate {
  val selectedFeatures: Set[String] = Set(
    ""
  ).filter(!_.isEmpty)

  val selectedScenarios: Set[String] = Set(
    "A bound node should be recognized after projection with WITH + MERGE node",
    "A bound node should be recognized after projection with WITH + UNWIND",
    "Accept valid Unicode literal",
    "Aggregate on list values",
    "Aggregate on property",
    "Aggregating by a list property has a correct definition of equality",
    "Aliasing",
    "Aliasing expressions",
    "Combine MATCH and CREATE",
    "Comparing nodes for equality",
    "Comparing nodes to nodes",
    "Comparing relationships to relationships",
    "Comparing strings and integers using > in an AND'd predicate",
    "Comparing strings and integers using > in a OR'd predicate",
    "Cope with shadowed variables",
    "Count nodes",
    "Count non-null values",
    "Create a pattern with multiple hops",
    "Create a pattern with multiple hops in the reverse direction",
    "Create a pattern with multiple hops in varying directions",
    "Create a pattern with multiple hops with multiple types and varying directions",
    "Create a relationship and an end node from a matched starting node",
    "Create a relationship with a property",
    "Create a relationship with a reversed direction",
    "Create a relationship with the correct direction",
    "Create a self loop",
    "Create a self loop using MATCH",
    "Create a simple pattern",
    "Create a single node with multiple labels",
    "Create nodes and relationships",
    "Creating a node",
    "Creating a node and return a dummy value",
    "Creating a node with a label",
    "Creating a node with a property",
    "Creating node without label",
    "Creating two nodes",
    "Creating two nodes and a relationship",
    "Dependant CREATE with single row",
    "Dependant CREATE with single row - with aliased attribute",
    "Directed match of a simple relationship",
    "Directed match of a simple relationship, count",
    "Directed match on self-relationship graph",
    "Directed match on self-relationship graph, count",
    "Distinct on null",
    "DISTINCT on nullable values",
    "Distinct on unbound node",
    "Does not lose precision",
    "Do not fail when evaluating predicates with illegal operations if the AND'ed predicate evaluates to false",
    "Do not fail when evaluating predicates with illegal operations if the OR'd predicate evaluates to true",
    "Do not fail when predicates on optionally matched and missed nodes are invalid",
    "Do not return non-existent nodes",
    "Do not return non-existent relationships",
    "Double unwinding a list of lists",
    "Execute n[0]",
    "`exists()` is case insensitive",
    "Filter based on rel prop name",
    "Filter out based on node prop name",
    "Find all nodes",
    "Find friends of others",
    "Find labelled nodes",
    "Find nodes by property",
    "Generic CASE",
    "Get neighbours",
    "Get related to related to",
    "Get rows in the middle",
    "Get two related nodes",
    "Handle comparison between node properties",
    "Handle ORDER BY with LIMIT 1",
    "Handle OR in the WHERE clause",
    "Handle projections with ORDER BY - GH#4937",
    "Handling a variable length relationship and a standard relationship in chain, longer 1",
    "Handling a variable length relationship and a standard relationship in chain, longer 2",
    "Handling a variable length relationship and a standard relationship in chain, single length 1",
    "Handling a variable length relationship and a standard relationship in chain, single length 2",
    "Handling a variable length relationship and a standard relationship in chain, zero length 1",
    "Handling a variable length relationship and a standard relationship in chain, zero length 2",
    "Handling correlated optional matches; first does not match implies second does not match",
    "Handling cyclic patterns",
    "Handling cyclic patterns when separated into two parts",
    "Handling explicit equality of large integer",
    "Handling explicit equality of large integer, non-equal values",
    "Handling explicitly unbounded variable length match",
    "Handling inlined equality of large integer",
    "Handling inlined equality of large integer, non-equal values",
    "Handling lower bounded variable length match 1",
    "Handling lower bounded variable length match 2",
    "Handling lower bounded variable length match 3",
    "Handling optional matches between nulls",
    "Handling optional matches between optionally matched entities",
    "Handling single bounded variable length match 1",
    "Handling single bounded variable length match 2",
    "Handling single bounded variable length match 3",
    "Handling symmetrically bounded variable length match, bounds are one",
    "Handling symmetrically bounded variable length match, bounds are two",
    "Handling symmetrically bounded variable length match, bounds are zero",
    "Handling triadic friend of a friend",
    "Handling unbounded variable length match",
    "Handling upper and lower bounded variable length match 1",
    "Handling upper and lower bounded variable length match 2",
    "Handling upper and lower bounded variable length match, empty interval 1",
    "Handling upper and lower bounded variable length match, empty interval 2",
    "Handling upper bounded variable length match 1",
    "Handling upper bounded variable length match 2",
    "Handling upper bounded variable length match, empty interval",
    "Honour the column name for RETURN items",
    "Indexing into nested literal lists",
    "It is unknown - i.e. null - if a null is equal to a null",
    "It is unknown - i.e. null - if a null is not equal to a null",
    "Keeping used expression 1",
    "LDBC SNB small - GetVertices",
    "LDBC SNB small - Unwind",
    "Limit to two hits with explicit order",
    "Longer pattern with bound nodes",
    "Longer pattern with bound nodes without matches",
    "Matching all nodes",
    "Matching and returning ordered results, with LIMIT",
    "Matching a relationship pattern using a label predicate",
    "Matching a relationship pattern using a label predicate on both sides",
    "Matching disconnected patterns",
    "Matching from null nodes should return no results owing to finding no matches",
    "Matching from null nodes should return no results owing to matches being filtered out",
    "Matching nodes using multiple labels",
    "Matching nodes with many labels",
    "Matching using a relationship that is already bound",
    "Matching using a relationship that is already bound, in conjunction with aggregation",
    "Matching using a simple pattern with label predicate",
    "Matching using list property",
    "Matching using relationship predicate with multiples of the same type",
    "Matching with aggregation",
    "Matching with many predicates and larger pattern",
    "MATCH with OPTIONAL MATCH in longer pattern",
    "Missing node property should become null",
    "Missing relationship property should become null",
    "Multiple WITHs using a predicate and aggregation",
    "Newly-created nodes not visible to preceding MATCH",
    "No dependencies between the query parts",
    "Nodes are not created when aliases are applied to variable names",
    "Nodes are not created when aliases are applied to variable names multiple times",
    "Non-optional matches should not return nulls",
    "Only a single node is created when an alias is applied to a variable name",
    "Only a single node is created when an alias is applied to a variable name multiple times",
    "Optionally matching from null nodes should return null",
    "OPTIONAL MATCH and bound nodes",
    "OPTIONAL MATCH and WHERE",
    "OPTIONAL MATCH on two relationships and WHERE",
    "OPTIONAL MATCH returns null",
    "OPTIONAL MATCH with labels on the optional end node",
    "OPTIONAL MATCH with previously bound nodes",
    "ORDER BY and LIMIT can be used",
    "ORDER BY DESC should order booleans in the expected order",
    "ORDER BY DESC should order floats in the expected order",
    "ORDER BY DESC should order ints in the expected order",
    "ORDER BY DESC should order strings in the expected order",
    "ORDER BY should order booleans in the expected order",
    "ORDER BY should order floats in the expected order",
    "ORDER BY should order ints in the expected order",
    "ORDER BY should order strings in the expected order",
    "ORDER BY with LIMIT",
    "ORDER BY with LIMIT 0 should not generate errors",
    "Ordering with aggregation",
    "Projecting nodes and relationships",
    "Rel type function works as expected",
    "Respect predicates on the OPTIONAL MATCH",
    "Return a boolean",
    "Return a double-quoted string",
    "Return a float",
    "Return an empty list",
    "Return an integer",
    "Return a nonempty list",
    "Return a single-quoted string",
    "Returned columns do not change from using ORDER BY",
    "Returning a list property",
    "Returning an expression",
    "Returning a node property value",
    "Returning a relationship property value",
    "Returning bound nodes that are not part of the pattern",
    "Returning label predicate expression",
    "Return null",
    "Return null when no matches due to inline label predicate",
    "Return two subgraphs with bound undirected relationship",
    "Return two subgraphs with bound undirected relationship and optional relationship",
    "Return vertices and edges",
    "Return vertices and edges with integer properties",
    "Run coalesce",
    "Satisfies the open world assumption, relationships between different nodes",
    "Satisfies the open world assumption, relationships between same nodes",
    "Satisfies the open world assumption, single relationship",
    "Should work when finding multiple elements",
    "Simple CASE",
    "Simple node property predicate",
    "Simple OPTIONAL MATCH on empty graph",
    "Simple variable length pattern",
    "Single WITH using a predicate and aggregation",
    "Start the result from the second row",
    "Support column renaming",
    "Support ordering by a property after being distinct-ified",
    "Support sort and distinct",
    "Three bound nodes pointing to the same node",
    "Three bound nodes pointing to the same node with extra connections",
    "`toBoolean()` on valid literal string",
    "`toString()` handling boolean literal",
    "`toString()` handling inlined boolean",
    "Two bound nodes pointing to the same node",
    "Two OPTIONAL MATCH clauses and WHERE",
    "`type()`",
    "`type()` on mixed null and non-null relationships",
    "`type()` on null relationship",
    "`type()` on two relationships",
    "Undirected match on simple relationship graph",
    "Undirected match on simple relationship graph, count",
    "Unnamed columns",
    "Unwind does not remove variables from scope",
    "Unwinding a list",
    "Unwinding list with duplicates",
    "Unwinding null",
    "Unwinding the empty list",
    "Use multiple MATCH clauses to do a Cartesian product",
    "Use params in pattern matching predicates",
    "Using aliased DISTINCT expression in ORDER BY",
    "Using `labels()` in return clauses",
    "Variable length optional relationships",
    "Variable length optional relationships with bound nodes",
    "Variable length optional relationships with length predicates",
    "Variable length pattern checking labels on endnodes",
    "Variable length patterns and nulls",
    "Variable length pattern with label predicate on both sides",
    "Walk alternative relationships",
    "WHERE after WITH can filter on top of an aggregation",
    "WHERE after WITH should filter results",
    "WITH after OPTIONAL MATCH",
    "Zero-length variable length pattern in the middle of the pattern",
    ""
  ).filter(!_.isEmpty)

  val ignoredScenarios: Set[String] = Set(
    "Many CREATE clauses",
    "Generate the movie graph correctly",
    "Returning multiple node property values",
    // placeholder
    ""
  ).filter(!_.isEmpty)

  val scenarioSet = new TckScenarioSet(selectedFeatures, ignoredScenarios, selectedScenarios)
}

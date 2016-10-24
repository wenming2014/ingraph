/**
 * generated by Xtext 2.10.0
 */
package org.slizaa.neo4j.opencypher.openCypher;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Pattern Element Chain</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.slizaa.neo4j.opencypher.openCypher.PatternElementChain#getRelationshipPattern <em>Relationship Pattern</em>}</li>
 *   <li>{@link org.slizaa.neo4j.opencypher.openCypher.PatternElementChain#getNodePattern <em>Node Pattern</em>}</li>
 * </ul>
 *
 * @see org.slizaa.neo4j.opencypher.openCypher.OpenCypherPackage#getPatternElementChain()
 * @model
 * @generated
 */
public interface PatternElementChain extends EObject
{
  /**
   * Returns the value of the '<em><b>Relationship Pattern</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Relationship Pattern</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Relationship Pattern</em>' containment reference.
   * @see #setRelationshipPattern(RelationshipPattern)
   * @see org.slizaa.neo4j.opencypher.openCypher.OpenCypherPackage#getPatternElementChain_RelationshipPattern()
   * @model containment="true"
   * @generated
   */
  RelationshipPattern getRelationshipPattern();

  /**
   * Sets the value of the '{@link org.slizaa.neo4j.opencypher.openCypher.PatternElementChain#getRelationshipPattern <em>Relationship Pattern</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Relationship Pattern</em>' containment reference.
   * @see #getRelationshipPattern()
   * @generated
   */
  void setRelationshipPattern(RelationshipPattern value);

  /**
   * Returns the value of the '<em><b>Node Pattern</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Node Pattern</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Node Pattern</em>' containment reference.
   * @see #setNodePattern(NodePattern)
   * @see org.slizaa.neo4j.opencypher.openCypher.OpenCypherPackage#getPatternElementChain_NodePattern()
   * @model containment="true"
   * @generated
   */
  NodePattern getNodePattern();

  /**
   * Sets the value of the '{@link org.slizaa.neo4j.opencypher.openCypher.PatternElementChain#getNodePattern <em>Node Pattern</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Node Pattern</em>' containment reference.
   * @see #getNodePattern()
   * @generated
   */
  void setNodePattern(NodePattern value);

} // PatternElementChain

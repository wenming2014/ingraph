/**
 * generated by Xtext 2.10.0
 */
package org.slizaa.neo4j.opencypher.openCypher.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.slizaa.neo4j.opencypher.openCypher.Hint;
import org.slizaa.neo4j.opencypher.openCypher.Match;
import org.slizaa.neo4j.opencypher.openCypher.OpenCypherPackage;
import org.slizaa.neo4j.opencypher.openCypher.Pattern;
import org.slizaa.neo4j.opencypher.openCypher.Where;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Match</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.slizaa.neo4j.opencypher.openCypher.impl.MatchImpl#isOptional <em>Optional</em>}</li>
 *   <li>{@link org.slizaa.neo4j.opencypher.openCypher.impl.MatchImpl#getPattern <em>Pattern</em>}</li>
 *   <li>{@link org.slizaa.neo4j.opencypher.openCypher.impl.MatchImpl#getHints <em>Hints</em>}</li>
 *   <li>{@link org.slizaa.neo4j.opencypher.openCypher.impl.MatchImpl#getWhere <em>Where</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MatchImpl extends ClauseImpl implements Match
{
  /**
   * The default value of the '{@link #isOptional() <em>Optional</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isOptional()
   * @generated
   * @ordered
   */
  protected static final boolean OPTIONAL_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isOptional() <em>Optional</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isOptional()
   * @generated
   * @ordered
   */
  protected boolean optional = OPTIONAL_EDEFAULT;

  /**
   * The cached value of the '{@link #getPattern() <em>Pattern</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPattern()
   * @generated
   * @ordered
   */
  protected Pattern pattern;

  /**
   * The cached value of the '{@link #getHints() <em>Hints</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getHints()
   * @generated
   * @ordered
   */
  protected EList<Hint> hints;

  /**
   * The cached value of the '{@link #getWhere() <em>Where</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getWhere()
   * @generated
   * @ordered
   */
  protected Where where;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected MatchImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return OpenCypherPackage.Literals.MATCH;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isOptional()
  {
    return optional;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setOptional(boolean newOptional)
  {
    boolean oldOptional = optional;
    optional = newOptional;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, OpenCypherPackage.MATCH__OPTIONAL, oldOptional, optional));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Pattern getPattern()
  {
    return pattern;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetPattern(Pattern newPattern, NotificationChain msgs)
  {
    Pattern oldPattern = pattern;
    pattern = newPattern;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OpenCypherPackage.MATCH__PATTERN, oldPattern, newPattern);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPattern(Pattern newPattern)
  {
    if (newPattern != pattern)
    {
      NotificationChain msgs = null;
      if (pattern != null)
        msgs = ((InternalEObject)pattern).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OpenCypherPackage.MATCH__PATTERN, null, msgs);
      if (newPattern != null)
        msgs = ((InternalEObject)newPattern).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OpenCypherPackage.MATCH__PATTERN, null, msgs);
      msgs = basicSetPattern(newPattern, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, OpenCypherPackage.MATCH__PATTERN, newPattern, newPattern));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<Hint> getHints()
  {
    if (hints == null)
    {
      hints = new EObjectContainmentEList<Hint>(Hint.class, this, OpenCypherPackage.MATCH__HINTS);
    }
    return hints;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Where getWhere()
  {
    return where;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetWhere(Where newWhere, NotificationChain msgs)
  {
    Where oldWhere = where;
    where = newWhere;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OpenCypherPackage.MATCH__WHERE, oldWhere, newWhere);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setWhere(Where newWhere)
  {
    if (newWhere != where)
    {
      NotificationChain msgs = null;
      if (where != null)
        msgs = ((InternalEObject)where).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OpenCypherPackage.MATCH__WHERE, null, msgs);
      if (newWhere != null)
        msgs = ((InternalEObject)newWhere).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OpenCypherPackage.MATCH__WHERE, null, msgs);
      msgs = basicSetWhere(newWhere, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, OpenCypherPackage.MATCH__WHERE, newWhere, newWhere));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case OpenCypherPackage.MATCH__PATTERN:
        return basicSetPattern(null, msgs);
      case OpenCypherPackage.MATCH__HINTS:
        return ((InternalEList<?>)getHints()).basicRemove(otherEnd, msgs);
      case OpenCypherPackage.MATCH__WHERE:
        return basicSetWhere(null, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case OpenCypherPackage.MATCH__OPTIONAL:
        return isOptional();
      case OpenCypherPackage.MATCH__PATTERN:
        return getPattern();
      case OpenCypherPackage.MATCH__HINTS:
        return getHints();
      case OpenCypherPackage.MATCH__WHERE:
        return getWhere();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case OpenCypherPackage.MATCH__OPTIONAL:
        setOptional((Boolean)newValue);
        return;
      case OpenCypherPackage.MATCH__PATTERN:
        setPattern((Pattern)newValue);
        return;
      case OpenCypherPackage.MATCH__HINTS:
        getHints().clear();
        getHints().addAll((Collection<? extends Hint>)newValue);
        return;
      case OpenCypherPackage.MATCH__WHERE:
        setWhere((Where)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case OpenCypherPackage.MATCH__OPTIONAL:
        setOptional(OPTIONAL_EDEFAULT);
        return;
      case OpenCypherPackage.MATCH__PATTERN:
        setPattern((Pattern)null);
        return;
      case OpenCypherPackage.MATCH__HINTS:
        getHints().clear();
        return;
      case OpenCypherPackage.MATCH__WHERE:
        setWhere((Where)null);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case OpenCypherPackage.MATCH__OPTIONAL:
        return optional != OPTIONAL_EDEFAULT;
      case OpenCypherPackage.MATCH__PATTERN:
        return pattern != null;
      case OpenCypherPackage.MATCH__HINTS:
        return hints != null && !hints.isEmpty();
      case OpenCypherPackage.MATCH__WHERE:
        return where != null;
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (optional: ");
    result.append(optional);
    result.append(')');
    return result.toString();
  }

} //MatchImpl

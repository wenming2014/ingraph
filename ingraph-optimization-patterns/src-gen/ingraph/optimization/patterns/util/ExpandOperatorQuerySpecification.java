/**
 * Generated from platform:/resource/ingraph-optimization-patterns/src/ingraph/optimization/patterns/Relalg2Rete.vql
 */
package ingraph.optimization.patterns.util;

import com.google.common.collect.Sets;
import ingraph.optimization.patterns.ExpandOperatorMatch;
import ingraph.optimization.patterns.ExpandOperatorMatcher;
import ingraph.optimization.patterns.util.DefaultExpandOperatorQuerySpecification;
import ingraph.optimization.patterns.util.ParentOperatorQuerySpecification;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.viatra.query.runtime.api.ViatraQueryEngine;
import org.eclipse.viatra.query.runtime.api.impl.BaseGeneratedEMFPQuery;
import org.eclipse.viatra.query.runtime.api.impl.BaseGeneratedEMFQuerySpecification;
import org.eclipse.viatra.query.runtime.emf.types.EClassTransitiveInstancesKey;
import org.eclipse.viatra.query.runtime.exception.ViatraQueryException;
import org.eclipse.viatra.query.runtime.matchers.backend.IQueryBackendFactory;
import org.eclipse.viatra.query.runtime.matchers.backend.QueryEvaluationHint;
import org.eclipse.viatra.query.runtime.matchers.psystem.PBody;
import org.eclipse.viatra.query.runtime.matchers.psystem.PVariable;
import org.eclipse.viatra.query.runtime.matchers.psystem.basicdeferred.ExportedParameter;
import org.eclipse.viatra.query.runtime.matchers.psystem.basicenumerables.PositivePatternCall;
import org.eclipse.viatra.query.runtime.matchers.psystem.basicenumerables.TypeConstraint;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.PParameter;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.PParameterDirection;
import org.eclipse.viatra.query.runtime.matchers.psystem.queries.QueryInitializationException;
import org.eclipse.viatra.query.runtime.matchers.tuple.FlatTuple;

/**
 * A pattern-specific query specification that can instantiate ExpandOperatorMatcher in a type-safe way.
 * 
 * @see ExpandOperatorMatcher
 * @see ExpandOperatorMatch
 * 
 */
@SuppressWarnings("all")
public final class ExpandOperatorQuerySpecification extends BaseGeneratedEMFQuerySpecification<ExpandOperatorMatcher> {
  private ExpandOperatorQuerySpecification() {
    super(GeneratedPQuery.INSTANCE);
  }
  
  /**
   * @return the singleton instance of the query specification
   * @throws ViatraQueryException if the pattern definition could not be loaded
   * 
   */
  public static ExpandOperatorQuerySpecification instance() throws ViatraQueryException {
    try{
    	return LazyHolder.INSTANCE;
    } catch (ExceptionInInitializerError err) {
    	throw processInitializerError(err);
    }
  }
  
  @Override
  protected ExpandOperatorMatcher instantiate(final ViatraQueryEngine engine) throws ViatraQueryException {
    return ExpandOperatorMatcher.on(engine);
  }
  
  @Override
  public ExpandOperatorMatcher instantiate() throws ViatraQueryException {
    return ExpandOperatorMatcher.create();
  }
  
  @Override
  public ExpandOperatorMatch newEmptyMatch() {
    return ExpandOperatorMatch.newEmptyMatch();
  }
  
  @Override
  public ExpandOperatorMatch newMatch(final Object... parameters) {
    return ExpandOperatorMatch.newMatch((relalg.ExpandOperator) parameters[0], (relalg.Operator) parameters[1]);
  }
  
  /**
   * Inner class allowing the singleton instance of {@link ExpandOperatorQuerySpecification} to be created 
   * 	<b>not</b> at the class load time of the outer class, 
   * 	but rather at the first call to {@link ExpandOperatorQuerySpecification#instance()}.
   * 
   * <p> This workaround is required e.g. to support recursion.
   * 
   */
  private static class LazyHolder {
    private final static ExpandOperatorQuerySpecification INSTANCE = new ExpandOperatorQuerySpecification();
    
    /**
     * Statically initializes the query specification <b>after</b> the field {@link #INSTANCE} is assigned.
     * This initialization order is required to support indirect recursion.
     * 
     * <p> The static initializer is defined using a helper field to work around limitations of the code generator.
     * 
     */
    private final static Object STATIC_INITIALIZER = ensureInitialized();
    
    public static Object ensureInitialized() {
      INSTANCE.ensureInitializedInternalSneaky();
      return null;
    }
  }
  
  private static class GeneratedPQuery extends BaseGeneratedEMFPQuery {
    private final static ExpandOperatorQuerySpecification.GeneratedPQuery INSTANCE = new GeneratedPQuery();
    
    private final PParameter parameter_pExpandOperator = new PParameter("expandOperator", "relalg.ExpandOperator", new EClassTransitiveInstancesKey((EClass)getClassifierLiteralSafe("http://ingraph/relalg", "ExpandOperator")), PParameterDirection.INOUT);
    
    private final PParameter parameter_pParentOperator = new PParameter("parentOperator", "relalg.Operator", new EClassTransitiveInstancesKey((EClass)getClassifierLiteralSafe("http://ingraph/relalg", "Operator")), PParameterDirection.INOUT);
    
    private final List<PParameter> parameters = Arrays.asList(parameter_pExpandOperator, parameter_pParentOperator);
    
    @Override
    public String getFullyQualifiedName() {
      return "ingraph.optimization.patterns.expandOperator";
    }
    
    @Override
    public List<String> getParameterNames() {
      return Arrays.asList("expandOperator","parentOperator");
    }
    
    @Override
    public List<PParameter> getParameters() {
      return parameters;
    }
    
    @Override
    public Set<PBody> doGetContainedBodies() throws QueryInitializationException {
      setEvaluationHints(new QueryEvaluationHint(null, (IQueryBackendFactory)null));
      Set<PBody> bodies = Sets.newLinkedHashSet();
      try {
      	{
      		PBody body = new PBody(this);
      		PVariable var_expandOperator = body.getOrCreateVariableByName("expandOperator");
      		PVariable var_parentOperator = body.getOrCreateVariableByName("parentOperator");
      		new TypeConstraint(body, new FlatTuple(var_expandOperator), new EClassTransitiveInstancesKey((EClass)getClassifierLiteral("http://ingraph/relalg", "ExpandOperator")));
      		new TypeConstraint(body, new FlatTuple(var_parentOperator), new EClassTransitiveInstancesKey((EClass)getClassifierLiteral("http://ingraph/relalg", "Operator")));
      		body.setSymbolicParameters(Arrays.<ExportedParameter>asList(
      		   new ExportedParameter(body, var_expandOperator, parameter_pExpandOperator),
      		   new ExportedParameter(body, var_parentOperator, parameter_pParentOperator)
      		));
      		// 	find parentOperator(parentOperator, expandOperator)
      		new PositivePatternCall(body, new FlatTuple(var_parentOperator, var_expandOperator), ParentOperatorQuerySpecification.instance().getInternalQueryRepresentation());
      		// 	find defaultExpandOperator(expandOperator)
      		new PositivePatternCall(body, new FlatTuple(var_expandOperator), DefaultExpandOperatorQuerySpecification.instance().getInternalQueryRepresentation());
      		bodies.add(body);
      	}
      	// to silence compiler error
      	if (false) throw new ViatraQueryException("Never", "happens");
      } catch (ViatraQueryException ex) {
      	throw processDependencyException(ex);
      }
      return bodies;
    }
  }
}

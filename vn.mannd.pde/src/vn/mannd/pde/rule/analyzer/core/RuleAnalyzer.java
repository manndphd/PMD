package vn.mannd.pde.rule.analyzer.core;

import java.util.List;

import vn.mannd.pde.loader.configuration.management.RuleAnalyzerFactory;
import vn.mannd.pde.loader.configuration.management.RuleDescriptorStore;
import vn.mannd.pde.preprocessor.ASTNodeCollector;
import vn.mannd.pde.rule.ProblemItem;
import vn.mannd.pde.util.ValidatorUtil;

/**
 * The rule analyzer is an object that analyzes an ASTNode to find out all of
 * potential problems of it.
 * 
 * @author Pham Van Dong
 * 
 * @see RuleDescriptor
 * 
 */
public abstract class RuleAnalyzer
{

	protected static final int DEFAULT_PROBLEMS_LIST_CAPACITY = 10;

	private final RuleDescriptor descriptor;
	private final ASTNodeCollector collector;

	/**
	 * Constructs a rule analyzer.
	 * 
	 * @param collector
	 *            the pre-processor that collected all information of an
	 *            ASTNode.
	 * @throws NullPointerException
	 *             if the collector is null.
	 */
	public RuleAnalyzer(ASTNodeCollector collector)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(collector);

		this.descriptor = RuleDescriptorStore.getInstance().getRuleDescriptor(
			RuleAnalyzerFactory.getInstance().getKey(this.getClass()));
		this.collector = collector;
	}

	/**
	 * Gets the rule's descriptor.
	 * 
	 * @return the rule's descriptor.
	 * @see RuleDescriptor
	 */
	public RuleDescriptor getDescriptor()
	{
		return descriptor;
	}

	/**
	 * Gets the collector.
	 * 
	 * @return the collector.
	 */
	protected ASTNodeCollector getCollector()
	{
		return collector;
	}

	/**
	 * The rule analyzer analyzes and returns a list that contains all problem
	 * items the rule analyzer finds out.
	 * 
	 * @return a list that contains all problem items the rule analyzer finds
	 *         out.
	 */
	public abstract List<ProblemItem> analyze();

	@Override
	public final Object clone() throws java.lang.CloneNotSupportedException
	{
		throw new java.lang.CloneNotSupportedException();
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

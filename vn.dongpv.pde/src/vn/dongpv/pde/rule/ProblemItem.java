package vn.dongpv.pde.rule;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.dom.ASTNode;

import vn.dongpv.pde.rule.analyzer.core.RuleDescriptor;
import vn.dongpv.pde.rule.analyzer.core.RuleType;
import vn.dongpv.pde.rule.checker.core.ConditionalCheckerType;
import vn.dongpv.pde.util.ASTUtil;
import vn.dongpv.pde.util.ValidatorUtil;

/**
 * The problem-item is an item that represents all information about a potential
 * problem ASTNode.
 * 
 * @author Pham Van Dong
 * 
 */
public class ProblemItem
{

	private final RuleDescriptor descriptor;
	private final ConditionalCheckerType checkerType;
	private final ASTNode problemNode;

	/**
	 * Constructs an problem item.
	 * 
	 * @param descriptor
	 *            the descriptor.
	 * @param checkerType
	 *            the checker type.
	 * @param problemNode
	 *            the problem node.
	 * @throws NullPointerException
	 *             if the descriptor, checker type or problem node is null.
	 */
	public ProblemItem(
		RuleDescriptor descriptor,
		ConditionalCheckerType checkerType,
		ASTNode problemNode)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(descriptor, checkerType, problemNode);

		this.descriptor = descriptor;
		this.checkerType = checkerType;
		this.problemNode = problemNode;
	}

	/**
	 * Gets the rule type.
	 * 
	 * @return the rule type.
	 */
	public RuleType getRuleType()
	{
		return descriptor.getType();
	}

	/**
	 * Gets the type of checker.
	 * 
	 * @return the type of checker.
	 */
	public ConditionalCheckerType getCheckerType()
	{
		return checkerType;
	}

	/**
	 * Gets the category of the problem.
	 * 
	 * @return the category of the problem.
	 */
	public String getCategory()
	{
		return descriptor.getCategory();
	}

	/**
	 * Gets the severity of the problem.
	 * 
	 * @return the severity of the problem.
	 */
	public String getSeverity()
	{
		return descriptor.getSeverity();
	}

	/**
	 * Gets the description of the problem.
	 * 
	 * @return the description of the problem.
	 */
	public String getDescription()
	{
		return descriptor.getDescription();
	}

	/**
	 * Gets the start position of problem node.
	 * 
	 * @return the start position of problem node.
	 */
	public int getStartPosition()
	{
		return problemNode.getStartPosition();
	}

	/**
	 * Gets the length of problem node.
	 * 
	 * @return the length of problem node.
	 */
	public int getLength()
	{
		return problemNode.getLength();
	}

	/**
	 * Gets the type of problem node.
	 * 
	 * @return the type of problem node.
	 */
	public int getNodeType()
	{
		return problemNode.getNodeType();
	}

	/**
	 * Gets the resource to which corresponding.
	 * 
	 * @return the resource to which corresponding.
	 */
	public IResource getResource()
	{
		return ASTUtil.getResource(problemNode);
	}

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

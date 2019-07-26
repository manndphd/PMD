package vn.dongpv.pde.rule.analyzer.core;

import vn.dongpv.pde.util.ValidatorUtil;

/**
 * The rule descriptor is an object that contains all need information about a
 * specified rule will be analyzed.
 * 
 * @author Pham Van Dong
 * 
 * @see RuleType
 * @see RuleAnalyzer
 * 
 */
public class RuleDescriptor
{

	private final RuleType type;
	private final String category;
	private final String severity;
	private final String refactoringSupport;
	private final String description;
	private final String reason;
	private final String usageExample;

	/**
	 * Constructs a rule descriptor.
	 * 
	 * @param type
	 *            the rule type.
	 * @param category
	 *            the category of the rule.
	 * @param severity
	 *            the severity.
	 * @param refactoringSupport
	 *            indicates this rule is supported refactoring or not.
	 * @param description
	 *            the description of the rule.
	 * @param reason
	 *            the reason of the rule.
	 * @param usageExample
	 *            the usage example of the rule.
	 * @throws NullPointerException
	 *             if the rule type, description or usage example is null.
	 */
	public RuleDescriptor(
		RuleType type,
		String category,
		String severity,
		String refactoringSupport,
		String description,
		String reason,
		String usageExample)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(type, severity,
			refactoringSupport, description, usageExample);

		this.type = type;
		this.category = category;
		this.severity = severity;
		this.refactoringSupport = refactoringSupport;
		this.description = description;
		this.reason = reason;
		this.usageExample = usageExample;
	}

	/**
	 * Gets the rule type.
	 * 
	 * @return the rule type.
	 */
	public RuleType getType()
	{
		return type;
	}

	/**
	 * Gets the category of the rule.
	 * 
	 * @return the category of the rule.
	 */
	public String getCategory()
	{
		return category;
	}

	/**
	 * Gets the severity of the rule.
	 * 
	 * @return the severity of the rule.
	 */
	public String getSeverity()
	{
		return severity;
	}

	/**
	 * Gets the refactoring supported indication.
	 * 
	 * @return the string that indicates this rule is supported refactoring or
	 *         not.
	 */
	public String getRefactoringSupport()
	{
		return refactoringSupport;
	}

	/**
	 * Gets the description.
	 * 
	 * @return the description.
	 */
	public String getDescription()
	{
		return description;
	}

	/**
	 * Gets the reason.
	 * 
	 * @return the reason.
	 */
	public String getReason()
	{
		return reason;
	}

	/**
	 * Gets the usage example.
	 * 
	 * @return the usage example.
	 */
	public String getUsageExample()
	{
		return usageExample;
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

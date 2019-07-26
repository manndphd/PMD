package vn.dongpv.pde.rule.checker.core;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;

import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.loader.configuration.management.ConditionalCheckerMappingStore;
import vn.dongpv.pde.refactoring.change.creator.core.ChangeCreator;
import vn.dongpv.pde.refactoring.core.ProblemRefactoring;
import vn.dongpv.pde.rule.analyzer.core.RuleAnalyzer;
import vn.dongpv.pde.util.ValidatorUtil;

/**
 * The conditional checker is an object that checks an {@link ASTNode} whether
 * violates a condition or not. It receives a parameter from
 * {@link RuleAnalyzer} or {@link ProblemRefactoring}.
 * 
 * @author Pham Van Dong
 * 
 * @see ConditionalCheckerType
 * 
 */
public abstract class ConditionalChecker
{
	public static final String PARSED_ROOT_NODE = "PARSED_ROOT_NODE";
	public static final String PARSED_NODE = "PARSED_NODE";

	private final ConditionalCheckerType type;
	private final CompositeParameter parameter;
	private final CompositeParameter additionalParameter;
	private boolean allowProvideMoreParameters;

	/**
	 * Constructs a conditional checker.
	 * 
	 * @param parameter
	 *            the parameter which contains all needed information to check.
	 * @throws NullPointerException
	 *             if the parameter is null.
	 */
	public ConditionalChecker(CompositeParameter parameter)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(parameter);

		this.type = ConditionalCheckerMappingStore.
			getInstance().getKey(this.getClass());
		this.parameter = parameter;
		this.additionalParameter = new CompositeParameter();
		this.allowProvideMoreParameters = false;
	}

	/**
	 * Gets the additional parameter for creating session.
	 * 
	 * @return the additional parameter that is needed for {@link ChangeCreator}
	 *         creating the {@link Change} in {@link RefactoringWizard}.
	 */
	public CompositeParameter getAdditionalParameter()
	{
		return additionalParameter;
	}

	/**
	 * Gets the checker type.
	 * 
	 * @return the checker type.
	 */
	public ConditionalCheckerType getType()
	{
		return type;
	}

	/**
	 * Indicates this checker allow push needed information to the additional
	 * parameter.
	 * 
	 * @return true if this checker allow push needed information to the
	 *         additional parameter.
	 */
	public boolean isAllowProvideMoreParameters()
	{
		return allowProvideMoreParameters;
	}

	/**
	 * Sets the checker's mode. If the input boolean is true, checker will push
	 * needed information for creating session. Otherwise, this checker will
	 * check the ASTNode only.
	 * 
	 * @param allowProvideMoreParameters
	 *            the checker's mode.
	 */
	public void setAllowProvideMoreParameters(boolean allowProvideMoreParameters)
	{
		this.allowProvideMoreParameters = allowProvideMoreParameters;
	}

	/**
	 * Gets the input parameter.
	 * 
	 * @return the input parameter.
	 */
	protected CompositeParameter getParameter()
	{
		return parameter;
	}

	/**
	 * Indicates the ASTNode violates the condition or not.
	 * 
	 * @return true if the ASTNode violates the condition.
	 */
	public final boolean isViolated()
	{
		try
		{
			if (!checkParameters())
			{
				return false;
			}

			return checkDetails();
		}
		catch (final Exception e)
		{
			e.printStackTrace();

			return false;
		}
	}

	/**
	 * Checks all got parameters.
	 * 
	 * @return true if all parameters were got be valid or not.
	 */
	protected abstract boolean checkParameters();

	/**
	 * Checks whether the ASTNode violates the condition or not.
	 * 
	 * @return true if the ASTNode violates the condition.
	 */
	protected abstract boolean checkDetails();

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

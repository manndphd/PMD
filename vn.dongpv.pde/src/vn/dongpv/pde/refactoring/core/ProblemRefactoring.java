package vn.dongpv.pde.refactoring.core;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.NullChange;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ui.views.markers.MarkerItem;

import vn.dongpv.pde.PluginConstants;
import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.refactoring.change.creator.core.ChangeCreator;
import vn.dongpv.pde.rule.checker.core.ConditionalChecker;
import vn.dongpv.pde.util.TypeUtil;
import vn.dongpv.pde.util.ValidatorUtil;

/**
 * The problem-refactoring is an template object that receives input parameter
 * from {@link MarkerItem}, then creates {@link ConditionalChecker} and
 * {@link ChangeCreator} in order to check and create {@link Change} if needed.
 * 
 * @author Pham Van Dong
 * 
 * @see ProblemRefactoringKit
 * @see ConditionalChecker
 * @see ChangeCreator
 * 
 */
public class ProblemRefactoring extends Refactoring
{

	public static final String PARSED_ROOT_NODE = "PARSED_ROOT_NODE";
	public static final String PARSED_NODE = "PARSED_NODE";

	public static final String I_COMPILATION_UNIT = "I_COMPILATION_UNIT";
	public static final String REFACTORING_KIT = "REFACTORING_KIT";

	public static final String INVALID_PARAMETER_MSG = "The input parameter is not valid.";
	public static final String PROBLEM_NOT_EXIST_MSG = "This problem doesn't exist anymore. Please re-analyze or delete this problem item!";
	public static final String ERROR_OCCURRED_MSG = "An error occurred! Please try again later!";

	private final ASTNode rootNode;
	private final ASTNode problemNode;
	private final ICompilationUnit icu;
	private final ProblemRefactoringKit kit;

	private CompositeParameter additionalParameter;

	/**
	 * Constructs a problem refactoring.
	 * 
	 * @param parameter
	 *            the input parameter.
	 * @throws NullPointerException
	 *             if the parameter is null.
	 */
	public ProblemRefactoring(CompositeParameter parameter)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(parameter);

		icu = TypeUtil.cast(parameter.getParameter(I_COMPILATION_UNIT));
		rootNode = TypeUtil.cast(parameter.getParameter(ConditionalChecker.PARSED_ROOT_NODE));
		problemNode = TypeUtil.cast(parameter.getParameter(ConditionalChecker.PARSED_NODE));
		kit = TypeUtil.cast(parameter.getParameter(REFACTORING_KIT));
		additionalParameter = new CompositeParameter();
	}

	@Override
	public String getName()
	{
		return (kit == null) ?
			PluginConstants.EMPTY_STRING :
			kit.getType().toString();
	}

	@Override
	public RefactoringStatus checkInitialConditions(IProgressMonitor pm)
		throws CoreException, OperationCanceledException
	{
		final RefactoringStatus status = new RefactoringStatus();
		if (!ValidatorUtil.notNull(rootNode, problemNode, kit))
		{
			status.addFatalError(INVALID_PARAMETER_MSG);
		}
		else
		{
			final CompositeParameter parameter = new CompositeParameter();
			parameter.addParameter(ConditionalChecker.PARSED_ROOT_NODE, rootNode);
			parameter.addParameter(ConditionalChecker.PARSED_NODE, problemNode);

			try
			{
				// Creates checker via ProblemRefactoringKit
				final ConditionalChecker checker = kit.newChecker(parameter);
				checker.setAllowProvideMoreParameters(true);

				if (!checker.isViolated())
				{
					status.addFatalError(PROBLEM_NOT_EXIST_MSG);
				}
				else
				{
					additionalParameter = checker.getAdditionalParameter();
				}
			}
			catch (final Exception e)
			{
				status.addFatalError(ERROR_OCCURRED_MSG);
			}
		}
		return status;
	}

	@Override
	public RefactoringStatus checkFinalConditions(IProgressMonitor pm)
		throws CoreException, OperationCanceledException
	{
		return new RefactoringStatus();
	}

	@Override
	public Change createChange(IProgressMonitor pm)
		throws CoreException, OperationCanceledException
	{
		final CompositeParameter parameter = new CompositeParameter();
		parameter.addParameter(ChangeCreator.I_COMPILATION_UNIT, icu);
		parameter.addParameter(ChangeCreator.PARSED_ROOT_NODE, rootNode);
		parameter.addParameter(ChangeCreator.PARSED_NODE, problemNode);
		parameter.addParameter(additionalParameter);

		try
		{
			// Creates change creator via ProblemRefactoringKit
			final ChangeCreator creator = kit.newChangeCreator(parameter);
			return creator.getChange();
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			return new NullChange();
		}
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

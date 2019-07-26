package vn.mannd.pde.refactoring.change.creator.core;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.refactoring.CompilationUnitChange;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.NullChange;

import vn.mannd.pde.adt.CompositeParameter;
import vn.mannd.pde.loader.configuration.management.ChangeCreatorMappingStore;
import vn.mannd.pde.refactoring.core.ProblemRefactoring;
import vn.mannd.pde.rule.checker.core.ConditionalChecker;
import vn.mannd.pde.util.TypeUtil;
import vn.mannd.pde.util.ValidatorUtil;

/**
 * The change creator is an object that creates {@link Change} for the specified
 * situation. It receives the information from {@link ProblemRefactoring} and
 * {@link ConditionalChecker}.
 * 
 * @author Pham Van Dong
 * 
 * @see ChangeCreatorType
 * @see ProblemRefactoring
 * 
 */
public abstract class ChangeCreator
{

	public static final String PARSED_ROOT_NODE = "PARSED_ROOT_NODE";
	public static final String PARSED_NODE = "PARSED_NODE";

	public static final String I_COMPILATION_UNIT = "I_COMPILATION_UNIT";

	private final ChangeCreatorType type;
	private final CompositeParameter parameter;
	private final ICompilationUnit icu;
	private final ASTNode rootNode;

	/**
	 * Constructs a change creator.
	 * 
	 * @param parameter
	 *            the parameter that contains all needed information.
	 * @throws NullPointerException
	 *             if the parameter is null.
	 */
	public ChangeCreator(CompositeParameter parameter)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(parameter);

		this.parameter = parameter;
		this.icu = TypeUtil.cast(parameter.getParameter(I_COMPILATION_UNIT));
		this.rootNode = TypeUtil.cast(parameter.getParameter(PARSED_ROOT_NODE));

		this.type = ChangeCreatorMappingStore.getInstance().getKey(this.getClass());
	}

	/**
	 * Gets the type of this change creator.
	 * 
	 * @return the type of this change creator.
	 */
	public ChangeCreatorType getType()
	{
		return type;
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
	 * Gets the created change.
	 * 
	 * @return the {@link Change} this creator created.
	 */
	public final Change getChange()
	{
		if (!checkSuperParameters() || !checkParameters())
		{
			return new NullChange();
		}

		final ASTRewrite rewrite = ASTRewrite.create(rootNode.getAST());
		createChange(rewrite);
		try
		{
			return writeChange(rewrite);
		}
		catch (final Exception e)
		{
			return new NullChange();
		}
	}

	private boolean checkSuperParameters()
	{
		return ValidatorUtil.notNull(icu, rootNode);
	}

	private Change writeChange(ASTRewrite rewrite)
		throws JavaModelException, IllegalArgumentException
	{
		final CompilationUnitChange change = new CompilationUnitChange("none", icu);
		change.setEdit(rewrite.rewriteAST());
		return change;
	}

	/**
	 * Checks all got parameters be valid or not.
	 * 
	 * @return true if all got parameters be valid.
	 */
	protected abstract boolean checkParameters();

	/**
	 * Creates real-change.
	 * 
	 * @param rewrite
	 *            the {@link ASTRewrite} that need to create change.
	 */
	protected abstract void createChange(ASTRewrite rewrite);

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

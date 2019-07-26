package vn.dongpv.pde.refactoring.change.creator;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.refactoring.change.creator.core.ChangeCreator;
import vn.dongpv.pde.util.TypeUtil;
import vn.dongpv.pde.util.ValidatorUtil;
import vn.dongpv.pde.rule.checker.AvoidMethodCallsInLoopChecker;


public class AvoidMethodCallsInLoopChangeCreator extends ChangeCreator
{

	public static final String VALUE_TYPE = "VALUE_TYPE";
	public static final String METHOD_INVOCATION = "METHOD_INVOCATION";
	public static final String LIMIT = "limit";

	private final ASTNode rootNode;
	private final ASTNode loopStatement;
	private final MethodInvocation invocation;
	private final String valueType;

	public AvoidMethodCallsInLoopChangeCreator(CompositeParameter parameter)
		throws NullPointerException
	{
		super(parameter);

		rootNode = TypeUtil.cast(parameter.getParameter(PARSED_ROOT_NODE));
		loopStatement = TypeUtil.cast(parameter.getParameter(PARSED_NODE));
		invocation = TypeUtil.cast(parameter.getParameter(METHOD_INVOCATION));
		valueType = TypeUtil.cast(parameter.getParameter(VALUE_TYPE));
	}

	@Override
	protected boolean checkParameters()
	{
		return ValidatorUtil.notNull(rootNode, loopStatement, invocation, valueType);
	}

	@Override
	protected void createChange(ASTRewrite rewrite)
	{
		createNewDeclStatement(rewrite);
		replaceOldExpression(rewrite);
	}

	private void createNewDeclStatement(ASTRewrite rewrite)
	{
		final VariableDeclarationFragment frag =
			rootNode.getAST().newVariableDeclarationFragment();
		frag.setName(
			rootNode.getAST().newSimpleName(LIMIT));
		frag.setInitializer(
			(Expression)ASTNode.copySubtree(rootNode.getAST(), invocation));

		final VariableDeclarationStatement statement =
			rootNode.getAST().newVariableDeclarationStatement(frag);

		if (valueType.equalsIgnoreCase(AvoidMethodCallsInLoopChecker.INT))
		{
			statement.setType(rootNode.getAST().newPrimitiveType(PrimitiveType.INT));
		}
		else if (valueType.equalsIgnoreCase(AvoidMethodCallsInLoopChecker.FLOAT))
		{
			statement.setType(rootNode.getAST().newPrimitiveType(PrimitiveType.FLOAT));
		}
		else if (valueType.equalsIgnoreCase(AvoidMethodCallsInLoopChecker.DOUBLE))
		{
			statement.setType(rootNode.getAST().newPrimitiveType(PrimitiveType.DOUBLE));
		}

		final ListRewrite listRewrite = rewrite.getListRewrite(
			loopStatement.getParent(), Block.STATEMENTS_PROPERTY);
		listRewrite.insertBefore(statement, loopStatement, null);
	}

	private void replaceOldExpression(ASTRewrite rewrite)
	{
		rewrite.replace(invocation, rootNode.getAST().newSimpleName(LIMIT), null);
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

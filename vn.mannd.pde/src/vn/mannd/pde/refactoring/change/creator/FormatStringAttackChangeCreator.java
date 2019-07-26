package vn.mannd.pde.refactoring.change.creator;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import vn.mannd.pde.adt.CompositeParameter;
import vn.mannd.pde.refactoring.change.creator.core.ChangeCreator;
import vn.mannd.pde.util.TypeUtil;
import vn.mannd.pde.util.ValidatorUtil;

public class FormatStringAttackChangeCreator extends ChangeCreator
{

	private static final String EXCEPTION_CLASS = "Exception";
	private static final String EXCEPTION_PARAM_NAME = "e";
	private static final String PRINT_STACK_TRACE_METHOD_NAME = "printStackTrace";

	private final ASTNode rootNode;
	private final MethodInvocation methodInvocation;

	public FormatStringAttackChangeCreator(CompositeParameter parameter)
		throws NullPointerException
	{
		super(parameter);

		rootNode = TypeUtil.cast(parameter.getParameter(PARSED_ROOT_NODE));
		methodInvocation = TypeUtil.cast(parameter.getParameter(PARSED_NODE));
	}

	@Override
	protected boolean checkParameters()
	{
		return ValidatorUtil.notNull(methodInvocation, rootNode);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void createChange(ASTRewrite rewrite)
	{
		final ASTNode parent = methodInvocation.getParent();
		if (parent.getNodeType() != ASTNode.EXPRESSION_STATEMENT)
		{
			return;
		}

		final ExpressionStatement statement = (ExpressionStatement)
			ASTNode.copySubtree(rootNode.getAST(), parent);

		final SingleVariableDeclaration svd =
			rootNode.getAST().newSingleVariableDeclaration();
		svd.setType(rootNode.getAST().newSimpleType(
			rootNode.getAST().newSimpleName(EXCEPTION_CLASS)));
		svd.setName(rootNode.getAST().newSimpleName(EXCEPTION_PARAM_NAME));

		final CatchClause catchClause = rootNode.getAST().newCatchClause();
		catchClause.setException(svd);

		final MethodInvocation mi = rootNode.getAST().newMethodInvocation();
		mi.setExpression(rootNode.getAST().newSimpleName(EXCEPTION_PARAM_NAME));
		mi.setName(rootNode.getAST().newSimpleName(PRINT_STACK_TRACE_METHOD_NAME));

		catchClause.getBody().statements().add(
			rootNode.getAST().newExpressionStatement(mi));

		final TryStatement tryStatement = rootNode.getAST().newTryStatement();
		tryStatement.catchClauses().add(catchClause);
		tryStatement.getBody().statements().add(statement);

		rewrite.replace(parent, tryStatement, null);
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

package vn.mannd.pde.refactoring.change.creator;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import vn.mannd.pde.PluginConstants;
import vn.mannd.pde.adt.CompositeParameter;
import vn.mannd.pde.refactoring.change.creator.core.ChangeCreator;
import vn.mannd.pde.util.ASTUtil;
import vn.mannd.pde.util.TypeUtil;
import vn.mannd.pde.util.ValidatorUtil;

public class UseStringLengthCompareEmptyStringChangeCreator extends ChangeCreator
{

	public static final String STRING_LENGTH_METHOD_NAME = "length";

	private final ASTNode rootNode;
	private final MethodInvocation methodInvocation;

	public UseStringLengthCompareEmptyStringChangeCreator(CompositeParameter parameter)
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
	protected void createChange(ASTRewrite rewrite)
	{
		if (hasExclamationOperatorAtFront())
		{
			createParenthesizedExpression(rewrite);
		}
		else
		{
			createInfixExpression(rewrite);
		}
	}

	private boolean hasExclamationOperatorAtFront()
	{
		final ASTNode parent = methodInvocation.getParent();
		if (parent.getNodeType() == ASTNode.PREFIX_EXPRESSION)
		{
			final PrefixExpression prefixParent = (PrefixExpression)parent;
			if (prefixParent.getOperator() == PrefixExpression.Operator.NOT)
			{
				return true;
			}
		}
		return false;
	}

	private void createParenthesizedExpression(ASTRewrite rewrite)
	{
		final InfixExpression infix = createInfixExpression();
		final ParenthesizedExpression pe =
			ASTUtil.createParenthesizedExpression(rootNode.getAST(), infix);
		rewrite.replace(methodInvocation, pe, null);
	}

	private void createInfixExpression(ASTRewrite rewrite)
	{
		final InfixExpression infix = createInfixExpression();
		rewrite.replace(methodInvocation, infix, null);
	}

	private InfixExpression createInfixExpression()
	{
		final MethodInvocation invocation = ASTUtil.createMethodInvocation(
			rootNode.getAST(),
			(Expression)ASTNode.copySubtree(rootNode.getAST(), methodInvocation.getExpression()),
			STRING_LENGTH_METHOD_NAME,
			null);

		final NumberLiteral zero = ASTUtil.createNumberLiteral(rootNode.getAST(),
			PluginConstants.ZERO_STRING);

		final InfixExpression infix = ASTUtil.createInfixExpression(
			rootNode.getAST(),
			invocation,
			zero,
			InfixExpression.Operator.EQUALS);

		return infix;
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}
}

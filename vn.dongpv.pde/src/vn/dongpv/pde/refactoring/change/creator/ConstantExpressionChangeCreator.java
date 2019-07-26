package vn.dongpv.pde.refactoring.change.creator;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import vn.dongpv.pde.PluginConstants;
import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.refactoring.change.creator.core.ChangeCreator;
import vn.dongpv.pde.util.ASTUtil;
import vn.dongpv.pde.util.TypeUtil;
import vn.dongpv.pde.util.ValidatorUtil;

public class ConstantExpressionChangeCreator extends ChangeCreator
{

	public static final String REMOVED_EXPS = "REMOVED_EXPS";
	public static final String VALUE_TYPE = "VALUE_TYPE";
	public static final String VALUE = "VALUE";

	private final ASTNode rootNode;
	private final InfixExpression infixExpression;
	private final String valueType;
	private String value;

	private final List<Expression> removedExpressions;

	public ConstantExpressionChangeCreator(CompositeParameter parameter)
		throws NullPointerException
	{
		super(parameter);

		rootNode = TypeUtil.cast(parameter.getParameter(PARSED_ROOT_NODE));
		infixExpression = TypeUtil.cast(parameter.getParameter(PARSED_NODE));
		valueType = TypeUtil.cast(parameter.getParameter(VALUE_TYPE));
		value = TypeUtil.cast(parameter.getParameter(VALUE));
		removedExpressions = TypeUtil.cast(parameter.getParameter(REMOVED_EXPS));
	}

	@Override
	protected boolean checkParameters()
	{
		return ValidatorUtil.notNull(
			rootNode, infixExpression, valueType, value, removedExpressions);
	}

	@Override
	@SuppressWarnings("unchecked")
	protected void createChange(ASTRewrite rewrite)
	{
		final Expression replacement = createReplacementNode();
		if (replacement != null)
		{
			final int removedSize = removedExpressions.size();
			for (int i = 1; i < removedSize; i += 1)
			{
				rewrite.remove(removedExpressions.get(i), null);
			}

			if (removedExpressions.get(0) == infixExpression.getLeftOperand())
			{
				final int extendedSize = infixExpression.extendedOperands().size();
				final int infixSize = 2 + extendedSize;
				if (infixSize == removedSize)
				{
					rewrite.replace(infixExpression, replacement, null);
				}
				else
				{
					Expression rightOperand = null;
					int lastIndex = -1;
					if (removedExpressions.get(removedSize - 1) == infixExpression.getRightOperand())
					{
						rightOperand = (Expression)ASTNode.copySubtree(
							rootNode.getAST(),
							(ASTNode)infixExpression.extendedOperands().get(0));
					}
					else
					{
						lastIndex = infixExpression.extendedOperands().
							lastIndexOf(removedExpressions.get(removedSize - 1));
						rightOperand = (Expression)ASTNode.copySubtree(
							rootNode.getAST(),
							(ASTNode)infixExpression.extendedOperands().get(lastIndex + 1));
					}

					final InfixExpression other = ASTUtil.createInfixExpression(
						rootNode.getAST(),
						replacement,
						rightOperand,
						infixExpression.getOperator());

					for (int i = lastIndex + 2; i < extendedSize; ++i)
					{
						other.extendedOperands().add(ASTNode.copySubtree(
							rootNode.getAST(),
							(ASTNode)infixExpression.extendedOperands().get(i)));
					}

					rewrite.replace(infixExpression, other, null);
				}
			}
			else
			{
				rewrite.replace(removedExpressions.get(0), replacement, null);
			}
		}
	}

	private Expression createReplacementNode()
	{
		if (valueType.equalsIgnoreCase(PluginConstants.BOOLEAN_NAME))
		{
			return ASTUtil.createBooleanLiteral(
				rootNode.getAST(), Boolean.parseBoolean(value));
		}

		if (valueType.equalsIgnoreCase(PluginConstants.NUMBER_NAME))
		{
			final String expType = infixExpression.resolveTypeBinding().getQualifiedName();
			if (expType.equalsIgnoreCase(PluginConstants.INT) ||
				expType.equalsIgnoreCase(PluginConstants.LONG))
			{
				try
				{
					Integer.parseInt(value);
				}
				catch (final NumberFormatException e)
				{
					e.printStackTrace();

					value += "L";
				}
			}

			return ASTUtil.createNumberLiteral(rootNode.getAST(), value);
		}

		if (valueType.equalsIgnoreCase(PluginConstants.STRING_NAME))
		{
			return ASTUtil.createStringLiteral(rootNode.getAST(), value);
		}

		return null;
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

package vn.dongpv.pde.rule.checker;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;
import org.eclipse.jdt.core.dom.NumberLiteral;

import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.refactoring.change.creator.UseShiftOperatorsChangeCreator;
import vn.dongpv.pde.rule.checker.core.ConditionalChecker;
import vn.dongpv.pde.util.MathUtil;
import vn.dongpv.pde.util.TypeUtil;
import vn.dongpv.pde.util.ValidatorUtil;

public class UseShiftOperatorsChecker extends ConditionalChecker
{

	public static final String INT = "int";

	private final ASTNode rootNode;
	private final InfixExpression infixExpression;

	public UseShiftOperatorsChecker(CompositeParameter parameter)
		throws NullPointerException
	{
		super(parameter);

		rootNode = TypeUtil.cast(parameter.getParameter(PARSED_ROOT_NODE));
		infixExpression = TypeUtil.cast(parameter.getParameter(PARSED_NODE));
	}

	@Override
	protected boolean checkParameters()
	{
		return ValidatorUtil.notNull(rootNode, infixExpression);
	}

	@Override
	protected boolean checkDetails()
	{
		if ((infixExpression.getOperator() != Operator.TIMES) &&
			(infixExpression.getOperator() != Operator.DIVIDE))
		{
			return false;
		}

		final Expression rightExpression = infixExpression.getRightOperand();
		if (rightExpression.getNodeType() != ASTNode.NUMBER_LITERAL)
		{
			return false;
		}

		final NumberLiteral literal = (NumberLiteral)rightExpression;

		final ITypeBinding binding = literal.resolveTypeBinding();
		if (!binding.getQualifiedName().equalsIgnoreCase(INT))
		{
			return false;
		}

		try
		{
			final int value = Integer.parseInt(literal.getToken());
			if (MathUtil.isPowerOfTwo(value))
			{
				if (isAllowProvideMoreParameters())
				{
					getAdditionalParameter().addParameter(
						UseShiftOperatorsChangeCreator.VALUE,
						String.valueOf(MathUtil.computeExponent(value, 2)));
				}
				return true;
			}

			return false;
		}
		catch (final NumberFormatException e)
		{
			e.printStackTrace();

			return false;
		}
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

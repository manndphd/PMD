package vn.dongpv.pde.rule.checker;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;

import vn.dongpv.pde.PluginConstants;
import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.refactoring.change.creator.UseSingleQuotesWhenConcatenatingCharacterToStringChangeCreator;
import vn.dongpv.pde.rule.checker.core.ConditionalChecker;
import vn.dongpv.pde.util.TypeUtil;
import vn.dongpv.pde.util.ValidatorUtil;

public class UseSingleQuotesWhenConcatenatingCharacterToStringChecker
	extends ConditionalChecker
{

	private final ASTNode rootNode;
	private final InfixExpression infixExpression;

	public UseSingleQuotesWhenConcatenatingCharacterToStringChecker(
		CompositeParameter parameter)
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
		if (infixExpression.getOperator() != Operator.PLUS)
		{
			return false;
		}

		final Expression leftExpression = infixExpression.getLeftOperand();
		final Expression rightExpression = infixExpression.getRightOperand();

		// str2 = str1 + "a"

		if (leftExpression instanceof SimpleName)
		{
			if (rightExpression instanceof StringLiteral)
			{
				if (isValid(
					(SimpleName)leftExpression, (StringLiteral)rightExpression))
				{
					saveStateToParameterWithOneChar(2);
					return true;
				}
			}
		}

		if (leftExpression instanceof StringLiteral)
		{
			// str2 = "abc" + "a"

			// str2 = "a" + "abc"

			if (rightExpression instanceof StringLiteral)
			{
				return isValid((StringLiteral)leftExpression,
					(StringLiteral)rightExpression);
			}

			// str2 = "a" + str1

			if (rightExpression instanceof SimpleName)
			{
				if (isValid(
					(SimpleName)rightExpression, (StringLiteral)leftExpression))
				{
					saveStateToParameterWithOneChar(1);
					return true;
				}
			}
		}

		return false;
	}

	private boolean isValid(SimpleName exp1, StringLiteral exp2)
	{
		final IVariableBinding binding = TypeUtil.cast(exp1.resolveBinding());
		if (binding == null)
		{
			return false;
		}

		// SimpleName must be String type

		final String typeName = binding.getType().getQualifiedName();
		if (!typeName.equalsIgnoreCase(PluginConstants.STRING_CLASS_NAME))
		{
			return false;
		}

		// Checks the literal has length equals 1 or not

		final String literal = exp2.getLiteralValue();
		if (literal.length() == 1)
		{
			return true;
		}

		return false;
	}

	private boolean isValid(StringLiteral exp1, StringLiteral exp2)
	{
		// Checks the literal(s) has/have length equals 1 or not

		// Checks the first literal

		boolean firstLiteralHasLengthEqualsOne = false;

		final String literal1 = exp1.getLiteralValue();
		if (literal1.length() == 1)
		{
			firstLiteralHasLengthEqualsOne = true;
		}

		// Checks the second literal

		boolean secondLiteralHasLengthEqualsOne = false;

		final String literal2 = exp2.getLiteralValue();
		if (literal2.length() == 1)
		{
			secondLiteralHasLengthEqualsOne = true;
		}

		if (!firstLiteralHasLengthEqualsOne && !secondLiteralHasLengthEqualsOne)
		{
			return false;
		}

		if (firstLiteralHasLengthEqualsOne == secondLiteralHasLengthEqualsOne)
		{
			saveStateToParameterWithTwoChars();
		}
		else if (firstLiteralHasLengthEqualsOne)
		{
			saveStateToParameterWithOneChar(1);
		}
		else
		{
			saveStateToParameterWithOneChar(2);
		}

		return true;
	}

	private void saveStateToParameterWithOneChar(int pos)
	{
		saveStateToParameter(1, pos);
	}

	private void saveStateToParameterWithTwoChars()
	{
		saveStateToParameter(2, 0);
	}

	private void saveStateToParameter(int nChars, int pos)
	{
		if (isAllowProvideMoreParameters())
		{
			getAdditionalParameter().addParameter(
				UseSingleQuotesWhenConcatenatingCharacterToStringChangeCreator.NUMBER_OF_CHARACTERS,
				nChars);

			if (nChars == 1)
			{
				getAdditionalParameter().addParameter(
					UseSingleQuotesWhenConcatenatingCharacterToStringChangeCreator.POSITION,
					pos);
			}
		}
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

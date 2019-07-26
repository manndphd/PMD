package vn.dongpv.pde.rule.checker;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;

import vn.dongpv.pde.PluginConstants;
import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.refactoring.change.creator.ConstantExpressionChangeCreator;
import vn.dongpv.pde.rule.checker.core.ConditionalChecker;
import vn.dongpv.pde.util.ASTUtil;
import vn.dongpv.pde.util.TypeUtil;
import vn.dongpv.pde.util.ValidatorUtil;

public class ConstantExpressionChecker extends ConditionalChecker
{

	private static final int DEFAULT_CAPACITY = 5;

	private final ASTNode rootNode;
	private final InfixExpression infixExpression;

	private final List<Expression> removedExpressions;

	public ConstantExpressionChecker(CompositeParameter parameter)
		throws NullPointerException
	{
		super(parameter);

		rootNode = TypeUtil.cast(parameter.getParameter(PARSED_ROOT_NODE));
		infixExpression = TypeUtil.cast(parameter.getParameter(PARSED_NODE));
		removedExpressions = new ArrayList<Expression>(DEFAULT_CAPACITY);
	}

	@Override
	protected boolean checkParameters()
	{
		return ValidatorUtil.notNull(rootNode, infixExpression);
	}

	@Override
	protected boolean checkDetails()
	{
		final String result =
			ASTUtil.evaluateExpression(infixExpression, removedExpressions);
		if (!result.equalsIgnoreCase(PluginConstants.EMPTY_STRING))
		{
			saveStateToParameter(result);
			return true;
		}

		return false;
	}

	private void saveStateToParameter(String value)
	{
		if (isAllowProvideMoreParameters())
		{
			final String expType =
				infixExpression.resolveTypeBinding().getQualifiedName();

			String valueType = PluginConstants.EMPTY_STRING;

			if (expType.equalsIgnoreCase(PluginConstants.BOOL))
			{
				valueType = PluginConstants.BOOLEAN_NAME;
			}
			else if (expType.equalsIgnoreCase(PluginConstants.STRING_CLASS_NAME))
			{
				valueType = PluginConstants.STRING_NAME;
			}
			else
			{
				valueType = PluginConstants.NUMBER_NAME;
			}

			getAdditionalParameter().addParameter(
				ConstantExpressionChangeCreator.VALUE_TYPE, valueType);

			getAdditionalParameter().addParameter(
				ConstantExpressionChangeCreator.VALUE, value);

			getAdditionalParameter().addParameter(
				ConstantExpressionChangeCreator.REMOVED_EXPS, removedExpressions);
		}
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

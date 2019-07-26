package vn.mannd.pde.rule.checker;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IfStatement;

import vn.mannd.pde.adt.CompositeParameter;
import vn.mannd.pde.refactoring.change.creator.AvoidUnnecessaryIfChangeCreator;
import vn.mannd.pde.rule.checker.core.ConditionalChecker;
import vn.mannd.pde.util.ASTUtil;
import vn.mannd.pde.util.TypeUtil;
import vn.mannd.pde.util.ValidatorUtil;

public class AvoidUnnecessaryIfChecker extends ConditionalChecker
{

	private final ASTNode rootNode;
	private final IfStatement ifStatement;

	public AvoidUnnecessaryIfChecker(CompositeParameter parameter)
		throws NullPointerException
	{
		super(parameter);

		rootNode = TypeUtil.cast(parameter.getParameter(PARSED_ROOT_NODE));
		ifStatement = TypeUtil.cast(parameter.getParameter(PARSED_NODE));
	}

	@Override
	protected boolean checkParameters()
	{
		return ValidatorUtil.notNull(rootNode, ifStatement);
	}

	private static final int DEFAULT_CAPACITY = 5;

	@Override
	protected boolean checkDetails()
	{
		final String result = ASTUtil.evaluateExpression(
			ifStatement.getExpression(),
			new ArrayList<Expression>(DEFAULT_CAPACITY));
		if (result.equalsIgnoreCase("true"))
		{
			saveStateToParameter(AvoidUnnecessaryIfChangeCreator.NUMBER_1);
			return true;
		}

		if (result.equalsIgnoreCase("false"))
		{
			saveStateToParameter(AvoidUnnecessaryIfChangeCreator.NUMBER_2);
			return true;
		}

		return false;
	}

	private void saveStateToParameter(String value)
	{
		if (isAllowProvideMoreParameters())
		{
			getAdditionalParameter().addParameter(
				AvoidUnnecessaryIfChangeCreator.BLOCK_NUMBER, value);
		}
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

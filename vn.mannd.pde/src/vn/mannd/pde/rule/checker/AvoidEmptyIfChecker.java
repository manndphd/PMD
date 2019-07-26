package vn.mannd.pde.rule.checker;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;

import vn.mannd.pde.adt.CompositeParameter;
import vn.mannd.pde.refactoring.change.creator.AvoidEmptyIfChangeCreator;
import vn.mannd.pde.rule.checker.core.ConditionalChecker;
import vn.mannd.pde.util.TypeUtil;
import vn.mannd.pde.util.ValidatorUtil;

public class AvoidEmptyIfChecker extends ConditionalChecker
{

	private final ASTNode rootNode;
	private final IfStatement ifStatement;

	public AvoidEmptyIfChecker(CompositeParameter parameter)
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

	@Override
	protected boolean checkDetails()
	{
		Statement statement = ifStatement.getThenStatement();
		if (statement.getNodeType() != ASTNode.BLOCK)
		{
			return false;
		}

		final Block body = (Block)statement;

		if (body.statements().size() == 0)
		{
			saveStateToParameter(AvoidEmptyIfChangeCreator.NUMBER_1);
			return true;
		}

		statement = ifStatement.getElseStatement();
		if (statement == null)
		{
			return false;
		}

		if (statement.getNodeType() != ASTNode.BLOCK)
		{
			return false;
		}

		if (((Block)statement).statements().size() == 0)
		{
			saveStateToParameter(AvoidEmptyIfChangeCreator.NUMBER_2);
			return true;
		}

		return false;
	}

	private void saveStateToParameter(String value)
	{
		if (isAllowProvideMoreParameters())
		{
			getAdditionalParameter().addParameter(
				AvoidEmptyIfChangeCreator.BLOCK_NUMBER, value);
		}
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

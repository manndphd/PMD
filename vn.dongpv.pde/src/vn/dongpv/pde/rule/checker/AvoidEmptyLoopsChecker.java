package vn.dongpv.pde.rule.checker;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.WhileStatement;


import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.rule.checker.core.ConditionalChecker;
import vn.dongpv.pde.util.TypeUtil;
import vn.dongpv.pde.util.ValidatorUtil;

public class AvoidEmptyLoopsChecker extends ConditionalChecker
{

	private final ASTNode rootNode;
	private final ASTNode loopStatement;

	public AvoidEmptyLoopsChecker(CompositeParameter parameter)
		throws NullPointerException
	{
		super(parameter);

		rootNode = TypeUtil.cast(parameter.getParameter(PARSED_ROOT_NODE));
		loopStatement = TypeUtil.cast(parameter.getParameter(PARSED_NODE));
	}

	@Override
	protected boolean checkParameters()
	{
		return ValidatorUtil.notNull(rootNode, loopStatement);
	}

	@Override
	protected boolean checkDetails()
	{
		if (loopStatement.getNodeType() == ASTNode.DO_STATEMENT)
		{
			final DoStatement doStatement = (DoStatement)loopStatement;
			return isEmptyBody(doStatement);
		}

		if (loopStatement.getNodeType() == ASTNode.WHILE_STATEMENT)
		{
			final WhileStatement whileStatement = (WhileStatement)loopStatement;
			return isEmptyBody(whileStatement);
		}

		if (loopStatement.getNodeType() == ASTNode.FOR_STATEMENT)
		{
			final ForStatement forStatement = (ForStatement)loopStatement;
			return isEmptyBody(forStatement);
		}

		return false;
	}

	private boolean isEmptyBody(DoStatement doStatement)
	{
		final Statement statement = doStatement.getBody();
		if (statement.getNodeType() != ASTNode.BLOCK)
		{
			return false;
		}

		final Block body = (Block)statement;

		return (body.statements().size() == 0);
	}

	private boolean isEmptyBody(WhileStatement whileStatement)
	{
		final Statement statement = whileStatement.getBody();
		if (statement.getNodeType() != ASTNode.BLOCK)
		{
			return false;
		}

		final Block body = (Block)statement;

		return (body.statements().size() == 0);
	}

	private boolean isEmptyBody(ForStatement forStatement)
	{
		final Statement statement = forStatement.getBody();
		if (statement.getNodeType() != ASTNode.BLOCK)
		{
			return false;
		}

		final Block body = (Block)statement;

		return (body.statements().size() == 0);
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

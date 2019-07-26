package vn.dongpv.pde.refactoring.change.creator;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.refactoring.change.creator.core.ChangeCreator;
import vn.dongpv.pde.util.TypeUtil;
import vn.dongpv.pde.util.ValidatorUtil;

public class AvoidUnnecessaryIfChangeCreator extends ChangeCreator
{

	public static final String BLOCK_NUMBER = "BLOCK_NUMBER";
	public static final String NUMBER_1 = "IF_BLOCK";
	public static final String NUMBER_2 = "ELSE_BLOCK";

	private final ASTNode rootNode;
	private final IfStatement ifStatement;
	private final String blockNumber;

	public AvoidUnnecessaryIfChangeCreator(CompositeParameter parameter)
		throws NullPointerException
	{
		super(parameter);

		rootNode = TypeUtil.cast(parameter.getParameter(PARSED_ROOT_NODE));
		ifStatement = TypeUtil.cast(parameter.getParameter(PARSED_NODE));
		blockNumber = TypeUtil.cast(parameter.getParameter(BLOCK_NUMBER));
	}

	@Override
	protected boolean checkParameters()
	{
		return ValidatorUtil.notNull(rootNode, ifStatement, blockNumber);
	}

	@Override
	protected void createChange(ASTRewrite rewrite)
	{
		Statement block = null;
		if (blockNumber.equalsIgnoreCase(NUMBER_1))
		{
			block = ifStatement.getThenStatement();
		}
		else if (blockNumber.equalsIgnoreCase(NUMBER_2))
		{
			block = ifStatement.getElseStatement();
		}

		if ((block == null) || (block.getNodeType() == ASTNode.BLOCK))
		{
			final Block body = (Block)block;

			final ASTNode parent = ifStatement.getParent();
			if (parent.getNodeType() == ASTNode.IF_STATEMENT)
			{
				final IfStatement parentIfStatement = (IfStatement)parent;
				final IfStatement otherIfStatement = rootNode.getAST().newIfStatement();
				otherIfStatement.setExpression((Expression)ASTNode.copySubtree(
					rootNode.getAST(), parentIfStatement.getExpression()));
				otherIfStatement.setThenStatement((Statement)ASTNode.copySubtree(
					rootNode.getAST(), parentIfStatement.getThenStatement()));
				if ((body == null) || (body.statements().size() == 0))
				{
					otherIfStatement.setElseStatement(null);
				}
				else
				{
					otherIfStatement.setElseStatement((Statement)
						ASTNode.copySubtree(rootNode.getAST(), block));
				}

				rewrite.replace(parent, otherIfStatement, null);
			}
			else
			{
				final ListRewrite listRewrite = rewrite.getListRewrite(
					parent, Block.STATEMENTS_PROPERTY);

				if (body != null)
				{
					@SuppressWarnings("unchecked")
					final List<Statement> statements = body.statements();
					for (int i = statements.size() - 1; i >= 0; --i)
					{
						listRewrite.insertAfter(
							ASTNode.copySubtree(rootNode.getAST(), statements.get(i)),
							ifStatement,
							null);
					}
				}
				listRewrite.remove(ifStatement, null);
			}
		}
		else if (block.getNodeType() == ASTNode.IF_STATEMENT)
		{
			rewrite.replace(ifStatement,
				ASTNode.copySubtree(rootNode.getAST(), block), null);
		}
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

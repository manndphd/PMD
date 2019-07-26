package vn.dongpv.pde.refactoring.change.creator;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression.Operator;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.refactoring.change.creator.core.ChangeCreator;
import vn.dongpv.pde.util.TypeUtil;
import vn.dongpv.pde.util.ValidatorUtil;

public class AvoidEmptyIfChangeCreator extends ChangeCreator
{

	public static final String BLOCK_NUMBER = "BLOCK_NUMBER";
	public static final String NUMBER_1 = "IF_BLOCK";
	public static final String NUMBER_2 = "ELSE_BLOCK";

	private final ASTNode rootNode;
	private final IfStatement ifStatement;
	private final String blockNumber;

	public AvoidEmptyIfChangeCreator(CompositeParameter parameter)
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
		if (blockNumber.equalsIgnoreCase(NUMBER_1))
		{
			final Statement stat = ifStatement.getElseStatement();
			if ((stat == null) ||
				((stat.getNodeType() == ASTNode.BLOCK) &&
				(((Block)stat).statements().size() == 0)) ||
				(stat.getNodeType() == ASTNode.IF_STATEMENT))
			{
				rewrite.remove(ifStatement, null);
			}
			else
			{
				final ParenthesizedExpression pe = rootNode.getAST().newParenthesizedExpression();
				pe.setExpression((Expression)ASTNode.copySubtree(
					rootNode.getAST(), ifStatement.getExpression()));

				final PrefixExpression pre = rootNode.getAST().newPrefixExpression();
				pre.setOperator(Operator.NOT);
				pre.setOperand(pe);

				final IfStatement otherIfStatement = rootNode.getAST().newIfStatement();
				otherIfStatement.setExpression(pre);
				otherIfStatement.setThenStatement((Statement)ASTNode.copySubtree(
					rootNode.getAST(), ifStatement.getElseStatement()));

				rewrite.replace(ifStatement, otherIfStatement, null);
			}
		}
		else if (blockNumber.equalsIgnoreCase(NUMBER_2))
		{
			final IfStatement otherIfStatement = rootNode.getAST().newIfStatement();
			otherIfStatement.setExpression((Expression)ASTNode.copySubtree(
				rootNode.getAST(), ifStatement.getExpression()));
			otherIfStatement.setThenStatement((Statement)ASTNode.copySubtree(
				rootNode.getAST(), ifStatement.getThenStatement()));

			rewrite.replace(ifStatement, otherIfStatement, null);
		}
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

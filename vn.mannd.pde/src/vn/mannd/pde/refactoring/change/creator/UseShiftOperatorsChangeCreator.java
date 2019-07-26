package vn.mannd.pde.refactoring.change.creator;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import vn.mannd.pde.adt.CompositeParameter;
import vn.mannd.pde.refactoring.change.creator.core.ChangeCreator;
import vn.mannd.pde.util.ASTUtil;
import vn.mannd.pde.util.TypeUtil;
import vn.mannd.pde.util.ValidatorUtil;

public class UseShiftOperatorsChangeCreator extends ChangeCreator
{

	public static final String VALUE = "VALUE";

	private final ASTNode rootNode;
	private final InfixExpression infixExpression;
	private final String value;

	public UseShiftOperatorsChangeCreator(CompositeParameter parameter)
		throws NullPointerException
	{
		super(parameter);

		rootNode = TypeUtil.cast(parameter.getParameter(PARSED_ROOT_NODE));
		infixExpression = TypeUtil.cast(parameter.getParameter(PARSED_NODE));
		value = TypeUtil.cast(parameter.getParameter(VALUE));
	}

	@Override
	protected boolean checkParameters()
	{
		return ValidatorUtil.notNull(rootNode, infixExpression, value);
	}

	@Override
	protected void createChange(ASTRewrite rewrite)
	{

		final NumberLiteral literal = ASTUtil.createNumberLiteral(
			rootNode.getAST(), value);

		final InfixExpression newInfixExp = rootNode.getAST().newInfixExpression();
		newInfixExp.setLeftOperand(
			(Expression)ASTNode.copySubtree(
				rootNode.getAST(), infixExpression.getLeftOperand()));
		newInfixExp.setRightOperand(literal);

		if (infixExpression.getOperator() == Operator.TIMES)
		{
			newInfixExp.setOperator(Operator.LEFT_SHIFT);
		}
		else if (infixExpression.getOperator() == Operator.DIVIDE)
		{
			newInfixExp.setOperator(Operator.RIGHT_SHIFT_SIGNED);
		}

		rewrite.replace(infixExpression, newInfixExp, null);
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

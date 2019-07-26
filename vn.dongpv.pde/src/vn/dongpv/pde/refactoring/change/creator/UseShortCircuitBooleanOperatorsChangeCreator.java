package vn.dongpv.pde.refactoring.change.creator;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.refactoring.change.creator.core.ChangeCreator;
import vn.dongpv.pde.util.TypeUtil;
import vn.dongpv.pde.util.ValidatorUtil;

public class UseShortCircuitBooleanOperatorsChangeCreator extends ChangeCreator
{

	private final ASTNode rootNode;
	private final InfixExpression infixExpression;

	public UseShortCircuitBooleanOperatorsChangeCreator(
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
	protected void createChange(ASTRewrite rewrite)
	{
		final InfixExpression newInfixExpression = (InfixExpression)
			ASTNode.copySubtree(rootNode.getAST(), infixExpression);

		if (infixExpression.getOperator() == Operator.AND)
		{
			newInfixExpression.setOperator(Operator.CONDITIONAL_AND);
		}
		else if (infixExpression.getOperator() == Operator.OR)
		{
			newInfixExpression.setOperator(Operator.CONDITIONAL_OR);
		}

		rewrite.replace(infixExpression, newInfixExpression, null);
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

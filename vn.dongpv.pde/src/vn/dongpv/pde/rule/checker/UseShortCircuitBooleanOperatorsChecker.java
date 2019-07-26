package vn.dongpv.pde.rule.checker;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;

import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.rule.checker.core.ConditionalChecker;
import vn.dongpv.pde.util.TypeUtil;
import vn.dongpv.pde.util.ValidatorUtil;

public class UseShortCircuitBooleanOperatorsChecker extends ConditionalChecker
{

	public static final String BOOL = "boolean";

	private final ASTNode rootNode;
	private final InfixExpression infixExpression;

	public UseShortCircuitBooleanOperatorsChecker(CompositeParameter parameter)
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
		final String returnType = infixExpression.resolveTypeBinding().getQualifiedName();
		if (!returnType.equalsIgnoreCase(BOOL))
		{
			return false;
		}

		final boolean isBitwiseOperator =
			(infixExpression.getOperator() == Operator.AND) ||
				(infixExpression.getOperator() == Operator.OR);

		return isBitwiseOperator;
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

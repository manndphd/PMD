package vn.dongpv.pde.rule.checker;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.WhileStatement;

import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.refactoring.change.creator.AvoidMethodCallsInLoopChangeCreator;
import vn.dongpv.pde.rule.checker.core.ConditionalChecker;
import vn.dongpv.pde.util.ASTUtil;
import vn.dongpv.pde.util.TypeUtil;
import vn.dongpv.pde.util.ValidatorUtil;

public class AvoidMethodCallsInLoopChecker extends ConditionalChecker
{

	public static final String INT = "int";
	public static final String FLOAT = "float";
	public static final String DOUBLE = "double";

	private final ASTNode rootNode;
	private final ASTNode loopStatement;

	public AvoidMethodCallsInLoopChecker(CompositeParameter parameter)
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
	@SuppressWarnings("unchecked")
	protected boolean checkDetails()
	{
		Expression exp = null;
		List<VariableDeclarationFragment> initFrags = null;
		if (loopStatement.getNodeType() == ASTNode.FOR_STATEMENT)
		{
			exp = ((ForStatement)loopStatement).getExpression();
			final ASTNode initializers = (ASTNode)
				((ForStatement)loopStatement).initializers().get(0);
			if (initializers.getNodeType() == ASTNode.VARIABLE_DECLARATION_EXPRESSION)
			{
				initFrags = ((VariableDeclarationExpression)initializers).fragments();
			}
		}
		else if (loopStatement.getNodeType() == ASTNode.WHILE_STATEMENT)
		{
			exp = ((WhileStatement)loopStatement).getExpression();
		}
		else
		{
			return false;
		}

		if (exp.getNodeType() != ASTNode.INFIX_EXPRESSION)
		{
			return false;
		}

		final InfixExpression infixExpression = (InfixExpression)exp;

		exp = infixExpression.getRightOperand();
		if (exp.getNodeType() != ASTNode.METHOD_INVOCATION)
		{
			return false;
		}

		final MethodInvocation invocation = (MethodInvocation)exp;

		if (initFrags != null)
		{
			final List<ASTNode> args = invocation.arguments();
			if (args.size() > 0)
			{
				final List<SimpleName> simpleNames = new ArrayList<SimpleName>();
				for (final ASTNode node : args)
				{
					if (node.getNodeType() == ASTNode.SIMPLE_NAME)
					{
						simpleNames.add((SimpleName)node);
					}
				}

				if (simpleNames.size() > 0)
				{
					for (final SimpleName simpleName : simpleNames)
					{
						for (final VariableDeclarationFragment vdf : initFrags)
						{
							if (ASTUtil.isSameReference(simpleName, vdf.resolveBinding()))
							{
								return false;
							}
						}
					}
				}
			}
		}

		final String returnTypeName = invocation.resolveMethodBinding().
			getReturnType().getQualifiedName();

		if (returnTypeName.equalsIgnoreCase(INT))
		{
			saveStateToParameter(invocation, INT);
			return true;
		}

		if (returnTypeName.equalsIgnoreCase(FLOAT))
		{
			saveStateToParameter(invocation, FLOAT);
			return true;
		}

		if (returnTypeName.equalsIgnoreCase(DOUBLE))
		{
			saveStateToParameter(invocation, DOUBLE);
			return true;
		}

		return false;
	}

	private void saveStateToParameter(MethodInvocation invocation, String valueType)
	{
		if (isAllowProvideMoreParameters())
		{
			getAdditionalParameter().addParameter(
				AvoidMethodCallsInLoopChangeCreator.METHOD_INVOCATION, invocation);

			getAdditionalParameter().addParameter(
				AvoidMethodCallsInLoopChangeCreator.VALUE_TYPE, valueType);
		}
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

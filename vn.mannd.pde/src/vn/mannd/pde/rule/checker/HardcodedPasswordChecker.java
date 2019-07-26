package vn.mannd.pde.rule.checker;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.StringLiteral;

import vn.mannd.pde.PluginConstants;
import vn.mannd.pde.adt.CompositeParameter;
import vn.mannd.pde.rule.checker.core.ConditionalChecker;
import vn.mannd.pde.util.TypeUtil;
import vn.mannd.pde.util.ValidatorUtil;

public class HardcodedPasswordChecker extends ConditionalChecker
{

	public static final String SET_PROPERTY_METHOD_NAME = "setProperty";
	public static final String PASSWORD_LITERAL = "password";

	private final ASTNode rootNode;
	private final MethodInvocation methodInvocation;

	public HardcodedPasswordChecker(CompositeParameter parameter)
		throws NullPointerException
	{
		super(parameter);

		rootNode = TypeUtil.cast(parameter.getParameter(PARSED_ROOT_NODE));
		methodInvocation = TypeUtil.cast(parameter.getParameter(PARSED_NODE));
	}

	@Override
	protected boolean checkParameters()
	{
		return ValidatorUtil.notNull(rootNode, methodInvocation);
	}

	@Override
	protected boolean checkDetails()
	{
		// Checks this method is setProperty() or not

		final IMethodBinding binding = methodInvocation.resolveMethodBinding();
		if (!binding.getName().equalsIgnoreCase(SET_PROPERTY_METHOD_NAME))
		{
			return false;
		}

		// Checks this method is one of Properties's methods

		final String declaringClassName =
			binding.getMethodDeclaration().
				getDeclaringClass().getQualifiedName();

		if (!PluginConstants.PROPERTIES_CLASS_NAME.equalsIgnoreCase(declaringClassName))
		{
			return false;
		}

		final List<?> arguments = methodInvocation.arguments();
		if ((arguments == null) || (arguments.size() == 0))
		{
			return false;
		}

		final Expression firstArg = (Expression)arguments.get(0);
		if (firstArg.getNodeType() == ASTNode.STRING_LITERAL)
		{
			final StringLiteral literal = (StringLiteral)firstArg;
			if (PASSWORD_LITERAL.equalsIgnoreCase(literal.getLiteralValue()))
			{
				return true;
			}
		}

		return false;
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

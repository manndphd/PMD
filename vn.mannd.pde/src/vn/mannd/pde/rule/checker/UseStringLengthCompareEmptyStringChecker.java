package vn.mannd.pde.rule.checker;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.StringLiteral;

import vn.mannd.pde.PluginConstants;
import vn.mannd.pde.adt.CompositeParameter;
import vn.mannd.pde.rule.checker.core.ConditionalChecker;
import vn.mannd.pde.util.TypeUtil;
import vn.mannd.pde.util.ValidatorUtil;

public class UseStringLengthCompareEmptyStringChecker extends ConditionalChecker
{

	public static final String EQUALS_METHOD_NAME = "equals";
	public static final String EQUALS_IGNORE_CASE_METHOD_NAME = "equalsIgnoreCase";

	private final ASTNode rootNode;
	private final MethodInvocation methodInvocation;

	public UseStringLengthCompareEmptyStringChecker(CompositeParameter parameter)
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
		final IMethodBinding binding = methodInvocation.resolveMethodBinding();
		final String declaringClassName = binding.getDeclaringClass().getQualifiedName();
		final String methodName = binding.getName();

		// Makes sure: String.equals() or String.equalsIgnoreCase() method

		if (declaringClassName.equalsIgnoreCase(PluginConstants.STRING_CLASS_NAME) &&
			(methodName.equalsIgnoreCase(EQUALS_METHOD_NAME) ||
			methodName.equalsIgnoreCase(EQUALS_IGNORE_CASE_METHOD_NAME)))
		{
			final List<?> arguments = methodInvocation.arguments();

			// This method must has only one argument
			if (arguments.size() == 1)
			{
				final Object argument = arguments.get(0);
				if (argument instanceof StringLiteral)
				{
					// String argument is empty string?

					final StringLiteral literal = (StringLiteral)argument;
					if (literal.getLiteralValue().length() == 0)
					{
						return true;
					}
				}
			}
		}

		return false;
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

package vn.dongpv.pde.rule.checker;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.StringLiteral;

import vn.dongpv.pde.PluginConstants;
import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.rule.checker.core.ConditionalChecker;
import vn.dongpv.pde.util.ASTUtil;
import vn.dongpv.pde.util.TypeUtil;
import vn.dongpv.pde.util.ValidatorUtil;

public class FormatStringAttackChecker extends ConditionalChecker
{

	public static final String PRINTF_METHOD_NAME = "printf";

	private final ASTNode rootNode;
	private final MethodInvocation methodInvocation;

	public FormatStringAttackChecker(CompositeParameter parameter)
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

		// Makes sure: PrintStream.printf() method

		if (declaringClassName.equalsIgnoreCase(PluginConstants.PRINT_STREAM_CLASS_NAME) &&
			methodName.equalsIgnoreCase(PRINTF_METHOD_NAME))
		{
			final List<?> arguments = methodInvocation.arguments();

			if (arguments.size() > 0)
			{
				final Object argument = arguments.get(0);
				if (argument instanceof StringLiteral)
				{
					final StringLiteral literal = (StringLiteral)argument;
					if (literal.getLiteralValue().replace("%n", "").indexOf('%') >= 0)
					{
						return (ASTUtil.getAncestor(methodInvocation, ASTNode.TRY_STATEMENT) == null);
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

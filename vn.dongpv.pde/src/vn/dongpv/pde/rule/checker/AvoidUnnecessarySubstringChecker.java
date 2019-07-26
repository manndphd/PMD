package vn.dongpv.pde.rule.checker;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NumberLiteral;

import vn.dongpv.pde.PluginConstants;
import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.rule.checker.core.ConditionalChecker;
import vn.dongpv.pde.util.TypeUtil;
import vn.dongpv.pde.util.ValidatorUtil;

public class AvoidUnnecessarySubstringChecker extends ConditionalChecker
{

	public static final String SUB_STRING_METHOD_NAME = "substring";

	private final ASTNode rootNode;
	private final MethodInvocation methodInvocation;

	public AvoidUnnecessarySubstringChecker(CompositeParameter parameter) throws NullPointerException
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
		final String className = binding.getDeclaringClass().getQualifiedName();
		final String methodName = binding.getName();

		if (className.equalsIgnoreCase(PluginConstants.STRING_CLASS_NAME) &&
			methodName.equalsIgnoreCase(SUB_STRING_METHOD_NAME))
		{
			final List<?> arguments = methodInvocation.arguments();
			if (arguments.size() != 1)
			{
				return false;
			}

			final NumberLiteral literal = TypeUtil.cast(arguments.get(0));
			if (literal == null)
			{
				return false;
			}

			if (literal.getToken().equalsIgnoreCase(PluginConstants.ZERO_STRING))
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

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

public class AvoidUsingMathClassMethodsOnConstantChecker extends ConditionalChecker
{

	private final ASTNode rootNode;
	private final MethodInvocation methodInvocation;

	public AvoidUsingMathClassMethodsOnConstantChecker(CompositeParameter parameter)
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
		final String className = binding.getDeclaringClass().getQualifiedName();

		// Math class

		if (className.equalsIgnoreCase(PluginConstants.MATH_CLASS_NAME))
		{
			final List<?> arguments = methodInvocation.arguments();
			for (final Object arg : arguments)
			{
				// Accepts only literal argument

				if (!(arg instanceof NumberLiteral))
				{
					return false;
				}
			}

			return true;
		}

		return false;
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

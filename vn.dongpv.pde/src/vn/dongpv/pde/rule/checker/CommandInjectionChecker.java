package vn.dongpv.pde.rule.checker;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;

import vn.dongpv.pde.PluginConstants;
import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.rule.checker.core.ConditionalChecker;
import vn.dongpv.pde.util.TypeUtil;
import vn.dongpv.pde.util.ValidatorUtil;

public class CommandInjectionChecker extends ConditionalChecker
{

	public static final String EXEC_METHOD_NAME = "exec";

	private final ASTNode rootNode;
	private final MethodInvocation methodInvocation;

	public CommandInjectionChecker(CompositeParameter parameter)
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

		// Makes sure: Runtime.exec() method

		if (declaringClassName.equalsIgnoreCase(PluginConstants.RUNTIME_CLASS_NAME) &&
			methodName.equalsIgnoreCase(EXEC_METHOD_NAME))
		{
			return true;
		}

		return false;
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

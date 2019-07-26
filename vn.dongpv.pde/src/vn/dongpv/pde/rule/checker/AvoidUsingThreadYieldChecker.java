package vn.dongpv.pde.rule.checker;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;

import vn.dongpv.pde.PluginConstants;
import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.rule.checker.core.ConditionalChecker;
import vn.dongpv.pde.util.TypeUtil;
import vn.dongpv.pde.util.ValidatorUtil;

public class AvoidUsingThreadYieldChecker extends ConditionalChecker
{

	public static final String YIELD_METHOD_NAME = "yield";

	private final ASTNode rootNode;
	private final MethodInvocation methodInvocation;

	public AvoidUsingThreadYieldChecker(CompositeParameter parameter)
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
		// Checks this method is yield() or not

		final IMethodBinding binding = methodInvocation.resolveMethodBinding();
		if (!binding.getName().equalsIgnoreCase(YIELD_METHOD_NAME))
		{
			return false;
		}

		// Checks this method is one of Thread's methods

		final String declaringClassName = binding.getDeclaringClass().getQualifiedName();

		return TypeUtil.hasRelationship(declaringClassName,
			PluginConstants.THREAD_CLASS_NAME);
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

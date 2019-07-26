package vn.mannd.pde.rule.checker;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;

import vn.mannd.pde.PluginConstants;
import vn.mannd.pde.adt.CompositeParameter;
import vn.mannd.pde.rule.checker.core.ConditionalChecker;
import vn.mannd.pde.util.ASTUtil;
import vn.mannd.pde.util.TypeUtil;
import vn.mannd.pde.util.ValidatorUtil;

public class AvoidPollingLoopsChecker extends ConditionalChecker
{

	public static final String SLEEP_METHOD_NAME = "sleep";

	private final ASTNode rootNode;
	private final MethodInvocation methodInvocation;

	public AvoidPollingLoopsChecker(CompositeParameter parameter)
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
		// Checks this method is sleep() or not

		final IMethodBinding binding = methodInvocation.resolveMethodBinding();
		if (!binding.getName().equalsIgnoreCase(SLEEP_METHOD_NAME))
		{
			return false;
		}

		// Checks this method is one of Thread's methods

		final String declaringClassName = binding.getDeclaringClass().getQualifiedName();

		if (!TypeUtil.hasRelationship(
			declaringClassName, PluginConstants.THREAD_CLASS_NAME))
		{
			return false;
		}

		// Checks this method invocation in loop or not

		return ASTUtil.hasLoopStatementAncestor(methodInvocation);
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

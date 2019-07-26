package vn.mannd.pde.rule.checker;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;

import vn.mannd.pde.PluginConstants;
import vn.mannd.pde.adt.CompositeParameter;
import vn.mannd.pde.rule.checker.core.ConditionalChecker;
import vn.mannd.pde.util.TypeUtil;
import vn.mannd.pde.util.ValidatorUtil;

public class UseToArrayWithArrayAsParameterChecker extends ConditionalChecker
{

	public static final String TO_ARRAY_METHOD_NAME = "toArray";

	private final ASTNode rootNode;
	private final MethodInvocation methodInvocation;

	public UseToArrayWithArrayAsParameterChecker(CompositeParameter parameter)
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
		// Checks this method is toArray() or not

		final IMethodBinding binding = methodInvocation.resolveMethodBinding();
		if (!binding.getName().equalsIgnoreCase(TO_ARRAY_METHOD_NAME))
		{
			return false;
		}

		// Checks this method is one of Collection's methods

		final String declaringClassName =
			binding.getMethodDeclaration().
				getDeclaringClass().getQualifiedName();

		if (!TypeUtil.hasRelationship(
			declaringClassName, PluginConstants.COLLECTION_CLASS_NAME))
		{
			return false;
		}

		// Checks this method has how many parameters

		return (binding.getParameterTypes().length == 0);
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

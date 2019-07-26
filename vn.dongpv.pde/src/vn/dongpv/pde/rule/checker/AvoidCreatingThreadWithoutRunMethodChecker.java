package vn.dongpv.pde.rule.checker;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;

import vn.dongpv.pde.PluginConstants;
import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.rule.checker.core.ConditionalChecker;
import vn.dongpv.pde.util.TypeUtil;
import vn.dongpv.pde.util.ValidatorUtil;

public class AvoidCreatingThreadWithoutRunMethodChecker extends ConditionalChecker
{

	public static final String START_METHOD_NAME = "start";

	private final ASTNode rootNode;
	private final ClassInstanceCreation creation;

	public AvoidCreatingThreadWithoutRunMethodChecker(CompositeParameter parameter)
		throws NullPointerException
	{
		super(parameter);

		rootNode = TypeUtil.cast(parameter.getParameter(PARSED_ROOT_NODE));
		creation = TypeUtil.cast(parameter.getParameter(PARSED_NODE));
	}

	@Override
	protected boolean checkParameters()
	{
		return ValidatorUtil.notNull(rootNode, creation);
	}

	@Override
	protected boolean checkDetails()
	{
		// Checks: `new Thread()`?

		final IMethodBinding binding = creation.resolveConstructorBinding();
		final String creationTypeName = binding.getDeclaringClass().getQualifiedName();

		if (!creationTypeName.equalsIgnoreCase(PluginConstants.THREAD_CLASS_NAME) ||
			(binding.getParameterTypes().length > 0))
		{
			return false;
		}

		// Makes sure its father is not parenthesized expression

		ASTNode parent = creation.getParent();

		boolean isDone = false;
		while (!isDone)
		{
			if ((parent == null) ||
				(parent.getNodeType() != ASTNode.PARENTHESIZED_EXPRESSION))
			{
				isDone = true;
			}
			else
			{
				parent = parent.getParent();
			}
		}

		// Checks: `new Thread().start()`?

		if ((parent != null) &&
			(parent.getNodeType() == ASTNode.METHOD_INVOCATION))
		{
			final MethodInvocation invocation = (MethodInvocation)parent;
			final IMethodBinding parentBinding = invocation.resolveMethodBinding();
			if (parentBinding.getName().equalsIgnoreCase(START_METHOD_NAME))
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

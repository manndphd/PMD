package vn.mannd.pde.rule.checker;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;

import vn.mannd.pde.PluginConstants;
import vn.mannd.pde.adt.CompositeParameter;
import vn.mannd.pde.rule.checker.core.ConditionalChecker;
import vn.mannd.pde.util.TypeUtil;
import vn.mannd.pde.util.ValidatorUtil;

public class UseStringEqualsIgnoreCaseChecker extends ConditionalChecker
{

	public static final String EQUALS_METHOD_NAME = "equals";

	private final ASTNode rootNode;
	private final MethodInvocation methodInvocation;

	public UseStringEqualsIgnoreCaseChecker(CompositeParameter parameter)
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

		// Makes sure: String.equals() method

		if (declaringClassName.equalsIgnoreCase(PluginConstants.STRING_CLASS_NAME) &&
			methodName.equalsIgnoreCase(EQUALS_METHOD_NAME))
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

package vn.mannd.pde.rule.checker;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;

import vn.mannd.pde.PluginConstants;
import vn.mannd.pde.adt.CompositeParameter;
import vn.mannd.pde.rule.checker.core.ConditionalChecker;
import vn.mannd.pde.util.TypeUtil;
import vn.mannd.pde.util.ValidatorUtil;

public class UseDataSourceInsteadOfDriverManagerChecker extends ConditionalChecker
{

	public static final String GET_CONNECTION_METHOD_NAME = "getConnection";

	private final ASTNode rootNode;
	private final MethodInvocation methodInvocation;

	public UseDataSourceInsteadOfDriverManagerChecker(CompositeParameter parameter)
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
		// Checks this method is getConnection() or not

		final IMethodBinding binding = methodInvocation.resolveMethodBinding();
		if (!binding.getName().equalsIgnoreCase(GET_CONNECTION_METHOD_NAME))
		{
			return false;
		}

		// Checks this method is one of DriverManager's methods

		final String declaringClassName = binding.getDeclaringClass().getQualifiedName();

		return TypeUtil.hasRelationship(declaringClassName,
			PluginConstants.DRIVER_MANAGER_CLASS_NAME);
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

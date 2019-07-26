package vn.mannd.pde.rule.checker;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;

import vn.mannd.pde.PluginConstants;
import vn.mannd.pde.adt.CompositeParameter;
import vn.mannd.pde.refactoring.change.creator.AvoidInstantiationForGetClassChangeCreator;
import vn.mannd.pde.rule.checker.core.ConditionalChecker;
import vn.mannd.pde.util.TypeUtil;
import vn.mannd.pde.util.ValidatorUtil;

public class AvoidInstantiationForGetClassChecker extends ConditionalChecker
{

	public static final String GET_CLASS_METHOD_NAME = "getClass";

	private final ASTNode rootNode;
	private final ClassInstanceCreation creation;

	public AvoidInstantiationForGetClassChecker(CompositeParameter parameter)
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

		// Checks: `new MyClass().getClass()`?

		if ((parent != null) &&
			(parent.getNodeType() == ASTNode.METHOD_INVOCATION))
		{
			final MethodInvocation invocation = (MethodInvocation)parent;
			final IMethodBinding parentBinding = invocation.resolveMethodBinding();

			final String methodName = parentBinding.getName();

			final String declaringClassName =
				parentBinding.getDeclaringClass().getQualifiedName();

			if (methodName.equalsIgnoreCase(GET_CLASS_METHOD_NAME) &&
				declaringClassName.equalsIgnoreCase(PluginConstants.OBJECT_CLASS_NAME))
			{
				if (isAllowProvideMoreParameters())
				{
					getAdditionalParameter().addParameter(
						AvoidInstantiationForGetClassChangeCreator.SURROUNDED_METHOD_INVOCATION,
						parent);
				}

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

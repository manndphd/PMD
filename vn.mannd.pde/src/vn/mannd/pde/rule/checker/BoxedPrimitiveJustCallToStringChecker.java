package vn.mannd.pde.rule.checker;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;

import vn.mannd.pde.PluginConstants;
import vn.mannd.pde.adt.CompositeParameter;
import vn.mannd.pde.rule.checker.core.ConditionalChecker;
import vn.mannd.pde.util.ASTUtil;
import vn.mannd.pde.util.TypeUtil;
import vn.mannd.pde.util.ValidatorUtil;

public class BoxedPrimitiveJustCallToStringChecker extends ConditionalChecker
{

	public static final String TO_STRING_METHOD_NAME = "toString";

	private final ASTNode rootNode;
	private final MethodInvocation methodInvocation;

	public BoxedPrimitiveJustCallToStringChecker(CompositeParameter parameter)
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
		final Expression expression = methodInvocation.getExpression();
		final SimpleName name = methodInvocation.getName();

		// Checks this method is toString() and it was invoked by the
		// construction of a `primitive` object

		if (isToStringMethodName(name) && isPrimitiveConstruction(expression))
		{
			return true;
		}

		return false;
	}

	private boolean isToStringMethodName(SimpleName name)
	{
		final IMethodBinding binding = TypeUtil.cast(name.resolveBinding());
		if (binding == null)
		{
			return false;
		}

		// Checks the invocation is on `primitive` object

		final String type = ASTUtil.getObjectType(binding);
		final boolean isToStringMethod =
			(type != PluginConstants.NON_PRIMITIVE_CLASS_NAME) &&
				(binding.getName().equalsIgnoreCase(TO_STRING_METHOD_NAME));

		return isToStringMethod;
	}

	private boolean isPrimitiveConstruction(Expression expression)
	{
		final ClassInstanceCreation creation = TypeUtil.cast(expression);
		if (creation == null)
		{
			return false;
		}

		// Checks the object that call toString() method is the construction and
		// get some information for refactoring session

		final CompositeParameter parameter = new CompositeParameter();
		parameter.addParameter(ConditionalChecker.PARSED_ROOT_NODE, rootNode);
		parameter.addParameter(ConditionalChecker.PARSED_NODE, creation);

		final AvoidConstructingPrimitiveTypeChecker checker =
			new AvoidConstructingPrimitiveTypeChecker(parameter);
		checker.setAllowProvideMoreParameters(true);
		checker.setCheckParentNode(false);

		if (!checker.isViolated())
		{
			return false;
		}

		getAdditionalParameter().addParameter(checker.getAdditionalParameter());

		return true;
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

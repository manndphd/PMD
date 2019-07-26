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

public class AvoidWriteByteMethodInLoopChecker extends ConditionalChecker
{

	public static final String WRITE_BYTE_METHOD_NAME = "writeByte";

	private final ASTNode rootNode;
	private final MethodInvocation methodInvocation;

	public AvoidWriteByteMethodInLoopChecker(CompositeParameter parameter)
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
		// Checks this method is writeByte() or not

		final IMethodBinding binding = methodInvocation.resolveMethodBinding();
		if (!binding.getName().equalsIgnoreCase(WRITE_BYTE_METHOD_NAME))
		{
			return false;
		}

		// Checks this method is one of DataOutput's methods

		final String declaringClassName = binding.getDeclaringClass().getQualifiedName();

		if (!TypeUtil.hasRelationship(
			declaringClassName, PluginConstants.DATA_OUTPUT_CLASS_NAME))
		{
			return false;
		}

		// Checks this method in loop

		return ASTUtil.hasLoopStatementAncestor(methodInvocation);
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

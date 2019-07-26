package vn.dongpv.pde.rule.checker;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;

import vn.dongpv.pde.PluginConstants;
import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.rule.checker.core.ConditionalChecker;
import vn.dongpv.pde.util.ASTUtil;
import vn.dongpv.pde.util.TypeUtil;
import vn.dongpv.pde.util.ValidatorUtil;

public class EnsureEfficientRemovalOfElementsInCollectionChecker
	extends ConditionalChecker
{

	public static final String REMOVE_METHOD_NAME = "remove";

	private final ASTNode rootNode;
	private final MethodInvocation methodInvocation;

	public EnsureEfficientRemovalOfElementsInCollectionChecker(
		CompositeParameter parameter)
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
		// Checks this method is remove() or not

		final IMethodBinding binding = methodInvocation.resolveMethodBinding();
		if (!binding.getName().equalsIgnoreCase(REMOVE_METHOD_NAME))
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

		// Checks this method in loop

		return ASTUtil.hasLoopStatementAncestor(methodInvocation);
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

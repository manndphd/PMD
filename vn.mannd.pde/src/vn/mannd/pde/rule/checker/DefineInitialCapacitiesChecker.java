package vn.mannd.pde.rule.checker;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.IMethodBinding;

import vn.mannd.pde.PluginConstants;
import vn.mannd.pde.adt.CompositeParameter;
import vn.mannd.pde.rule.checker.core.ConditionalChecker;
import vn.mannd.pde.util.TypeUtil;
import vn.mannd.pde.util.ValidatorUtil;

public class DefineInitialCapacitiesChecker extends ConditionalChecker
{

	private final ASTNode rootNode;
	private final ClassInstanceCreation creation;

	public DefineInitialCapacitiesChecker(CompositeParameter parameter)
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
		if (creation.arguments().size() > 0)
		{
			return false;
		}

		// Checks new ArrayList() ?

		final IMethodBinding binding = creation.resolveConstructorBinding();
		final String declaringClassName =
			binding.getMethodDeclaration().
				getDeclaringClass().getQualifiedName();

		return !declaringClassName.equalsIgnoreCase(PluginConstants.LINKED_LIST_CLASS_NAME) &&
			TypeUtil.hasRelationship(
				declaringClassName, PluginConstants.ABSTRACT_LIST_CLASS_NAME);
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

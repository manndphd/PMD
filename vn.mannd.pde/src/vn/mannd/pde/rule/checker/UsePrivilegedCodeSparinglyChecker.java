package vn.mannd.pde.rule.checker;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ITypeBinding;

import vn.mannd.pde.PluginConstants;
import vn.mannd.pde.adt.CompositeParameter;
import vn.mannd.pde.rule.checker.core.ConditionalChecker;
import vn.mannd.pde.util.TypeUtil;
import vn.mannd.pde.util.ValidatorUtil;

public class UsePrivilegedCodeSparinglyChecker extends ConditionalChecker
{

	private final ASTNode rootNode;
	private final ClassInstanceCreation creation;

	public UsePrivilegedCodeSparinglyChecker(CompositeParameter parameter)
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
		final ITypeBinding typeBinding = creation.resolveTypeBinding();
		final ITypeBinding[] interfaceTypeBindings = typeBinding.getInterfaces();
		if ((interfaceTypeBindings == null) || (interfaceTypeBindings.length == 0))
		{
			return false;
		}

		for (final ITypeBinding binding : interfaceTypeBindings)
		{
			final ITypeBinding typeDecl = binding.getTypeDeclaration();
			if (typeDecl != null)
			{
				final String interfaceTypeName = typeDecl.getQualifiedName();
				if (PluginConstants.PRIVILEGED_ACTION_CLASS_NAME.equalsIgnoreCase(interfaceTypeName) ||
					PluginConstants.PRIVILEGED_EXCEPTION_ACTION_CLASS_NAME.equalsIgnoreCase(interfaceTypeName))
				{
					return true;
				}
			}
		}

		return false;
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

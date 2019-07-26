package vn.mannd.pde.rule.checker;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import vn.mannd.pde.PluginConstants;
import vn.mannd.pde.adt.CompositeParameter;
import vn.mannd.pde.rule.checker.core.ConditionalChecker;
import vn.mannd.pde.util.TypeUtil;
import vn.mannd.pde.util.ValidatorUtil;

public class UseTransientKeywordChecker extends ConditionalChecker
{

	private final ASTNode rootNode;
	private final FieldDeclaration fieldDecl;

	public UseTransientKeywordChecker(CompositeParameter parameter)
		throws NullPointerException
	{
		super(parameter);

		rootNode = TypeUtil.cast(parameter.getParameter(PARSED_ROOT_NODE));
		fieldDecl = TypeUtil.cast(parameter.getParameter(PARSED_NODE));
	}

	@Override
	protected boolean checkParameters()
	{
		return ValidatorUtil.notNull(rootNode, fieldDecl);
	}

	@Override
	protected boolean checkDetails()
	{
		final int modifiers = fieldDecl.getModifiers();
		if (!Modifier.isFinal(modifiers))
		{
			return false;
		}

		final TypeDeclaration typeDecl = TypeUtil.cast(fieldDecl.getParent());
		if (typeDecl == null)
		{
			return false;
		}

		final ITypeBinding typeBinding = typeDecl.resolveBinding();
		final ITypeBinding[] superInterfaceBindings = typeBinding.getInterfaces();
		if (superInterfaceBindings == null)
		{
			return false;
		}

		for (final ITypeBinding interfaceBinding : superInterfaceBindings)
		{
			final String interfaceName = interfaceBinding.getQualifiedName();
			if (interfaceName.equalsIgnoreCase(PluginConstants.SERIALIZABLE_CLASS_NAME))
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

package vn.dongpv.pde.rule.checker;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import vn.dongpv.pde.PluginConstants;
import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.rule.checker.core.ConditionalChecker;
import vn.dongpv.pde.util.TypeUtil;
import vn.dongpv.pde.util.ValidatorUtil;

public class DoNotSubclassClassLoaderChecker extends ConditionalChecker
{

	private final ASTNode rootNode;
	private final TypeDeclaration typeDecl;

	public DoNotSubclassClassLoaderChecker(CompositeParameter parameter)
		throws NullPointerException
	{
		super(parameter);

		rootNode = TypeUtil.cast(parameter.getParameter(PARSED_ROOT_NODE));
		typeDecl = TypeUtil.cast(parameter.getParameter(PARSED_NODE));
	}

	@Override
	protected boolean checkParameters()
	{
		return ValidatorUtil.notNull(rootNode, typeDecl);
	}

	@Override
	protected boolean checkDetails()
	{
		final Type superType = typeDecl.getSuperclassType();
		if (superType == null)
		{
			return false;
		}

		final ITypeBinding superTypeBinding = superType.resolveBinding();
		final String superTypeName = superTypeBinding.getQualifiedName();

		return PluginConstants.CLASS_LOADER_CLASS_NAME.equalsIgnoreCase(superTypeName);
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

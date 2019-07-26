package vn.mannd.pde.rule.checker;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import vn.mannd.pde.adt.CompositeParameter;
import vn.mannd.pde.rule.checker.core.ConditionalChecker;
import vn.mannd.pde.util.TypeUtil;
import vn.mannd.pde.util.ValidatorUtil;

public class AvoidPackageScopeChecker extends ConditionalChecker
{

	private final ASTNode rootNode;
	private final ASTNode decl;

	public AvoidPackageScopeChecker(CompositeParameter parameter)
		throws NullPointerException
	{
		super(parameter);

		rootNode = TypeUtil.cast(parameter.getParameter(PARSED_ROOT_NODE));
		decl = TypeUtil.cast(parameter.getParameter(PARSED_NODE));
	}

	@Override
	protected boolean checkParameters()
	{
		return ValidatorUtil.notNull(rootNode, decl);
	}

	@Override
	protected boolean checkDetails()
	{
		if (decl.getNodeType() == ASTNode.TYPE_DECLARATION)
		{
			final TypeDeclaration typeDecl = (TypeDeclaration)decl;
			return isPackageScope(typeDecl);
		}

		if (decl.getNodeType() == ASTNode.METHOD_DECLARATION)
		{
			final MethodDeclaration methodDecl = (MethodDeclaration)decl;
			return isPackageScope(methodDecl);
		}

		if (decl.getNodeType() == ASTNode.FIELD_DECLARATION)
		{
			final FieldDeclaration fieldDecl = (FieldDeclaration)decl;
			return isPackageScope(fieldDecl);
		}

		return false;
	}

	private boolean isPackageScope(TypeDeclaration typeDecl)
	{
		final int modifiers = typeDecl.getModifiers();
		return !Modifier.isPrivate(modifiers) &&
			!Modifier.isProtected(modifiers) &&
			!Modifier.isPublic(modifiers);
	}

	private boolean isPackageScope(MethodDeclaration methodDecl)
	{
		final int modifiers = methodDecl.getModifiers();
		return !Modifier.isPrivate(modifiers) &&
			!Modifier.isProtected(modifiers) &&
			!Modifier.isPublic(modifiers);
	}

	private boolean isPackageScope(FieldDeclaration fieldDecl)
	{
		final int modifiers = fieldDecl.getModifiers();
		return !Modifier.isPrivate(modifiers) &&
			!Modifier.isProtected(modifiers) &&
			!Modifier.isPublic(modifiers);
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

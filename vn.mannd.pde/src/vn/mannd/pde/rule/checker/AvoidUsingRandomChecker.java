package vn.mannd.pde.rule.checker;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import vn.mannd.pde.PluginConstants;
import vn.mannd.pde.adt.CompositeParameter;
import vn.mannd.pde.rule.checker.core.ConditionalChecker;
import vn.mannd.pde.util.TypeUtil;
import vn.mannd.pde.util.ValidatorUtil;

public class AvoidUsingRandomChecker extends ConditionalChecker
{

	private final ASTNode rootNode;
	private final ASTNode decl;

	public AvoidUsingRandomChecker(
		CompositeParameter parameter)
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
		if (decl.getNodeType() == ASTNode.FIELD_DECLARATION)
		{
			final FieldDeclaration fieldDecl = (FieldDeclaration)decl;
			final ITypeBinding typeBinding = fieldDecl.getType().resolveBinding();
			final String typeName = typeBinding.getQualifiedName();

			return (PluginConstants.RANDOM_CLASS_NAME.equalsIgnoreCase(typeName));
		}

		if (decl.getNodeType() == ASTNode.VARIABLE_DECLARATION_STATEMENT)
		{
			final VariableDeclarationStatement vds = (VariableDeclarationStatement)decl;
			final ITypeBinding typeBinding = vds.getType().resolveBinding();
			final String typeName = typeBinding.getQualifiedName();

			return (PluginConstants.RANDOM_CLASS_NAME.equalsIgnoreCase(typeName));
		}

		return false;
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

package vn.dongpv.pde.rule.checker;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import vn.dongpv.pde.PluginConstants;
import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.rule.checker.core.ConditionalChecker;
import vn.dongpv.pde.util.TypeUtil;
import vn.dongpv.pde.util.ValidatorUtil;

public class UsePreparedStatementInsteadOfStatementChecker extends ConditionalChecker
{

	private final ASTNode rootNode;
	private final ASTNode decl;

	public UsePreparedStatementInsteadOfStatementChecker(
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

			return (typeName.equalsIgnoreCase(PluginConstants.STATEMENT_CLASS_NAME));
		}

		if (decl.getNodeType() == ASTNode.VARIABLE_DECLARATION_STATEMENT)
		{
			final VariableDeclarationStatement vds = (VariableDeclarationStatement)decl;
			final ITypeBinding typeBinding = vds.getType().resolveBinding();
			final String typeName = typeBinding.getQualifiedName();

			return (typeName.equalsIgnoreCase(PluginConstants.STATEMENT_CLASS_NAME));
		}

		return false;
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

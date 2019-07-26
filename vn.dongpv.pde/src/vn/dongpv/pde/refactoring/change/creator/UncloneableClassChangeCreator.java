package vn.dongpv.pde.refactoring.change.creator;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import vn.dongpv.pde.PluginConstants;
import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.refactoring.change.creator.core.ChangeCreator;
import vn.dongpv.pde.rule.checker.UncloneableClassChecker;
import vn.dongpv.pde.util.TypeUtil;
import vn.dongpv.pde.util.ValidatorUtil;

public class UncloneableClassChangeCreator extends ChangeCreator
{

	private final ASTNode rootNode;
	private final TypeDeclaration typeDecl;

	public UncloneableClassChangeCreator(CompositeParameter parameter)
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
	@SuppressWarnings("unchecked")
	protected void createChange(ASTRewrite rewrite)
	{
		final ClassInstanceCreation creation = rootNode.getAST().newClassInstanceCreation();
		creation.setType(
			rootNode.getAST().newSimpleType(
				rootNode.getAST().newName(
					PluginConstants.CLONE_NOT_SUPPORTED_EXCEPTION_CLASS_NAME)));

		final ThrowStatement throwStatement = rootNode.getAST().newThrowStatement();
		throwStatement.setExpression(creation);

		final Block body = rootNode.getAST().newBlock();
		body.statements().add(throwStatement);

		final MethodDeclaration decl = rootNode.getAST().newMethodDeclaration();
		decl.setName(
			rootNode.getAST().newSimpleName(
				UncloneableClassChecker.CLONE_METHOD_NAME));
		decl.setReturnType2(
			rootNode.getAST().newSimpleType(
				rootNode.getAST().newSimpleName(
					PluginConstants.OBJECT_NAME)));
		decl.modifiers().addAll(
			rootNode.getAST().newModifiers(
				Modifier.PUBLIC | Modifier.FINAL));
		decl.thrownExceptions().add(
			rootNode.getAST().newName(
				PluginConstants.CLONE_NOT_SUPPORTED_EXCEPTION_CLASS_NAME));

		decl.setBody(body);

		final ListRewrite listRewrite = rewrite.getListRewrite(
			typeDecl, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
		listRewrite.insertLast(decl, null);
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

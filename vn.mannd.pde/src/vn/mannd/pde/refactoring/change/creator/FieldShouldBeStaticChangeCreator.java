package vn.mannd.pde.refactoring.change.creator;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import vn.mannd.pde.adt.CompositeParameter;
import vn.mannd.pde.refactoring.change.creator.core.ChangeCreator;
import vn.mannd.pde.util.TypeUtil;
import vn.mannd.pde.util.ValidatorUtil;

public class FieldShouldBeStaticChangeCreator extends ChangeCreator
{

	private final ASTNode rootNode;
	private final VariableDeclarationFragment fragment;
	private FieldDeclaration fieldDecl;

	public FieldShouldBeStaticChangeCreator(CompositeParameter parameter) throws NullPointerException
	{
		super(parameter);

		rootNode = TypeUtil.cast(parameter.getParameter(PARSED_ROOT_NODE));
		fragment = TypeUtil.cast(parameter.getParameter(PARSED_NODE));
		fieldDecl = null;
	}

	@Override
	protected boolean checkParameters()
	{
		return ValidatorUtil.notNull(rootNode, fragment);
	}

	@Override
	protected void createChange(ASTRewrite rewrite)
	{
		fieldDecl = (FieldDeclaration)fragment.getParent();

		if (hasOnlyOneDeclaration())
		{
			addStaticModifierToDeclaration(rewrite);
		}
		else
		{
			splitDeclarations(rewrite);
		}
	}

	private boolean hasOnlyOneDeclaration()
	{
		return (fieldDecl.fragments().size() == 1);
	}

	private void addStaticModifierToDeclaration(ASTRewrite rewrite)
	{
		final Modifier modifier = rootNode.getAST().
			newModifier(ModifierKeyword.STATIC_KEYWORD);

		final ListRewrite listRewrite = rewrite.getListRewrite(
			fieldDecl, FieldDeclaration.MODIFIERS2_PROPERTY);

		listRewrite.insertBefore(modifier,
			(ASTNode)fieldDecl.modifiers().get(fieldDecl.modifiers().size() - 1),
			null);
	}

	private void splitDeclarations(ASTRewrite rewrite)
	{
		createNewDeclaration(rewrite);
		removeOldDelcaration(rewrite);
	}

	@SuppressWarnings("unchecked")
	private void createNewDeclaration(ASTRewrite rewrite)
	{
		final VariableDeclarationFragment copyFragment = (VariableDeclarationFragment)
			ASTNode.copySubtree(rootNode.getAST(), fragment);

		// Creates new field declaration

		final FieldDeclaration newDeclaration =
			rootNode.getAST().newFieldDeclaration(copyFragment);

		newDeclaration.setType((Type)
			ASTNode.copySubtree(rootNode.getAST(), fieldDecl.getType()));

		newDeclaration.modifiers().addAll(
			ASTNode.copySubtrees(rootNode.getAST(), fieldDecl.modifiers()));

		newDeclaration.modifiers().add(
			rootNode.getAST().newModifier(ModifierKeyword.STATIC_KEYWORD));

		// Records change

		final ListRewrite listRewrite = rewrite.getListRewrite(
			fieldDecl.getParent(), TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
		listRewrite.insertBefore(newDeclaration, fieldDecl, null);
	}

	private void removeOldDelcaration(ASTRewrite rewrite)
	{
		final ListRewrite listRewrite = rewrite.getListRewrite(
			fieldDecl, FieldDeclaration.FRAGMENTS_PROPERTY);
		listRewrite.remove(fragment, null);
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

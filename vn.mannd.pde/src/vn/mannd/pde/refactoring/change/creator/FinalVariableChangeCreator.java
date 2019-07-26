package vn.mannd.pde.refactoring.change.creator;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import vn.mannd.pde.adt.CompositeParameter;
import vn.mannd.pde.refactoring.change.creator.core.ChangeCreator;
import vn.mannd.pde.util.TypeUtil;
import vn.mannd.pde.util.ValidatorUtil;

public class FinalVariableChangeCreator extends ChangeCreator
{

	private final ASTNode rootNode;
	private final VariableDeclarationFragment fragment;
	private ASTNode parent;

	public FinalVariableChangeCreator(CompositeParameter parameter)
		throws NullPointerException
	{
		super(parameter);

		rootNode = TypeUtil.cast(parameter.getParameter(PARSED_ROOT_NODE));
		fragment = TypeUtil.cast(parameter.getParameter(PARSED_NODE));
		parent = null;
	}

	@Override
	protected boolean checkParameters()
	{
		return ValidatorUtil.notNull(rootNode, fragment);
	}

	@Override
	protected void createChange(ASTRewrite rewrite)
	{
		parent = fragment.getParent();

		if (hasOnlyOneDeclaration())
		{
			addFinalModifierToDeclaration(rewrite);
		}
		else
		{
			splitDeclarations(rewrite);
		}
	}

	private boolean hasOnlyOneDeclaration()
	{
		if (parent.getNodeType() == ASTNode.FIELD_DECLARATION)
		{
			final FieldDeclaration fd = (FieldDeclaration)parent;
			return (fd.fragments().size() == 1);
		}

		if (parent.getNodeType() == ASTNode.VARIABLE_DECLARATION_STATEMENT)
		{
			final VariableDeclarationStatement vds = (VariableDeclarationStatement)parent;
			return (vds.fragments().size() == 1);
		}

		return true;
	}

	private void addFinalModifierToDeclaration(ASTRewrite rewrite)
	{
		final Modifier modifier = rootNode.getAST().
			newModifier(ModifierKeyword.FINAL_KEYWORD);

		if (parent.getNodeType() == ASTNode.FIELD_DECLARATION)
		{
			final FieldDeclaration fieldDecl = (FieldDeclaration)parent;

			final ListRewrite listRewrite = rewrite.getListRewrite(
				fieldDecl, FieldDeclaration.MODIFIERS2_PROPERTY);
			listRewrite.insertLast(modifier, null);
		}
		else if (parent.getNodeType() == ASTNode.VARIABLE_DECLARATION_STATEMENT)
		{
			final VariableDeclarationStatement vds = (VariableDeclarationStatement)parent;

			final ListRewrite listRewrite = rewrite.getListRewrite(
				vds, VariableDeclarationStatement.MODIFIERS2_PROPERTY);
			listRewrite.insertLast(modifier, null);
		}
	}

	private void splitDeclarations(ASTRewrite rewrite)
	{
		if (parent.getNodeType() == ASTNode.FIELD_DECLARATION)
		{
			final FieldDeclaration fd = (FieldDeclaration)parent;
			createNewDeclaration(rewrite, fd);
			removeOldDelcaration(rewrite, fd);
		}
		else if (parent.getNodeType() == ASTNode.VARIABLE_DECLARATION_STATEMENT)
		{
			final VariableDeclarationStatement vds = (VariableDeclarationStatement)parent;
			createNewDeclaration(rewrite, vds);
			removeOldDelcaration(rewrite, vds);
		}
	}

	@SuppressWarnings("unchecked")
	private void createNewDeclaration(ASTRewrite rewrite, FieldDeclaration fd)
	{
		final VariableDeclarationFragment copyFragment = (VariableDeclarationFragment)
			ASTNode.copySubtree(rootNode.getAST(), fragment);

		// Creates new field declaration

		final FieldDeclaration newDeclaration =
			rootNode.getAST().newFieldDeclaration(copyFragment);
		newDeclaration.setType((Type)
			ASTNode.copySubtree(rootNode.getAST(), fd.getType()));
		newDeclaration.modifiers().addAll(
			ASTNode.copySubtrees(rootNode.getAST(), fd.modifiers()));
		newDeclaration.modifiers().add(
			rootNode.getAST().newModifier(ModifierKeyword.FINAL_KEYWORD));

		// Records the change

		final ListRewrite listRewrite = rewrite.getListRewrite(
			fd.getParent(), TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
		listRewrite.insertBefore(newDeclaration, fd, null);
	}

	private void removeOldDelcaration(ASTRewrite rewrite, FieldDeclaration fd)
	{
		final ListRewrite listRewrite = rewrite.getListRewrite(
			fd, FieldDeclaration.FRAGMENTS_PROPERTY);
		listRewrite.remove(fragment, null);
	}

	@SuppressWarnings("unchecked")
	private void createNewDeclaration(
		ASTRewrite rewrite, VariableDeclarationStatement vds)
	{
		final VariableDeclarationFragment copyFragment = (VariableDeclarationFragment)
			ASTNode.copySubtree(rootNode.getAST(), fragment);

		// Creates new declaration statement

		final VariableDeclarationStatement newStatement =
			rootNode.getAST().newVariableDeclarationStatement(copyFragment);
		newStatement.setType((Type)
			ASTNode.copySubtree(rootNode.getAST(), vds.getType()));
		newStatement.modifiers().addAll(
			ASTNode.copySubtrees(rootNode.getAST(), vds.modifiers()));
		newStatement.modifiers().add(
			rootNode.getAST().newModifier(ModifierKeyword.FINAL_KEYWORD));

		// Records the change

		final ListRewrite listRewrite =
			rewrite.getListRewrite(vds.getParent(), Block.STATEMENTS_PROPERTY);
		listRewrite.insertBefore(newStatement, vds, null);
	}

	private void removeOldDelcaration(
		ASTRewrite rewrite, VariableDeclarationStatement vds)
	{
		final ListRewrite listRewrite = rewrite.getListRewrite(
			vds, VariableDeclarationStatement.FRAGMENTS_PROPERTY);
		listRewrite.remove(fragment, null);
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

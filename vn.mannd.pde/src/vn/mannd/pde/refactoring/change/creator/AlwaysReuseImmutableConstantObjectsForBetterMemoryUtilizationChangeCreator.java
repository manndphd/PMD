package vn.mannd.pde.refactoring.change.creator;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import vn.mannd.pde.adt.CompositeParameter;
import vn.mannd.pde.refactoring.change.creator.core.ChangeCreator;
import vn.mannd.pde.util.ASTUtil;
import vn.mannd.pde.util.TypeUtil;
import vn.mannd.pde.util.ValidatorUtil;

public class AlwaysReuseImmutableConstantObjectsForBetterMemoryUtilizationChangeCreator
	extends ChangeCreator
{

	public static final String NO_OBJECTS = "NO_OBJECTS";
	public static final String OBJECT_NAME = "Object";

	private final ASTNode rootNode;
	private final ArrayCreation creation;

	public AlwaysReuseImmutableConstantObjectsForBetterMemoryUtilizationChangeCreator(
		CompositeParameter parameter)
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
	protected void createChange(ASTRewrite rewrite)
	{
		if (createNewFieldDeclaration(rewrite))
		{
			replaceOldArrayCreation(rewrite);
		}
	}

	@SuppressWarnings("unchecked")
	private boolean createNewFieldDeclaration(ASTRewrite rewrite)
	{
		final ASTNode typeNode = ASTUtil.getAncestor(creation, ASTNode.TYPE_DECLARATION);
		if (typeNode == null)
		{
			return false;
		}

		final VariableDeclarationFragment frag =
			rootNode.getAST().newVariableDeclarationFragment();
		frag.setName(
			rootNode.getAST().newSimpleName(NO_OBJECTS));
		frag.setInitializer(
			(Expression)ASTNode.copySubtree(rootNode.getAST(), creation));

		final FieldDeclaration declaration = rootNode.getAST().newFieldDeclaration(frag);
		declaration.setType(
			rootNode.getAST().newArrayType(
				rootNode.getAST().newSimpleType(
					rootNode.getAST().newName(OBJECT_NAME))));
		declaration.modifiers().add(
			rootNode.getAST().newModifier(
				ModifierKeyword.PUBLIC_KEYWORD));
		declaration.modifiers().add(
			rootNode.getAST().newModifier(
				ModifierKeyword.STATIC_KEYWORD));
		declaration.modifiers().add(
			rootNode.getAST().newModifier(
				ModifierKeyword.FINAL_KEYWORD));

		final TypeDeclaration typeDecl = (TypeDeclaration)typeNode;
		final ListRewrite listRewrite = rewrite.getListRewrite(
			typeDecl, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
		listRewrite.insertFirst(declaration, null);

		return true;
	}

	private void replaceOldArrayCreation(ASTRewrite rewrite)
	{
		final SimpleName name = rootNode.getAST().newSimpleName(NO_OBJECTS);
		rewrite.replace(creation, name, null);
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

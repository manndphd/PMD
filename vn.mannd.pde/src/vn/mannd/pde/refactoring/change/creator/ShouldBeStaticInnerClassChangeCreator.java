package vn.mannd.pde.refactoring.change.creator;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import vn.mannd.pde.adt.CompositeParameter;
import vn.mannd.pde.refactoring.change.creator.core.ChangeCreator;
import vn.mannd.pde.util.TypeUtil;
import vn.mannd.pde.util.ValidatorUtil;

public class ShouldBeStaticInnerClassChangeCreator extends ChangeCreator
{

	private final ASTNode rootNode;
	private final TypeDeclaration typeDecl;

	public ShouldBeStaticInnerClassChangeCreator(CompositeParameter parameter) throws NullPointerException
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
	protected void createChange(ASTRewrite rewrite)
	{
		final ListRewrite listRewrite = rewrite.getListRewrite(
			typeDecl, TypeDeclaration.MODIFIERS2_PROPERTY);
		listRewrite.insertLast(
			rootNode.getAST().newModifier(ModifierKeyword.STATIC_KEYWORD), null);
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

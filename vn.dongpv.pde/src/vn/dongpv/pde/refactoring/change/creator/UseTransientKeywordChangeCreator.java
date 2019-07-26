package vn.dongpv.pde.refactoring.change.creator;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.refactoring.change.creator.core.ChangeCreator;
import vn.dongpv.pde.util.TypeUtil;
import vn.dongpv.pde.util.ValidatorUtil;

public class UseTransientKeywordChangeCreator extends ChangeCreator
{

	private final ASTNode rootNode;
	private final FieldDeclaration fieldDecl;

	public UseTransientKeywordChangeCreator(CompositeParameter parameter)
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
	protected void createChange(ASTRewrite rewrite)
	{
		final Modifier modifier = rootNode.getAST().newModifier(
			ModifierKeyword.TRANSIENT_KEYWORD);

		final ListRewrite listRewrite = rewrite.getListRewrite(
			fieldDecl, FieldDeclaration.MODIFIERS2_PROPERTY);
		listRewrite.insertLast(modifier, null);
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

package vn.dongpv.pde.refactoring.change.creator;

import java.util.List;

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

public class FieldShouldBePrivateChangeCreator extends ChangeCreator
{

	private final ASTNode rootNode;
	private final FieldDeclaration fieldDecl;

	public FieldShouldBePrivateChangeCreator(CompositeParameter parameter)
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
		final ListRewrite listRewrite = rewrite.getListRewrite(
			fieldDecl, FieldDeclaration.MODIFIERS2_PROPERTY);

		final List<?> modifiers = fieldDecl.modifiers();
		for (final Object object : modifiers)
		{
			if (object instanceof Modifier)
			{
				final Modifier modifier = (Modifier)object;
				if (modifier.isPublic() || modifier.isProtected())
				{
					listRewrite.remove(modifier, null);
				}
			}
		}

		final Modifier modifier = rootNode.getAST().newModifier(
			ModifierKeyword.PRIVATE_KEYWORD);

		listRewrite.insertFirst(modifier, null);
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

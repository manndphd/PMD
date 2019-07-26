package vn.dongpv.pde.refactoring.change.creator;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.refactoring.change.creator.core.ChangeCreator;
import vn.dongpv.pde.util.TypeUtil;
import vn.dongpv.pde.util.ValidatorUtil;

public class FinalizeShouldNotBePublicChangeCreator extends ChangeCreator
{

	private final ASTNode rootNode;
	private final MethodDeclaration methodDecl;

	public FinalizeShouldNotBePublicChangeCreator(CompositeParameter parameter)
		throws NullPointerException
	{
		super(parameter);

		rootNode = TypeUtil.cast(parameter.getParameter(PARSED_ROOT_NODE));
		methodDecl = TypeUtil.cast(parameter.getParameter(PARSED_NODE));
	}

	@Override
	protected boolean checkParameters()
	{
		return ValidatorUtil.notNull(rootNode, methodDecl);
	}

	@Override
	protected void createChange(ASTRewrite rewrite)
	{
		final ListRewrite listRewrite = rewrite.getListRewrite(
			methodDecl, MethodDeclaration.MODIFIERS2_PROPERTY);

		final List<?> modifiers = methodDecl.modifiers();
		for (final Object object : modifiers)
		{
			if (object instanceof Modifier)
			{
				final Modifier modifier = (Modifier)object;
				if (modifier.isPublic())
				{
					final Modifier newModifier = rootNode.getAST().newModifier(
						ModifierKeyword.PROTECTED_KEYWORD);
					listRewrite.replace(modifier, newModifier, null);
					return;
				}
			}
		}

	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

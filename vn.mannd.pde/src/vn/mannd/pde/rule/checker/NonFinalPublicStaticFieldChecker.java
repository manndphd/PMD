package vn.mannd.pde.rule.checker;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier;

import vn.mannd.pde.adt.CompositeParameter;
import vn.mannd.pde.rule.checker.core.ConditionalChecker;
import vn.mannd.pde.util.TypeUtil;
import vn.mannd.pde.util.ValidatorUtil;

public class NonFinalPublicStaticFieldChecker extends ConditionalChecker
{

	private final ASTNode rootNode;
	private final FieldDeclaration fieldDecl;

	public NonFinalPublicStaticFieldChecker(CompositeParameter parameter)
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
	protected boolean checkDetails()
	{
		final int modifiers = fieldDecl.getModifiers();
		final boolean isPublicStaticButNotFinal =
			Modifier.isPublic(modifiers) &&
				Modifier.isStatic(modifiers) &&
				!Modifier.isFinal(modifiers);

		return isPublicStaticButNotFinal;
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

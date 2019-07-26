package vn.mannd.pde.rule.checker;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import vn.mannd.pde.adt.CompositeParameter;
import vn.mannd.pde.rule.checker.core.ConditionalChecker;
import vn.mannd.pde.util.TypeUtil;
import vn.mannd.pde.util.ValidatorUtil;

public class FieldShouldBeStaticChecker extends ConditionalChecker
{

	private final ASTNode rootNode;
	private final VariableDeclarationFragment fragment;

	public FieldShouldBeStaticChecker(CompositeParameter parameter) throws NullPointerException
	{
		super(parameter);

		rootNode = TypeUtil.cast(parameter.getParameter(PARSED_ROOT_NODE));
		fragment = TypeUtil.cast(parameter.getParameter(PARSED_NODE));
	}

	@Override
	protected boolean checkParameters()
	{
		return ValidatorUtil.notNull(rootNode, fragment);
	}

	@Override
	protected boolean checkDetails()
	{
		if (fragment.getInitializer() == null)
		{
			return false;
		}

		// Checks the variable declaration fragment is in field declaration only

		final ASTNode parent = fragment.getParent();
		if (parent.getNodeType() != ASTNode.FIELD_DECLARATION)
		{
			return false;
		}

		// Checks the variable declaration fragment is final?

		// And makes sure this variable is not static yet

		final FieldDeclaration fieldDecl = (FieldDeclaration)parent;
		final int modifiers = fieldDecl.getModifiers();

		return (!Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers));
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

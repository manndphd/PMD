package vn.dongpv.pde.rule.checker;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;

import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.rule.checker.core.ConditionalChecker;
import vn.dongpv.pde.util.TypeUtil;
import vn.dongpv.pde.util.ValidatorUtil;

public class AvoidSynchronizedModifierInMethodChecker extends ConditionalChecker
{

	private final ASTNode rootNode;
	private final MethodDeclaration methodDecl;

	public AvoidSynchronizedModifierInMethodChecker(CompositeParameter parameter)
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
	protected boolean checkDetails()
	{
		// Makes sure this method is not constructor

		if (methodDecl.isConstructor())
		{
			return false;
		}

		// Makes sure this method must be synchronized

		final int modifiers = methodDecl.getModifiers();
		return Modifier.isSynchronized(modifiers);
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

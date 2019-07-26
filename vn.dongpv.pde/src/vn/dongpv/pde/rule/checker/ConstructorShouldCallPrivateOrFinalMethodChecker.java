package vn.dongpv.pde.rule.checker;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;

import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.rule.checker.core.ConditionalChecker;
import vn.dongpv.pde.util.ASTUtil;
import vn.dongpv.pde.util.TypeUtil;
import vn.dongpv.pde.util.ValidatorUtil;

public class ConstructorShouldCallPrivateOrFinalMethodChecker
	extends ConditionalChecker
{

	private final ASTNode rootNode;
	private final MethodInvocation methodInvocation;

	public ConstructorShouldCallPrivateOrFinalMethodChecker(
		CompositeParameter parameter)
		throws NullPointerException
	{
		super(parameter);

		rootNode = TypeUtil.cast(parameter.getParameter(PARSED_ROOT_NODE));
		methodInvocation = TypeUtil.cast(parameter.getParameter(PARSED_NODE));
	}

	@Override
	protected boolean checkParameters()
	{
		return ValidatorUtil.notNull(rootNode, methodInvocation);
	}

	@Override
	protected boolean checkDetails()
	{
		if (methodInvocation.getExpression() != null)
		{
			return false;
		}

		IMethodBinding binding = methodInvocation.resolveMethodBinding();
		final int modifiers = binding.getModifiers();
		if (Modifier.isPrivate(modifiers) || Modifier.isFinal(modifiers))
		{
			return false;
		}

		final ASTNode constructor = ASTUtil.getAncestor(
			methodInvocation, ASTNode.METHOD_DECLARATION);
		if (constructor == null)
		{
			return false;
		}

		binding = ((MethodDeclaration)constructor).resolveBinding();
		return binding.isConstructor();
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

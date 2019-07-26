package vn.dongpv.pde.rule.checker;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;

import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.rule.checker.core.ConditionalChecker;
import vn.dongpv.pde.rule.checker.core.ConditionalVisitor;
import vn.dongpv.pde.util.TypeUtil;
import vn.dongpv.pde.util.ValidatorUtil;

public class UncalledPrivateMethodChecker extends ConditionalChecker
{

	private final ASTNode rootNode;
	private final MethodDeclaration methodDecl;

	public UncalledPrivateMethodChecker(CompositeParameter parameter)
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

		// Makes sure this method must be private

		final int modifiers = methodDecl.getModifiers();
		if (!Modifier.isPrivate(modifiers))
		{
			return false;
		}

		// Checks this method is invoked yet

		final Visitor visitor = new Visitor(methodDecl);
		rootNode.accept(visitor);
		return visitor.isViolated();
	}

	private static class Visitor extends ConditionalVisitor
	{
		private final IMethodBinding binding;
		private boolean isCalled;

		private Visitor(MethodDeclaration methodDecl)
		{
			this.binding = methodDecl.resolveBinding();
			this.isCalled = false;
		}

		@Override
		public boolean preVisit2(ASTNode node)
		{
			return !isCalled;
		}

		@Override
		public boolean visit(MethodInvocation node)
		{
			if (node.resolveMethodBinding() == binding)
			{
				isCalled = true;
			}
			return true;
		}

		@Override
		public boolean isViolated()
		{
			return !isCalled;
		}

		private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
		{
			throw new java.io.IOException("Class cannot be deserialized");
		}

	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

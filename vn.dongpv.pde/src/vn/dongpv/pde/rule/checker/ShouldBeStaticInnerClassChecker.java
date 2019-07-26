package vn.dongpv.pde.rule.checker;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.rule.checker.core.ConditionalChecker;
import vn.dongpv.pde.rule.checker.core.ConditionalVisitor;
import vn.dongpv.pde.util.TypeUtil;
import vn.dongpv.pde.util.ValidatorUtil;

public class ShouldBeStaticInnerClassChecker extends ConditionalChecker
{

	private final ASTNode rootNode;
	private final TypeDeclaration typeDecl;

	public ShouldBeStaticInnerClassChecker(CompositeParameter parameter)
		throws NullPointerException
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
	protected boolean checkDetails()
	{
		// This type is not inner!

		if (typeDecl.getParent().getNodeType() == ASTNode.COMPILATION_UNIT)
		{
			return false;
		}

		// Sures that this type is not private or static

		final int modifiers = typeDecl.getModifiers();
		if (Modifier.isPrivate(modifiers) || Modifier.isStatic(modifiers))
		{
			return false;
		}

		// Checks this type is used or not

		final Visitor visitor = new Visitor(typeDecl.resolveBinding());
		rootNode.accept(visitor);

		return visitor.isViolated();
	}

	private static class Visitor extends ConditionalVisitor
	{

		private final ITypeBinding binding;
		private boolean isUsed;

		public Visitor(ITypeBinding binding)
		{
			this.binding = binding;
			this.isUsed = false;
		}

		@Override
		public boolean preVisit2(ASTNode node)
		{
			return !isUsed;
		}

		@Override
		public boolean visit(ClassInstanceCreation node)
		{
			if (node.resolveTypeBinding() == binding)
			{
				isUsed = true;
			}
			return true;
		}

		@Override
		public boolean isViolated()
		{
			return !isUsed;
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

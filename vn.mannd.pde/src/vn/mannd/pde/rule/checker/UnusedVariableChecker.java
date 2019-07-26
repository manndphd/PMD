package vn.mannd.pde.rule.checker;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import vn.mannd.pde.adt.CompositeParameter;
import vn.mannd.pde.rule.checker.core.ConditionalChecker;
import vn.mannd.pde.rule.checker.core.ConditionalVisitor;
import vn.mannd.pde.util.ASTUtil;
import vn.mannd.pde.util.TypeUtil;
import vn.mannd.pde.util.ValidatorUtil;

public class UnusedVariableChecker extends ConditionalChecker
{

	private final ASTNode rootNode;
	private final VariableDeclarationFragment fragment;

	public UnusedVariableChecker(CompositeParameter parameter)
		throws NullPointerException
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
		if (!checkDeclaration())
		{
			return false;
		}

		final Visitor visitor = new Visitor(fragment);
		rootNode.accept(visitor);
		return visitor.isViolated();
	}

	private boolean checkDeclaration()
	{
		final IVariableBinding binding = fragment.resolveBinding();
		final int modifiers = binding.getModifiers();

		// Accepts field only

		if (binding.isParameter())
		{
			return false;
		}

		if (binding.isField())
		{
			// Must be private
			return Modifier.isPrivate(modifiers);
		}

		return true;
	}

	private static class Visitor extends ConditionalVisitor
	{
		private final IVariableBinding binding;
		private boolean isUsed;

		private Visitor(VariableDeclarationFragment fragment)
		{
			this.binding = fragment.resolveBinding();
			this.isUsed = false;
		}

		@Override
		public boolean preVisit2(ASTNode node)
		{
			return !isUsed;
		}

		@Override
		public boolean visit(VariableDeclarationFragment node)
		{
			if (ASTUtil.isSameVariableReference(node.getName(), binding))
			{
				return false;
			}
			return true;
		}

		@Override
		public boolean visit(SimpleName node)
		{
			if (ASTUtil.isSameVariableReference(node, binding))
			{
				final ASTNode parent = node.getParent();
				if (!isLeftHandSideOfAnAssigment(parent, node) &&
					!((parent.getNodeType() == ASTNode.FIELD_ACCESS) &&
					isLeftHandSideOfAnAssigment(parent.getParent(), parent)))
				{
					isUsed = true;
				}
			}
			return true;
		}

		@Override
		public boolean isViolated()
		{
			return !isUsed;
		}

		private boolean isLeftHandSideOfAnAssigment(
			ASTNode parent, ASTNode child)
		{
			if (parent.getNodeType() == ASTNode.ASSIGNMENT)
			{
				return ((Assignment)parent).getLeftHandSide() == child;
			}
			return false;
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

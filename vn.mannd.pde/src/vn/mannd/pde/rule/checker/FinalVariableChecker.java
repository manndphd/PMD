package vn.mannd.pde.rule.checker;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression.Operator;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import vn.mannd.pde.adt.CompositeParameter;
import vn.mannd.pde.rule.checker.core.ConditionalChecker;
import vn.mannd.pde.rule.checker.core.ConditionalVisitor;
import vn.mannd.pde.util.ASTUtil;
import vn.mannd.pde.util.TypeUtil;
import vn.mannd.pde.util.ValidatorUtil;

public class FinalVariableChecker extends ConditionalChecker
{

	private final ASTNode rootNode;
	private final VariableDeclarationFragment fragment;

	public FinalVariableChecker(CompositeParameter parameter)
		throws NullPointerException
	{
		super(parameter);

		rootNode = TypeUtil.cast(parameter.getParameter(PARSED_ROOT_NODE));
		fragment = TypeUtil.cast(parameter.getParameter(PARSED_NODE));
	}

	@Override
	protected boolean checkParameters()
	{
		// Don't accept variable declaration expression.
		// For example, a initializer of for statment.

		if (fragment.getParent().getNodeType() == ASTNode.VARIABLE_DECLARATION_EXPRESSION)
		{
			return false;
		}

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

		if (binding.isField())
		{
			// Field must be private and not yet final
			return (Modifier.isPrivate(modifiers) && !Modifier.isFinal(modifiers));
		}

		if (!binding.isParameter())
		{
			// Local variable must be not yet final
			return !Modifier.isFinal(modifiers);
		}

		return false;
	}

	private static class Visitor extends ConditionalVisitor
	{
		private final IVariableBinding binding;
		private final Expression initializer;
		private boolean valueChanged;

		private Visitor(VariableDeclarationFragment fragment)
		{
			this.binding = fragment.resolveBinding();
			this.initializer = fragment.getInitializer();
			this.valueChanged = false;
		}

		@Override
		public boolean preVisit2(ASTNode node)
		{
			return !valueChanged;
		}

		@Override
		public boolean visit(Assignment node)
		{
			final Expression expression = node.getLeftHandSide();
			if (expression instanceof Name)
			{
				final Name name = (Name)expression;
				if (ASTUtil.isSameReference(name, binding))
				{
					valueChanged = true;
				}
			}
			return true;
		}

		@Override
		public boolean visit(PrefixExpression node)
		{
			final Operator operator = node.getOperator();
			final boolean isChangeValueOperator =
				(operator == Operator.INCREMENT) ||
					(operator == Operator.DECREMENT);

			final Expression expression = node.getOperand();
			if (isChangeValueOperator && (expression instanceof Name))
			{
				final Name name = (Name)expression;
				if (ASTUtil.isSameReference(name, binding))
				{
					valueChanged = true;
				}
			}
			return true;
		}

		@Override
		public boolean visit(PostfixExpression node)
		{
			final Expression expression = node.getOperand();
			if (expression instanceof Name)
			{
				final Name name = (Name)expression;
				if (ASTUtil.isSameReference(name, binding))
				{
					valueChanged = true;
				}
			}
			return true;
		}

		@Override
		public boolean isViolated()
		{
			return ((initializer != null) && !valueChanged);
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

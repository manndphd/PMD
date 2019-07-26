package vn.mannd.pde.rule.checker;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Assignment.Operator;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.SimpleName;

import vn.mannd.pde.PluginConstants;
import vn.mannd.pde.adt.CompositeParameter;
import vn.mannd.pde.rule.checker.core.ConditionalChecker;
import vn.mannd.pde.rule.checker.core.ConditionalVisitor;
import vn.mannd.pde.util.ASTUtil;
import vn.mannd.pde.util.TypeUtil;
import vn.mannd.pde.util.ValidatorUtil;

public class AvoidConcatenatingStringUsingPlusOperatorInLoopChecker
	extends ConditionalChecker
{

	private final ASTNode rootNode;
	private final Assignment assignment;

	public AvoidConcatenatingStringUsingPlusOperatorInLoopChecker(CompositeParameter parameter) throws NullPointerException
	{
		super(parameter);

		rootNode = TypeUtil.cast(parameter.getParameter(PARSED_ROOT_NODE));
		assignment = TypeUtil.cast(parameter.getParameter(PARSED_NODE));
	}

	@Override
	protected boolean checkParameters()
	{
		return ValidatorUtil.notNull(rootNode, assignment);
	}

	@Override
	protected boolean checkDetails()
	{
		return (isConcatenatedString() && hasAncestorForStatement());
	}

	private boolean isConcatenatedString()
	{
		// Must be simple name and this simple name has type: java.lang.String

		if (assignment.getLeftHandSide().getNodeType() != ASTNode.SIMPLE_NAME)
		{
			return false;
		}

		// Must be variable

		final SimpleName name = (SimpleName)assignment.getLeftHandSide();
		if (!(name.resolveBinding() instanceof IVariableBinding))
		{
			return false;
		}

		// Must be String type

		final IVariableBinding binding = (IVariableBinding)name.resolveBinding();
		if (!binding.getType().getQualifiedName().equalsIgnoreCase(
			PluginConstants.STRING_CLASS_NAME))
		{
			return false;
		}

		// If this assignment has operator: `+=`, it's true

		if (assignment.getOperator() == Operator.PLUS_ASSIGN)
		{
			return true;
		}

		// Checks this assignment has format: `s = s + other` or not

		final Visitor visitor = new Visitor(binding);
		assignment.accept(visitor);

		return visitor.isViolated();
	}

	private boolean hasAncestorForStatement()
	{
		return ASTUtil.hasLoopStatementAncestor(assignment);
	}

	private static class Visitor extends ConditionalVisitor
	{

		private final IVariableBinding binding;
		private boolean isUsedToSelfConcatenated;

		private Visitor(IVariableBinding binding)
		{
			this.binding = binding;
			this.isUsedToSelfConcatenated = false;
		}

		@Override
		public boolean preVisit2(ASTNode node)
		{
			return !isUsedToSelfConcatenated;
		}

		@Override
		public boolean visit(InfixExpression node)
		{
			if (node.getLeftOperand().getNodeType() == ASTNode.SIMPLE_NAME)
			{
				final SimpleName name = (SimpleName)node.getLeftOperand();
				if (ASTUtil.isSameReference(name, binding) &&
					(node.getOperator() == InfixExpression.Operator.PLUS))
				{
					isUsedToSelfConcatenated = true;
				}
			}

			if (!isUsedToSelfConcatenated &&
				(node.getRightOperand().getNodeType() == ASTNode.SIMPLE_NAME))
			{
				final SimpleName name = (SimpleName)node.getRightOperand();
				if (ASTUtil.isSameReference(name, binding) &&
					(node.getOperator() == InfixExpression.Operator.PLUS))
				{
					isUsedToSelfConcatenated = true;
				}
			}

			return true;
		}

		@Override
		public boolean isViolated()
		{
			return isUsedToSelfConcatenated;
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

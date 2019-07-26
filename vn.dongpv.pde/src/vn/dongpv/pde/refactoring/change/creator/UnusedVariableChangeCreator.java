package vn.dongpv.pde.refactoring.change.creator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.refactoring.change.creator.core.ChangeCreator;
import vn.dongpv.pde.util.ASTUtil;
import vn.dongpv.pde.util.TypeUtil;
import vn.dongpv.pde.util.ValidatorUtil;

public class UnusedVariableChangeCreator extends ChangeCreator
{

	private final ASTNode rootNode;
	private final VariableDeclarationFragment fragment;

	public UnusedVariableChangeCreator(CompositeParameter parameter)
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
	protected void createChange(ASTRewrite rewrite)
	{
		final ASTNode parent = fragment.getParent();
		if (parent.getNodeType() == ASTNode.FIELD_DECLARATION)
		{
			removeDeclInFieldDecl(rewrite, (FieldDeclaration)parent);
		}
		else if (parent.getNodeType() == ASTNode.VARIABLE_DECLARATION_STATEMENT)
		{
			removeDeclInVarDeclStatement(rewrite, (VariableDeclarationStatement)parent);
		}
		else if (parent.getNodeType() == ASTNode.VARIABLE_DECLARATION_EXPRESSION)
		{
			removeDeclInVarDeclExp(rewrite, (VariableDeclarationExpression)parent);
		}

		final Visitor visitor = new Visitor(fragment);
		rootNode.accept(visitor);
		final List<ASTNode> statements = visitor.getStatements();
		for (final ASTNode statement : statements)
		{
			rewrite.remove(statement, null);
		}
	}

	private void removeDeclInFieldDecl(
		ASTRewrite rewrite, FieldDeclaration parent)
	{
		if (parent.fragments().size() <= 1)
		{
			rewrite.remove(parent, null);
		}
		else
		{
			final ListRewrite listRewrite = rewrite.getListRewrite(
				parent, FieldDeclaration.FRAGMENTS_PROPERTY);
			listRewrite.remove(fragment, null);
		}
	}

	private void removeDeclInVarDeclStatement(
		ASTRewrite rewrite, VariableDeclarationStatement parent)
	{
		if (parent.fragments().size() <= 1)
		{
			rewrite.remove(parent, null);
		}
		else
		{
			final ListRewrite listRewrite = rewrite.getListRewrite(
				parent, VariableDeclarationStatement.FRAGMENTS_PROPERTY);
			listRewrite.remove(fragment, null);
		}
	}

	private void removeDeclInVarDeclExp(
		ASTRewrite rewrite, VariableDeclarationExpression parent)
	{
		if (parent.fragments().size() <= 1)
		{
			rewrite.remove(parent, null);
		}
		else
		{
			final ListRewrite listRewrite = rewrite.getListRewrite(
				parent, VariableDeclarationExpression.FRAGMENTS_PROPERTY);
			listRewrite.remove(fragment, null);
		}
	}

	private static class Visitor extends ASTVisitor
	{
		private final IVariableBinding binding;
		private final List<ASTNode> statements;

		private Visitor(VariableDeclarationFragment fragment)
		{
			this.binding = fragment.resolveBinding();
			this.statements = new ArrayList<ASTNode>();
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
					statements.add(ASTUtil.getAncestor(
						node, ASTNode.EXPRESSION_STATEMENT));
				}
			}
			return true;
		}

		@Override
		public boolean visit(PrefixExpression node)
		{
			final Expression expression = node.getOperand();
			if (expression instanceof Name)
			{
				final Name name = (Name)expression;
				if (ASTUtil.isSameReference(name, binding))
				{
					statements.add(ASTUtil.getAncestor(
						node, ASTNode.EXPRESSION_STATEMENT));
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
					statements.add(ASTUtil.getAncestor(
						node, ASTNode.EXPRESSION_STATEMENT));
				}
			}
			return true;
		}

		public List<ASTNode> getStatements()
		{
			return statements;
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

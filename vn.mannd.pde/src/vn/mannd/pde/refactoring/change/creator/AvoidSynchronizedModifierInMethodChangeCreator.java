package vn.mannd.pde.refactoring.change.creator;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import vn.mannd.pde.adt.CompositeParameter;
import vn.mannd.pde.refactoring.change.creator.core.ChangeCreator;
import vn.mannd.pde.util.TypeUtil;
import vn.mannd.pde.util.ValidatorUtil;

public class AvoidSynchronizedModifierInMethodChangeCreator extends ChangeCreator
{

	private final ASTNode rootNode;
	private final MethodDeclaration methodDecl;

	public AvoidSynchronizedModifierInMethodChangeCreator(CompositeParameter parameter)
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
	protected void createChange(ASTRewrite rewrite)
	{
		removeSyncModifier(rewrite);
		createSyncBlock(rewrite);
	}

	private void removeSyncModifier(ASTRewrite rewrite)
	{
		final List<?> modifiers = methodDecl.modifiers();
		for (final Object object : modifiers)
		{
			final Modifier modifier = TypeUtil.cast(object);
			if ((modifier != null) && modifier.isSynchronized())
			{
				final ListRewrite listRewrite = rewrite.getListRewrite(
					methodDecl, MethodDeclaration.MODIFIERS2_PROPERTY);
				listRewrite.remove(modifier, null);
				return;
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void createSyncBlock(ASTRewrite rewrite)
	{
		final SynchronizedStatement statement = rootNode.getAST().newSynchronizedStatement();
		statement.setBody((Block)
			ASTNode.copySubtree(rootNode.getAST(), methodDecl.getBody()));
		statement.setExpression(rootNode.getAST().newThisExpression());

		final Block body = rootNode.getAST().newBlock();
		body.statements().add(statement);

		rewrite.replace(methodDecl.getBody(), body, null);
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

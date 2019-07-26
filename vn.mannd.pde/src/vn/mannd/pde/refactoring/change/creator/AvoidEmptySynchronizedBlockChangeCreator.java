package vn.mannd.pde.refactoring.change.creator;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import vn.mannd.pde.adt.CompositeParameter;
import vn.mannd.pde.refactoring.change.creator.core.ChangeCreator;
import vn.mannd.pde.util.TypeUtil;
import vn.mannd.pde.util.ValidatorUtil;

public class AvoidEmptySynchronizedBlockChangeCreator extends ChangeCreator
{

	private final ASTNode rootNode;
	private final SynchronizedStatement synchronizedStatement;

	public AvoidEmptySynchronizedBlockChangeCreator(CompositeParameter parameter)
		throws NullPointerException
	{
		super(parameter);

		rootNode = TypeUtil.cast(parameter.getParameter(PARSED_ROOT_NODE));
		synchronizedStatement = TypeUtil.cast(parameter.getParameter(PARSED_NODE));
	}

	@Override
	protected boolean checkParameters()
	{
		return ValidatorUtil.notNull(rootNode, synchronizedStatement);
	}

	@Override
	protected void createChange(ASTRewrite rewrite)
	{
		rewrite.remove(synchronizedStatement, null);
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

package vn.dongpv.pde.rule.checker;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.SynchronizedStatement;

import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.rule.checker.core.ConditionalChecker;
import vn.dongpv.pde.util.ASTUtil;
import vn.dongpv.pde.util.TypeUtil;
import vn.dongpv.pde.util.ValidatorUtil;

public class AvoidSynchronizedBlocksInLoopChecker extends ConditionalChecker
{

	private final ASTNode rootNode;
	private final SynchronizedStatement synchronizedStatement;

	public AvoidSynchronizedBlocksInLoopChecker(CompositeParameter parameter)
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
	protected boolean checkDetails()
	{
		return ASTUtil.hasLoopStatementAncestor(synchronizedStatement);
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

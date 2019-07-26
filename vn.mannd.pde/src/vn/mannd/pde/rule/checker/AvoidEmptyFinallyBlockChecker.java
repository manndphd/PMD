package vn.mannd.pde.rule.checker;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.TryStatement;

import vn.mannd.pde.adt.CompositeParameter;
import vn.mannd.pde.rule.checker.core.ConditionalChecker;
import vn.mannd.pde.util.TypeUtil;
import vn.mannd.pde.util.ValidatorUtil;

public class AvoidEmptyFinallyBlockChecker extends ConditionalChecker
{

	private final ASTNode rootNode;
	private final TryStatement tryStatement;

	public AvoidEmptyFinallyBlockChecker(CompositeParameter parameter)
		throws NullPointerException
	{
		super(parameter);

		rootNode = TypeUtil.cast(parameter.getParameter(PARSED_ROOT_NODE));
		tryStatement = TypeUtil.cast(parameter.getParameter(PARSED_NODE));
	}

	@Override
	protected boolean checkParameters()
	{
		return ValidatorUtil.notNull(rootNode, tryStatement);
	}

	@Override
	protected boolean checkDetails()
	{
		final Block finallyBlock = tryStatement.getFinally();
		if (finallyBlock == null)
		{
			return false;
		}

		return (finallyBlock.statements().size() == 0);
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

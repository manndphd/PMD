package vn.dongpv.pde.rule.checker;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.TryStatement;

import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.rule.checker.core.ConditionalChecker;
import vn.dongpv.pde.util.TypeUtil;
import vn.dongpv.pde.util.ValidatorUtil;

public class AvoidEmptyCatchBlocksChecker extends ConditionalChecker
{

	private final ASTNode rootNode;
	private final TryStatement tryStatement;

	public AvoidEmptyCatchBlocksChecker(CompositeParameter parameter)
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
		final List<?> catchClauses = tryStatement.catchClauses();
		if (catchClauses == null)
		{
			return false;
		}

		for (final Object object : catchClauses)
		{
			final CatchClause catchClause = (CatchClause)object;
			if (catchClause.getBody().statements().size() == 0)
			{
				return true;
			}
		}

		return false;
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

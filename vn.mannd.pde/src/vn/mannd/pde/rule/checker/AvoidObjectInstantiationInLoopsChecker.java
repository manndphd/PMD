package vn.mannd.pde.rule.checker;

import org.eclipse.jdt.core.dom.ASTNode;

import vn.mannd.pde.adt.CompositeParameter;
import vn.mannd.pde.rule.checker.core.ConditionalChecker;
import vn.mannd.pde.util.ASTUtil;
import vn.mannd.pde.util.TypeUtil;
import vn.mannd.pde.util.ValidatorUtil;

public class AvoidObjectInstantiationInLoopsChecker extends ConditionalChecker
{

	private final ASTNode rootNode;
	private final ASTNode creation;

	public AvoidObjectInstantiationInLoopsChecker(CompositeParameter parameter)
		throws NullPointerException
	{
		super(parameter);

		rootNode = TypeUtil.cast(parameter.getParameter(PARSED_ROOT_NODE));
		creation = TypeUtil.cast(parameter.getParameter(PARSED_NODE));
	}

	@Override
	protected boolean checkParameters()
	{
		return ValidatorUtil.notNull(rootNode, creation);
	}

	@Override
	protected boolean checkDetails()
	{
		if ((creation.getNodeType() == ASTNode.CLASS_INSTANCE_CREATION) ||
			(creation.getNodeType() == ASTNode.ARRAY_CREATION))
		{
			return ASTUtil.hasLoopStatementAncestor(creation);
		}

		return false;
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

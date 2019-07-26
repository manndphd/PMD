package vn.mannd.pde.refactoring.change.creator;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import vn.mannd.pde.adt.CompositeParameter;
import vn.mannd.pde.refactoring.change.creator.core.ChangeCreator;
import vn.mannd.pde.util.TypeUtil;
import vn.mannd.pde.util.ValidatorUtil;

public class AvoidUsingThreadYieldChangeCreator extends ChangeCreator
{

	public static final String WAIT_METHOD_NAME = "wait";

	private final ASTNode rootNode;
	private final MethodInvocation methodInvocation;

	public AvoidUsingThreadYieldChangeCreator(CompositeParameter parameter)
		throws NullPointerException
	{
		super(parameter);

		rootNode = TypeUtil.cast(parameter.getParameter(PARSED_ROOT_NODE));
		methodInvocation = TypeUtil.cast(parameter.getParameter(PARSED_NODE));
	}

	@Override
	protected boolean checkParameters()
	{
		return ValidatorUtil.notNull(rootNode, methodInvocation);
	}

	@Override
	protected void createChange(ASTRewrite rewrite)
	{
		final SimpleName methodName = rootNode.getAST().newSimpleName(WAIT_METHOD_NAME);
		final MethodInvocation newInvocation = rootNode.getAST().newMethodInvocation();
		newInvocation.setName(methodName);

		rewrite.replace(methodInvocation, newInvocation, null);
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}
}

package vn.dongpv.pde.refactoring.change.creator;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.refactoring.change.creator.core.ChangeCreator;
import vn.dongpv.pde.util.TypeUtil;
import vn.dongpv.pde.util.ValidatorUtil;

public class UncalledPrivateMethodChangeCreator extends ChangeCreator
{

	private final ASTNode rootNode;
	private final MethodDeclaration methodDecl;

	public UncalledPrivateMethodChangeCreator(CompositeParameter parameter)
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
		rewrite.remove(methodDecl, null);
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

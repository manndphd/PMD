package vn.dongpv.pde.refactoring.change.creator;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.refactoring.change.creator.core.ChangeCreator;
import vn.dongpv.pde.util.TypeUtil;
import vn.dongpv.pde.util.ValidatorUtil;

public class AvoidInstantiationForGetClassChangeCreator extends ChangeCreator
{

	public static final String SURROUNDED_METHOD_INVOCATION = "SURROUNDED_METHOD_INVOCATION";

	private final ASTNode rootNode;
	private final ClassInstanceCreation creation;
	private final ASTNode parenthesizedExp;

	public AvoidInstantiationForGetClassChangeCreator(CompositeParameter parameter)
		throws NullPointerException
	{
		super(parameter);

		rootNode = TypeUtil.cast(parameter.getParameter(PARSED_ROOT_NODE));
		creation = TypeUtil.cast(parameter.getParameter(PARSED_NODE));
		parenthesizedExp = TypeUtil.cast(parameter.getParameter(SURROUNDED_METHOD_INVOCATION));
	}

	@Override
	protected boolean checkParameters()
	{
		return ValidatorUtil.notNull(rootNode, creation, parenthesizedExp);
	}

	@Override
	protected void createChange(ASTRewrite rewrite)
	{
		final TypeLiteral literal = rootNode.getAST().newTypeLiteral();
		literal.setType((Type)
			ASTNode.copySubtree(rootNode.getAST(), creation.getType()));

		rewrite.replace(parenthesizedExp, literal, null);
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

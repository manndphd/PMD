package vn.dongpv.pde.rule.checker;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ITypeBinding;

import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.rule.checker.core.ConditionalChecker;
import vn.dongpv.pde.util.TypeUtil;
import vn.dongpv.pde.util.ValidatorUtil;

public class AvoidBooleanArrayChecker extends ConditionalChecker
{

	public static final String BOOLEAN_PRIMITIVE_TYPE_NAME = "boolean";

	private final ASTNode rootNode;
	private final ArrayCreation creation;

	public AvoidBooleanArrayChecker(CompositeParameter parameter)
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
		final ITypeBinding componentBinding =
			creation.getType().getComponentType().resolveBinding();

		final String componentTypeName = componentBinding.getQualifiedName();

		if (componentTypeName.equalsIgnoreCase(BOOLEAN_PRIMITIVE_TYPE_NAME))
		{
			return true;
		}

		return false;
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

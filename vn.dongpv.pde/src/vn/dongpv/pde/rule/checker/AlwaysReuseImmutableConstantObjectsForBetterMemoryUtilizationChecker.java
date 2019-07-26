package vn.dongpv.pde.rule.checker;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.NumberLiteral;

import vn.dongpv.pde.PluginConstants;
import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.rule.checker.core.ConditionalChecker;
import vn.dongpv.pde.util.TypeUtil;
import vn.dongpv.pde.util.ValidatorUtil;

public class AlwaysReuseImmutableConstantObjectsForBetterMemoryUtilizationChecker
	extends ConditionalChecker
{

	private final ASTNode rootNode;
	private final ArrayCreation creation;

	public AlwaysReuseImmutableConstantObjectsForBetterMemoryUtilizationChecker(
		CompositeParameter parameter)
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
		// Object[0] ?

		final ITypeBinding componentBinding =
			creation.getType().getComponentType().resolveBinding();

		final String componentTypeName = componentBinding.getQualifiedName();

		@SuppressWarnings("unchecked")
		final List<Expression> dimensions = creation.dimensions();

		if (componentTypeName.equalsIgnoreCase(PluginConstants.OBJECT_CLASS_NAME) &&
			(dimensions.size() == 1))
		{
			final Expression exp = dimensions.get(0);
			if (exp.getNodeType() != ASTNode.NUMBER_LITERAL)
			{
				return false;
			}

			return (((NumberLiteral)exp).
				getToken().equalsIgnoreCase(PluginConstants.ZERO_STRING));
		}

		return false;
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

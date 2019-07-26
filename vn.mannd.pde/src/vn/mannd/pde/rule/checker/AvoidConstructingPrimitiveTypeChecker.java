package vn.mannd.pde.rule.checker;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.StringLiteral;

import vn.mannd.pde.PluginConstants;
import vn.mannd.pde.adt.CompositeParameter;
import vn.mannd.pde.refactoring.change.creator.AvoidConstructingPrimitiveTypeChangeCreator;
import vn.mannd.pde.rule.checker.core.ConditionalChecker;
import vn.mannd.pde.util.ASTUtil;
import vn.mannd.pde.util.TypeUtil;
import vn.mannd.pde.util.ValidatorUtil;

public class AvoidConstructingPrimitiveTypeChecker extends ConditionalChecker
{

	private final ASTNode rootNode;
	private final ClassInstanceCreation creation;
	private boolean checkParentNode;

	public AvoidConstructingPrimitiveTypeChecker(CompositeParameter parameter)
		throws NullPointerException
	{
		super(parameter);

		rootNode = TypeUtil.cast(parameter.getParameter(PARSED_ROOT_NODE));
		creation = TypeUtil.cast(parameter.getParameter(PARSED_NODE));
		checkParentNode = true;
	}

	@Override
	protected boolean checkParameters()
	{
		return ValidatorUtil.notNull(rootNode, creation);
	}

	@Override
	protected boolean checkDetails()
	{
		// Checks parent node example: new Integer(8).toString()

		if (checkParentNode && isExpressionOfParentMethodInvocation())
		{
			return false;
		}

		// Checks the creation of an object has primitive type?

		final IMethodBinding binding = creation.resolveConstructorBinding();
		return isPrimitiveConstruction(binding);
	}

	/**
	 * If need to check the creation of an object is an expression of its parent
	 * method invocation node or not, set to true. If just check the creation of
	 * an object is primitive type construction, set to false.
	 * 
	 * @param checkParentNode
	 *            true if need to check parent node.
	 */
	public void setCheckParentNode(boolean checkParentNode)
	{
		this.checkParentNode = checkParentNode;
	}

	private boolean isExpressionOfParentMethodInvocation()
	{
		if (creation.getParent().getNodeType() == ASTNode.METHOD_INVOCATION)
		{
			// This creation is expression of parent - method invocation node

			final MethodInvocation parent = (MethodInvocation)creation.getParent();
			if (creation.equals(parent.getExpression()))
			{
				return true;
			}
		}
		return false;
	}

	private boolean isPrimitiveConstruction(IMethodBinding binding)
	{
		// Primitive type?

		final String type = ASTUtil.getObjectType(binding);
		if (type == PluginConstants.NON_PRIMITIVE_CLASS_NAME)
		{
			// not primitive type
			return false;
		}

		// Many arguments?

		final ITypeBinding[] parameterBindings = binding.getParameterTypes();
		if ((parameterBindings.length == 0) &&
			(type == PluginConstants.STRING_CLASS_NAME))
		{
			// This situation is String.String() only
			saveStateToAdditionalParameter(type, true);
			return true;
		}

		// Just accepts only constructors that have one argument

		if (parameterBindings.length != 1)
		{
			return false;
		}

		// Argument value
		final ASTNode value = TypeUtil.cast(creation.arguments().get(0));
		if (value == null)
		{
			return false;
		}

		final ITypeBinding parameterBinding = parameterBindings[0];
		boolean needConvert = false;

		// T.T("blah")
		if (parameterBinding.getQualifiedName().equalsIgnoreCase(
			PluginConstants.STRING_CLASS_NAME))
		{
			if (type != PluginConstants.STRING_CLASS_NAME) // Not String
			{
				if (value instanceof StringLiteral)
				{
					final String literal = ((StringLiteral)value).getLiteralValue();
					try
					{
						// Boolean type has different analyze strategy

						if (type != PluginConstants.BOOLEAN_CLASS_NAME)
						{
							Double.parseDouble(literal);
						}
						needConvert = true;
					}
					catch (final NumberFormatException e)
					{
						e.printStackTrace();
						return false;
					}
				}
				else
				{
					return false;
				}
			}
			else
			{
				saveStateToAdditionalParameter(type, needConvert);
				return true;
			}
		}

		if (type != PluginConstants.STRING_CLASS_NAME)
		{
			saveStateToAdditionalParameter(type, needConvert);
			return true;
		}

		return false;
	}

	private void saveStateToAdditionalParameter(String type, boolean needConvert)
	{
		if (isAllowProvideMoreParameters())
		{
			getAdditionalParameter().addParameter(
				AvoidConstructingPrimitiveTypeChangeCreator.OBJECT_TYPE,
				type);
			getAdditionalParameter().addParameter(
				AvoidConstructingPrimitiveTypeChangeCreator.NEED_CONVERT_PARAM_TO_STRING,
				needConvert);
		}
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

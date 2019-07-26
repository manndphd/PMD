package vn.mannd.pde.refactoring.change.creator;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.PrimitiveType.Code;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import vn.mannd.pde.PluginConstants;
import vn.mannd.pde.adt.CompositeParameter;
import vn.mannd.pde.refactoring.change.creator.core.ChangeCreator;
import vn.mannd.pde.util.ASTUtil;
import vn.mannd.pde.util.TypeUtil;
import vn.mannd.pde.util.ValidatorUtil;

public class AvoidConstructingPrimitiveTypeChangeCreator extends ChangeCreator
{

	public static final String OBJECT_TYPE = "OBJECT_TYPE";
	public static final String NEED_CONVERT_PARAM_TO_STRING = "NEED_CONVERT_PARAM_TO_STRING";

	private final ASTNode rootNode;
	private final ClassInstanceCreation creation;
	private final String objectType;
	private final boolean needConvert;

	public AvoidConstructingPrimitiveTypeChangeCreator(CompositeParameter parameter)
		throws NullPointerException
	{
		super(parameter);

		rootNode = TypeUtil.cast(parameter.getParameter(PARSED_ROOT_NODE));
		creation = TypeUtil.cast(parameter.getParameter(PARSED_NODE));
		objectType = TypeUtil.cast(parameter.getParameter(OBJECT_TYPE));
		needConvert = TypeUtil.cast(parameter.getParameter(NEED_CONVERT_PARAM_TO_STRING));
	}

	@Override
	protected boolean checkParameters()
	{
		return ValidatorUtil.notNull(rootNode, creation, objectType, needConvert);
	}

	@Override
	protected void createChange(ASTRewrite rewrite)
	{
		final ASTNode replacement = createReplacementNode();
		if (replacement != null)
		{
			rewrite.replace(creation, replacement, null);
		}
	}

	private ASTNode createReplacementNode()
	{
		if (!needConvert)
		{
			// Just copy original parameter

			return copyOriginalParameterNode();
		}

		// Only String's constructor has non-argument

		if (creation.arguments().size() == 0)
		{
			if (objectType == PluginConstants.STRING_CLASS_NAME)
			{
				// Creates empty string literal

				return ASTUtil.createStringLiteral(
					rootNode.getAST(), PluginConstants.EMPTY_STRING);
			}
			return null;
		}

		// Takes the sole argument

		final StringLiteral originalParameter = TypeUtil.cast(creation.arguments().get(0));
		if (originalParameter == null)
		{
			return null;
		}

		// ... and this is string value

		final String originalValue = originalParameter.getLiteralValue();

		// Creates corresponding expression

		if (objectType.equalsIgnoreCase(
			PluginConstants.BOOLEAN_CLASS_NAME))
		{
			return createExpressionForBooleanType(originalValue);
		}

		if (objectType.equalsIgnoreCase(
			PluginConstants.BYTE_CLASS_NAME))
		{
			return createExpressionForByteType(originalValue);
		}

		if (objectType.equalsIgnoreCase(
			PluginConstants.SHORT_CLASS_NAME))
		{
			return createExpressionForShortType(originalValue);
		}

		if (objectType.equalsIgnoreCase(
			PluginConstants.INTEGER_CLASS_NAME))
		{
			return createExpressionForIntegerType(originalValue);
		}

		if (objectType.equalsIgnoreCase(
			PluginConstants.LONG_CLASS_NAME))
		{
			return createExpressionForLongType(originalValue);
		}

		if (objectType.equalsIgnoreCase(
			PluginConstants.FLOAT_CLASS_NAME))
		{
			return createExpressionForFloatType(originalValue);
		}

		if (objectType.equalsIgnoreCase(
			PluginConstants.DOUBLE_CLASS_NAME))
		{
			return createExpressionForDoubleType(originalValue);
		}

		return null;
	}

	private ASTNode copyOriginalParameterNode()
	{
		// Just copy

		final ASTNode replacement = ASTNode.copySubtree(rootNode.getAST(),
			(ASTNode)creation.arguments().get(0));
		return replacement;
	}

	private ASTNode createExpressionForBooleanType(String originalValue)
	{
		final BooleanLiteral literal =
			ASTUtil.createBooleanLiteral(
				rootNode.getAST(), Boolean.parseBoolean(originalValue));
		return literal;
	}

	private ASTNode createExpressionForByteType(String originalValue)
	{
		try
		{
			// Valid value?

			Byte.valueOf(originalValue);
			return createNumberLiteral(originalValue);
		}
		catch (final NumberFormatException e)
		{
			// Creates cast expression

			return createCastExpression(PrimitiveType.BYTE, originalValue);
		}
	}

	private ASTNode createExpressionForShortType(String originalValue)
	{
		try
		{
			// Valid value?

			Short.valueOf(originalValue);
			return createNumberLiteral(originalValue);
		}
		catch (final NumberFormatException e)
		{
			// Creates cast expression

			return createCastExpression(PrimitiveType.SHORT, originalValue);
		}
	}

	private ASTNode createExpressionForIntegerType(String originalValue)
	{
		try
		{
			// Valid value?

			Integer.valueOf(originalValue);
			return createNumberLiteral(originalValue);
		}
		catch (final NumberFormatException e)
		{
			// Creates cast expression

			return createCastExpression(PrimitiveType.INT, originalValue);
		}
	}

	private ASTNode createExpressionForLongType(String originalValue)
	{
		try
		{
			// Valid value?

			Long.valueOf(originalValue);
			return createNumberLiteral(originalValue);
		}
		catch (final NumberFormatException e)
		{
			// Creates cast expression

			return createCastExpression(PrimitiveType.LONG, originalValue);
		}
	}

	private ASTNode createExpressionForFloatType(String originalValue)
	{
		try
		{
			// Valid value?

			Float.valueOf(originalValue);
			return createNumberLiteral(originalValue);
		}
		catch (final NumberFormatException e)
		{
			// Creates cast expression

			return createCastExpression(PrimitiveType.FLOAT, originalValue);
		}
	}

	private ASTNode createExpressionForDoubleType(String originalValue)
	{
		try
		{
			// Valid value?

			Double.valueOf(originalValue);
			return createNumberLiteral(originalValue);
		}
		catch (final NumberFormatException e)
		{
			// Creates cast expression

			return createCastExpression(PrimitiveType.DOUBLE, originalValue);
		}
	}

	private NumberLiteral createNumberLiteral(String originalValue)
	{
		try
		{
			// Valid value?

			Integer.valueOf(originalValue);
		}
		catch (final NumberFormatException e)
		{
			e.printStackTrace();

			// Adds 'L' character to indicate this literial is long type
			originalValue += "L";
		}

		final NumberLiteral literal = ASTUtil.createNumberLiteral(
			rootNode.getAST(), originalValue);

		return literal;
	}

	private ASTNode createCastExpression(Code type, String originalValue)
	{
		final CastExpression castExpression = rootNode.getAST().newCastExpression();
		castExpression.setType(rootNode.getAST().newPrimitiveType(type));
		castExpression.setExpression(createNumberLiteral(originalValue));

		return castExpression;
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

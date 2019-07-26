package vn.mannd.pde.refactoring.change.creator;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.PrimitiveType.Code;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import vn.mannd.pde.PluginConstants;
import vn.mannd.pde.adt.CompositeParameter;
import vn.mannd.pde.refactoring.change.creator.core.ChangeCreator;
import vn.mannd.pde.rule.checker.BoxedPrimitiveJustCallToStringChecker;
import vn.mannd.pde.util.ASTUtil;
import vn.mannd.pde.util.TypeUtil;
import vn.mannd.pde.util.ValidatorUtil;

public class BoxedPrimitiveJustCallToStringChangeCreator extends ChangeCreator
{

	private final ASTNode rootNode;
	private final MethodInvocation methodInvocation;
	private final String objectType;
	private final boolean needConvert;

	public BoxedPrimitiveJustCallToStringChangeCreator(CompositeParameter parameter)
		throws NullPointerException
	{
		super(parameter);

		rootNode = TypeUtil.cast(parameter.getParameter(PARSED_ROOT_NODE));
		methodInvocation = TypeUtil.cast(parameter.getParameter(PARSED_NODE));

		objectType = TypeUtil.cast(parameter.getParameter(
			AvoidConstructingPrimitiveTypeChangeCreator.OBJECT_TYPE));

		needConvert = TypeUtil.cast(parameter.getParameter(
			AvoidConstructingPrimitiveTypeChangeCreator.NEED_CONVERT_PARAM_TO_STRING));
	}

	@Override
	protected boolean checkParameters()
	{
		return ValidatorUtil.notNull(rootNode, methodInvocation, objectType, needConvert);
	}

	@Override
	protected void createChange(ASTRewrite rewrite)
	{
		final ASTNode replacement = createReplacementNode();
		if (replacement != null)
		{
			rewrite.replace(methodInvocation, replacement, null);
		}
	}

	private ASTNode createReplacementNode()
	{
		if (objectType.equalsIgnoreCase(
			PluginConstants.STRING_CLASS_NAME))
		{
			return ASTUtil.createStringLiteral(rootNode.getAST(),
				((StringLiteral)createArgument()).getLiteralValue());
		}

		return createMethodInvocation();
	}

	@SuppressWarnings("unchecked")
	private ASTNode createMethodInvocation()
	{
		// Creates new method invocation
		final MethodInvocation newMethodInvocation =
			rootNode.getAST().newMethodInvocation();

		// ... creates expression
		newMethodInvocation.setExpression(createExpression());

		// ... and creates name
		newMethodInvocation.setName(rootNode.getAST().newSimpleName(
			BoxedPrimitiveJustCallToStringChecker.TO_STRING_METHOD_NAME));

		// ... then adds arguments
		newMethodInvocation.arguments().add(createArgument());

		return newMethodInvocation;
	}

	private Expression createExpression()
	{
		if (objectType.equalsIgnoreCase(
			PluginConstants.BOOLEAN_CLASS_NAME))
		{
			return rootNode.getAST().newSimpleName(PluginConstants.BOOLEAN_NAME);
		}

		if (objectType.equalsIgnoreCase(
			PluginConstants.CHARACTER_CLASS_NAME))
		{
			return rootNode.getAST().newSimpleName(PluginConstants.CHARACTER_NAME);
		}

		if (objectType.equalsIgnoreCase(
			PluginConstants.BYTE_CLASS_NAME))
		{
			return rootNode.getAST().newSimpleName(PluginConstants.BYTE_NAME);
		}

		if (objectType.equalsIgnoreCase(
			PluginConstants.SHORT_CLASS_NAME))
		{
			return rootNode.getAST().newSimpleName(PluginConstants.SHORT_NAME);
		}

		if (objectType.equalsIgnoreCase(
			PluginConstants.INTEGER_CLASS_NAME))
		{
			return rootNode.getAST().newSimpleName(PluginConstants.INTEGER_NAME);
		}

		if (objectType.equalsIgnoreCase(
			PluginConstants.LONG_CLASS_NAME))
		{
			return rootNode.getAST().newSimpleName(PluginConstants.LONG_NAME);
		}

		if (objectType.equalsIgnoreCase(
			PluginConstants.FLOAT_CLASS_NAME))
		{
			return rootNode.getAST().newSimpleName(PluginConstants.FLOAT_NAME);
		}

		if (objectType.equalsIgnoreCase(
			PluginConstants.DOUBLE_CLASS_NAME))
		{
			return rootNode.getAST().newSimpleName(PluginConstants.DOUBLE_NAME);
		}

		return null;
	}

	private Object createArgument()
	{
		final ClassInstanceCreation creation = TypeUtil.cast(
			methodInvocation.getExpression());
		if (creation == null)
		{
			return null;
		}

		if (!needConvert)
		{
			return copyOriginalParameterNode(creation);
		}

		if (creation.arguments().size() == 0)
		{
			if (objectType == PluginConstants.STRING_CLASS_NAME)
			{
				return ASTUtil.createStringLiteral(
					rootNode.getAST(), PluginConstants.EMPTY_STRING);
			}
			return null;
		}

		final StringLiteral originalParameter =
			TypeUtil.cast(creation.arguments().get(0));
		if (originalParameter == null)
		{
			return null;
		}

		final String originalValue = originalParameter.getLiteralValue();

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

	private ASTNode copyOriginalParameterNode(ClassInstanceCreation creation)
	{
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
			Byte.valueOf(originalValue);
			return createNumberLiteral(originalValue);
		}
		catch (final NumberFormatException e)
		{
			return createCastExpression(PrimitiveType.BYTE, originalValue);
		}
	}

	private ASTNode createExpressionForShortType(String originalValue)
	{
		try
		{
			Short.valueOf(originalValue);
			return createNumberLiteral(originalValue);
		}
		catch (final NumberFormatException e)
		{
			return createCastExpression(PrimitiveType.SHORT, originalValue);
		}
	}

	private ASTNode createExpressionForIntegerType(String originalValue)
	{
		try
		{
			Integer.valueOf(originalValue);
			return createNumberLiteral(originalValue);
		}
		catch (final NumberFormatException e)
		{
			return createCastExpression(PrimitiveType.INT, originalValue);
		}
	}

	private ASTNode createExpressionForLongType(String originalValue)
	{
		try
		{
			Long.valueOf(originalValue);
			return createNumberLiteral(originalValue);
		}
		catch (final NumberFormatException e)
		{
			return createCastExpression(PrimitiveType.LONG, originalValue);
		}
	}

	private ASTNode createExpressionForFloatType(String originalValue)
	{
		try
		{
			Float.valueOf(originalValue);
			return createNumberLiteral(originalValue);
		}
		catch (final NumberFormatException e)
		{
			return createCastExpression(PrimitiveType.FLOAT, originalValue);
		}
	}

	private ASTNode createExpressionForDoubleType(String originalValue)
	{
		try
		{
			Double.valueOf(originalValue);
			return createNumberLiteral(originalValue);
		}
		catch (final NumberFormatException e)
		{
			return createCastExpression(PrimitiveType.DOUBLE, originalValue);
		}
	}

	private NumberLiteral createNumberLiteral(String originalValue)
	{
		try
		{
			Integer.valueOf(originalValue);
		}
		catch (final NumberFormatException e)
		{
			e.printStackTrace();
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

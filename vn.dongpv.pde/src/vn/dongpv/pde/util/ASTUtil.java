package vn.dongpv.pde.util;

import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.StringLiteral;

import vn.dongpv.pde.PluginConstants;

/**
 * This class contains many functions that handle {@link ASTNode} or
 * {@link IJavaElement}.
 * 
 * @author Pham Van Dong
 * 
 * @see ASTNode
 * @see ASTAnalyzer
 * @see IJavaElement
 * 
 */
public class ASTUtil
{

	private ASTUtil()
	{
	}

	/**
	 * Creates a compilation unit.
	 * 
	 * @param icu
	 *            the unit.
	 * @param monitor
	 *            the progress monitor.
	 * @return new compilation unit.
	 * @throws NullPointerException
	 *             if icu is null.
	 */
	public static CompilationUnit createCompilationUnit(
		ICompilationUnit icu, IProgressMonitor monitor)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(icu);

		final ASTParser analyzer = ASTParser.newParser(AST.JLS4);
		analyzer.setKind(ASTParser.K_COMPILATION_UNIT);
		analyzer.setSource(icu);
		analyzer.setResolveBindings(true);
		analyzer.setBindingsRecovery(true);
		return (CompilationUnit)analyzer.createAST(monitor);
	}

	/**
	 * Gets the compilation unit of the specified node.
	 * 
	 * @param node
	 *            the node whose ancestor will be returned.
	 * @return the compilation unit of the specified node.
	 * @throws NullPointerException
	 *             if the speicified node is null.
	 */
	public static CompilationUnit getCompilationUnit(ASTNode node)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(node);

		final ASTNode rootNode = node.getRoot();
		if (rootNode.getNodeType() != ASTNode.COMPILATION_UNIT)
		{
			return null;
		}

		final CompilationUnit compilationUnit = (CompilationUnit)rootNode;
		return compilationUnit;
	}

	/**
	 * Gets resource of the specified node.
	 * 
	 * @param node
	 *            the node whose resrouce will be returned.
	 * @return resource of the specified node.
	 * @throws NullPointerException
	 *             if the speicified node is null.
	 */
	public static IResource getResource(ASTNode node)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(node);

		final CompilationUnit compilationUnit = getCompilationUnit(node);
		if (compilationUnit != null)
		{
			final IJavaElement element = compilationUnit.getJavaElement();
			if (element instanceof ICompilationUnit)
			{
				final ICompilationUnit icu = (ICompilationUnit)element;
				return icu.getResource();
			}
		}
		return null;
	}

	/**
	 * Checks the name and the binding reference to the same ASTNode or not.
	 * 
	 * @param name
	 *            the name.
	 * @param binding
	 *            the binding information.
	 * @return true if the name and the binding reference to the same ASTNode.
	 * @throws NullPointerException
	 *             if the name or the binding is null.
	 */
	public static boolean isSameReference(Name name, IBinding binding)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(name, binding);
		return (name.resolveBinding() == binding);
	}

	/**
	 * Checks the name and the binding reference to the same variable or not.
	 * 
	 * @param name
	 *            the name.
	 * @param binding
	 *            the binding information.
	 * @return true if the name and the binding reference to the same variable.
	 * @throws NullPointerException
	 *             if the name or the binding is null.
	 */
	public static boolean isSameVariableReference(Name name, IBinding binding)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(name, binding);
		if (!(binding instanceof IVariableBinding) ||
			!(name.resolveBinding() instanceof IVariableBinding))
		{
			return false;
		}
		final IVariableBinding binding1 = (IVariableBinding)binding;
		final IVariableBinding binding2 = (IVariableBinding)name.resolveBinding();
		return (binding1.getVariableDeclaration() == binding2.getVariableDeclaration());
	}

	/**
	 * Gets the ancestor has specified node type of the speicified node if
	 * exist, otherwise return null.
	 * 
	 * @param node
	 *            the node.
	 * @param nodeType
	 *            the expected node type.
	 * @return the ancestor has specified node type of the speicified node.
	 * @throws NullPointerException
	 *             if the specified node is null.
	 */
	public static ASTNode getAncestor(ASTNode node, int nodeType)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(node);

		ASTNode parent = node.getParent();

		boolean isDone = false;
		while (!isDone)
		{
			if (parent == null)
			{
				isDone = true;
			}
			else if (parent.getNodeType() == nodeType)
			{
				return parent;
			}
			else
			{
				parent = parent.getParent();
			}
		}

		return null;
	}

	/**
	 * Gets the first loop statement that surrounds the specified node if
	 * present, otherwise return null.
	 * 
	 * @param node
	 *            the specified node.
	 * @return the first loop statement that surrounds the specified node.
	 * @throws NullPointerException
	 *             if the specified node is null.
	 */
	public static ASTNode getLoopStatementAncestor(ASTNode node)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(node);

		ASTNode expectedLoopStatement = getAncestor(node, ASTNode.DO_STATEMENT);
		if (expectedLoopStatement != null)
		{
			return expectedLoopStatement;
		}

		expectedLoopStatement = getAncestor(node, ASTNode.FOR_STATEMENT);
		if (expectedLoopStatement != null)
		{
			return expectedLoopStatement;
		}

		expectedLoopStatement = getAncestor(node, ASTNode.WHILE_STATEMENT);
		if (expectedLoopStatement != null)
		{
			return expectedLoopStatement;
		}

		return null;
	}

	/**
	 * Checks the specified node has any ancestor is loop statement or not.
	 * 
	 * @param node
	 *            the specified node.
	 * @return true if the specified node has any ancestor is loop statement.
	 * @throws NullPointerException
	 *             if the specified node is null.
	 */
	public static boolean hasLoopStatementAncestor(ASTNode node)
		throws NullPointerException
	{
		final ASTNode expectedLoopStatement = getLoopStatementAncestor(node);
		return (expectedLoopStatement != null);
	}

	/**
	 * Gets the Java Element's name.
	 * 
	 * @param element
	 *            the specified element.
	 * @return name of the specified element.
	 * @throws NullPointerException
	 *             if the speicified element is null.
	 */
	public static String getJavaElementTypeName(IJavaElement element)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(element);

		if (element instanceof IJavaProject)
		{
			return PluginConstants.JAVA_PROJECT_TYPE_NAME;
		}

		if (element instanceof IPackageFragment)
		{
			return PluginConstants.JAVA_PACKAGE_TYPE_NAME;
		}

		if (element instanceof ICompilationUnit)
		{
			return PluginConstants.JAVA_COMPILATION_UNIT_TYPE_NAME;
		}

		return PluginConstants.EMPTY_STRING;
	}

	/**
	 * Creates the boolean literal node.
	 * 
	 * @param ast
	 *            the AST.
	 * @param bool
	 *            the bool value.
	 * @return new boolean literal node.
	 * @throws NullPointerException
	 *             if the AST is null.
	 */
	public static BooleanLiteral createBooleanLiteral(AST ast, boolean bool)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(ast);

		final BooleanLiteral boolLiteral = ast.newBooleanLiteral(bool);
		return boolLiteral;
	}

	/**
	 * Creates the character literal node.
	 * 
	 * @param ast
	 *            the AST.
	 * @param value
	 *            the char value
	 * @return new character literal node.
	 * @throws NullPointerException
	 *             if the AST is null.
	 */
	public static CharacterLiteral createCharacterLiteral(AST ast, char value)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(ast);

		final CharacterLiteral charLiteral = ast.newCharacterLiteral();
		charLiteral.setCharValue(value);
		return charLiteral;
	}

	/**
	 * Creates the infix expression.
	 * 
	 * @param ast
	 *            the AST
	 * @param leftOperand
	 *            the left operand.
	 * @param rightOperand
	 *            the right operand.
	 * @param operator
	 *            the operator.
	 * @return new infix expression.
	 * @throws NullPointerException
	 *             if the AST, leftOperand, rightOperand or operator is null.
	 */
	public static InfixExpression createInfixExpression(
		AST ast,
		Expression leftOperand,
		Expression rightOperand,
		InfixExpression.Operator operator)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(ast, leftOperand, rightOperand, operator);

		final InfixExpression infixExpression = ast.newInfixExpression();
		infixExpression.setLeftOperand(leftOperand);
		infixExpression.setRightOperand(rightOperand);
		infixExpression.setOperator(operator);
		return infixExpression;
	}

	/**
	 * Creates the method invocation.
	 * 
	 * @param ast
	 *            the AST.
	 * @param expression
	 *            the expression.
	 * @param name
	 *            the name.
	 * @param arguments
	 *            the arguments list.
	 * @return new method invocation.
	 * @throws NullPointerException
	 *             if the AST, expression or name is null.
	 */
	@SuppressWarnings("unchecked")
	public static MethodInvocation createMethodInvocation(
		AST ast,
		Expression expression,
		String name,
		List<?> arguments)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(ast, expression, name);

		final MethodInvocation methodInvocation = ast.newMethodInvocation();
		methodInvocation.setExpression(expression);
		methodInvocation.setName(ast.newSimpleName(name));
		if (arguments != null)
		{
			methodInvocation.arguments().addAll(arguments);
		}
		return methodInvocation;
	}

	/**
	 * Creates the number literal.
	 * 
	 * @param ast
	 *            the AST.
	 * @param literal
	 *            the String literal.
	 * @return new number literal.
	 * @throws NullPointerException
	 *             if the AST or literal is null.
	 */
	public static NumberLiteral createNumberLiteral(AST ast, String literal)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(ast, literal);

		final NumberLiteral numberLiteral = ast.newNumberLiteral(literal);
		return numberLiteral;
	}

	/**
	 * Creates the parenthesized expression.
	 * 
	 * @param ast
	 *            the AST.
	 * @param expression
	 *            the expression.
	 * @return new parenthesizd expression.
	 * @throws NullPointerException
	 *             if the AST or expression is null.
	 */
	public static ParenthesizedExpression createParenthesizedExpression(
		AST ast,
		Expression expression)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(ast, expression);

		final ParenthesizedExpression parenthesizedExpression =
			ast.newParenthesizedExpression();
		parenthesizedExpression.setExpression(expression);
		return parenthesizedExpression;
	}

	/**
	 * Creates the string literal.
	 * 
	 * @param ast
	 *            the AST.
	 * @param literal
	 *            the string value.
	 * @return new string literal.
	 * @throws NullPointerException
	 *             if the AST or literal is null.
	 */
	public static StringLiteral createStringLiteral(AST ast, String literal)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(ast, literal);

		final StringLiteral stringLiteral = ast.newStringLiteral();
		stringLiteral.setLiteralValue(literal);
		return stringLiteral;
	}

	/**
	 * Gets type of an object that has primitive type.
	 * 
	 * @param binding
	 *            the binding.
	 * @return type name of an object has primitive type.
	 * @throws NullPointerException
	 *             if the binding is null.
	 */
	public static String getObjectType(IMethodBinding binding)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(binding);

		final String typeName = binding.getDeclaringClass().getQualifiedName();
		if (typeName.equalsIgnoreCase(PluginConstants.BOOLEAN_CLASS_NAME))
		{
			return PluginConstants.BOOLEAN_CLASS_NAME;
		}
		if (typeName.equalsIgnoreCase(PluginConstants.BYTE_CLASS_NAME))
		{
			return PluginConstants.BYTE_CLASS_NAME;
		}
		if (typeName.equalsIgnoreCase(PluginConstants.CHARACTER_CLASS_NAME))
		{
			return PluginConstants.CHARACTER_CLASS_NAME;
		}
		if (typeName.equalsIgnoreCase(PluginConstants.SHORT_CLASS_NAME))
		{
			return PluginConstants.SHORT_CLASS_NAME;
		}
		if (typeName.equalsIgnoreCase(PluginConstants.INTEGER_CLASS_NAME))
		{
			return PluginConstants.INTEGER_CLASS_NAME;
		}
		if (typeName.equalsIgnoreCase(PluginConstants.LONG_CLASS_NAME))
		{
			return PluginConstants.LONG_CLASS_NAME;
		}
		if (typeName.equalsIgnoreCase(PluginConstants.FLOAT_CLASS_NAME))
		{
			return PluginConstants.FLOAT_CLASS_NAME;
		}
		if (typeName.equalsIgnoreCase(PluginConstants.DOUBLE_CLASS_NAME))
		{
			return PluginConstants.DOUBLE_CLASS_NAME;
		}
		if (typeName.equalsIgnoreCase(PluginConstants.STRING_CLASS_NAME))
		{
			return PluginConstants.STRING_CLASS_NAME;
		}

		return PluginConstants.NON_PRIMITIVE_CLASS_NAME;
	}

	/**
	 * Gets the node of the specified range.
	 * 
	 * @param rootNode
	 *            the root node that will be visited to find out the node.
	 * @param start
	 *            the start position.
	 * @param end
	 *            the end position.
	 * @return the node of the specified range.
	 * @throws NullPointerException
	 *             if the rootNode is null.
	 */
	public static ASTNode getNode(ASTNode rootNode, int start, int end)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(rootNode);

		final Visitor visitor = new Visitor(start, end);
		rootNode.accept(visitor);
		return visitor.getNode();
	}

	private static final int DEFAULT_CAPACITY = 5;

	public static String evaluateExpression(
		Expression expression, List<Expression> removedExpressions)
	{
		if (expression.getNodeType() == ASTNode.STRING_LITERAL)
		{
			final StringLiteral literal = (StringLiteral)expression;
			return literal.getLiteralValue();
		}

		if (expression.getNodeType() == ASTNode.NUMBER_LITERAL)
		{
			final NumberLiteral literal = (NumberLiteral)expression;
			return literal.getToken();
		}

		if (expression.getNodeType() == ASTNode.CHARACTER_LITERAL)
		{
			final CharacterLiteral literal = (CharacterLiteral)expression;
			return String.valueOf(literal.charValue());
		}

		if (expression.getNodeType() == ASTNode.NULL_LITERAL)
		{
			final NullLiteral literal = (NullLiteral)expression;
			return literal.toString();
		}

		if (expression.getNodeType() == ASTNode.BOOLEAN_LITERAL)
		{
			final BooleanLiteral literal = (BooleanLiteral)expression;
			return String.valueOf(literal.booleanValue());
		}

		if (expression.getNodeType() == ASTNode.CAST_EXPRESSION)
		{
			final CastExpression exp = (CastExpression)expression;
			return evaluateCastExpression(exp, removedExpressions);
		}

		if (expression.getNodeType() == ASTNode.INFIX_EXPRESSION)
		{
			final InfixExpression exp = (InfixExpression)expression;
			return evaluateInfixExpression(exp, removedExpressions);
		}

		if (expression.getNodeType() == ASTNode.PARENTHESIZED_EXPRESSION)
		{
			final ParenthesizedExpression exp = (ParenthesizedExpression)expression;
			return evaluateExpression(exp.getExpression(), removedExpressions);
		}

		return PluginConstants.EMPTY_STRING;
	}

	private static String evaluateInfixExpression(
		InfixExpression infixExpression, List<Expression> removedExpressions)
	{
		final Expression leftExpression = infixExpression.getLeftOperand();
		final Expression rightExpression = infixExpression.getRightOperand();
		final Operator operator = infixExpression.getOperator();

		final boolean hasExtendedOperands = infixExpression.hasExtendedOperands();

		final String leftValue = evaluateExpression(leftExpression, removedExpressions);
		if (!hasExtendedOperands && (leftValue == PluginConstants.EMPTY_STRING))
		{
			return PluginConstants.EMPTY_STRING;
		}

		final String rightValue = evaluateExpression(rightExpression, removedExpressions);
		if (!hasExtendedOperands && (rightValue == PluginConstants.EMPTY_STRING))
		{
			return PluginConstants.EMPTY_STRING;
		}

		final List<?> extendedOperands = infixExpression.extendedOperands();
		List<String> values = new ArrayList<String>(DEFAULT_CAPACITY);

		removedExpressions.clear();

		if (extendedOperands != null)
		{
			int i = 0;
			final int size = extendedOperands.size();

			for (i = 0; i < size; i += 1)
			{
				if (!(extendedOperands.get(i) instanceof Expression))
				{
					return PluginConstants.EMPTY_STRING;
				}

				final Expression extendedExpression =
					(Expression)extendedOperands.get(i);

				final String extendedValue =
					evaluateExpression(extendedExpression, removedExpressions);

				if (extendedValue == PluginConstants.EMPTY_STRING)
				{
					break;
				}
				values.add(extendedValue);
				removedExpressions.add(extendedExpression);
			}
		}

		if (rightValue != PluginConstants.EMPTY_STRING)
		{
			values.add(0, rightValue);
			removedExpressions.add(0, rightExpression);

			if (leftValue != PluginConstants.EMPTY_STRING)
			{
				values.add(0, leftValue);
				removedExpressions.add(0, leftExpression);
			}
		}

		if (values.size() < 2)
		{
			return PluginConstants.EMPTY_STRING;
		}

		values = normalize(values);

		final String expType = infixExpression.resolveTypeBinding().getQualifiedName();

		if (expType.equalsIgnoreCase(PluginConstants.STRING_CLASS_NAME))
		{
			return evaluateStringExpressions(values, operator);
		}

		if (expType.equalsIgnoreCase(PluginConstants.BOOL))
		{
			return evaluateBooleanExpressions(values, operator);
		}

		return evaluateNumberExpressions(values, operator);
	}

	private static String evaluateCastExpression(
		CastExpression expression, List<Expression> removedExpressions)
	{
		final String originalValue =
			evaluateExpression(expression.getExpression(), removedExpressions);
		if (originalValue == PluginConstants.EMPTY_STRING)
		{
			return PluginConstants.EMPTY_STRING;
		}

		final String targetType =
			expression.getType().resolveBinding().getQualifiedName();

		try
		{
			final long value = Long.parseLong(originalValue);
			return cast(targetType, value);
		}
		catch (final NumberFormatException e)
		{
			e.printStackTrace();

			final double value = Double.parseDouble(originalValue);
			return cast(targetType, value);
		}
	}

	private static String cast(String targetType, long value)
	{
		if (targetType.equalsIgnoreCase(PluginConstants.LONG))
		{
			return String.valueOf(value);
		}

		if (targetType.equalsIgnoreCase(PluginConstants.INT))
		{
			return String.valueOf((int)value);
		}

		if (targetType.equalsIgnoreCase(PluginConstants.SHORT))
		{
			return String.valueOf((short)value);
		}

		if (targetType.equalsIgnoreCase(PluginConstants.BYTE))
		{
			return String.valueOf((byte)value);
		}

		if (targetType.equalsIgnoreCase(PluginConstants.CHAR))
		{
			return String.valueOf((char)value);
		}

		if (targetType.equalsIgnoreCase(PluginConstants.DOUBLE))
		{
			return String.valueOf((double)value);
		}

		if (targetType.equalsIgnoreCase(PluginConstants.FLOAT))
		{
			return String.valueOf((float)value);
		}

		return PluginConstants.EMPTY_STRING;
	}

	private static String cast(String targetType, double value)
	{
		if (targetType.equalsIgnoreCase(PluginConstants.LONG))
		{
			return String.valueOf((long)value);
		}

		if (targetType.equalsIgnoreCase(PluginConstants.INT))
		{
			return String.valueOf((int)value);
		}

		if (targetType.equalsIgnoreCase(PluginConstants.SHORT))
		{
			return String.valueOf((short)value);
		}

		if (targetType.equalsIgnoreCase(PluginConstants.BYTE))
		{
			return String.valueOf((byte)value);
		}

		if (targetType.equalsIgnoreCase(PluginConstants.CHAR))
		{
			return String.valueOf((char)value);
		}

		if (targetType.equalsIgnoreCase(PluginConstants.DOUBLE))
		{
			return String.valueOf(value);
		}

		if (targetType.equalsIgnoreCase(PluginConstants.FLOAT))
		{
			return String.valueOf((float)value);
		}

		return PluginConstants.EMPTY_STRING;
	}

	private static String evaluateNumberExpressions(List<String> values, Operator operator)
	{
		String result = PluginConstants.EMPTY_STRING;
		if (isFloatingPointNumber(values.get(0)) ||
			isFloatingPointNumber(values.get(1)))
		{
			result = evaluateDoubleExpression(
				Double.parseDouble(values.get(0)),
				Double.parseDouble(values.get(1)),
				operator);
		}
		else
		{
			result = evaluateLongExpression(
				Long.parseLong(values.get(0)),
				Long.parseLong(values.get(1)),
				operator);
		}

		final int size = values.size();
		for (int i = 2; i < size; i += 1)
		{
			if (isFloatingPointNumber(result) ||
				isFloatingPointNumber(values.get(i)))
			{
				result = evaluateDoubleExpression(
					Double.parseDouble(result),
					Double.parseDouble(values.get(i)),
					operator);
			}
			else
			{
				result = evaluateLongExpression(
					Long.parseLong(result),
					Long.parseLong(values.get(i)),
					operator);
			}
		}
		return result;
	}

	private static String evaluateStringExpressions(List<String> values, Operator operator)
	{
		String result = evaluateStringExpression(
			values.get(0), values.get(1), operator);

		final int size = values.size();
		for (int i = 2; i < size; i += 1)
		{
			result = evaluateStringExpression(
				result, values.get(i), operator);
		}
		return result;
	}

	private static String evaluateBooleanExpressions(List<String> values, Operator operator)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append(values.get(0));

		final int size = values.size();
		for (int i = 1; i < size; i += 1)
		{
			builder.append(operator);
			builder.append(values.get(i));
		}

		return evaluateBooleanExpression(builder.toString());
	}

	private static String evaluateDoubleExpression(
		double num1, double num2, Operator operator)
	{
		if (operator == Operator.TIMES)
		{
			return String.valueOf(num1 * num2);
		}

		if (operator == Operator.DIVIDE)
		{
			return String.valueOf(num1 / num2);
		}

		if (operator == Operator.REMAINDER)
		{
			return String.valueOf(num1 % num2);
		}

		if (operator == Operator.PLUS)
		{
			return String.valueOf(num1 + num2);
		}

		if (operator == Operator.MINUS)
		{
			return String.valueOf(num1 - num2);
		}

		return PluginConstants.EMPTY_STRING;
	}

	private static String evaluateLongExpression(
		long num1, long num2, Operator operator)
	{
		if (operator == Operator.TIMES)
		{
			return String.valueOf(num1 * num2);
		}

		if (operator == Operator.DIVIDE)
		{
			return String.valueOf(num1 / num2);
		}

		if (operator == Operator.REMAINDER)
		{
			return String.valueOf(num1 % num2);
		}

		if (operator == Operator.PLUS)
		{
			return String.valueOf(num1 + num2);
		}

		if (operator == Operator.MINUS)
		{
			return String.valueOf(num1 - num2);
		}

		if (operator == Operator.LEFT_SHIFT)
		{
			try
			{
				return String.valueOf(num1 << num2);
			}
			catch (final NumberFormatException e)
			{
				e.printStackTrace();
			}
		}

		if (operator == Operator.RIGHT_SHIFT_SIGNED)
		{
			try
			{
				return String.valueOf(num1 >> num2);
			}
			catch (final NumberFormatException e)
			{
				e.printStackTrace();
			}
		}

		if (operator == Operator.RIGHT_SHIFT_UNSIGNED)
		{
			try
			{
				return String.valueOf(num1 >>> num2);
			}
			catch (final NumberFormatException e)
			{
				e.printStackTrace();
			}
		}

		if (operator == Operator.XOR)
		{
			try
			{
				return String.valueOf(num1 ^ num2);
			}
			catch (final NumberFormatException e)
			{
				e.printStackTrace();
			}
		}

		if (operator == Operator.AND)
		{
			try
			{
				return String.valueOf(num1 & num2);
			}
			catch (final NumberFormatException e)
			{
				e.printStackTrace();
			}
		}

		if (operator == Operator.OR)
		{
			try
			{
				return String.valueOf(num1 | num2);
			}
			catch (final NumberFormatException e)
			{
				e.printStackTrace();
			}
		}

		return PluginConstants.EMPTY_STRING;
	}

	private static String evaluateStringExpression(
		String string1, String string2, Operator operator)
	{
		if (operator == Operator.PLUS)
		{
			return (string1 + string2);
		}

		return PluginConstants.EMPTY_STRING;
	}

	private static String evaluateBooleanExpression(String expression)
	{
		final ScriptEngineManager manager = new ScriptEngineManager();
		final ScriptEngine engine = manager.getEngineByName("JavaScript");

		try
		{
			return engine.eval(expression).toString();
		}
		catch (final ScriptException e)
		{
			e.printStackTrace();

			return PluginConstants.EMPTY_STRING;
		}
	}

	private static boolean isFloatingPointNumber(String value)
	{
		try
		{
			Long.parseLong(value);
			return false;
		}
		catch (final NumberFormatException e)
		{
			e.printStackTrace();

			return true;
		}
	}

	private static List<String> normalize(List<String> extendedValues)
	{
		final List<String> result = new ArrayList<String>(extendedValues.size());
		for (final String value : extendedValues)
		{
			final int len = value.length();
			if ((len > 0) &&
				((value.charAt(len - 1) == 'L') ||
				((value.charAt(len - 1) == 'l'))))
			{
				result.add(value.substring(0, len - 1));
			}
			else
			{
				result.add(value);
			}
		}
		return result;
	}

	private static class Visitor extends ASTVisitor
	{

		private final int start;
		private final int end;

		private ASTNode outerNode;
		private ASTNode innerNode;

		private Visitor(int start, int end)
		{
			this.start = start;
			this.end = end;
			this.outerNode = null;
			this.innerNode = null;
		}

		@Override
		public boolean preVisit2(ASTNode node)
		{
			final int nodeStart = node.getStartPosition();
			final int nodeEnd = nodeStart + node.getLength();

			if ((nodeEnd <= start) || (end <= nodeStart) || (innerNode != null))
			{
				return false;
			}

			if ((nodeStart <= start) && (end <= nodeEnd))
			{
				outerNode = node;
			}

			if ((start <= nodeStart) && (nodeEnd <= end))
			{
				if (outerNode == node)
				{
					innerNode = node;
					return true;
				}

				innerNode = node;
				return false;
			}

			return true;
		}

		private ASTNode getNode()
		{
			return (innerNode == null) ? outerNode : innerNode;
		}

		private final void readObject(java.io.ObjectInputStream in)
			throws java.io.IOException
		{
			throw new java.io.IOException("Class cannot be deserialized");
		}
	}

	@Override
	public final Object clone() throws java.lang.CloneNotSupportedException
	{
		throw new java.lang.CloneNotSupportedException();
	}

	private final void readObject(java.io.ObjectInputStream in)
		throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

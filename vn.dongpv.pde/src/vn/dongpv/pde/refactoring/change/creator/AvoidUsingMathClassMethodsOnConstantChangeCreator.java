package vn.dongpv.pde.refactoring.change.creator;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import vn.dongpv.pde.PluginConstants;
import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.refactoring.change.creator.core.ChangeCreator;
import vn.dongpv.pde.util.ASTUtil;
import vn.dongpv.pde.util.TypeUtil;
import vn.dongpv.pde.util.ValidatorUtil;

public class AvoidUsingMathClassMethodsOnConstantChangeCreator extends ChangeCreator
{

	public static final String INT_TYPE = "int";
	public static final String FLOAT_TYPE = "float";
	public static final String DOUBLE_TYPE = "double";

	private final ASTNode rootNode;
	private final MethodInvocation methodInvocation;

	public AvoidUsingMathClassMethodsOnConstantChangeCreator(
		CompositeParameter parameter)
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
		try
		{
			// Calculates the real value by invoking the method

			final Object calculatedValue = invokeMethod();
			if (calculatedValue != null)
			{
				// Creates the literal instead

				final NumberLiteral numberLiteral = ASTUtil.createNumberLiteral(
					rootNode.getAST(), String.valueOf(calculatedValue));
				rewrite.replace(methodInvocation, numberLiteral, null);
			}
		}
		catch (final ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (final SecurityException e)
		{
			e.printStackTrace();
		}
		catch (final NoSuchMethodException e)
		{
			e.printStackTrace();
		}
		catch (final IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (final IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (final InvocationTargetException e)
		{
			e.printStackTrace();
		}
	}

	private Object invokeMethod()
		throws
		ClassNotFoundException, SecurityException, NoSuchMethodException,
		IllegalArgumentException, IllegalAccessException, InvocationTargetException
	{
		// Loads Math class by name: java.lang.Math

		final Class<? extends Math> clazz = Class.forName(
			PluginConstants.MATH_CLASS_NAME).
			asSubclass(Math.class);

		final IMethodBinding binding = methodInvocation.resolveMethodBinding();
		final ITypeBinding[] typeBindings = binding.getParameterTypes();

		// Takes parameter types

		final Class<?>[] classes = getParameterTypes(typeBindings);
		if (classes != null)
		{
			// Takes parameter values

			final Object[] values = getParameters(typeBindings);

			// Gets the method's signature and invoke it

			final String methodName = binding.getName();
			final Method method = clazz.getMethod(methodName, classes);
			final Object result = method.invoke(null, values);
			return result;
		}

		return null;
	}

	private Class<?>[] getParameterTypes(ITypeBinding[] typeBindings)
	{
		final int length = typeBindings.length;
		final Class<?>[] classes = new Class<?>[length];

		// Gets the parameter type by type name which is binded

		for (int i = typeBindings.length - 1; i >= 0; i -= 1)
		{
			final String typeName = typeBindings[i].getQualifiedName();
			if (typeName.equalsIgnoreCase(INT_TYPE))
			{
				classes[i] = int.class;
			}
			else if (typeName.equalsIgnoreCase(FLOAT_TYPE))
			{
				classes[i] = float.class;
			}
			else if (typeName.equalsIgnoreCase(DOUBLE_TYPE))
			{
				classes[i] = double.class;
			}
			else
			{
				return null;
			}
		}

		return classes;
	}

	private Object[] getParameters(ITypeBinding[] typeBindings)
	{
		final List<?> arguments = methodInvocation.arguments();
		final Object[] values = new Object[arguments.size()];

		// Gets the parameter value by parsing the arguments list

		for (int i = typeBindings.length - 1; i >= 0; i -= 1)
		{
			final String typeName = typeBindings[i].getQualifiedName();
			if (typeName.equalsIgnoreCase(INT_TYPE))
			{
				values[i] = Integer.parseInt(
					((NumberLiteral)arguments.get(i)).getToken());
			}
			else if (typeName.equalsIgnoreCase(FLOAT_TYPE))
			{
				values[i] = Float.parseFloat(
					((NumberLiteral)arguments.get(i)).getToken());
			}
			else if (typeName.equalsIgnoreCase(DOUBLE_TYPE))
			{
				values[i] = Double.parseDouble(
					((NumberLiteral)arguments.get(i)).getToken());
			}
			else
			{
				return null;
			}
		}

		return values;
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}
}

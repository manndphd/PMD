package vn.dongpv.pde.rule.checker;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import vn.dongpv.pde.PluginConstants;
import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.rule.checker.core.ConditionalChecker;
import vn.dongpv.pde.util.TypeUtil;
import vn.dongpv.pde.util.ValidatorUtil;

public class UncloneableClassChecker extends ConditionalChecker
{

	public static final String CLONE_METHOD_NAME = "clone";

	private final ASTNode rootNode;
	private final TypeDeclaration typeDecl;

	public UncloneableClassChecker(CompositeParameter parameter)
		throws NullPointerException
	{
		super(parameter);

		rootNode = TypeUtil.cast(parameter.getParameter(PARSED_ROOT_NODE));
		typeDecl = TypeUtil.cast(parameter.getParameter(PARSED_NODE));
	}

	@Override
	protected boolean checkParameters()
	{
		return ValidatorUtil.notNull(rootNode, typeDecl);
	}

	@Override
	protected boolean checkDetails()
	{
		// Checks this type is class or interface

		if (typeDecl.isInterface())
		{
			return false;
		}

		// Checks this type has already clone() method?

		final List<?> bodyDecls = typeDecl.bodyDeclarations();
		for (final Object bodyObject : bodyDecls)
		{
			if (bodyObject instanceof MethodDeclaration)
			{
				final MethodDeclaration methodDecl = (MethodDeclaration)bodyObject;
				final String methodName = methodDecl.getName().getIdentifier();

				if (!methodDecl.isConstructor() &&
					methodName.equalsIgnoreCase(CLONE_METHOD_NAME))
				{
					final List<?> thrownExceptions = methodDecl.thrownExceptions();
					for (final Object exceptionObject : thrownExceptions)
					{
						if (exceptionObject instanceof Name)
						{
							final String exceptionName = ((Name)exceptionObject).
								resolveTypeBinding().getQualifiedName();
							if (exceptionName.equalsIgnoreCase(
								PluginConstants.CLONE_NOT_SUPPORTED_EXCEPTION_CLASS_NAME))
							{
								return false;
							}
						}
					}
				}
			}
		}

		// Checks the clone() method has been already overloaded in super type?

		return checkSuperType();
	}

	private boolean checkSuperType()
	{
		// Checks the clone() method has been already overloaded in super type?

		final Type superType = typeDecl.getSuperclassType();
		if (superType == null)
		{
			return true;
		}

		ITypeBinding superTypeBinding = superType.resolveBinding();

		boolean isDone = false;
		while (!isDone)
		{
			final ITypeBinding binding = superTypeBinding.getSuperclass();
			if ((binding == null) ||
				(binding.getQualifiedName().equalsIgnoreCase(
					PluginConstants.OBJECT_CLASS_NAME)))
			{
				isDone = true;
			}

			final IMethodBinding[] superMethodBindings = binding.getDeclaredMethods();
			for (final IMethodBinding methodBinding : superMethodBindings)
			{
				if (!methodBinding.isConstructor() &&
					methodBinding.getName().equalsIgnoreCase(CLONE_METHOD_NAME))
				{
					final ITypeBinding[] exceptionTypeBindings = methodBinding.getExceptionTypes();
					for (final ITypeBinding exception : exceptionTypeBindings)
					{
						final String exceptionName = exception.getQualifiedName();
						if (exceptionName.equalsIgnoreCase(
							PluginConstants.CLONE_NOT_SUPPORTED_EXCEPTION_CLASS_NAME))
						{
							return false;
						}
					}
				}
			}

			superTypeBinding = binding;
		}

		return true;
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

package vn.mannd.pde.rule.checker;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import vn.mannd.pde.PluginConstants;
import vn.mannd.pde.adt.CompositeParameter;
import vn.mannd.pde.rule.checker.core.ConditionalChecker;
import vn.mannd.pde.util.TypeUtil;
import vn.mannd.pde.util.ValidatorUtil;

public class UnserializeableClassChecker extends ConditionalChecker
{

	public static final String WRITE_OBJECT_METHOD_NAME = "writeObject";

	private final ASTNode rootNode;
	private final TypeDeclaration typeDecl;

	public UnserializeableClassChecker(CompositeParameter parameter)
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
		// Checks this type implements Serializable interface

		final ITypeBinding typeBinding = typeDecl.resolveBinding();
		final ITypeBinding[] superInterfaceBindings = typeBinding.getInterfaces();
		if (superInterfaceBindings == null)
		{
			return false;
		}

		boolean exactInterface = false;
		for (final ITypeBinding interfaceBinding : superInterfaceBindings)
		{
			final String interfaceName = interfaceBinding.getQualifiedName();
			if (interfaceName.equalsIgnoreCase(PluginConstants.SERIALIZABLE_CLASS_NAME))
			{
				exactInterface = true;
				break;
			}
		}

		if (exactInterface == false)
		{
			return false;
		}

		// This type has writeObject method already?

		final List<?> bodyDecls = typeDecl.bodyDeclarations();
		for (final Object bodyObject : bodyDecls)
		{
			if (bodyObject instanceof MethodDeclaration)
			{
				final MethodDeclaration methodDecl = (MethodDeclaration)bodyObject;
				final String methodName = methodDecl.getName().getIdentifier();

				if (!methodDecl.isConstructor() &&
					methodName.equalsIgnoreCase(WRITE_OBJECT_METHOD_NAME))
				{
					boolean exactException = false;
					final List<?> thrownExceptions = methodDecl.thrownExceptions();
					for (final Object exceptionObject : thrownExceptions)
					{
						if (exceptionObject instanceof Name)
						{
							final String exceptionName = ((Name)exceptionObject).
								resolveTypeBinding().getQualifiedName();
							if (exceptionName.equalsIgnoreCase(
								PluginConstants.IO_EXCEPTION_CLASS_NAME))
							{
								exactException = true;
							}
						}
					}

					if (exactException)
					{
						final List<?> parameters = methodDecl.parameters();
						for (final Object paramObject : parameters)
						{
							if (paramObject instanceof SingleVariableDeclaration)
							{
								final SingleVariableDeclaration svd =
									(SingleVariableDeclaration)paramObject;
								final String paramType =
									svd.getType().resolveBinding().getQualifiedName();
								if (paramType.equalsIgnoreCase(
									PluginConstants.OBJECT_OUTPUT_STREAM_CLASS_NAME))
								{
									return false;
								}
							}
						}
					}
				}
			}
		}

		return true;
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

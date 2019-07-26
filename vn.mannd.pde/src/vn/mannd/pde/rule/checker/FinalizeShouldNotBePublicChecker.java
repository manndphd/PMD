package vn.mannd.pde.rule.checker;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;

import vn.mannd.pde.PluginConstants;
import vn.mannd.pde.adt.CompositeParameter;
import vn.mannd.pde.rule.checker.core.ConditionalChecker;
import vn.mannd.pde.util.TypeUtil;
import vn.mannd.pde.util.ValidatorUtil;

public class FinalizeShouldNotBePublicChecker extends ConditionalChecker
{

	public static final String FINALIZE_METHOD_NAME = "finalize";

	private final ASTNode rootNode;
	private final MethodDeclaration methodDecl;

	public FinalizeShouldNotBePublicChecker(CompositeParameter parameter)
		throws NullPointerException
	{
		super(parameter);

		rootNode = TypeUtil.cast(parameter.getParameter(PARSED_ROOT_NODE));
		methodDecl = TypeUtil.cast(parameter.getParameter(PARSED_NODE));
	}

	@Override
	protected boolean checkParameters()
	{
		return ValidatorUtil.notNull(rootNode, methodDecl);
	}

	@Override
	protected boolean checkDetails()
	{
		final String methodName = methodDecl.getName().getIdentifier();

		if (!methodDecl.isConstructor() &&
			methodName.equalsIgnoreCase(FINALIZE_METHOD_NAME))
		{
			final List<?> thrownExceptions = methodDecl.thrownExceptions();
			for (final Object exceptionObject : thrownExceptions)
			{
				if (exceptionObject instanceof Name)
				{
					final String exceptionName = ((Name)exceptionObject).
						resolveTypeBinding().getQualifiedName();
					if (PluginConstants.THROWABLE_CLASS_NAME.
						equalsIgnoreCase(exceptionName))
					{
						final int modifiers = methodDecl.getModifiers();
						return Modifier.isPublic(modifiers);
					}
				}
			}
		}

		return false;
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

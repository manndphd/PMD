package vn.dongpv.pde.rule.checker;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import vn.dongpv.pde.PluginConstants;
import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.refactoring.change.creator.DoNotImplementSerializableChangeCreator;
import vn.dongpv.pde.rule.checker.core.ConditionalChecker;
import vn.dongpv.pde.util.TypeUtil;
import vn.dongpv.pde.util.ValidatorUtil;

public class DoNotImplementSerializableChecker extends ConditionalChecker
{

	private final ASTNode rootNode;
	private final TypeDeclaration typeDecl;

	public DoNotImplementSerializableChecker(CompositeParameter parameter)
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
		final List<?> interfaceTypes = typeDecl.superInterfaceTypes();
		if (interfaceTypes == null)
		{
			return false;
		}

		for (final Object object : interfaceTypes)
		{
			if (object instanceof Type)
			{
				final Type type = (Type)object;
				final String interfaceName = type.resolveBinding().getQualifiedName();
				if (interfaceName.equalsIgnoreCase(
					PluginConstants.SERIALIZABLE_CLASS_NAME))
				{
					saveStateToParameter(type);
					return true;
				}
			}
		}

		return false;
	}

	private void saveStateToParameter(Type type)
	{
		if (isAllowProvideMoreParameters())
		{
			getAdditionalParameter().addParameter(
				DoNotImplementSerializableChangeCreator.INTERFACE, type);
		}
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

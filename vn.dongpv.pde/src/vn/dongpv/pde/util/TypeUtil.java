package vn.dongpv.pde.util;

/**
 * This class has responsibility to handle object's type.
 * 
 * @author Pham Van Dong
 * 
 */
public class TypeUtil
{

	private TypeUtil()
	{
	}

	/**
	 * Auto-cast the specified object.
	 * 
	 * @param object
	 *            the specified objec that will be cast.
	 * @return the cast object.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T cast(Object object)
	{
		return (T)object;
	}

	/**
	 * Casts the specified object to the speicified target class.
	 * 
	 * @param object
	 *            the specified object that will be cast.
	 * @param clazz
	 *            the target class.
	 * @return the cast object.
	 * @throws NullPointerException
	 *             if the target class is null.
	 */
	public static <T> T cast(Object object, Class<T> clazz)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(clazz);

		if (clazz.isInstance(object))
		{
			return clazz.cast(object);
		}
		return null;
	}

	/**
	 * Checks the relationship of two class.
	 * 
	 * @param subClassName
	 *            the expected sub-class name.
	 * @param superClassName
	 *            the expected super-class name.
	 * @return true if the expectation is right.
	 * @throws NullPointerException
	 *             if the sub-class name or super-class name is null.
	 */
	public static boolean hasRelationship(String subClassName, String superClassName)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(subClassName, superClassName);

		try
		{
			final boolean result =
				isRelationshipSuperInterface(subClassName, superClassName) ||
					isRelationshipSuperClass(subClassName, superClassName);
			return result;
		}
		catch (final ClassNotFoundException e)
		{
			e.printStackTrace();
		}

		return false;
	}

	private static boolean isRelationshipSuperClass(
		String subClassName,
		String superClassName)
		throws ClassNotFoundException
	{
		final Class<?> subClass = Class.forName(subClassName);
		final Class<?> superClass = Class.forName(superClassName);

		if (subClass == superClass)
		{
			return true;
		}

		Class<?> tempSuperClass = subClass;

		boolean isDone = false;
		while (!isDone)
		{
			tempSuperClass = tempSuperClass.getSuperclass();
			if (tempSuperClass == null)
			{
				isDone = true;
			}
			else if (tempSuperClass == superClass)
			{
				return true;
			}
		}

		return false;
	}

	private static boolean isRelationshipSuperInterface(
		String subClassName,
		String superClassName)
		throws ClassNotFoundException
	{
		final Class<?> subClass = Class.forName(subClassName);
		final Class<?> superClass = Class.forName(superClassName);

		if (subClass == superClass)
		{
			return true;
		}

		final Class<?>[] interfaces = subClass.getInterfaces();
		for (final Class<?> interfaze : interfaces)
		{
			if (interfaze == superClass)
			{
				return true;
			}

			if (isRelationshipSuperInterface(interfaze.getName(), superClassName))
			{
				return true;
			}
		}

		return false;
	}

	@Override
	public final Object clone() throws java.lang.CloneNotSupportedException
	{
		throw new java.lang.CloneNotSupportedException();
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

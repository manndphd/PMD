package vn.dongpv.pde.util;

/**
 * This class has responsibility to validate any objects.
 * 
 * @author Pham Van Dong
 * 
 */
public class ValidatorUtil
{

	public static final String NOT_NULL_MSG_DEFAULT = "Parameter(s) can't be null";

	private ValidatorUtil()
	{
	}

	/**
	 * Checks the specified list of objects are null or not.
	 * 
	 * @param message
	 *            the shown message if any objects are null.
	 * @param objects
	 *            the specified list of objects that will be validated.
	 * @throws NullPointerException
	 *             if the specified list of objects is null.
	 */
	public static void checkNotNull(String message, Object... objects)
		throws NullPointerException
	{
		if (!ValidatorUtil.notNull(objects))
		{
			throw new NullPointerException(message);
		}
	}

	/**
	 * Checks the specified list of objects are null or not.
	 * 
	 * @param objects
	 *            the specified list of objects that will be validated.
	 * @throws NullPointerException
	 *             if the specified list of objects is null.
	 */
	public static void checkNotNull(Object... objects)
		throws NullPointerException
	{
		checkNotNull(NOT_NULL_MSG_DEFAULT, objects);
	}

	/**
	 * Checks the specified list of objects are null or not.
	 * 
	 * @param objects
	 * @return true if has not any object is null.
	 */
	public static boolean notNull(Object... objects)
	{
		if (objects == null)
		{
			return false;
		}

		for (final Object object : objects)
		{
			if (object == null)
			{
				return false;
			}
		}

		return true;
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

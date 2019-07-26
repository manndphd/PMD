package vn.dongpv.pde.util;

public class MathUtil
{

	private MathUtil()
	{
	}

	/**
	 * Checks the specified number is the power of two or not.
	 * 
	 * @param number
	 *            the specified number that will be checked.
	 * @return true if the specified number is the power of two.
	 */
	public static boolean isPowerOfTwo(int number)
	{
		return ((number != 0) && ((number & (number - 1)) == 0));
	}

	/**
	 * Calculates the exponent of the number with the specified base.
	 * 
	 * @param number
	 *            the number.
	 * @param base
	 *            the base.
	 * @return the exponent of the number with the specified base.
	 */
	public static int computeExponent(int number, int base)
	{
		return (int)(Math.log(number) / Math.log(base));
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

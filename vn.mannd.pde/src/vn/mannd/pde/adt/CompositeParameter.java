package vn.mannd.pde.adt;

import java.util.HashMap;
import java.util.Map;

import vn.mannd.pde.util.ValidatorUtil;

/**
 * @author Pham Van Dong
 * 
 */
public class CompositeParameter
{

	/**
	 * The key-value map, store all of added parameters.
	 */
	private final Map<String, Object> params;

	/**
	 * Constructs an empty CompositeParameter.
	 */
	public CompositeParameter()
	{
		params = new HashMap<String, Object>();
	}

	/**
	 * Adds an parameter is represented as a key-value mapping to this composite
	 * parameter.
	 * 
	 * @param key
	 *            the specified key.
	 * @param value
	 *            the specified value is represented as a parameter.
	 * @throws NullPointerException
	 *             if the speicified key or value is null.
	 */
	public void addParameter(String key, Object value)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(key, value);
		params.put(key, value);
	}

	/**
	 * Adds an parameter to this composite parameter.
	 * 
	 * @param parameter
	 *            the specified parameter.
	 * @throws NullPointerException
	 *             if the speicified parameter is null.
	 */
	public void addParameter(CompositeParameter parameter)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(parameter);
		params.putAll(parameter.params);
	}

	/**
	 * Adds all of parameters is represented as key-value mappings to this
	 * composite parameter.
	 * 
	 * @param parameters
	 *            the specified map whose mappings will be added to this
	 *            composite parameter.
	 * @throws NullPointerException
	 *             if the speicified map is null.
	 */
	public void addParameters(Map<String, Object> parameters)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(parameters);
		this.params.putAll(parameters);
	}

	/**
	 * Removes the parameter for the speicified key from this map if present.
	 * 
	 * @param key
	 *            the key whose parameter will be removed from this composite
	 *            parameter.
	 * @throws NullPointerException
	 *             if the specified key is null.
	 */
	public void removeParameter(String key)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(key);
		params.remove(key);
	}

	/**
	 * Returns the parameter to which the specified key is mapped, or null if
	 * this map contains no parameter for the key.
	 * 
	 * @param key
	 *            the specified key whose associated parameter is to be
	 *            returned.
	 * @return the parameter to which the specified key is mapped, or null if
	 *         this map contains no parameter for the key.
	 * @throws NullPointerException
	 *             if the specified key is null.
	 */
	public Object getParameter(String key)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(key);
		return params.get(key);
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

package vn.dongpv.pde.adt;

import java.util.HashMap;
import java.util.Map;

import vn.dongpv.pde.util.ValidatorUtil;

/**
 * BiMap is an Abstract Data Type (ADT), represent the one-to-one relationship.
 * 
 * @author Pham Van Dong
 * 
 * @param <K>
 *            the type of keys maintained by this map.
 * @param <V>
 *            the type of mapped values.
 */
public class BiMap<K, V>
{

	/**
	 * The key-value map.
	 */
	private final Map<K, V> map1;

	/**
	 * The value-key map.
	 */
	private final Map<V, K> map2;

	/**
	 * Constructs an empty BiMap.
	 */
	public BiMap()
	{
		map1 = new HashMap<K, V>();
		map2 = new HashMap<V, K>();
	}

	/**
	 * Associates the specified value with the specified key in this map. If the
	 * map previously contained a mapping for the key, the old value is
	 * replaced.
	 * 
	 * @param key
	 *            the key.
	 * @param value
	 *            the value.
	 * @throws NullPointerException
	 *             if the specified key or value is null.
	 */
	public void add(K key, V value)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(key, value);

		final V oldValue = map1.put(key, value);
		if (map1.containsKey(key))
		{
			map2.remove(oldValue);
		}
		map2.put(value, key);
	}

	/**
	 * Copies all of the key-value mappings from the specified map to this map.
	 * These mappings will replace any mappings that this map had for the same
	 * keys.
	 * 
	 * @param map
	 *            the map will be copied to this map.
	 * @throws NullPointerException
	 *             if the specified map is null.
	 */
	public void addAll(Map<K, V> map)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(map);

		for (final Map.Entry<K, V> entry : map.entrySet())
		{
			add(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * Copies all of the value-key mappings from the specified map to this map.
	 * These mappings will replace any mappings that this map had for the same
	 * keys.
	 * 
	 * @param map
	 *            the map will be copied to this map.
	 * @throws NullPointerException
	 *             if the specified map is null.
	 */
	public void addAll2(Map<V, K> map)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(map);

		for (final Map.Entry<V, K> entry : map.entrySet())
		{
			add(entry.getValue(), entry.getKey());
		}
	}

	/**
	 * Removes the mapping for the specified key from this map if present.
	 * 
	 * @param key
	 *            the key whose mapping will be removed from this map.
	 * @throws NullPointerException
	 *             if the specified key is null.
	 */
	public void removeByKey(K key)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(key);

		final V oldValue = map1.remove(key);
		if (oldValue != null)
		{
			map2.remove(oldValue);
		}
	}

	/**
	 * Removes the mapping for the specified value from this map if present.
	 * 
	 * @param value
	 *            the value whose mapping will be removed from this map.
	 * @throws NullPointerException
	 *             if the specified key is null.
	 */
	public void removeByValue(V value)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(value);

		final K oldKey = map2.remove(value);
		if (oldKey != null)
		{
			map1.remove(oldKey);
		}
	}

	/**
	 * Returns the value to which the specified key is mapped, or null if this
	 * map contains no mapping for the key.
	 * 
	 * @param key
	 *            the specified key whose associated value is to be returned.
	 * @return the value to which the specified key is mapped, or null if this
	 *         map contains no mapping for the key.
	 * @throws NullPointerException
	 *             if the specified key is null.
	 */
	public V getValue(K key)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(key);
		return map1.get(key);
	}

	/**
	 * Returns the key to which the specified value is mapped, or null if this
	 * map contains no mapping for the value.
	 * 
	 * @param value
	 *            the specified value whose associated key is to be returned.
	 * @return the key to which the specified value is mapped, or null if this
	 *         map contains no mapping for the value.
	 * @throws NullPointerException
	 *             if the specified value is null.
	 */
	public K getKey(V value)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(value);
		return map2.get(value);
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

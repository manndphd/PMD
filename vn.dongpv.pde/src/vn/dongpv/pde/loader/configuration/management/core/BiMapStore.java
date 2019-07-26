package vn.dongpv.pde.loader.configuration.management.core;

import vn.dongpv.pde.adt.BiMap;

public class BiMapStore<K, V>
{

	/**
	 * The BiMap that stores all mappings.
	 */
	private final BiMap<K, V> map;

	/**
	 * Constructs an empty store.
	 */
	public BiMapStore()
	{
		map = new BiMap<K, V>();
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
	public void addMapping(K key, V value)
		throws NullPointerException
	{
		map.add(key, value);
	}

	/**
	 * Removes the mapping for the specified key from this map if present.
	 * 
	 * @param key
	 *            the key whose mapping will be removed from this map.
	 * @throws NullPointerException
	 *             if the specified key is null.
	 */
	public void removeMapping(K key)
		throws NullPointerException
	{
		map.removeByKey(key);
	}

	/**
	 * Returns the key to which the specified value is mapped, or null if this
	 * map contains no mapping for key.
	 * 
	 * @param value
	 *            the specified value.
	 * @return the key to which the specified value is mapped, or null if this
	 *         map contains no mapping for the value.
	 * @throws NullPointerException
	 *             if the value is null.
	 */
	public K getKey(V value)
		throws NullPointerException
	{
		return map.getKey(value);
	}

	/**
	 * Returns the value to which the specified key is mapped, or null if this
	 * map contains no mapping for the key.
	 * 
	 * @param key
	 *            the specified key of a ChangeCreator that will be returned.
	 * @return the value to which the specified key is mapped, or null if this
	 *         map contains no mapping for the key.
	 * @throws NullPointerException
	 *             if the specified key is null.
	 */
	public V getValue(K key)
		throws NullPointerException
	{
		return map.getValue(key);
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

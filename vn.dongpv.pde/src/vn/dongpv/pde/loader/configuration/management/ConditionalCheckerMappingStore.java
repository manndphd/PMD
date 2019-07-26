package vn.dongpv.pde.loader.configuration.management;

import vn.dongpv.pde.loader.configuration.management.core.BiMapStore;
import vn.dongpv.pde.rule.checker.core.ConditionalChecker;
import vn.dongpv.pde.rule.checker.core.ConditionalCheckerType;

public class ConditionalCheckerMappingStore
	extends BiMapStore<ConditionalCheckerType, Class<? extends ConditionalChecker>>
{

	private static ConditionalCheckerMappingStore instance;

	static
	{
		instance = new ConditionalCheckerMappingStore();
	}

	private ConditionalCheckerMappingStore()
	{
		super();
	}

	/**
	 * Gets the sole instance of this class.
	 * 
	 * @return the sole instance of this class.
	 */
	public static ConditionalCheckerMappingStore getInstance()
	{
		return instance;
	}

	@Override
	public ConditionalCheckerType getKey(Class<? extends ConditionalChecker> value)
		throws NullPointerException
	{
		final ConditionalCheckerType key = super.getKey(value);
		return (key == null) ? ConditionalCheckerType.Null : key;
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

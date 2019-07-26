package vn.dongpv.pde.loader.configuration.management;

import vn.dongpv.pde.loader.configuration.management.core.BiMapStore;
import vn.dongpv.pde.refactoring.change.creator.core.ChangeCreator;
import vn.dongpv.pde.refactoring.change.creator.core.ChangeCreatorType;

public class ChangeCreatorMappingStore
	extends BiMapStore<ChangeCreatorType, Class<? extends ChangeCreator>>
{

	private static ChangeCreatorMappingStore instance;

	static
	{
		instance = new ChangeCreatorMappingStore();
	}

	private ChangeCreatorMappingStore()
	{
		super();
	}

	/**
	 * Gets the sole instance of this class.
	 * 
	 * @return the sole instance of this class.
	 */
	public static ChangeCreatorMappingStore getInstance()
	{
		return instance;
	}

	@Override
	public ChangeCreatorType getKey(Class<? extends ChangeCreator> value)
		throws NullPointerException
	{
		final ChangeCreatorType key = super.getKey(value);
		return (key == null) ? ChangeCreatorType.Null : key;
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

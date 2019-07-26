package vn.mannd.pde.loader.configuration.management;

import java.util.HashMap;
import java.util.Map;

import vn.mannd.pde.rule.analyzer.core.RuleDescriptor;
import vn.mannd.pde.rule.analyzer.core.RuleType;
import vn.mannd.pde.util.ValidatorUtil;

public class RuleDescriptorStore
{

	private static RuleDescriptorStore instance;

	private final Map<RuleType, RuleDescriptor> map;

	static
	{
		instance = new RuleDescriptorStore();
	}

	private RuleDescriptorStore()
	{
		map = new HashMap<RuleType, RuleDescriptor>();
	}

	public static RuleDescriptorStore getInstance()
	{
		return instance;
	}

	public void addRuleDescriptor(RuleDescriptor descriptor)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(descriptor);

		RuleType type = descriptor.getType();
		if (type == null)
		{
			type = RuleType.Null;
		}
		map.put(type, descriptor);
	}

	public void addRuleDescriptors(Map<RuleType, RuleDescriptor> map)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(map);
		this.map.putAll(map);
	}

	public void removeRuleDescriptor(RuleType type)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(type);
		map.remove(type);
	}

	public RuleDescriptor getRuleDescriptor(RuleType type)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(type);
		return map.get(type);
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

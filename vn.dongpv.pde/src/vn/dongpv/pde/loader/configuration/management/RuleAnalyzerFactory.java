package vn.dongpv.pde.loader.configuration.management;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import vn.dongpv.pde.loader.configuration.management.core.BiMapStore;
import vn.dongpv.pde.preprocessor.ASTNodeCollector;
import vn.dongpv.pde.rule.analyzer.NullAnalyzer;
import vn.dongpv.pde.rule.analyzer.core.RuleAnalyzer;
import vn.dongpv.pde.rule.analyzer.core.RuleType;
import vn.dongpv.pde.util.ValidatorUtil;

public class RuleAnalyzerFactory
	extends BiMapStore<RuleType, Class<? extends RuleAnalyzer>>
{

	private static RuleAnalyzerFactory instance;

	static
	{
		instance = new RuleAnalyzerFactory();
	}

	private RuleAnalyzerFactory()
	{
		super();
	}

	/**
	 * Gets the sole instance of this class.
	 * 
	 * @return the sole instance of this class.
	 */
	public static RuleAnalyzerFactory getInstance()
	{
		return instance;
	}

	@Override
	public RuleType getKey(Class<? extends RuleAnalyzer> value)
		throws NullPointerException
	{
		final RuleType type = super.getKey(value);
		return (type == null) ? RuleType.Null : type;
	}

	/**
	 * Gets new rule analyzer whose type is specified by the key.
	 * 
	 * @param key
	 *            the specified key which is the type of the rule analyzer will
	 *            be created.
	 * @param collector
	 *            the initial argument for the rule analyzer's constructor.
	 * @return new rule analyzer whose type is specified by the key
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NullPointerException
	 *             if the specified key or collector argument is null.
	 */
	public RuleAnalyzer newAnalyzer(RuleType key, ASTNodeCollector collector)
		throws
		SecurityException, NoSuchMethodException, IllegalArgumentException,
		InstantiationException, IllegalAccessException, InvocationTargetException,
		NullPointerException
	{
		ValidatorUtil.checkNotNull(collector);

		final Class<? extends RuleAnalyzer> clazz = super.getValue(key);
		if (clazz == null)
		{
			return new NullAnalyzer(collector);
		}

		final Constructor<? extends RuleAnalyzer> constructor =
			clazz.getConstructor(ASTNodeCollector.class);
		return constructor.newInstance(collector);
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

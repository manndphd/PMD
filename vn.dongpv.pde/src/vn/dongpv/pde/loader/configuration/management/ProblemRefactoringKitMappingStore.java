package vn.dongpv.pde.loader.configuration.management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.dongpv.pde.refactoring.core.ProblemRefactoringKitType;
import vn.dongpv.pde.rule.analyzer.core.RuleType;
import vn.dongpv.pde.rule.checker.core.ConditionalCheckerType;
import vn.dongpv.pde.util.ValidatorUtil;

public class ProblemRefactoringKitMappingStore
{

	private static final int PAIR_CAPACITY = 2;

	private static ProblemRefactoringKitMappingStore instance;

	static
	{
		instance = new ProblemRefactoringKitMappingStore();
	}

	private final Map<List<?>, ProblemRefactoringKitType> map;

	private ProblemRefactoringKitMappingStore()
	{
		map = new HashMap<List<?>, ProblemRefactoringKitType>();
	}

	public static ProblemRefactoringKitMappingStore getInstance()
	{
		return instance;
	}

	public void addMapping(
		RuleType ruleType,
		ConditionalCheckerType checkerType,
		ProblemRefactoringKitType kitType)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(ruleType, checkerType, kitType);

		final List<Object> pair = new ArrayList<Object>(PAIR_CAPACITY);
		pair.add(ruleType);
		pair.add(checkerType);
		map.put(pair, kitType);
	}

	public void removeMapping(
		RuleType ruleType,
		ConditionalCheckerType checkerType)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(ruleType, checkerType);

		final List<Object> pair = new ArrayList<Object>(PAIR_CAPACITY);
		pair.add(ruleType);
		pair.add(checkerType);
		map.remove(pair);
	}

	public ProblemRefactoringKitType getKitType(
		RuleType ruleType,
		ConditionalCheckerType checkerType)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(ruleType, checkerType);

		final List<Object> pair = new ArrayList<Object>(PAIR_CAPACITY);
		pair.add(ruleType);
		pair.add(checkerType);
		return map.get(pair);
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

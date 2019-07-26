package vn.dongpv.pde.loader.configuration.management;

import java.util.HashMap;
import java.util.Map;

import vn.dongpv.pde.refactoring.core.ProblemRefactoringKit;
import vn.dongpv.pde.refactoring.core.ProblemRefactoringKitType;
import vn.dongpv.pde.util.ValidatorUtil;

public class ProblemRefactoringKitStore
{

	private static ProblemRefactoringKitStore instance;

	static
	{
		instance = new ProblemRefactoringKitStore();
	}

	private final Map<ProblemRefactoringKitType, ProblemRefactoringKit> map;

	private ProblemRefactoringKitStore()
	{
		map = new HashMap<ProblemRefactoringKitType, ProblemRefactoringKit>();
	}

	public static ProblemRefactoringKitStore getInstance()
	{
		return instance;
	}

	public void addKit(ProblemRefactoringKit kit)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(kit);
		map.put(kit.getType(), kit);
	}

	public void removeKit(ProblemRefactoringKitType type)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(type);
		map.remove(type);
	}

	public ProblemRefactoringKit getKit(ProblemRefactoringKitType type)
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

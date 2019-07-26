package vn.dongpv.pde.refactoring.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.refactoring.change.creator.core.ChangeCreator;
import vn.dongpv.pde.rule.checker.core.ConditionalChecker;
import vn.dongpv.pde.util.ValidatorUtil;

/**
 * This kit is an object that contains two other importance class instance:
 * {@link ConditionalChecker} and {@link ChangeCreator} classes. This object
 * acts like a factory that creates new checker and change-creator if needed.
 * 
 * @author Pham Van Dong
 * 
 */
public class ProblemRefactoringKit
{

	private final ProblemRefactoringKitType type;
	private final Class<? extends ConditionalChecker> checkerClass;
	private final Class<? extends ChangeCreator> changeCreatorClass;

	/**
	 * Constructs a kit with the specified type.
	 * 
	 * @param type
	 *            the type of kit.
	 * @param checkerClass
	 *            the {@link ConditionalChecker} class.
	 * @param changeCreatorClass
	 *            the {@link ChangeCreator} class.
	 * @throws NullPointerException
	 *             if the type, {@link ConditionalChecker} or
	 *             {@link ChangeCreator} is null.
	 */
	public ProblemRefactoringKit(
		ProblemRefactoringKitType type,
		Class<? extends ConditionalChecker> checkerClass,
		Class<? extends ChangeCreator> changeCreatorClass)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(type, checkerClass, changeCreatorClass);

		this.type = type;
		this.checkerClass = checkerClass;
		this.changeCreatorClass = changeCreatorClass;
	}

	/**
	 * Constructs a kit with implicit type.
	 * 
	 * @param checkerClass
	 *            the {@link ConditionalChecker} class.
	 * @param changeCreatorClass
	 *            the {@link ChangeCreator} class.
	 * @throws NullPointerException
	 *             if the {@link ConditionalChecker} or {@link ChangeCreator} is
	 *             null.
	 */
	public ProblemRefactoringKit(
		Class<? extends ConditionalChecker> checkerClass,
		Class<? extends ChangeCreator> changeCreatorClass)
		throws NullPointerException
	{
		this(ProblemRefactoringKitType.None, checkerClass, changeCreatorClass);
	}

	/**
	 * Gets the type of kit.
	 * 
	 * @return the type of kit.
	 */
	public ProblemRefactoringKitType getType()
	{
		return type;
	}

	/**
	 * Creates new checker.
	 * 
	 * @param parameter
	 *            the parameter will be passed to the checker.
	 * @return new checker.
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws IllegalArgumentException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NullPointerException
	 *             if the parameter is null.
	 */
	public ConditionalChecker newChecker(CompositeParameter parameter)
		throws
		SecurityException, NoSuchMethodException, IllegalArgumentException,
		InstantiationException, IllegalAccessException, InvocationTargetException,
		NullPointerException
	{
		ValidatorUtil.checkNotNull(parameter);

		final Constructor<? extends ConditionalChecker> constructor =
			checkerClass.getConstructor(CompositeParameter.class);
		return constructor.newInstance(parameter);
	}

	/**
	 * Creates new change creator.
	 * 
	 * @param parameter
	 *            the parameter will be passed to the change creator.
	 * @return new change creator.
	 * @throws IllegalArgumentException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws NullPointerException
	 */
	public ChangeCreator newChangeCreator(CompositeParameter parameter)
		throws
		IllegalArgumentException, InstantiationException, IllegalAccessException,
		InvocationTargetException, SecurityException, NoSuchMethodException,
		NullPointerException
	{
		ValidatorUtil.checkNotNull(parameter);

		final Constructor<? extends ChangeCreator> constructor =
			changeCreatorClass.getConstructor(CompositeParameter.class);
		return constructor.newInstance(parameter);
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

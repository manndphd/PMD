package vn.mannd.pde.rule.analyzer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ArrayCreation;

import vn.mannd.pde.adt.CompositeParameter;
import vn.mannd.pde.preprocessor.ASTNodeCollector;
import vn.mannd.pde.rule.ProblemItem;
import vn.mannd.pde.rule.analyzer.core.RuleAnalyzer;
import vn.mannd.pde.rule.checker.AlwaysReuseImmutableConstantObjectsForBetterMemoryUtilizationChecker;
import vn.mannd.pde.rule.checker.core.ConditionalChecker;

public class AlwaysReuseImmutableConstantObjectsForBetterMemoryUtilizationAnalyzer
	extends RuleAnalyzer
{

	public AlwaysReuseImmutableConstantObjectsForBetterMemoryUtilizationAnalyzer(
		ASTNodeCollector collector)
		throws NullPointerException
	{
		super(collector);
	}

	@Override
	public List<ProblemItem> analyze()
	{
		final List<ProblemItem> result =
			new ArrayList<ProblemItem>(DEFAULT_PROBLEMS_LIST_CAPACITY);

		final List<ArrayCreation> arrayCreations = getCollector().getArrayCreations();
		for (final ArrayCreation arrayCreation : arrayCreations)
		{
			final CompositeParameter parameter = new CompositeParameter();
			parameter.addParameter(ConditionalChecker.PARSED_ROOT_NODE,
				getCollector().getAnalyzedNode());
			parameter.addParameter(ConditionalChecker.PARSED_NODE, arrayCreation);

			final AlwaysReuseImmutableConstantObjectsForBetterMemoryUtilizationChecker checker =
				new AlwaysReuseImmutableConstantObjectsForBetterMemoryUtilizationChecker(parameter);
			if (checker.isViolated())
			{
				final ProblemItem item = new ProblemItem(
					getDescriptor(), checker.getType(), arrayCreation);
				result.add(item);
			}
		}

		return result;
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

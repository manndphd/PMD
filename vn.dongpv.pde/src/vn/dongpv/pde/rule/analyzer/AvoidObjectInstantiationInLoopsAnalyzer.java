package vn.dongpv.pde.rule.analyzer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;

import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.preprocessor.ASTNodeCollector;
import vn.dongpv.pde.rule.ProblemItem;
import vn.dongpv.pde.rule.analyzer.core.RuleAnalyzer;
import vn.dongpv.pde.rule.checker.AvoidObjectInstantiationInLoopsChecker;
import vn.dongpv.pde.rule.checker.core.ConditionalChecker;

public class AvoidObjectInstantiationInLoopsAnalyzer extends RuleAnalyzer
{

	public AvoidObjectInstantiationInLoopsAnalyzer(ASTNodeCollector collector)
		throws NullPointerException
	{
		super(collector);
	}

	@Override
	public List<ProblemItem> analyze()
	{
		final List<ProblemItem> result =
			new ArrayList<ProblemItem>(DEFAULT_PROBLEMS_LIST_CAPACITY);

		final List<ASTNode> creations =
			new ArrayList<ASTNode>(DEFAULT_PROBLEMS_LIST_CAPACITY);
		creations.addAll(getCollector().getClassInstanceCreations());
		creations.addAll(getCollector().getArrayCreations());

		for (final ASTNode creation : creations)
		{
			final CompositeParameter parameter = new CompositeParameter();
			parameter.addParameter(ConditionalChecker.PARSED_ROOT_NODE,
				getCollector().getAnalyzedNode());
			parameter.addParameter(ConditionalChecker.PARSED_NODE, creation);

			final AvoidObjectInstantiationInLoopsChecker checker =
				new AvoidObjectInstantiationInLoopsChecker(parameter);
			if (checker.isViolated())
			{
				final ProblemItem item = new ProblemItem(
					getDescriptor(), checker.getType(), creation);
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

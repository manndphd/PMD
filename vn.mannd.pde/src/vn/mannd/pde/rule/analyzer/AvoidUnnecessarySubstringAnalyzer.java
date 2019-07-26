package vn.mannd.pde.rule.analyzer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.MethodInvocation;

import vn.mannd.pde.adt.CompositeParameter;
import vn.mannd.pde.preprocessor.ASTNodeCollector;
import vn.mannd.pde.rule.ProblemItem;
import vn.mannd.pde.rule.analyzer.core.RuleAnalyzer;
import vn.mannd.pde.rule.checker.AvoidUnnecessarySubstringChecker;
import vn.mannd.pde.rule.checker.core.ConditionalChecker;

public class AvoidUnnecessarySubstringAnalyzer extends RuleAnalyzer
{

	public AvoidUnnecessarySubstringAnalyzer(ASTNodeCollector collector)
		throws NullPointerException
	{
		super(collector);
	}

	@Override
	public List<ProblemItem> analyze()
	{
		final List<ProblemItem> result =
			new ArrayList<ProblemItem>(DEFAULT_PROBLEMS_LIST_CAPACITY);

		final List<MethodInvocation> methodInvocations =
			getCollector().getMethodInvocations();
		for (final MethodInvocation invocation : methodInvocations)
		{
			final CompositeParameter parameter = new CompositeParameter();
			parameter.addParameter(ConditionalChecker.PARSED_ROOT_NODE,
				getCollector().getAnalyzedNode());
			parameter.addParameter(ConditionalChecker.PARSED_NODE, invocation);

			final AvoidUnnecessarySubstringChecker checker =
				new AvoidUnnecessarySubstringChecker(parameter);
			if (checker.isViolated())
			{
				final ProblemItem item = new ProblemItem(
					getDescriptor(), checker.getType(), invocation);
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

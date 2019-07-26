package vn.dongpv.pde.rule.analyzer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.MethodInvocation;

import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.preprocessor.ASTNodeCollector;
import vn.dongpv.pde.rule.ProblemItem;
import vn.dongpv.pde.rule.analyzer.core.RuleAnalyzer;
import vn.dongpv.pde.rule.checker.ConstructorShouldCallPrivateOrFinalMethodChecker;
import vn.dongpv.pde.rule.checker.core.ConditionalChecker;

public class ConstructorShouldCallPrivateOrFinalMethodAnalyzer extends RuleAnalyzer
{

	public ConstructorShouldCallPrivateOrFinalMethodAnalyzer(
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

		final List<MethodInvocation> methodInvocations =
			getCollector().getMethodInvocations();
		for (final MethodInvocation invocation : methodInvocations)
		{
			final CompositeParameter parameter = new CompositeParameter();
			parameter.addParameter(ConditionalChecker.PARSED_ROOT_NODE,
				getCollector().getAnalyzedNode());
			parameter.addParameter(ConditionalChecker.PARSED_NODE, invocation);

			final ConstructorShouldCallPrivateOrFinalMethodChecker checker =
				new ConstructorShouldCallPrivateOrFinalMethodChecker(parameter);
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

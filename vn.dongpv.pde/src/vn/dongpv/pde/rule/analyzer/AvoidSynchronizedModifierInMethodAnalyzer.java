package vn.dongpv.pde.rule.analyzer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.MethodDeclaration;

import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.preprocessor.ASTNodeCollector;
import vn.dongpv.pde.rule.ProblemItem;
import vn.dongpv.pde.rule.analyzer.core.RuleAnalyzer;
import vn.dongpv.pde.rule.checker.AvoidSynchronizedModifierInMethodChecker;
import vn.dongpv.pde.rule.checker.core.ConditionalChecker;

public class AvoidSynchronizedModifierInMethodAnalyzer extends RuleAnalyzer
{

	public AvoidSynchronizedModifierInMethodAnalyzer(ASTNodeCollector collector)
		throws NullPointerException
	{
		super(collector);
	}

	@Override
	public List<ProblemItem> analyze()
	{
		final List<ProblemItem> result =
			new ArrayList<ProblemItem>(DEFAULT_PROBLEMS_LIST_CAPACITY);

		final List<MethodDeclaration> methodDecls = getCollector().getMethodDeclarations();
		for (final MethodDeclaration methodDecl : methodDecls)
		{
			final CompositeParameter parameter = new CompositeParameter();
			parameter.addParameter(ConditionalChecker.PARSED_ROOT_NODE,
				getCollector().getAnalyzedNode());
			parameter.addParameter(ConditionalChecker.PARSED_NODE, methodDecl);

			final AvoidSynchronizedModifierInMethodChecker checker =
				new AvoidSynchronizedModifierInMethodChecker(parameter);
			if (checker.isViolated())
			{
				final ProblemItem item = new ProblemItem(
					getDescriptor(), checker.getType(), methodDecl);
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

package vn.mannd.pde.rule.analyzer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;

import vn.mannd.pde.adt.CompositeParameter;
import vn.mannd.pde.preprocessor.ASTNodeCollector;
import vn.mannd.pde.rule.ProblemItem;
import vn.mannd.pde.rule.analyzer.core.RuleAnalyzer;
import vn.mannd.pde.rule.checker.AvoidEmptyLoopsChecker;
import vn.mannd.pde.rule.checker.core.ConditionalChecker;

public class AvoidEmptyLoopsAnalyzer extends RuleAnalyzer
{

	public AvoidEmptyLoopsAnalyzer(ASTNodeCollector collector)
		throws NullPointerException
	{
		super(collector);
	}

	@Override
	public List<ProblemItem> analyze()
	{
		final List<ProblemItem> result =
			new ArrayList<ProblemItem>(DEFAULT_PROBLEMS_LIST_CAPACITY);

		// Gets all loop statements: for/do/while loop type

		final List<ASTNode> loopStatements = new ArrayList<ASTNode>(
			getCollector().getForStatements());
		loopStatements.addAll(getCollector().getDoStatements());
		loopStatements.addAll(getCollector().getWhileStatements());

		for (final ASTNode loopStatement : loopStatements)
		{
			final CompositeParameter parameter = new CompositeParameter();
			parameter.addParameter(ConditionalChecker.PARSED_ROOT_NODE,
				getCollector().getAnalyzedNode());
			parameter.addParameter(ConditionalChecker.PARSED_NODE, loopStatement);

			final AvoidEmptyLoopsChecker checker = new AvoidEmptyLoopsChecker(parameter);
			if (checker.isViolated())
			{
				final ProblemItem item = new ProblemItem(
					getDescriptor(), checker.getType(), loopStatement);
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

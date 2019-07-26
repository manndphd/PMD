package vn.mannd.pde.loader;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import vn.mannd.pde.PluginConstants;
import vn.mannd.pde.loader.configuration.management.ChangeCreatorMappingStore;
import vn.mannd.pde.loader.configuration.management.ConditionalCheckerMappingStore;
import vn.mannd.pde.loader.configuration.management.ProblemRefactoringKitMappingStore;
import vn.mannd.pde.loader.configuration.management.ProblemRefactoringKitStore;
import vn.mannd.pde.loader.configuration.management.RuleAnalyzerFactory;
import vn.mannd.pde.loader.configuration.management.RuleDescriptorStore;
import vn.mannd.pde.refactoring.change.creator.core.ChangeCreator;
import vn.mannd.pde.refactoring.change.creator.core.ChangeCreatorType;
import vn.mannd.pde.refactoring.core.ProblemRefactoringKit;
import vn.mannd.pde.refactoring.core.ProblemRefactoringKitType;
import vn.mannd.pde.rule.analyzer.core.RuleAnalyzer;
import vn.mannd.pde.rule.analyzer.core.RuleDescriptor;
import vn.mannd.pde.rule.analyzer.core.RuleType;
import vn.mannd.pde.rule.checker.core.ConditionalChecker;
import vn.mannd.pde.rule.checker.core.ConditionalCheckerType;
import vn.mannd.pde.util.ValidatorUtil;
import vn.mannd.pde.util.XMLUtil;

/**
 * The loader that loads all configuration files.
 * 
 * @author Pham Van Dong
 * 
 */
public class ConfigurationLoader
{

	private static boolean isLoaded = false;

	/**
	 * Loads all configuration files.
	 * 
	 * @throws SAXException
	 * @throws IOException
	 * @throws AnalyzerConfigurationException
	 * @throws NumberFormatException
	 * @throws XPathExpressionException
	 * @throws ClassNotFoundException
	 * @throws URISyntaxException
	 */
	public static void load()
		throws
		SAXException, IOException, ParserConfigurationException, NumberFormatException,
		XPathExpressionException, ClassNotFoundException, URISyntaxException
	{
		loadRuleDescriptions();
		loadConditionalCheckers();
		loadChangeCreators();
		loadProblemRefactoringKits();
		loadProblemRefactoringKitMappings();
		isLoaded = true;
	}

	/**
	 * Loads all configuration files, ignores any exceptions.
	 */
	public static void silentlyLoad()
	{
		if (!isLoaded)
		{
			try
			{
				load();
			}
			catch (final NumberFormatException e)
			{
				e.printStackTrace();
			}
			catch (final XPathExpressionException e)
			{
				e.printStackTrace();
			}
			catch (final SAXException e)
			{
				e.printStackTrace();
			}
			catch (final IOException e)
			{
				e.printStackTrace();
			}
			catch (final ParserConfigurationException e)
			{
				e.printStackTrace();
			}
			catch (final ClassNotFoundException e)
			{
				e.printStackTrace();
			}
			catch (final URISyntaxException e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Indicates all configuration files were loaded or not.
	 * 
	 * @return true if all configuration files were loaded.
	 */
	public static boolean isLoaded()
	{
		return isLoaded;
	}

	private static void loadRuleDescriptions()
		throws
		SAXException, IOException, ParserConfigurationException,
		URISyntaxException, NumberFormatException, XPathExpressionException
	{
		final Document doc = XMLUtil.analyze(PluginConstants.RULES_CONFIG_FILE_PATH);
		ValidatorUtil.checkNotNull("The configuration file for rule analyzers couldn't be analyzed.", doc);

		final XPath path = XMLUtil.newXPath();

		final int count = Integer.parseInt(
			path.evaluate("count(/rules/rule)", doc));
		for (int i = count; i > 0; i -= 1)
		{
			try
			{
				final RuleType type = RuleType.valueOf(
					path.evaluate("/rules/rule[" + i + "]/id", doc));
				final String category =
					path.evaluate("/rules/rule[" + i + "]/category", doc);
				final String severity =
					path.evaluate("/rules/rule[" + i + "]/severity", doc);
				final String refactoringSupport =
					path.evaluate("/rules/rule[" + i + "]/refactoringSupport", doc);
				final String description =
					path.evaluate("/rules/rule[" + i + "]/description", doc);
				final String reason =
					path.evaluate("/rules/rule[" + i + "]/reason", doc);
				final String usageExample =
					path.evaluate("/rules/rule[" + i + "]/usageExample", doc);

				final RuleDescriptor descriptor = new RuleDescriptor(
					type, category, severity, refactoringSupport,
					description, reason, usageExample);
				RuleDescriptorStore.getInstance().addRuleDescriptor(descriptor);

				final Class<? extends RuleAnalyzer> clazz = Class.forName(
					path.evaluate("/rules/rule[" + i + "]/analyzerClass", doc)).
					asSubclass(RuleAnalyzer.class);

				RuleAnalyzerFactory.getInstance().addMapping(type, clazz);
			}
			catch (final XPathExpressionException e)
			{
				e.printStackTrace();
			}
			catch (final NullPointerException e)
			{
				e.printStackTrace();
			}
			catch (final ClassNotFoundException e)
			{
				e.printStackTrace();
			}
		}
	}

	private static void loadConditionalCheckers()
		throws
		SAXException, IOException, ParserConfigurationException,
		URISyntaxException, NumberFormatException, XPathExpressionException
	{
		final Document doc = XMLUtil.analyze(PluginConstants.CHECKERS_CONFIG_FILE_PATH);
		ValidatorUtil.checkNotNull(doc, "The configuration file for conditional checkers couldn't be analyzed.");

		final XPath path = XMLUtil.newXPath();

		final int count = Integer.parseInt(
			path.evaluate("count(/checkers/checker)", doc));
		for (int i = count; i > 0; i -= 1)
		{
			try
			{
				final ConditionalCheckerType type = ConditionalCheckerType.valueOf(
					path.evaluate("/checkers/checker[" + i + "]/id", doc));
				final Class<? extends ConditionalChecker> clazz = Class.forName(
					path.evaluate("/checkers/checker[" + i + "]/class", doc)).
					asSubclass(ConditionalChecker.class);

				ConditionalCheckerMappingStore.getInstance().addMapping(type, clazz);
			}
			catch (final XPathExpressionException e)
			{
				e.printStackTrace();
			}
			catch (final NullPointerException e)
			{
				e.printStackTrace();
			}
			catch (final ClassNotFoundException e)
			{
				e.printStackTrace();
			}
		}
	}

	private static void loadChangeCreators()
		throws
		SAXException, IOException, ParserConfigurationException,
		URISyntaxException, NumberFormatException, XPathExpressionException
	{
		final Document doc = XMLUtil.analyze(PluginConstants.CHANGE_CREATORS_CONFIG_FILE_PATH);
		ValidatorUtil.checkNotNull(doc, "The configuration file for change creators couldn't be analyzed.");

		final XPath path = XMLUtil.newXPath();

		final int count = Integer.parseInt(
			path.evaluate("count(/changeCreators/changeCreator)", doc));
		for (int i = count; i > 0; i -= 1)
		{
			try
			{
				final ChangeCreatorType type = ChangeCreatorType.valueOf(
					path.evaluate("/changeCreators/changeCreator[" + i + "]/id", doc));
				final Class<? extends ChangeCreator> clazz = Class.forName(
					path.evaluate("/changeCreators/changeCreator[" + i + "]/class", doc)).
					asSubclass(ChangeCreator.class);
				ChangeCreatorMappingStore.getInstance().addMapping(type, clazz);
			}
			catch (final XPathExpressionException e)
			{
				e.printStackTrace();
			}
			catch (final NullPointerException e)
			{
				e.printStackTrace();
			}
			catch (final ClassNotFoundException e)
			{
				e.printStackTrace();
			}
		}
	}

	private static void loadProblemRefactoringKits()
		throws
		SAXException, IOException, ParserConfigurationException,
		URISyntaxException, NumberFormatException, XPathExpressionException
	{
		final Document doc = XMLUtil.analyze(PluginConstants.FACTORIES_CONFIG_FILE_PATH);
		ValidatorUtil.checkNotNull(doc, "The configuration file for refactoring kit factories couldn't be analyzed.");

		final XPath path = XMLUtil.newXPath();

		final int count = Integer.parseInt(
			path.evaluate("count(/factories/factory)", doc));
		for (int i = count; i > 0; i -= 1)
		{
			try
			{
				final ProblemRefactoringKitType type = ProblemRefactoringKitType.valueOf(
					path.evaluate("/factories/factory[" + i + "]/id", doc));
				final ConditionalCheckerType checkerType = ConditionalCheckerType.valueOf(
					path.evaluate("/factories/factory[" + i + "]/checkerId", doc));
				final ChangeCreatorType creatorType = ChangeCreatorType.valueOf(
					path.evaluate("/factories/factory[" + i + "]/changeCreatorId", doc));

				final Class<? extends ConditionalChecker> checkerClass =
					ConditionalCheckerMappingStore.getInstance().getValue(checkerType);

				final Class<? extends ChangeCreator> changeCreatorClass =
					ChangeCreatorMappingStore.getInstance().getValue(creatorType);

				final ProblemRefactoringKit kit = new ProblemRefactoringKit(
					type, checkerClass, changeCreatorClass);
				ProblemRefactoringKitStore.getInstance().addKit(kit);
			}
			catch (final XPathExpressionException e)
			{
				e.printStackTrace();
			}
			catch (final NullPointerException e)
			{
				e.printStackTrace();
			}
		}
	}

	private static void loadProblemRefactoringKitMappings()
		throws
		SAXException, IOException, ParserConfigurationException,
		URISyntaxException, NumberFormatException, XPathExpressionException
	{
		final Document doc = XMLUtil.analyze(PluginConstants.FACTORY_MAPPINGS_CONFIG_FILE_PATH);
		ValidatorUtil.checkNotNull(doc, "The configuration file for refactoring kit factory mappings couldn't be analyzed.");

		final XPath path = XMLUtil.newXPath();

		final int count = Integer.parseInt(
			path.evaluate("count(/mappings/mapping)", doc));
		for (int i = count; i > 0; i -= 1)
		{
			try
			{
				final RuleType ruleType = RuleType.valueOf(
					path.evaluate("/mappings/mapping[" + i + "]/ruleId", doc));
				final ConditionalCheckerType checkerType = ConditionalCheckerType.valueOf(
					path.evaluate("/mappings/mapping[" + i + "]/checkerId", doc));
				final ProblemRefactoringKitType kitType = ProblemRefactoringKitType.valueOf(
					path.evaluate("/mappings/mapping[" + i + "]/factoryId", doc));

				ProblemRefactoringKitMappingStore.
					getInstance().addMapping(ruleType, checkerType, kitType);
			}
			catch (final XPathExpressionException e)
			{
				e.printStackTrace();
			}
			catch (final NullPointerException e)
			{
				e.printStackTrace();
			}
		}
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

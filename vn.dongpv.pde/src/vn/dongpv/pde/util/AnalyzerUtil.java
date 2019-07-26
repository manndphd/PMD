package vn.dongpv.pde.util;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.ui.progress.IProgressConstants;

import vn.dongpv.pde.Activator;
import vn.dongpv.pde.loader.configuration.management.RuleAnalyzerFactory;
import vn.dongpv.pde.preprocessor.ASTNodeCollector;
import vn.dongpv.pde.rule.ProblemItem;
import vn.dongpv.pde.rule.analyzer.core.RuleAnalyzer;
import vn.dongpv.pde.rule.analyzer.core.RuleType;

/**
 * This class has the responsibility to analyze any {@link IJavaElement}.
 * 
 * @author Pham Van Dong
 * 
 * @see IJavaElement
 * 
 */
public class AnalyzerUtil
{

	private AnalyzerUtil()
	{
	}

	/**
	 * Analyzes the specified java element.
	 * 
	 * @param javaElement
	 *            the specified java element.
	 * @throws NullPointerException
	 *             if the javaElement is null.
	 * @throws JavaModelException
	 */
	public static void analyze(final IJavaElement javaElement)
		throws NullPointerException, JavaModelException
	{
		ValidatorUtil.checkNotNull(javaElement);

		final String name = javaElement.getElementName();
		final String jobName = "Finding problems in '" + name + "' " +
			ASTUtil.getJavaElementTypeName(javaElement);

		final Job job = new Job(jobName)
		{

			@Override
			protected IStatus run(IProgressMonitor monitor)
			{
				try
				{
					final Counter counter = new Counter();

					final int nJavaFiles = ResourceUtil.getNumberOfJavaFiles(javaElement);
					monitor.beginTask("Parsing...", nJavaFiles);
					analyzeJavaElement(javaElement, monitor, counter);
					monitor.done();

					final String report = "[Found: " + counter.count + " problem" +
						((counter.count > 1) ? "s" : "") + "]";
					return new Status(IStatus.OK, Activator.PLUGIN_ID, report);
				}
				catch (final JavaModelException e)
				{
					e.printStackTrace();
				}
				catch (final NullPointerException e)
				{
					e.printStackTrace();
				}
				catch (final SecurityException e)
				{
					e.printStackTrace();
				}
				catch (final IllegalArgumentException e)
				{
					e.printStackTrace();
				}
				catch (final NoSuchMethodException e)
				{
					e.printStackTrace();
				}
				catch (final InstantiationException e)
				{
					e.printStackTrace();
				}
				catch (final IllegalAccessException e)
				{
					e.printStackTrace();
				}
				catch (final InvocationTargetException e)
				{
					e.printStackTrace();
				}

				return Status.CANCEL_STATUS;
			}

		};

		job.setProperty(IProgressConstants.KEEP_PROPERTY, true);
		job.schedule();
	}

	private static void analyzeJavaElement(
		IJavaElement javaElement,
		IProgressMonitor monitor,
		Counter counter)
		throws
		NullPointerException, JavaModelException, SecurityException,
		IllegalArgumentException, NoSuchMethodException, InstantiationException,
		IllegalAccessException, InvocationTargetException
	{
		ValidatorUtil.checkNotNull(javaElement);

		if (javaElement instanceof IJavaProject)
		{
			final IJavaProject javaProject = (IJavaProject)javaElement;
			analyzeJavaProject(javaProject, monitor, counter);
		}
		else if (javaElement instanceof IPackageFragment)
		{
			final IPackageFragment packageFragment = (IPackageFragment)javaElement;
			analyzePackageFragment(packageFragment, monitor, counter);
		}
		else if (javaElement instanceof ICompilationUnit)
		{
			final ICompilationUnit icu = (ICompilationUnit)javaElement;
			analyzeCompilationUnit(icu, monitor, counter);
		}
	}

	private static void analyzeJavaProject(
		IJavaProject javaProject,
		IProgressMonitor monitor,
		Counter counter)
		throws
		NullPointerException, JavaModelException, NoSuchMethodException,
		InstantiationException, IllegalAccessException, InvocationTargetException
	{
		ValidatorUtil.checkNotNull(javaProject);

		for (final IPackageFragment packageFragment : javaProject.getPackageFragments())
		{
			analyzePackageFragment(packageFragment, monitor, counter);
		}
	}

	private static void analyzePackageFragment(
		IPackageFragment packageFragment,
		IProgressMonitor monitor,
		Counter counter)
		throws
		JavaModelException, NoSuchMethodException, InstantiationException,
		IllegalAccessException, InvocationTargetException, NullPointerException
	{
		ValidatorUtil.checkNotNull(packageFragment);

		for (final ICompilationUnit icu : packageFragment.getCompilationUnits())
		{
			analyzeCompilationUnit(icu, monitor, counter);
		}
	}

	private static void analyzeCompilationUnit(
		ICompilationUnit icu,
		IProgressMonitor monitor,
		Counter counter)
		throws
		NullPointerException, SecurityException, IllegalArgumentException,
		NoSuchMethodException, InstantiationException, IllegalAccessException,
		InvocationTargetException
	{
		ValidatorUtil.checkNotNull(icu, monitor);

		final CompilationUnit compilationUnit =
			ASTUtil.createCompilationUnit(icu, new NullProgressMonitor());
		final ASTNodeCollector collector = new ASTNodeCollector(compilationUnit);

		for (final RuleType type : RuleType.values())
		{
			try
			{
				final RuleAnalyzer analyzer =
					RuleAnalyzerFactory.getInstance().newAnalyzer(type, collector);
				final List<ProblemItem> items = analyzer.analyze();
				if (items != null)
				{
					UIUtil.createMarkers(items, compilationUnit);
					counter.count += items.size();
				}
			}
			catch (final Exception e)
			{
				e.printStackTrace();
			}
		}

		final String report = "[Found: " + counter.count + " problem" +
			((counter.count > 1) ? "s" : "") + "]";
		monitor.subTask(report);
		monitor.worked(1);
	}

	private static class Counter
	{
		private int count;

		private Counter(int count)
		{
			this.count = count;
		}

		private Counter()
		{
			this(0);
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

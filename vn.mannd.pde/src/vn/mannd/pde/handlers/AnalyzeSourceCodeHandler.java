package vn.mannd.pde.handlers;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

import vn.mannd.pde.PluginConstants;
import vn.mannd.pde.loader.ConfigurationLoader;
import vn.mannd.pde.util.AnalyzerUtil;
import vn.mannd.pde.util.UIUtil;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class AnalyzeSourceCodeHandler extends AbstractHandler
{
	/**
	 * The constructor.
	 */
	public AnalyzeSourceCodeHandler()
	{
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException
	{
		ConfigurationLoader.silentlyLoad();
		analyze(event);
		return null;
	}

	private void analyze(ExecutionEvent event)
	{
		final List<IJavaElement> javaElements =
			UIUtil.getCurrentSelectedJavaElements(event);
		if (javaElements == null)
		{
			return;
		}

		final IJavaElement failElement =
			firstJavaElementContainsAnyErrors(javaElements);
		if (failElement != null)
		{
			final String title = "Error";

			String prefixMessage = "";
			if (failElement instanceof IJavaProject)
			{
				prefixMessage = "Project";
			}
			else if (failElement instanceof IPackageFragment)
			{
				prefixMessage = "Package";
			}
			else if (failElement instanceof ICompilationUnit)
			{
				prefixMessage = "Compilation Unit";
			}
			final String message =
				prefixMessage + " '" + failElement.getElementName() +
					"' has error(s). Please try again later!";
			UIUtil.showMessageBox(event, title, message, SWT.ICON_ERROR);

			return;
		}

		for (final IJavaElement javaElement : javaElements)
		{
			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run()
				{
					UIUtil.deleteMarkers(javaElement, PluginConstants.MARKER_ID);
					try
					{
						AnalyzerUtil.analyze(javaElement);
					}
					catch (final NullPointerException e)
					{
						e.printStackTrace();
					}
					catch (final JavaModelException e)
					{
						e.printStackTrace();
					}
				}

			});
		}
	}

	private IJavaElement firstJavaElementContainsAnyErrors(
		List<IJavaElement> javaElements)
	{
		for (final IJavaElement javaElement : javaElements)
		{
			if (UIUtil.containsAnyErrors(javaElement))
			{
				return javaElement;
			}
		}
		return null;
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}
}

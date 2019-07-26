package vn.mannd.pde.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

import vn.mannd.pde.Activator;

/**
 * This class has the responsibility to handle resource.
 * 
 * @author Pham Van Dong
 * 
 * @see IResource
 * 
 */
public class ResourceUtil
{

	private ResourceUtil()
	{
	}

	/**
	 * Returns the current workspace.
	 * 
	 * @return the current workspace.
	 */
	public static IWorkspace getWorkspace()
	{
		return ResourcesPlugin.getWorkspace();
	}

	/**
	 * Returns the current workspace root.
	 * 
	 * @return the current workspace root.
	 */
	public static IWorkspaceRoot getWorkspaceRoot()
	{
		return getWorkspace().getRoot();
	}

	/**
	 * Gets the compilation unit of the specified file.
	 * 
	 * @param file
	 *            the specified file.
	 * @return the compilation unit of the specified file.
	 * @throws NullPointerException
	 *             if the speicified file is null.
	 */
	public static ICompilationUnit getCompilationUnit(IFile file)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(file);

		final IJavaElement element = JavaCore.create(file);
		final ICompilationUnit result = TypeUtil.cast(element);
		return result;
	}

	/**
	 * Counts number of java files in the specified java element.
	 * 
	 * @param javaElement
	 *            the specified java element.
	 * @return number of java files in the specified java element.
	 * @throws JavaModelException
	 * @throws NullPointerException
	 *             if the specified java element is null.
	 */
	public static int getNumberOfJavaFiles(IJavaElement javaElement)
		throws JavaModelException, NullPointerException
	{
		ValidatorUtil.checkNotNull(javaElement);

		if (javaElement instanceof IJavaProject)
		{
			final IJavaProject javaProject = (IJavaProject)javaElement;

			int count = 0;
			for (final IPackageFragment pack : javaProject.getPackageFragments())
			{
				count += pack.getCompilationUnits().length;
			}
			return count;
		}

		if (javaElement instanceof IPackageFragment)
		{
			final IPackageFragment fragment = (IPackageFragment)javaElement;
			return fragment.getCompilationUnits().length;
		}

		if (javaElement instanceof ICompilationUnit)
		{
			return 1;
		}

		return 0;
	}

	/**
	 * Counts number of java files in the specified java elements list.
	 * 
	 * @param javaElements
	 *            the list of java elements.
	 * @return number of java files in the specified java elements list.
	 * @throws JavaModelException
	 * @throws NullPointerException
	 *             if the specified java elements list is null.
	 */
	public static int getNumberOfJavaFiles(List<? extends IJavaElement> javaElements)
		throws JavaModelException, NullPointerException
	{
		ValidatorUtil.checkNotNull(javaElements);

		int count = 0;
		for (final IJavaElement javaElement : javaElements)
		{
			count += getNumberOfJavaFiles(javaElement);
		}
		return count;
	}

	/**
	 * Gets the input stream of internal plug-in's resource.
	 * 
	 * @param pluginName
	 *            the plugin's name
	 * @param filePath
	 *            the file path.
	 * @return the input stream of internal plug-in's resource.
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws NullPointerException
	 *             if the plugin's name or file path is null.
	 */
	public static InputStream getInputStream(String pluginName, String filePath)
		throws URISyntaxException, IOException, NullPointerException
	{
		ValidatorUtil.checkNotNull(pluginName, filePath);

		final String url = "platform:/plugin/" + pluginName + "/" + filePath;
		final URL fileURL = new URL(url);
		final InputStream inputStream = fileURL.openConnection().getInputStream();
		return inputStream;
	}

	/**
	 * Gets the input stream of internal of this plug-in's resource.
	 * 
	 * @param filePath
	 *            the file path.
	 * @return the input stream of internal of this plug-in's resource.
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws NullPointerException
	 *             if the file path is null.
	 */
	public static InputStream getInputStream(String filePath)
		throws URISyntaxException, IOException, NullPointerException
	{
		ValidatorUtil.checkNotNull(filePath);
		return getInputStream(Activator.PLUGIN_ID, filePath);
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

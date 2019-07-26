package vn.dongpv.pde.handlers;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.IJavaElement;

import vn.dongpv.pde.PluginConstants;
import vn.dongpv.pde.loader.ConfigurationLoader;
import vn.dongpv.pde.util.UIUtil;

public class RemoveAllProblemMarkersHandler extends AbstractHandler
{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException
	{
		ConfigurationLoader.silentlyLoad();
		removeAllProblemMarkers(event);
		return null;
	}

	private void removeAllProblemMarkers(ExecutionEvent event)
	{
		final List<IJavaElement> javaElements =
			UIUtil.getCurrentSelectedJavaElements(event);
		if (javaElements == null)
		{
			return;
		}

		for (final IJavaElement javaProject : javaElements)
		{
			UIUtil.deleteMarkers(javaProject, PluginConstants.MARKER_ID);
		}
	}

	private final void readObject(java.io.ObjectInputStream in)
		throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

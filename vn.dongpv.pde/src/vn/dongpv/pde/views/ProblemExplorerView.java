package vn.dongpv.pde.views;

import org.eclipse.ui.views.markers.MarkerSupportView;

import vn.dongpv.pde.PluginConstants;

public class ProblemExplorerView extends MarkerSupportView
{

	public ProblemExplorerView()
	{
		super(PluginConstants.MARKER_GENERATOR_ID);
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

package vn.dongpv.pde.markers;

import org.eclipse.core.resources.IMarker;
import org.eclipse.ui.views.markers.MarkerField;
import org.eclipse.ui.views.markers.MarkerItem;

import vn.dongpv.pde.PluginConstants;

public class CategoryMarkerField extends MarkerField
{

	@Override
	public String getValue(MarkerItem item)
	{
		final IMarker marker = item.getMarker();
		if (marker == null)
		{
			return PluginConstants.EMPTY_STRING;
		}

		final String category = marker.getAttribute(
			PluginConstants.MARKER_CATEGORY, PluginConstants.UNKNOWN_STRING);

		return category;
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

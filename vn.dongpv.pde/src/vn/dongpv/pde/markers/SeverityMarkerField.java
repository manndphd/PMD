package vn.dongpv.pde.markers;

import org.eclipse.core.resources.IMarker;
import org.eclipse.ui.views.markers.MarkerField;
import org.eclipse.ui.views.markers.MarkerItem;

import vn.dongpv.pde.PluginConstants;

public class SeverityMarkerField extends MarkerField
{

	@Override
	public String getValue(MarkerItem item)
	{
		final IMarker marker = item.getMarker();
		if (marker == null)
		{
			return PluginConstants.EMPTY_STRING;
		}

		final String severity = marker.getAttribute(
			PluginConstants.MARKER_SEVERITY, PluginConstants.UNKNOWN_STRING);

		return severity;
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

package vn.dongpv.pde.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PartInitException;

import vn.dongpv.pde.util.UIUtil;

public class ViewMoreInfoHandler extends AbstractHandler
{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException
	{
		try
		{
			UIUtil.showProblemInfoView(event);
		}
		catch (final PartInitException e)
		{
			e.printStackTrace();
		}
		catch (final NullPointerException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

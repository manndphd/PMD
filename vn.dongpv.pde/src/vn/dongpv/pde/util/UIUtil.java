package vn.dongpv.pde.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import vn.dongpv.pde.PluginConstants;
import vn.dongpv.pde.loader.configuration.management.RuleDescriptorStore;
import vn.dongpv.pde.rule.ProblemItem;
import vn.dongpv.pde.rule.analyzer.core.RuleDescriptor;
import vn.dongpv.pde.rule.analyzer.core.RuleType;
import vn.dongpv.pde.views.ProblemInfoView;

public class UIUtil
{

	private UIUtil()
	{
	}

	/**
	 * Gets the current selection.
	 * 
	 * @param event
	 *            the event.
	 * @return
	 * @throws NullPointerException
	 *             if the event is null.
	 */
	public static ISelection getCurrentSelection(ExecutionEvent event)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(event);
		return HandlerUtil.getCurrentSelection(event);
	}

	/**
	 * Gets the current selected java elements.
	 * 
	 * @param event
	 *            the event.
	 * @return
	 * @throws NullPointerException
	 *             if the event is null.
	 */
	public static List<IJavaElement> getCurrentSelectedJavaElements(
		ExecutionEvent event)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(event);

		final ISelection selection = getCurrentSelection(event);
		if (selection instanceof IStructuredSelection)
		{
			final int DEFAULT_CAPACITY = 5;
			final List<IJavaElement> result =
				new ArrayList<IJavaElement>(DEFAULT_CAPACITY);

			final IStructuredSelection iss = (IStructuredSelection)selection;
			final Object[] elements = iss.toArray();
			for (final Object element : elements)
			{
				if (element instanceof IJavaElement)
				{
					final IJavaElement javaElement = (IJavaElement)element;
					result.add(javaElement);
				}
			}
			return result;
		}
		return null;
	}

	/**
	 * Gets the current selected java projects.
	 * 
	 * @param event
	 *            the event.
	 * @return
	 * @throws NullPointerException
	 *             if the event is null.
	 */
	public static List<IJavaProject> getCurrentSelectedJavaProjects(
		ExecutionEvent event)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(event);

		final ISelection selection = getCurrentSelection(event);
		if (selection instanceof IStructuredSelection)
		{
			final int DEFAULT_CAPACITY = 5;
			final List<IJavaProject> result =
				new ArrayList<IJavaProject>(DEFAULT_CAPACITY);

			final IStructuredSelection iss = (IStructuredSelection)selection;
			final Object[] elements = iss.toArray();
			for (final Object element : elements)
			{
				if (element instanceof IJavaProject)
				{
					final IJavaProject javaProject = (IJavaProject)element;
					result.add(javaProject);
				}
			}
			return result;
		}
		return null;
	}

	/**
	 * Opens a view.
	 * 
	 * @param event
	 *            the event.
	 * @param viewId
	 *            the id of a view that will be opened.
	 * @return the view that will be opened.
	 * @throws PartInitException
	 * @throws ExecutionException
	 * @throws NullPointerException
	 *             if the event or view id is null.
	 */
	public static IViewPart showView(ExecutionEvent event, String viewId)
		throws PartInitException, ExecutionException, NullPointerException
	{
		ValidatorUtil.checkNotNull(event, viewId);

		final IViewPart view = HandlerUtil.getActiveWorkbenchWindowChecked(event).
			getActivePage().showView(viewId);
		return view;
	}

	/**
	 * Opens the problem inforamtion view.
	 * 
	 * @param event
	 *            the event.
	 * @throws NullPointerException
	 *             if the event is null.
	 * @throws PartInitException
	 * @throws ExecutionException
	 */
	public static void showProblemInfoView(ExecutionEvent event)
		throws NullPointerException, PartInitException, ExecutionException
	{
		ValidatorUtil.checkNotNull(event);

		final ISelection selection = getCurrentSelection(event);
		final IViewPart view = showView(event, PluginConstants.INFO_VIEW_ID);
		if (view instanceof ProblemInfoView)
		{
			final ProblemInfoView infoView = (ProblemInfoView)view;
			infoView.updateView(selection);
		}
	}

	/**
	 * Gets the active shell.
	 * 
	 * @param event
	 *            the event.
	 * @return
	 * @throws NullPointerException
	 *             if the event is null.
	 */
	public static Shell getShell(ExecutionEvent event)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(event);
		return HandlerUtil.getActiveShell(event);
	}

	/**
	 * Creates a marker from the specified problem item.
	 * 
	 * @param item
	 *            the specified problem item.
	 * @param compilationUnit
	 *            the compilation unit.
	 * @throws NullPointerException
	 *             if the problem item is null.
	 */
	public static void createMarker(
		ProblemItem item,
		CompilationUnit compilationUnit)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(item);

		final IResource resource = item.getResource();
		try
		{
			final IMarker marker = resource.createMarker(PluginConstants.MARKER_ID);
			marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_INFO);
			marker.setAttribute(IMarker.MESSAGE, item.getDescription());
			marker.setAttribute(IMarker.CHAR_START, item.getStartPosition());
			marker.setAttribute(IMarker.CHAR_END,
				item.getStartPosition() + item.getLength());
			marker.setAttribute(PluginConstants.MARKER_NODE_TYPE,
				item.getNodeType());
			marker.setAttribute(PluginConstants.MARKER_RULE_TYPE,
				item.getRuleType().toString());
			marker.setAttribute(PluginConstants.MARKER_CHECKER_TYPE,
				item.getCheckerType().toString());
			marker.setAttribute(PluginConstants.MARKER_CATEGORY,
				item.getCategory());
			marker.setAttribute(PluginConstants.MARKER_SEVERITY,
				item.getSeverity());

			if (compilationUnit != null)
			{
				final int lineNumber = compilationUnit.
					getLineNumber(item.getStartPosition());
				marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
			}
			else if (resource instanceof IFile)
			{
				final IFile file = (IFile)resource;
				final ICompilationUnit icu = ResourceUtil.getCompilationUnit(file);
				if (icu != null)
				{
					final CompilationUnit cu = ASTUtil.createCompilationUnit(
						icu, new NullProgressMonitor());
					final int lineNumber = cu.getLineNumber(item.getStartPosition());
					marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
				}
			}
		}
		catch (final CoreException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Creates a marker from the specified problem item.
	 * 
	 * @param item
	 *            the specified problem item.
	 * @throws NullPointerException
	 *             if the problem item is null.
	 */
	public static void createMarker(ProblemItem item)
		throws NullPointerException
	{
		createMarker(item, null);
	}

	/**
	 * Creates markers from the specified list of problem items.
	 * 
	 * @param items
	 *            the specified list of problem items.
	 * @param compilationUnit
	 *            the compilation unit.
	 * @throws NullPointerException
	 *             if the specified list of problem items is null.
	 */
	public static void createMarkers(
		List<ProblemItem> items,
		CompilationUnit compilationUnit)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(items);

		for (final ProblemItem item : items)
		{
			createMarker(item, compilationUnit);
		}
	}

	/**
	 * Creates markers from the specified list of problem items.
	 * 
	 * @param items
	 *            the specified list of problem items.
	 * @throws NullPointerException
	 *             if the specified list of problem items is null.
	 */
	public static void createMarkers(List<ProblemItem> items)
		throws NullPointerException
	{
		createMarkers(items, null);
	}

	/**
	 * Deletes markers of the specified resource.
	 * 
	 * @param resource
	 *            the resource whose markers will be deleted.
	 * @param markerType
	 *            the marker type.
	 * @throws NullPointerException
	 *             if the resource is null.
	 */
	public static void deleteMarkers(IResource resource, String markerType)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(resource);

		try
		{
			resource.deleteMarkers(markerType, true, IResource.DEPTH_INFINITE);
		}
		catch (final CoreException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Deletes markers of the specified java element.
	 * 
	 * @param javaElement
	 *            the specified java element.
	 * @param markerType
	 *            the marker type.
	 * @throws NullPointerException
	 *             if the specified java element is null.
	 */
	public static void deleteMarkers(IJavaElement javaElement, String markerType)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(javaElement);
		deleteMarkers(javaElement.getResource(), markerType);
	}

	/**
	 * Gets the rule descriptor from the specified marker.
	 * 
	 * @param marker
	 *            the specified marker which contains the rule type id.
	 * @return the rule descriptor.
	 * @throws NullPointerException
	 *             if the marker is null.
	 * @throws CoreException
	 */
	public static RuleDescriptor getRuleDescriptor(IMarker marker)
		throws NullPointerException, CoreException
	{
		ValidatorUtil.checkNotNull(marker);

		if (marker.getType().equalsIgnoreCase(PluginConstants.MARKER_ID))
		{
			final RuleType type = RuleType.valueOf(marker.getAttribute(
				PluginConstants.MARKER_RULE_TYPE,
				RuleType.Null.toString()));
			return RuleDescriptorStore.getInstance().getRuleDescriptor(type);
		}

		return null;
	}

	/**
	 * Checks the specified resource contains any errors or not.
	 * 
	 * @param resource
	 *            the specified resource.
	 * @return true if the specified resource contains any errors.
	 * @throws NullPointerException
	 *             if the specified resource is null.
	 */
	public static boolean containsAnyErrors(IResource resource)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(resource);

		try
		{
			final IMarker[] markers = resource.findMarkers(
				IMarker.PROBLEM, true, IResource.DEPTH_INFINITE);
			for (final IMarker marker : markers)
			{
				final int severity = marker.getAttribute(
					IMarker.SEVERITY, IMarker.SEVERITY_INFO);
				if (severity == IMarker.SEVERITY_ERROR)
				{
					return true;
				}
			}
		}
		catch (final CoreException e)
		{
			e.printStackTrace();
		}

		return false;
	}

	/**
	 * Checks the specified java element contains any errors or not.
	 * 
	 * @param javaElement
	 *            the specified java element.
	 * @return true if the specified java element contains any errors.
	 * @throws NullPointerException
	 *             if the specified java element is null.
	 */
	public static boolean containsAnyErrors(IJavaElement javaElement)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(javaElement);
		return containsAnyErrors(javaElement.getResource());
	}

	/**
	 * Shows a message box.
	 * 
	 * @param event
	 *            the event.
	 * @param title
	 *            the title of the message box.
	 * @param message
	 *            the text message.
	 * @param style
	 *            the style of the message box.
	 * @return the ID of the button that was selected to dismiss the message box
	 *         (e.g. SWT.OK, SWT.CANCEL, etc.)
	 * @throws NullPointerException
	 *             if the event, title or message is null.
	 */
	public static int showMessageBox(
		ExecutionEvent event, String title, String message, int style)
		throws NullPointerException
	{
		ValidatorUtil.checkNotNull(event, title, message);

		final MessageBox box = new MessageBox(getShell(event), style);
		box.setMessage(message);
		box.setText(title);
		final int returnId = box.open();

		return returnId;
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

package vn.dongpv.pde.views;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.markers.MarkerItem;

import vn.dongpv.pde.loader.ConfigurationLoader;
import vn.dongpv.pde.rule.analyzer.core.RuleDescriptor;
import vn.dongpv.pde.util.UIUtil;

public class ProblemInfoView extends ViewPart
{

	private static final String PROBLEM_ID_TITLE = "Problem ID: ";
	private static final String CATEGORY_TITLE = "Category: ";
	private static final String SEVERITY_TITLE = "Severity: ";
	private static final String REFACTORING_SUPPORT_TITLE = "Refactoring Support: ";
	private static final String DESCRIPTION_TITLE = "Description: ";
	private static final String REASON_TITLE = "Reason: ";
	private static final String USAGE_EXAMPLE_TITLE = "Usage example: ";

	private static final int LEFT_MARGIN = 10;
	private static final int RIGHT_MARGIN = 10;
	private static final int TOP_MARGIN = 10;
	private static final int BOTTOM_MARGIN = 10;

	private StyledText text;
	private ISelectionListener listener;

	public ProblemInfoView()
	{
	}

	@Override
	public void createPartControl(Composite parent)
	{
		createStyledText(parent);
		createSelectionListener();
		addSelectionListener();
	}

	@Override
	public void setFocus()
	{
		text.setFocus();
	}

	@Override
	public void dispose()
	{
		removeSelectionListener();
		super.dispose();
	}

	private void createStyledText(Composite parent)
	{
		text = new StyledText(parent, SWT.V_SCROLL | SWT.H_SCROLL);
		text.setEditable(false);
		text.setMargins(LEFT_MARGIN, TOP_MARGIN, RIGHT_MARGIN, BOTTOM_MARGIN);
	}

	private void createSelectionListener()
	{
		listener = new ISelectionListener() {

			@Override
			public void selectionChanged(IWorkbenchPart part, ISelection selection)
			{
				updateView(selection);
			}

		};
	}

	public void updateView(ISelection selection)
	{
		if (!(selection instanceof IStructuredSelection))
		{
			return;
		}

		final IStructuredSelection iss = (IStructuredSelection)selection;
		final Object[] objects = iss.toArray();
		if ((objects != null) &&
			(objects.length == 1) &&
			(objects[0] instanceof MarkerItem))
		{
			final IMarker marker = ((MarkerItem)objects[0]).getMarker();
			if (marker != null)
			{
				try
				{
					fetchInfo(marker);
				}
				catch (final NullPointerException e)
				{
					e.printStackTrace();
				}
				catch (final CoreException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	private void fetchInfo(IMarker marker)
		throws CoreException, NullPointerException
	{
		final RuleDescriptor descriptor = UIUtil.getRuleDescriptor(marker);
		if (descriptor == null)
		{
			return;
		}

		final String type = descriptor.getType().toString();
		final String category = descriptor.getCategory();
		final String severity = descriptor.getSeverity();
		final String refactoringSupport = descriptor.getRefactoringSupport();
		final String description = descriptor.getDescription();
		final String reason = descriptor.getReason();
		final String usageExample = descriptor.getUsageExample();

		drawContent(type, category, severity,
			refactoringSupport, description, reason, usageExample);

		decorateText(type, category, severity, refactoringSupport,
			description, reason, usageExample);
	}

	private void drawContent(
		String type,
		String category,
		String severity,
		String refactoringSupport,
		String description,
		String reason,
		String usageExample)
	{
		final StringBuilder builder = new StringBuilder();

		if (type.length() > 0)
		{
			builder.append(PROBLEM_ID_TITLE);
			builder.append(type);
		}

		if (category.length() > 0)
		{
			builder.append('\n');
			builder.append(CATEGORY_TITLE);
			builder.append(category);
		}

		if (severity.length() > 0)
		{
			builder.append('\n');
			builder.append(SEVERITY_TITLE);
			builder.append(severity);
		}

		if (refactoringSupport.length() > 0)
		{
			builder.append('\n');
			builder.append(REFACTORING_SUPPORT_TITLE);
			builder.append(refactoringSupport);
		}

		if (description.length() > 0)
		{
			builder.append('\n');
			builder.append('\n');
			builder.append(DESCRIPTION_TITLE);
			builder.append('\n');
			builder.append(description);
		}

		if (reason.length() > 0)
		{
			builder.append('\n');
			builder.append('\n');
			builder.append(REASON_TITLE);
			builder.append('\n');
			builder.append(reason);
		}

		if (usageExample.length() > 0)
		{
			builder.append('\n');
			builder.append('\n');
			builder.append(USAGE_EXAMPLE_TITLE);
			builder.append('\n');
			builder.append(usageExample);
		}

		text.setText(builder.toString());
	}

	private void decorateText(
		String type,
		String category,
		String severity,
		String refactoringSupport,
		String description,
		String reason,
		String usageExample)
	{
		StyleRange style = new StyleRange();
		style.start = 0;
		style.length = PROBLEM_ID_TITLE.length();
		style.fontStyle = SWT.BOLD;
		text.setStyleRange(style);

		int currPos = 0;
		currPos += PROBLEM_ID_TITLE.length() + type.length() + 1;

		if (category.length() > 0)
		{
			style = new StyleRange();
			style.start = currPos;
			style.length = CATEGORY_TITLE.length();
			style.fontStyle = SWT.BOLD;
			text.setStyleRange(style);

			currPos += CATEGORY_TITLE.length() + category.length() + 1;
		}

		if (severity.length() > 0)
		{
			style = new StyleRange();
			style.start = currPos;
			style.length = SEVERITY_TITLE.length();
			style.fontStyle = SWT.BOLD;
			text.setStyleRange(style);

			currPos += SEVERITY_TITLE.length() + severity.length() + 1;
		}

		if (refactoringSupport.length() > 0)
		{
			style = new StyleRange();
			style.start = currPos;
			style.length = REFACTORING_SUPPORT_TITLE.length();
			style.fontStyle = SWT.BOLD;
			text.setStyleRange(style);

			currPos += REFACTORING_SUPPORT_TITLE.length() + refactoringSupport.length() + 2;
		}

		if (description.length() > 0)
		{
			style = new StyleRange();
			style.start = currPos;
			style.length = DESCRIPTION_TITLE.length();
			style.fontStyle = SWT.BOLD;
			text.setStyleRange(style);

			currPos += DESCRIPTION_TITLE.length() + description.length() + 2;
		}

		if (reason.length() > 0)
		{
			style = new StyleRange();
			style.start = currPos;
			style.length = REASON_TITLE.length();
			style.fontStyle = SWT.BOLD;
			text.setStyleRange(style);

			currPos += REASON_TITLE.length() + reason.length() + 2;
		}

		if (usageExample.length() > 0)
		{
			style = new StyleRange();
			style.start = currPos;
			style.length = USAGE_EXAMPLE_TITLE.length();
			style.fontStyle = SWT.BOLD;
			text.setStyleRange(style);
		}
	}

	private void addSelectionListener()
	{
		ConfigurationLoader.silentlyLoad();
		getSite().getWorkbenchWindow().
			getSelectionService().addSelectionListener(listener);
	}

	private void removeSelectionListener()
	{
		getSite().getWorkbenchWindow().
			getSelectionService().removeSelectionListener(listener);
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}
}

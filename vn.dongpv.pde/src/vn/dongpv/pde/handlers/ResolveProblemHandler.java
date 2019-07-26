package vn.dongpv.pde.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ltk.ui.refactoring.RefactoringWizardOpenOperation;
import org.eclipse.swt.SWT;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.views.markers.MarkerItem;

import vn.dongpv.pde.PluginConstants;
import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.loader.ConfigurationLoader;
import vn.dongpv.pde.loader.configuration.management.ProblemRefactoringKitMappingStore;
import vn.dongpv.pde.loader.configuration.management.ProblemRefactoringKitStore;
import vn.dongpv.pde.refactoring.core.ProblemRefactoring;
import vn.dongpv.pde.refactoring.core.ProblemRefactoringKit;
import vn.dongpv.pde.refactoring.core.ProblemRefactoringKitType;
import vn.dongpv.pde.refactoring.core.ProblemRefactoringWizard;
import vn.dongpv.pde.rule.analyzer.core.RuleType;
import vn.dongpv.pde.rule.checker.core.ConditionalCheckerType;
import vn.dongpv.pde.util.ASTUtil;
import vn.dongpv.pde.util.ResourceUtil;
import vn.dongpv.pde.util.UIUtil;

public class ResolveProblemHandler extends AbstractHandler
{

	public static final String TITLE_NO_REFACTOR = "Oops, sorry!";
	public static final String MSG_NO_REFACTOR = "The refactoring of this problem is currently not supported yet. Would you like to read more information about this problem?";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException
	{
		ConfigurationLoader.silentlyLoad();
		silentlyResolve(event);
		return null;
	}

	private static void silentlyResolve(ExecutionEvent event)
	{
		try
		{
			resolve(event);
		}
		catch (final NullPointerException e)
		{
			e.printStackTrace();
		}
		catch (final CoreException e)
		{
			e.printStackTrace();
		}
		catch (final InterruptedException e)
		{
			e.printStackTrace();
		}
		catch (final ExecutionException e)
		{
			e.printStackTrace();
		}
	}

	private static void resolve(ExecutionEvent event)
		throws
		CoreException, NullPointerException,
		InterruptedException, ExecutionException
	{
		final ISelection selection = UIUtil.getCurrentSelection(event);
		if (selection instanceof IStructuredSelection)
		{
			final IStructuredSelection iss = (IStructuredSelection)selection;
			final Object[] objects = iss.toArray();
			for (final Object object : objects)
			{
				if (object instanceof MarkerItem)
				{
					final MarkerItem markerItem = (MarkerItem)object;
					final IMarker marker = markerItem.getMarker();

					if (marker == null)
					{
						return;
					}

					resolveProblemInMarker(event, marker);
				}
			}
		}
	}

	private static void resolveProblemInMarker(ExecutionEvent event, IMarker marker)
		throws
		CoreException, InterruptedException,
		NullPointerException, ExecutionException
	{
		// Accepts only plug-in's marker

		if (!marker.getType().equalsIgnoreCase(PluginConstants.MARKER_ID))
		{
			return;
		}

		// Analyzes marker to get all needed information

		final int start = marker.getAttribute(IMarker.CHAR_START, 0);
		final int end = marker.getAttribute(IMarker.CHAR_END, 0);
		final int problemNodeType = marker.getAttribute(PluginConstants.MARKER_NODE_TYPE, 0);

		final RuleType ruleType = RuleType.valueOf(marker.getAttribute(
			PluginConstants.MARKER_RULE_TYPE,
			RuleType.Null.toString()));

		final ConditionalCheckerType checkerType = ConditionalCheckerType.valueOf(
			marker.getAttribute(PluginConstants.MARKER_CHECKER_TYPE,
				ConditionalCheckerType.Null.toString()));

		final IResource resource = marker.getResource();
		if (resource instanceof IFile)
		{
			final IFile file = (IFile)resource;
			final ICompilationUnit icu = ResourceUtil.getCompilationUnit(file);
			if (icu != null)
			{
				final CompilationUnit compilationUnit = ASTUtil.createCompilationUnit(
					icu, new NullProgressMonitor());

				// The selected text is not an expected ASTNode

				final ASTNode problemNode = ASTUtil.getNode(compilationUnit, start, end);
				if ((problemNode == null) ||
					(problemNode.getNodeType() != problemNodeType))
				{
					return;
				}

				createAndOpenRefactoringWizard(event, marker, ruleType,
					checkerType, icu, compilationUnit, problemNode);
			}
		}
	}

	private static void createAndOpenRefactoringWizard(
		ExecutionEvent event,
		IMarker marker,
		RuleType ruleType,
		ConditionalCheckerType checkerType,
		ICompilationUnit icu,
		CompilationUnit compilationUnit,
		ASTNode problemNode)
		throws PartInitException, ExecutionException, InterruptedException
	{
		final ProblemRefactoringKitType kitType =
			ProblemRefactoringKitMappingStore.
				getInstance().getKitType(ruleType, checkerType);

		// Not support refactoring session

		if (kitType == null)
		{
			final int returnId = UIUtil.showMessageBox(
				event,
				TITLE_NO_REFACTOR,
				MSG_NO_REFACTOR,
				SWT.ICON_QUESTION | SWT.YES | SWT.NO);

			if (returnId == SWT.YES)
			{
				// Show more information
				UIUtil.showProblemInfoView(event);
			}
			return;
		}

		final ProblemRefactoringKit kit =
			ProblemRefactoringKitStore.
				getInstance().getKit(kitType);

		if (kit == null)
		{
			return;
		}

		// Prepares the parameter that will be passed to problem
		// refactoring object

		final CompositeParameter parameter = new CompositeParameter();
		parameter.addParameter(ProblemRefactoring.I_COMPILATION_UNIT, icu);
		parameter.addParameter(ProblemRefactoring.PARSED_ROOT_NODE, compilationUnit);
		parameter.addParameter(ProblemRefactoring.PARSED_NODE, problemNode);
		parameter.addParameter(ProblemRefactoring.REFACTORING_KIT, kit);

		// Creates refactoring object and open the wizard

		final ProblemRefactoring refactoring = new ProblemRefactoring(parameter);
		final ProblemRefactoringWizard wizard = new ProblemRefactoringWizard(refactoring, marker);
		final RefactoringWizardOpenOperation op = new RefactoringWizardOpenOperation(wizard);
		op.run(UIUtil.getShell(event), ruleType.toString());
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}
}

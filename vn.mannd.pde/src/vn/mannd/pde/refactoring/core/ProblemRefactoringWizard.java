package vn.mannd.pde.refactoring.core;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ltk.ui.refactoring.RefactoringWizard;

/**
 * The wizard will be shown for refactoring session.
 * 
 * @author Pham Van Dong
 * 
 */
public class ProblemRefactoringWizard extends RefactoringWizard
{

	private final IMarker marker;

	/**
	 * Constructs a wizard.
	 * 
	 * @param refactoring
	 *            the {@link ProblemRefactoring} object.
	 * @param marker
	 *            the marker that provides all needed information.
	 */
	public ProblemRefactoringWizard(ProblemRefactoring refactoring, IMarker marker)
	{
		super(refactoring, WIZARD_BASED_USER_INTERFACE);
		this.marker = marker;
	}

	/**
	 * Constructs a wizard.
	 * 
	 * @param refactoring
	 *            the {@link ProblemRefactoring} object.
	 */
	public ProblemRefactoringWizard(ProblemRefactoring refactoring)
	{
		this(refactoring, null);
	}

	@Override
	protected void addUserInputPages()
	{
	}

	@Override
	public boolean performFinish()
	{
		if (marker != null)
		{
			try
			{
				// Deletes this marker when the refactoring session finishes.
				marker.delete();
			}
			catch (final CoreException e)
			{
				e.printStackTrace();
			}
		}
		return super.performFinish();
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

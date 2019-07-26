package vn.dongpv.pde.rule.checker.core;

import org.eclipse.jdt.core.dom.ASTVisitor;

/**
 * The visitor that collects information from an ASTNode, then gives the
 * diagnosis this ASTNode violates the condition or not.
 * 
 * @author Pham Van Dong
 * 
 * @see ConditionalChecker
 * 
 */
public abstract class ConditionalVisitor extends ASTVisitor
{

	public abstract boolean isViolated();

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

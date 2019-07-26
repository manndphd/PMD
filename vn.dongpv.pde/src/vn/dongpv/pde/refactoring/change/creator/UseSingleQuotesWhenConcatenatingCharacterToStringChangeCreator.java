package vn.dongpv.pde.refactoring.change.creator;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import vn.dongpv.pde.adt.CompositeParameter;
import vn.dongpv.pde.refactoring.change.creator.core.ChangeCreator;
import vn.dongpv.pde.util.ASTUtil;
import vn.dongpv.pde.util.TypeUtil;
import vn.dongpv.pde.util.ValidatorUtil;

public class UseSingleQuotesWhenConcatenatingCharacterToStringChangeCreator
	extends ChangeCreator
{

	public static final String NUMBER_OF_CHARACTERS = "NUMBER_OF_CHARACTERS";
	public static final String POSITION = "POSITION";

	private final ASTNode rootNode;
	private final InfixExpression infixExpression;
	private final int nChars;
	private final int pos;

	public UseSingleQuotesWhenConcatenatingCharacterToStringChangeCreator(
		CompositeParameter parameter)
		throws NullPointerException
	{
		super(parameter);

		rootNode = TypeUtil.cast(parameter.getParameter(PARSED_ROOT_NODE));
		infixExpression = TypeUtil.cast(parameter.getParameter(PARSED_NODE));
		nChars = TypeUtil.cast(parameter.getParameter(NUMBER_OF_CHARACTERS));
		pos = TypeUtil.cast(parameter.getParameter(POSITION));
	}

	@Override
	protected boolean checkParameters()
	{
		return ValidatorUtil.notNull(rootNode, infixExpression, nChars, pos);
	}

	@Override
	protected void createChange(ASTRewrite rewrite)
	{
		if (nChars == 1)
		{
			if (pos == 1)
			{
				replaceLeftOperand(rewrite);
			}
			else
			{
				replaceRightOperand(rewrite);
			}
		}
		else if (nChars == 2)
		{
			replaceLeftOperand(rewrite);
			replaceRightOperand(rewrite);
		}
	}

	private void replaceLeftOperand(ASTRewrite rewrite)
	{
		final StringLiteral literal = TypeUtil.cast(infixExpression.getLeftOperand());

		final CharacterLiteral charLiteral = ASTUtil.createCharacterLiteral(
			rootNode.getAST(), literal.getLiteralValue().charAt(0));

		rewrite.replace(infixExpression.getLeftOperand(), charLiteral, null);
	}

	private void replaceRightOperand(ASTRewrite rewrite)
	{
		final StringLiteral literal = TypeUtil.cast(infixExpression.getRightOperand());

		final CharacterLiteral charLiteral = ASTUtil.createCharacterLiteral(
			rootNode.getAST(), literal.getLiteralValue().charAt(0));

		rewrite.replace(infixExpression.getRightOperand(), charLiteral, null);
	}

	private final void readObject(java.io.ObjectInputStream in) throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

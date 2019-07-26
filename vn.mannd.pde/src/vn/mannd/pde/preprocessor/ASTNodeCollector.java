package vn.mannd.pde.preprocessor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.AnnotationTypeMemberDeclaration;
import org.eclipse.jdt.core.dom.AnonymousClassDeclaration;
import org.eclipse.jdt.core.dom.ArrayAccess;
import org.eclipse.jdt.core.dom.ArrayCreation;
import org.eclipse.jdt.core.dom.ArrayInitializer;
import org.eclipse.jdt.core.dom.ArrayType;
import org.eclipse.jdt.core.dom.AssertStatement;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BlockComment;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.CastExpression;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.ConstructorInvocation;
import org.eclipse.jdt.core.dom.ContinueStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.EnumConstantDeclaration;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.InstanceofExpression;
import org.eclipse.jdt.core.dom.Javadoc;
import org.eclipse.jdt.core.dom.LabeledStatement;
import org.eclipse.jdt.core.dom.LineComment;
import org.eclipse.jdt.core.dom.MarkerAnnotation;
import org.eclipse.jdt.core.dom.MemberRef;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.MethodRef;
import org.eclipse.jdt.core.dom.MethodRefParameter;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.ParameterizedType;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;
import org.eclipse.jdt.core.dom.PostfixExpression;
import org.eclipse.jdt.core.dom.PrefixExpression;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.QualifiedType;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.SuperConstructorInvocation;
import org.eclipse.jdt.core.dom.SuperFieldAccess;
import org.eclipse.jdt.core.dom.SuperMethodInvocation;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.SynchronizedStatement;
import org.eclipse.jdt.core.dom.TagElement;
import org.eclipse.jdt.core.dom.TextElement;
import org.eclipse.jdt.core.dom.ThisExpression;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.TypeLiteral;
import org.eclipse.jdt.core.dom.TypeParameter;
import org.eclipse.jdt.core.dom.UnionType;
import org.eclipse.jdt.core.dom.VariableDeclarationExpression;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.core.dom.WildcardType;

public class ASTNodeCollector
{

	private final ASTNode analyzedNode;
	private final Visitor visitor;

	public ASTNodeCollector(ASTNode analyzedNode)
	{
		this.analyzedNode = analyzedNode;
		this.visitor = new Visitor();
		this.analyzedNode.accept(visitor);
	}

	public ASTNode getAnalyzedNode()
	{
		return analyzedNode;
	}

	public List<AnnotationTypeDeclaration> getAnnotationTypeDeclarations()
	{
		return visitor.annotationTypeDeclarations;
	}

	public List<AnnotationTypeMemberDeclaration> getAnnotationTypeMemberDeclarations()
	{
		return visitor.annotationTypeMemberDeclarations;
	}

	public List<AnonymousClassDeclaration> getAnonymousClassDeclarations()
	{
		return visitor.anonymousClassDeclarations;
	}

	public List<ArrayAccess> getArrayAccesss()
	{
		return visitor.arrayAccesss;
	}

	public List<ArrayCreation> getArrayCreations()
	{
		return visitor.arrayCreations;
	}

	public List<ArrayInitializer> getArrayInitializers()
	{
		return visitor.arrayInitializers;
	}

	public List<ArrayType> getArrayTypes()
	{
		return visitor.arrayTypes;
	}

	public List<AssertStatement> getAssertStatements()
	{
		return visitor.assertStatements;
	}

	public List<Assignment> getAssignments()
	{
		return visitor.assignments;
	}

	public List<Block> getBlocks()
	{
		return visitor.blocks;
	}

	public List<BlockComment> getBlockComments()
	{
		return visitor.blockComments;
	}

	public List<BooleanLiteral> getBooleanLiterals()
	{
		return visitor.booleanLiterals;
	}

	public List<BreakStatement> getBreakStatements()
	{
		return visitor.breakStatements;
	}

	public List<CastExpression> getCastExpressions()
	{
		return visitor.castExpressions;
	}

	public List<CatchClause> getCatchClauses()
	{
		return visitor.catchClauses;
	}

	public List<CharacterLiteral> getCharacterLiterals()
	{
		return visitor.characterLiterals;
	}

	public List<ClassInstanceCreation> getClassInstanceCreations()
	{
		return visitor.classInstanceCreations;
	}

	public List<CompilationUnit> getCompilationUnits()
	{
		return visitor.compilationUnits;
	}

	public List<ConditionalExpression> getConditionalExpressions()
	{
		return visitor.conditionalExpressions;
	}

	public List<ConstructorInvocation> getConstructorInvocations()
	{
		return visitor.constructorInvocations;
	}

	public List<ContinueStatement> getContinueStatements()
	{
		return visitor.continueStatements;
	}

	public List<DoStatement> getDoStatements()
	{
		return visitor.doStatements;
	}

	public List<EmptyStatement> getEmptyStatements()
	{
		return visitor.emptyStatements;
	}

	public List<EnhancedForStatement> getEnhancedForStatements()
	{
		return visitor.enhancedForStatements;
	}

	public List<EnumConstantDeclaration> getEnumConstantDeclarations()
	{
		return visitor.enumConstantDeclarations;
	}

	public List<EnumDeclaration> getEnumDeclarations()
	{
		return visitor.enumDeclarations;
	}

	public List<ExpressionStatement> getExpressionStatements()
	{
		return visitor.expressionStatements;
	}

	public List<FieldAccess> getFieldAccesss()
	{
		return visitor.fieldAccesss;
	}

	public List<FieldDeclaration> getFieldDeclarations()
	{
		return visitor.fieldDeclarations;
	}

	public List<ForStatement> getForStatements()
	{
		return visitor.forStatements;
	}

	public List<IfStatement> getIfStatements()
	{
		return visitor.ifStatements;
	}

	public List<ImportDeclaration> getImportDeclarations()
	{
		return visitor.importDeclarations;
	}

	public List<InfixExpression> getInfixExpressions()
	{
		return visitor.infixExpressions;
	}

	public List<InstanceofExpression> getInstanceofExpressions()
	{
		return visitor.instanceofExpressions;
	}

	public List<Initializer> getInitializers()
	{
		return visitor.initializers;
	}

	public List<Javadoc> getJavadocs()
	{
		return visitor.javadocs;
	}

	public List<LabeledStatement> getLabeledStatements()
	{
		return visitor.labeledStatements;
	}

	public List<LineComment> getLineComments()
	{
		return visitor.lineComments;
	}

	public List<MarkerAnnotation> getMarkerAnnotations()
	{
		return visitor.markerAnnotations;
	}

	public List<MemberRef> getMemberRefs()
	{
		return visitor.memberRefs;
	}

	public List<MemberValuePair> getMemberValuePairs()
	{
		return visitor.memberValuePairs;
	}

	public List<MethodRef> getMethodRefs()
	{
		return visitor.methodRefs;
	}

	public List<MethodRefParameter> getMethodRefParameters()
	{
		return visitor.methodRefParameters;
	}

	public List<MethodDeclaration> getMethodDeclarations()
	{
		return visitor.methodDeclarations;
	}

	public List<MethodInvocation> getMethodInvocations()
	{
		return visitor.methodInvocations;
	}

	public List<Modifier> getModifiers()
	{
		return visitor.modifiers;
	}

	public List<NormalAnnotation> getNormalAnnotations()
	{
		return visitor.normalAnnotations;
	}

	public List<NullLiteral> getNullLiterals()
	{
		return visitor.nullLiterals;
	}

	public List<NumberLiteral> getNumberLiterals()
	{
		return visitor.numberLiterals;
	}

	public List<PackageDeclaration> getPackageDeclarations()
	{
		return visitor.packageDeclarations;
	}

	public List<ParameterizedType> getParameterizedTypes()
	{
		return visitor.parameterizedTypes;
	}

	public List<ParenthesizedExpression> getParenthesizedExpressions()
	{
		return visitor.parenthesizedExpressions;
	}

	public List<PostfixExpression> getPostfixExpressions()
	{
		return visitor.postfixExpressions;
	}

	public List<PrefixExpression> getPrefixExpressions()
	{
		return visitor.prefixExpressions;
	}

	public List<PrimitiveType> getPrimitiveTypes()
	{
		return visitor.primitiveTypes;
	}

	public List<QualifiedName> getQualifiedNames()
	{
		return visitor.qualifiedNames;
	}

	public List<QualifiedType> getQualifiedTypes()
	{
		return visitor.qualifiedTypes;
	}

	public List<ReturnStatement> getReturnStatements()
	{
		return visitor.returnStatements;
	}

	public List<SimpleName> getSimpleNames()
	{
		return visitor.simpleNames;
	}

	public List<SimpleType> getSimpleTypes()
	{
		return visitor.simpleTypes;
	}

	public List<SingleMemberAnnotation> getSingleMemberAnnotations()
	{
		return visitor.singleMemberAnnotations;
	}

	public List<SingleVariableDeclaration> getSingleVariableDeclarations()
	{
		return visitor.singleVariableDeclarations;
	}

	public List<StringLiteral> getStringLiterals()
	{
		return visitor.stringLiterals;
	}

	public List<SuperConstructorInvocation> getSuperConstructorInvocations()
	{
		return visitor.superConstructorInvocations;
	}

	public List<SuperFieldAccess> getSuperFieldAccesss()
	{
		return visitor.superFieldAccesss;
	}

	public List<SuperMethodInvocation> getSuperMethodInvocations()
	{
		return visitor.superMethodInvocations;
	}

	public List<SwitchCase> getSwitchCases()
	{
		return visitor.switchCases;
	}

	public List<SwitchStatement> getSwitchStatements()
	{
		return visitor.switchStatements;
	}

	public List<SynchronizedStatement> getSynchronizedStatements()
	{
		return visitor.synchronizedStatements;
	}

	public List<TagElement> getTagElements()
	{
		return visitor.tagElements;
	}

	public List<TextElement> getTextElements()
	{
		return visitor.textElements;
	}

	public List<ThisExpression> getThisExpressions()
	{
		return visitor.thisExpressions;
	}

	public List<ThrowStatement> getThrowStatements()
	{
		return visitor.throwStatements;
	}

	public List<TryStatement> getTryStatements()
	{
		return visitor.tryStatements;
	}

	public List<TypeDeclaration> getTypeDeclarations()
	{
		return visitor.typeDeclarations;
	}

	public List<TypeDeclarationStatement> getTypeDeclarationStatements()
	{
		return visitor.typeDeclarationStatements;
	}

	public List<TypeLiteral> getTypeLiterals()
	{
		return visitor.typeLiterals;
	}

	public List<TypeParameter> getTypeParameters()
	{
		return visitor.typeParameters;
	}

	public List<UnionType> getUnionTypes()
	{
		return visitor.unionTypes;
	}

	public List<VariableDeclarationExpression> getVariableDeclarationExpressions()
	{
		return visitor.variableDeclarationExpressions;
	}

	public List<VariableDeclarationStatement> getVariableDeclarationStatements()
	{
		return visitor.variableDeclarationStatements;
	}

	public List<VariableDeclarationFragment> getVariableDeclarationFragments()
	{
		return visitor.variableDeclarationFragments;
	}

	public List<WhileStatement> getWhileStatements()
	{
		return visitor.whileStatements;
	}

	public List<WildcardType> getWildcardTypes()
	{
		return visitor.wildcardTypes;
	}

	private static class Visitor extends ASTVisitor
	{

		private static final int SMALL_CAPACITY = 10;
		private static final int MEDIUM_CAPACITY = 30;
		private static final int LARGE_CAPACITY = 100;

		private final List<AnnotationTypeDeclaration> annotationTypeDeclarations;
		private final List<AnnotationTypeMemberDeclaration> annotationTypeMemberDeclarations;
		private final List<AnonymousClassDeclaration> anonymousClassDeclarations;
		private final List<ArrayAccess> arrayAccesss;
		private final List<ArrayCreation> arrayCreations;
		private final List<ArrayInitializer> arrayInitializers;
		private final List<ArrayType> arrayTypes;
		private final List<AssertStatement> assertStatements;
		private final List<Assignment> assignments;
		private final List<Block> blocks;
		private final List<BlockComment> blockComments;
		private final List<BooleanLiteral> booleanLiterals;
		private final List<BreakStatement> breakStatements;
		private final List<CastExpression> castExpressions;
		private final List<CatchClause> catchClauses;
		private final List<CharacterLiteral> characterLiterals;
		private final List<ClassInstanceCreation> classInstanceCreations;
		private final List<CompilationUnit> compilationUnits;
		private final List<ConditionalExpression> conditionalExpressions;
		private final List<ConstructorInvocation> constructorInvocations;
		private final List<ContinueStatement> continueStatements;
		private final List<DoStatement> doStatements;
		private final List<EmptyStatement> emptyStatements;
		private final List<EnhancedForStatement> enhancedForStatements;
		private final List<EnumConstantDeclaration> enumConstantDeclarations;
		private final List<EnumDeclaration> enumDeclarations;
		private final List<ExpressionStatement> expressionStatements;
		private final List<FieldAccess> fieldAccesss;
		private final List<FieldDeclaration> fieldDeclarations;
		private final List<ForStatement> forStatements;
		private final List<IfStatement> ifStatements;
		private final List<ImportDeclaration> importDeclarations;
		private final List<InfixExpression> infixExpressions;
		private final List<InstanceofExpression> instanceofExpressions;
		private final List<Initializer> initializers;
		private final List<Javadoc> javadocs;
		private final List<LabeledStatement> labeledStatements;
		private final List<LineComment> lineComments;
		private final List<MarkerAnnotation> markerAnnotations;
		private final List<MemberRef> memberRefs;
		private final List<MemberValuePair> memberValuePairs;
		private final List<MethodRef> methodRefs;
		private final List<MethodRefParameter> methodRefParameters;
		private final List<MethodDeclaration> methodDeclarations;
		private final List<MethodInvocation> methodInvocations;
		private final List<Modifier> modifiers;
		private final List<NormalAnnotation> normalAnnotations;
		private final List<NullLiteral> nullLiterals;
		private final List<NumberLiteral> numberLiterals;
		private final List<PackageDeclaration> packageDeclarations;
		private final List<ParameterizedType> parameterizedTypes;
		private final List<ParenthesizedExpression> parenthesizedExpressions;
		private final List<PostfixExpression> postfixExpressions;
		private final List<PrefixExpression> prefixExpressions;
		private final List<PrimitiveType> primitiveTypes;
		private final List<QualifiedName> qualifiedNames;
		private final List<QualifiedType> qualifiedTypes;
		private final List<ReturnStatement> returnStatements;
		private final List<SimpleName> simpleNames;
		private final List<SimpleType> simpleTypes;
		private final List<SingleMemberAnnotation> singleMemberAnnotations;
		private final List<SingleVariableDeclaration> singleVariableDeclarations;
		private final List<StringLiteral> stringLiterals;
		private final List<SuperConstructorInvocation> superConstructorInvocations;
		private final List<SuperFieldAccess> superFieldAccesss;
		private final List<SuperMethodInvocation> superMethodInvocations;
		private final List<SwitchCase> switchCases;
		private final List<SwitchStatement> switchStatements;
		private final List<SynchronizedStatement> synchronizedStatements;
		private final List<TagElement> tagElements;
		private final List<TextElement> textElements;
		private final List<ThisExpression> thisExpressions;
		private final List<ThrowStatement> throwStatements;
		private final List<TryStatement> tryStatements;
		private final List<TypeDeclaration> typeDeclarations;
		private final List<TypeDeclarationStatement> typeDeclarationStatements;
		private final List<TypeLiteral> typeLiterals;
		private final List<TypeParameter> typeParameters;
		private final List<UnionType> unionTypes;
		private final List<VariableDeclarationExpression> variableDeclarationExpressions;
		private final List<VariableDeclarationStatement> variableDeclarationStatements;
		private final List<VariableDeclarationFragment> variableDeclarationFragments;
		private final List<WhileStatement> whileStatements;
		private final List<WildcardType> wildcardTypes;

		public Visitor()
		{
			annotationTypeDeclarations = new ArrayList<AnnotationTypeDeclaration>(SMALL_CAPACITY);
			annotationTypeMemberDeclarations = new ArrayList<AnnotationTypeMemberDeclaration>(SMALL_CAPACITY);
			anonymousClassDeclarations = new ArrayList<AnonymousClassDeclaration>(SMALL_CAPACITY);
			arrayAccesss = new ArrayList<ArrayAccess>(SMALL_CAPACITY);
			arrayCreations = new ArrayList<ArrayCreation>(SMALL_CAPACITY);
			arrayInitializers = new ArrayList<ArrayInitializer>(SMALL_CAPACITY);
			arrayTypes = new ArrayList<ArrayType>(SMALL_CAPACITY);
			assertStatements = new ArrayList<AssertStatement>(SMALL_CAPACITY);
			assignments = new ArrayList<Assignment>(MEDIUM_CAPACITY);
			blocks = new ArrayList<Block>(MEDIUM_CAPACITY);
			blockComments = new ArrayList<BlockComment>(SMALL_CAPACITY);
			booleanLiterals = new ArrayList<BooleanLiteral>(SMALL_CAPACITY);
			breakStatements = new ArrayList<BreakStatement>(SMALL_CAPACITY);
			castExpressions = new ArrayList<CastExpression>(SMALL_CAPACITY);
			catchClauses = new ArrayList<CatchClause>(SMALL_CAPACITY);
			characterLiterals = new ArrayList<CharacterLiteral>(SMALL_CAPACITY);
			classInstanceCreations = new ArrayList<ClassInstanceCreation>(MEDIUM_CAPACITY);
			compilationUnits = new ArrayList<CompilationUnit>(SMALL_CAPACITY);
			conditionalExpressions = new ArrayList<ConditionalExpression>(SMALL_CAPACITY);
			constructorInvocations = new ArrayList<ConstructorInvocation>(SMALL_CAPACITY);
			continueStatements = new ArrayList<ContinueStatement>(SMALL_CAPACITY);
			doStatements = new ArrayList<DoStatement>(SMALL_CAPACITY);
			emptyStatements = new ArrayList<EmptyStatement>(SMALL_CAPACITY);
			enhancedForStatements = new ArrayList<EnhancedForStatement>(SMALL_CAPACITY);
			enumConstantDeclarations = new ArrayList<EnumConstantDeclaration>(SMALL_CAPACITY);
			enumDeclarations = new ArrayList<EnumDeclaration>(SMALL_CAPACITY);
			expressionStatements = new ArrayList<ExpressionStatement>(MEDIUM_CAPACITY);
			fieldAccesss = new ArrayList<FieldAccess>(SMALL_CAPACITY);
			fieldDeclarations = new ArrayList<FieldDeclaration>(MEDIUM_CAPACITY);
			forStatements = new ArrayList<ForStatement>(SMALL_CAPACITY);
			ifStatements = new ArrayList<IfStatement>(SMALL_CAPACITY);
			importDeclarations = new ArrayList<ImportDeclaration>(MEDIUM_CAPACITY);
			infixExpressions = new ArrayList<InfixExpression>(SMALL_CAPACITY);
			instanceofExpressions = new ArrayList<InstanceofExpression>(SMALL_CAPACITY);
			initializers = new ArrayList<Initializer>(SMALL_CAPACITY);
			javadocs = new ArrayList<Javadoc>(SMALL_CAPACITY);
			labeledStatements = new ArrayList<LabeledStatement>(SMALL_CAPACITY);
			lineComments = new ArrayList<LineComment>(SMALL_CAPACITY);
			markerAnnotations = new ArrayList<MarkerAnnotation>(SMALL_CAPACITY);
			memberRefs = new ArrayList<MemberRef>(SMALL_CAPACITY);
			memberValuePairs = new ArrayList<MemberValuePair>(SMALL_CAPACITY);
			methodRefs = new ArrayList<MethodRef>(SMALL_CAPACITY);
			methodRefParameters = new ArrayList<MethodRefParameter>(SMALL_CAPACITY);
			methodDeclarations = new ArrayList<MethodDeclaration>(SMALL_CAPACITY);
			methodInvocations = new ArrayList<MethodInvocation>(MEDIUM_CAPACITY);
			modifiers = new ArrayList<Modifier>(MEDIUM_CAPACITY);
			normalAnnotations = new ArrayList<NormalAnnotation>(SMALL_CAPACITY);
			nullLiterals = new ArrayList<NullLiteral>(SMALL_CAPACITY);
			numberLiterals = new ArrayList<NumberLiteral>(SMALL_CAPACITY);
			packageDeclarations = new ArrayList<PackageDeclaration>(SMALL_CAPACITY);
			parameterizedTypes = new ArrayList<ParameterizedType>(SMALL_CAPACITY);
			parenthesizedExpressions = new ArrayList<ParenthesizedExpression>(SMALL_CAPACITY);
			postfixExpressions = new ArrayList<PostfixExpression>(SMALL_CAPACITY);
			prefixExpressions = new ArrayList<PrefixExpression>(SMALL_CAPACITY);
			primitiveTypes = new ArrayList<PrimitiveType>(MEDIUM_CAPACITY);
			qualifiedNames = new ArrayList<QualifiedName>(MEDIUM_CAPACITY);
			qualifiedTypes = new ArrayList<QualifiedType>(SMALL_CAPACITY);
			returnStatements = new ArrayList<ReturnStatement>(SMALL_CAPACITY);
			simpleNames = new ArrayList<SimpleName>(LARGE_CAPACITY);
			simpleTypes = new ArrayList<SimpleType>(SMALL_CAPACITY);
			singleMemberAnnotations = new ArrayList<SingleMemberAnnotation>(SMALL_CAPACITY);
			singleVariableDeclarations = new ArrayList<SingleVariableDeclaration>(MEDIUM_CAPACITY);
			stringLiterals = new ArrayList<StringLiteral>(SMALL_CAPACITY);
			superConstructorInvocations = new ArrayList<SuperConstructorInvocation>(SMALL_CAPACITY);
			superFieldAccesss = new ArrayList<SuperFieldAccess>(SMALL_CAPACITY);
			superMethodInvocations = new ArrayList<SuperMethodInvocation>(SMALL_CAPACITY);
			switchCases = new ArrayList<SwitchCase>(SMALL_CAPACITY);
			switchStatements = new ArrayList<SwitchStatement>(SMALL_CAPACITY);
			synchronizedStatements = new ArrayList<SynchronizedStatement>(SMALL_CAPACITY);
			tagElements = new ArrayList<TagElement>(SMALL_CAPACITY);
			textElements = new ArrayList<TextElement>(SMALL_CAPACITY);
			thisExpressions = new ArrayList<ThisExpression>(SMALL_CAPACITY);
			throwStatements = new ArrayList<ThrowStatement>(SMALL_CAPACITY);
			tryStatements = new ArrayList<TryStatement>(SMALL_CAPACITY);
			typeDeclarations = new ArrayList<TypeDeclaration>(SMALL_CAPACITY);
			typeDeclarationStatements = new ArrayList<TypeDeclarationStatement>(SMALL_CAPACITY);
			typeLiterals = new ArrayList<TypeLiteral>(SMALL_CAPACITY);
			typeParameters = new ArrayList<TypeParameter>(SMALL_CAPACITY);
			unionTypes = new ArrayList<UnionType>(SMALL_CAPACITY);
			variableDeclarationExpressions = new ArrayList<VariableDeclarationExpression>(SMALL_CAPACITY);
			variableDeclarationStatements = new ArrayList<VariableDeclarationStatement>(MEDIUM_CAPACITY);
			variableDeclarationFragments = new ArrayList<VariableDeclarationFragment>(MEDIUM_CAPACITY);
			whileStatements = new ArrayList<WhileStatement>(SMALL_CAPACITY);
			wildcardTypes = new ArrayList<WildcardType>(SMALL_CAPACITY);
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 * @since 3.1
		 */
		@Override
		public boolean visit(AnnotationTypeDeclaration node)
		{
			annotationTypeDeclarations.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 * @since 3.1
		 */
		@Override
		public boolean visit(AnnotationTypeMemberDeclaration node)
		{
			annotationTypeMemberDeclarations.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(AnonymousClassDeclaration node)
		{
			anonymousClassDeclarations.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(ArrayAccess node)
		{
			arrayAccesss.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(ArrayCreation node)
		{
			arrayCreations.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(ArrayInitializer node)
		{
			arrayInitializers.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(ArrayType node)
		{
			arrayTypes.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(AssertStatement node)
		{
			assertStatements.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(Assignment node)
		{
			assignments.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(Block node)
		{
			blocks.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * <p>
		 * Note: {@link LineComment} and {@link BlockComment} nodes are not
		 * considered part of main structure of the AST. This method will only
		 * be called if a client goes out of their way to visit this kind of
		 * node explicitly.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 * @since 3.0
		 */
		@Override
		public boolean visit(BlockComment node)
		{
			blockComments.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(BooleanLiteral node)
		{
			booleanLiterals.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(BreakStatement node)
		{
			breakStatements.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(CastExpression node)
		{
			castExpressions.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(CatchClause node)
		{
			catchClauses.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(CharacterLiteral node)
		{
			characterLiterals.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(ClassInstanceCreation node)
		{
			classInstanceCreations.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(CompilationUnit node)
		{
			compilationUnits.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(ConditionalExpression node)
		{
			conditionalExpressions.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(ConstructorInvocation node)
		{
			constructorInvocations.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(ContinueStatement node)
		{
			continueStatements.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(DoStatement node)
		{
			doStatements.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(EmptyStatement node)
		{
			emptyStatements.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 * @since 3.1
		 */
		@Override
		public boolean visit(EnhancedForStatement node)
		{
			enhancedForStatements.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 * @since 3.1
		 */
		@Override
		public boolean visit(EnumConstantDeclaration node)
		{
			enumConstantDeclarations.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 * @since 3.1
		 */
		@Override
		public boolean visit(EnumDeclaration node)
		{
			enumDeclarations.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(ExpressionStatement node)
		{
			expressionStatements.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(FieldAccess node)
		{
			fieldAccesss.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(FieldDeclaration node)
		{
			fieldDeclarations.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(ForStatement node)
		{
			forStatements.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(IfStatement node)
		{
			ifStatements.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(ImportDeclaration node)
		{
			importDeclarations.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(InfixExpression node)
		{
			infixExpressions.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(InstanceofExpression node)
		{
			instanceofExpressions.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(Initializer node)
		{
			initializers.add(node);
			return true;
		}

		/**
		 * Visits the given AST node.
		 * <p>
		 * Unlike other node types, the boolean returned by the default
		 * implementation is controlled by a constructor-supplied parameter
		 * {@link #ASTVisitor(boolean) ASTVisitor(boolean)} which is
		 * <code>false</code> by default. Subclasses may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 * @see #ASTVisitor()
		 * @see #ASTVisitor(boolean)
		 */
		@Override
		public boolean visit(Javadoc node)
		{
			javadocs.add(node);
			// visit tag elements inside doc comments only if requested
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(LabeledStatement node)
		{
			labeledStatements.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * <p>
		 * Note: {@link LineComment} and {@link BlockComment} nodes are not
		 * considered part of main structure of the AST. This method will only
		 * be called if a client goes out of their way to visit this kind of
		 * node explicitly.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 * @since 3.0
		 */
		@Override
		public boolean visit(LineComment node)
		{
			lineComments.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 * @since 3.1
		 */
		@Override
		public boolean visit(MarkerAnnotation node)
		{
			markerAnnotations.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 * @since 3.0
		 */
		@Override
		public boolean visit(MemberRef node)
		{
			memberRefs.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 * @since 3.1
		 */
		@Override
		public boolean visit(MemberValuePair node)
		{
			memberValuePairs.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 * @since 3.0
		 */
		@Override
		public boolean visit(MethodRef node)
		{
			methodRefs.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 * @since 3.0
		 */
		@Override
		public boolean visit(MethodRefParameter node)
		{
			methodRefParameters.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(MethodDeclaration node)
		{
			methodDeclarations.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(MethodInvocation node)
		{
			methodInvocations.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 * @since 3.1
		 */
		@Override
		public boolean visit(Modifier node)
		{
			modifiers.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 * @since 3.1
		 */
		@Override
		public boolean visit(NormalAnnotation node)
		{
			normalAnnotations.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(NullLiteral node)
		{
			nullLiterals.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(NumberLiteral node)
		{
			numberLiterals.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(PackageDeclaration node)
		{
			packageDeclarations.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 * @since 3.1
		 */
		@Override
		public boolean visit(ParameterizedType node)
		{
			parameterizedTypes.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(ParenthesizedExpression node)
		{
			parenthesizedExpressions.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(PostfixExpression node)
		{
			postfixExpressions.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(PrefixExpression node)
		{
			prefixExpressions.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(PrimitiveType node)
		{
			primitiveTypes.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(QualifiedName node)
		{
			qualifiedNames.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 * @since 3.1
		 */
		@Override
		public boolean visit(QualifiedType node)
		{
			qualifiedTypes.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(ReturnStatement node)
		{
			returnStatements.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(SimpleName node)
		{
			simpleNames.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(SimpleType node)
		{
			simpleTypes.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 * @since 3.1
		 */
		@Override
		public boolean visit(SingleMemberAnnotation node)
		{
			singleMemberAnnotations.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(SingleVariableDeclaration node)
		{
			singleVariableDeclarations.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(StringLiteral node)
		{
			stringLiterals.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(SuperConstructorInvocation node)
		{
			superConstructorInvocations.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(SuperFieldAccess node)
		{
			superFieldAccesss.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(SuperMethodInvocation node)
		{
			superMethodInvocations.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(SwitchCase node)
		{
			switchCases.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(SwitchStatement node)
		{
			switchStatements.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(SynchronizedStatement node)
		{
			synchronizedStatements.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 * @since 3.0
		 */
		@Override
		public boolean visit(TagElement node)
		{
			tagElements.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 * @since 3.0
		 */
		@Override
		public boolean visit(TextElement node)
		{
			textElements.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(ThisExpression node)
		{
			thisExpressions.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(ThrowStatement node)
		{
			throwStatements.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(TryStatement node)
		{
			tryStatements.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(TypeDeclaration node)
		{
			typeDeclarations.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(TypeDeclarationStatement node)
		{
			typeDeclarationStatements.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(TypeLiteral node)
		{
			typeLiterals.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 * @since 3.1
		 */
		@Override
		public boolean visit(TypeParameter node)
		{
			typeParameters.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 * @since 3.7.1
		 */
		@Override
		public boolean visit(UnionType node)
		{
			unionTypes.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(VariableDeclarationExpression node)
		{
			variableDeclarationExpressions.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(VariableDeclarationStatement node)
		{
			variableDeclarationStatements.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(VariableDeclarationFragment node)
		{
			variableDeclarationFragments.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 */
		@Override
		public boolean visit(WhileStatement node)
		{
			whileStatements.add(node);
			return true;
		}

		/**
		 * Visits the given type-specific AST node.
		 * <p>
		 * The default implementation does nothing and return true. Subclasses
		 * may reimplement.
		 * </p>
		 * 
		 * @param node
		 *            the node to visit
		 * @return <code>true</code> if the children of this node should be
		 *         visited, and <code>false</code> if the children of this node
		 *         should be skipped
		 * @since 3.1
		 */
		@Override
		public boolean visit(WildcardType node)
		{
			wildcardTypes.add(node);
			return true;
		}

		private final void readObject(java.io.ObjectInputStream in)
			throws java.io.IOException
		{
			throw new java.io.IOException("Class cannot be deserialized");
		}

	}

	@Override
	public final Object clone() throws java.lang.CloneNotSupportedException
	{
		throw new java.lang.CloneNotSupportedException();
	}

	private final void readObject(java.io.ObjectInputStream in)
		throws java.io.IOException
	{
		throw new java.io.IOException("Class cannot be deserialized");
	}

}

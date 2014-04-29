/*
* generated by Xtext
*/
package org.eclipse.xtend.ide.contentassist;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.swt.graphics.Image;
import org.eclipse.xtend.core.services.XtendGrammarAccess;
import org.eclipse.xtend.core.xtend.AnonymousClass;
import org.eclipse.xtend.core.xtend.XtendClass;
import org.eclipse.xtend.core.xtend.XtendField;
import org.eclipse.xtend.core.xtend.XtendFile;
import org.eclipse.xtend.core.xtend.XtendFunction;
import org.eclipse.xtend.core.xtend.XtendPackage;
import org.eclipse.xtend.core.xtend.XtendParameter;
import org.eclipse.xtext.AbstractRule;
import org.eclipse.xtext.Assignment;
import org.eclipse.xtext.EcoreUtil2;
import org.eclipse.xtext.GrammarUtil;
import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.common.types.JvmIdentifiableElement;
import org.eclipse.xtext.common.types.TypesPackage;
import org.eclipse.xtext.common.types.xtext.ui.ITypesProposalProvider;
import org.eclipse.xtext.common.types.xtext.ui.JdtVariableCompletions;
import org.eclipse.xtext.common.types.xtext.ui.JdtVariableCompletions.VariableType;
import org.eclipse.xtext.common.types.xtext.ui.TypeMatchFilters;
import org.eclipse.xtext.naming.QualifiedName;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.ui.editor.contentassist.ConfigurableCompletionProposal;
import org.eclipse.xtext.ui.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.editor.contentassist.ICompletionProposalAcceptor;
import org.eclipse.xtext.util.ITextRegion;
import org.eclipse.xtext.util.Strings;
import org.eclipse.xtext.xbase.XBlockExpression;
import org.eclipse.xtext.xbase.XExpression;
import org.eclipse.xtext.xbase.scoping.batch.IIdentifiableElementDescription;
import org.eclipse.xtext.xbase.scoping.featurecalls.OperatorMapping;
import org.eclipse.xtext.xbase.typesystem.IExpressionScope;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;
import com.google.inject.Inject;

/**
 * see http://www.eclipse.org/Xtext/documentation/latest/xtext.html#contentAssist on how to customize content assistant
 */
public class XtendProposalProvider extends AbstractXtendProposalProvider {

	@Inject
	private JdtVariableCompletions completions;

	@Inject
	private XtendGrammarAccess grammarAccess;

	@Inject
	private ImplementMemberFromSuperAssist overrideAssist;
	
	@Inject
	private OperatorMapping operatorMapping;

	@Override
	public void completeMember_Name(final EObject model, Assignment assignment, final ContentAssistContext context,
			final ICompletionProposalAcceptor acceptor) {
		EObject previousModel = context.getPreviousModel();
		if (previousModel instanceof XExpression) {
			if (!(previousModel instanceof XBlockExpression)) {
				return;
			}
		}
		if (model instanceof XtendField) {
			//TODO go up type hierarchy and collect all local fields
			final List<XtendField> siblings = EcoreUtil2.getSiblingsOfType(model, XtendField.class);
			Set<String> alreadyTaken = Sets.newHashSet();
			for(XtendField sibling: siblings) {
				alreadyTaken.add(sibling.getName());
			}
			alreadyTaken.addAll(getAllKeywords());
			completions.getVariableProposals(model, XtendPackage.Literals.XTEND_FIELD__TYPE,
					VariableType.INSTANCE_FIELD, alreadyTaken, new JdtVariableCompletions.CompletionDataAcceptor() {
						public void accept(String replaceText, StyledString label, Image img) {
							acceptor.accept(createCompletionProposal(replaceText, label, img, context));
						}
					});
		} else if (model instanceof XtendFunction) {
			for(QualifiedName operator: operatorMapping.getOperators()) {
				StyledString displayString = new StyledString(operator.getFirstSegment());
				displayString.append(" " + operatorMapping.getMethodName(operator), StyledString.DECORATIONS_STYLER);
				acceptor.accept(createCompletionProposal(
						operator.getFirstSegment(),
						displayString,
						getImage(model),
						context));
			}
			super.completeMember_Name(model, assignment, context, acceptor);
		} else {
			super.completeMember_Name(model, assignment, context, acceptor);
		}
	}
	
	@Override
	public void completeTypeReferenceNoTypeArgs_Type(EObject model, Assignment assignment,
			ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		if (getXbaseCrossReferenceProposalCreator().isShowTypeProposals() || getXbaseCrossReferenceProposalCreator().isShowSmartProposals()) {
			completeJavaTypes(context, TypesPackage.Literals.JVM_PARAMETERIZED_TYPE_REFERENCE__TYPE, acceptor);
		}
	}
	
	@Override
	protected ITypesProposalProvider.Filter createVisibilityFilter(ContentAssistContext context, int searchFor) {
		XtendFile file = (XtendFile) context.getRootModel();
		final char[] contextPackageName = Strings.emptyIfNull(file.getPackage()).toCharArray(); 
		return new TypeMatchFilters.All(searchFor) {
			@Override
			public boolean accept(int modifiers, char[] packageName, char[] simpleTypeName,
					char[][] enclosingTypeNames, String path) {
				if (super.accept(modifiers, packageName, simpleTypeName, enclosingTypeNames, path)) {
					if (Flags.isPublic(modifiers)) {
						return true;
					}
					if (Flags.isPrivate(modifiers)) {
						return false;
					}
					if (Arrays.equals(contextPackageName, packageName)) {
						return true;
					}
				}
				return false;
			}
		};
	}
	
	@Override
	public void completeMember_Type(EObject model, Assignment assignment, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
		if (model instanceof XtendField) {
			if(!((XtendField) model).getModifiers().isEmpty()) {
				// don't propose types everywhere but only if there's already an indicator for fields, e.g. static, extension, var, val
				completeJavaTypes(context, TypesPackage.Literals.JVM_PARAMETERIZED_TYPE_REFERENCE__TYPE, true,
						getQualifiedNameValueConverter(), new TypeMatchFilters.All(IJavaSearchConstants.TYPE), acceptor);
			}
		}
	}
	
	@Override
	public void completeMember_ReturnType(EObject model, Assignment assignment, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
		if (context.getPrefix().length() == 0) {
			EObject previousGrammarElement = context.getLastCompleteNode().getGrammarElement();
			AbstractRule rule = GrammarUtil.containingRule(previousGrammarElement);
			if (rule != grammarAccess.getValidIDRule()) {
				completeJavaTypes(context, TypesPackage.Literals.JVM_PARAMETERIZED_TYPE_REFERENCE__TYPE, true,
						getQualifiedNameValueConverter(), new TypeMatchFilters.All(IJavaSearchConstants.TYPE), acceptor);
			}
		}
	}

	protected Set<String> getAllKeywords() {
		return GrammarUtil.getAllKeywords(grammarAccess.getGrammar());
	}

	@Override
	public void completeParameter_Name(final EObject model, Assignment assignment, final ContentAssistContext context,
			final ICompletionProposalAcceptor acceptor) {
		if (model instanceof XtendParameter) {
			final List<XtendParameter> siblings = EcoreUtil2.getSiblingsOfType(model, XtendParameter.class);
			Set<String> alreadyTaken = Sets.newHashSet();
			for(XtendParameter sibling: siblings) {
				alreadyTaken.add(sibling.getName());
			}
			alreadyTaken.addAll(getAllKeywords());
			completions.getVariableProposals(model, XtendPackage.Literals.XTEND_PARAMETER__PARAMETER_TYPE,
					VariableType.PARAMETER, alreadyTaken, new JdtVariableCompletions.CompletionDataAcceptor() {
						public void accept(String replaceText, StyledString label, Image img) {
							acceptor.accept(createCompletionProposal(replaceText, label, img, context));
						}
					});
		} else {
			super.completeParameter_Name(model, assignment, context, acceptor);
		}
	}

	@Override
	protected Predicate<IEObjectDescription> getFeatureDescriptionPredicate(ContentAssistContext contentAssistContext) {
		if (contentAssistContext.getPrefix().startsWith("_"))
			return super.getFeatureDescriptionPredicate(contentAssistContext);
		final Predicate<IEObjectDescription> delegate = super.getFeatureDescriptionPredicate(contentAssistContext);
		return new Predicate<IEObjectDescription>() {

			public boolean apply(IEObjectDescription input) {
				boolean result = !input.getName().getFirstSegment().startsWith("_") && delegate.apply(input);
				if (result) {
					if (input instanceof IIdentifiableElementDescription) {
						IIdentifiableElementDescription casted = (IIdentifiableElementDescription) input;
						if (isDiscouragedExtension(casted)) {
							return false;
						}
					}
				}
				return result;
			}

		};
	}
	
	private final String WAIT = "java.lang.Object.wait";
	protected boolean isDiscouragedExtension(IIdentifiableElementDescription description) {
		if (description.isExtension()) {
			JvmIdentifiableElement candidate = description.getElementOrProxy();
			if ("equals".equals(candidate.getSimpleName())) {
				return description.getNumberOfParameters() == 0;
			}
			if ("wait".equals(candidate.getSimpleName())) {
				return WAIT.equals(candidate.getQualifiedName());
			}
		} else { // if (description.getImplicitFirstArgument() != null) {
			JvmIdentifiableElement candidate = description.getElementOrProxy();
			if ("equals".equals(candidate.getSimpleName())) {
				return description.getNumberOfParameters() == 0;
			}
		}
		return false;
	}

	@Override
	public void completeType_Extends(EObject model, Assignment assignment, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
		completeJavaTypes(context, TypesPackage.Literals.JVM_PARAMETERIZED_TYPE_REFERENCE__TYPE, true, getQualifiedNameValueConverter(),
				new ITypesProposalProvider.Filter() {
					public int getSearchFor() {
						return IJavaSearchConstants.CLASS;
					}

					public boolean accept(int modifiers, char[] packageName, char[] simpleTypeName,
							char[][] enclosingTypeNames, String path) {
						if (TypeMatchFilters.isInternalClass(simpleTypeName, enclosingTypeNames))
							return false;
						return !Flags.isFinal(modifiers);
					}
				}, acceptor);
	}

	@Override
	public void completeType_Implements(EObject model, Assignment assignment, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
		completeJavaTypes(context, TypesPackage.Literals.JVM_PARAMETERIZED_TYPE_REFERENCE__TYPE, true, getQualifiedNameValueConverter(),
				TypeMatchFilters.all(IJavaSearchConstants.INTERFACE), acceptor);
	}

	@Override
	public void completeType_Members(EObject model, Assignment assignment, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
		if (model instanceof XtendClass)
			overrideAssist.createOverrideProposals((XtendClass) model, context, acceptor, getConflictHelper());
		super.completeType_Members(model, assignment, context, acceptor);
	}
	
	@Override
	public void completeXConstructorCall_Members(EObject model, Assignment assignment, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
		if (model instanceof AnonymousClass)
			overrideAssist.createOverrideProposals((AnonymousClass) model, context, acceptor, getConflictHelper());
	}

	protected void addGuillemotsProposal(ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		acceptor.accept(new ConfigurableCompletionProposal("\u00AB\u00BB", context.getOffset(), context
				.getSelectedText().length(), 1));
	}

	public void completeInRichString(EObject model, RuleCall ruleCall, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
		INode node = context.getCurrentNode();
		ITextRegion textRegion = node.getTextRegion();
		int offset = textRegion.getOffset();
		int length = textRegion.getLength();
		String currentNodeText = node.getText();
		if (currentNodeText.startsWith("\u00BB") && offset + 1 <= context.getOffset()
				|| currentNodeText.startsWith("'''") && offset + 3 <= context.getOffset()) {
			if (context.getOffset() > offset && context.getOffset() < offset + length)
				addGuillemotsProposal(context, acceptor);
		} else if (currentNodeText.startsWith("\u00AB\u00AB")) {
			try {
				IDocument document = context.getViewer().getDocument();
				int nodeLine = document.getLineOfOffset(offset);
				int completionLine = document.getLineOfOffset(context.getOffset());
				if (completionLine > nodeLine) {
					addGuillemotsProposal(context, acceptor);
				}
			} catch (BadLocationException e) {
				// ignore
			}
		}
	}
	
	@Override
	public void completeXFeatureCall_Feature(EObject model, Assignment assignment, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
		if (model instanceof XtendField) {
			createLocalVariableAndImplicitProposals(context.getPreviousModel(), context, acceptor);
		} else {
			super.completeXFeatureCall_Feature(model, assignment, context, acceptor);
		}
	}

	@Override
	public void complete_RICH_TEXT(EObject model, RuleCall ruleCall, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
		completeInRichString(model, ruleCall, context, acceptor);
	}

	@Override
	public void complete_RICH_TEXT_START(EObject model, RuleCall ruleCall, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
		completeInRichString(model, ruleCall, context, acceptor);
	}

	@Override
	public void complete_RICH_TEXT_END(EObject model, RuleCall ruleCall, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
		completeInRichString(model, ruleCall, context, acceptor);
	}

	@Override
	public void complete_RICH_TEXT_INBETWEEN(EObject model, RuleCall ruleCall, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
		completeInRichString(model, ruleCall, context, acceptor);
	}

	@Override
	public void complete_COMMENT_RICH_TEXT_END(EObject model, RuleCall ruleCall, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
		completeInRichString(model, ruleCall, context, acceptor);
	}

	@Override
	public void complete_COMMENT_RICH_TEXT_INBETWEEN(EObject model, RuleCall ruleCall, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
		completeInRichString(model, ruleCall, context, acceptor);
	}
	
	@Override
	public void completeXVariableDeclaration_Right(EObject model, Assignment assignment, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
		createLocalVariableAndImplicitProposals(model, IExpressionScope.Anchor.BEFORE, context, acceptor);
	}
	
	@Override
	public void completeXbaseConstructorCall_Constructor(EObject model, Assignment assignment,
			ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		completeXConstructorCall_Constructor(model, assignment, context, acceptor);
	}

	@Override
	public void completeXSwitchExpression_Default(EObject model, Assignment assignment, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
		createLocalVariableAndImplicitProposals(model, IExpressionScope.Anchor.WITHIN, context, acceptor);
	}
}

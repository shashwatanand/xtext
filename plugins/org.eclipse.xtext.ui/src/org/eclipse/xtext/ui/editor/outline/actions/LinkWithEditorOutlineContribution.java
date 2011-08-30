/*******************************************************************************
 * Copyright (c) 2010 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.ui.editor.outline.actions;

import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.xtext.ui.XtextUIMessages;
import org.eclipse.xtext.ui.editor.outline.impl.OutlinePage;
import org.eclipse.xtext.ui.internal.XtextPluginImages;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Provider;

/**
 * @author Jan Koehnlein - Initial contribution and API
 */
public class LinkWithEditorOutlineContribution extends AbstractToggleOutlineContribution {

	public static final String PREFERENCE_KEY = "ui.outline.linkWithEditor";

	@Inject
	private Provider<OutlineWithEditorLinker> outlineWithEditorLinkerProvider;

	private Map<OutlinePage, OutlineWithEditorLinker> page2linker = Maps.newHashMap();

	@Override
	public String getPreferenceKey() {
		return PREFERENCE_KEY;
	}

	@Override
	protected void stateChanged(boolean newState) {
	}

	@Override
	protected void configureAction(Action action) {
		action.setText(XtextUIMessages.ToggleLinkWithEditorAction_label);
		action.setToolTipText(XtextUIMessages.ToggleLinkWithEditorAction_toolTip);
		action.setDescription(XtextUIMessages.ToggleLinkWithEditorAction_description);
		action.setImageDescriptor(XtextPluginImages.DESC_LINK_WITH_EDITOR);
		action.setDisabledImageDescriptor(XtextPluginImages.DESC_LINK_WITH_EDITOR_DISABLED);
	}

	@Override
	public void register(OutlinePage outlinePage) {
		super.register(outlinePage);
		OutlineWithEditorLinker outlineWithEditorLinker = outlineWithEditorLinkerProvider.get();
		outlineWithEditorLinker.activate(outlinePage);
		outlineWithEditorLinker.setLinkingEnabled(isPropertySet());
		getPreferenceStoreAccess().getPreferenceStore().addPropertyChangeListener(outlineWithEditorLinker);
		page2linker.put(outlinePage, outlineWithEditorLinker);
	}

	@Override
	public void deregister(OutlinePage outlinePage) {
		super.deregister(outlinePage);
		OutlineWithEditorLinker outlineWithEditorLinker = page2linker.remove(outlinePage);
		if (outlineWithEditorLinker != null) {
			outlineWithEditorLinker.deactivate();
			getPreferenceStoreAccess().getPreferenceStore().removePropertyChangeListener(outlineWithEditorLinker);
		}
	}

}

/*******************************************************************************
 * Copyright (c) 2008 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.parser.packrat.tokens;

import org.eclipse.xtext.Action;
import org.eclipse.xtext.parser.packrat.IParsedTokenVisitor;

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 */
public class ParsedAction extends ParsedToken {

	private final boolean isMany;
	
	private final String typeName;

	public ParsedAction(int offset, Action action, String typeName, boolean isMany) {
		super(offset, 0, action);
		this.typeName = typeName;
		this.isMany = isMany;
	}

	@Override
	public void accept(IParsedTokenVisitor visitor) {
		visitor.visitParsedAction(this);
	}
	
	@Override
	public Action getGrammarElement() {
		return (Action) super.getGrammarElement();
	}
	
	public String getTypeName() {
		return typeName;
	}

	public String getFeature() {
		return getGrammarElement().getFeature();
	}

	public boolean isMany() {
		return isMany;
	}
	
}

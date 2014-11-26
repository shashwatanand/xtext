/*******************************************************************************
 * Copyright (c) 2014 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.eclipse.xtext.generator;

import org.eclipse.xtext.Grammar;


/**
 * @since 2.8
 */
public interface IGeneratorFragmentExtension3 {
	
	/**
	 * return the bundles this fragment requires for ide services
	 *
	 * @param grammar
	 */
	String[] getRequiredBundlesIde(Grammar grammar);

	/**
	 * return the exported packages for the ide bundle
	 *
	 * @param grammar
	 */
	String[] getExportedPackagesIde(Grammar grammar);

	/**
	 * return the imported packages for the ide bundle
	 *
	 * @param grammar
	 */
	String[] getImportedPackagesIde(Grammar grammar);
}

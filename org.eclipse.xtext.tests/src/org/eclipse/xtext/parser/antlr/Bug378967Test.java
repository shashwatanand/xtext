/*******************************************************************************
 * Copyright (c) 2012 itemis AG (http://www.itemis.eu) and others.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.xtext.parser.antlr;

import org.eclipse.xtext.testing.AbstractXtextTests;

/**
 * @author Holger Schill - Initial contribution and API
 */
public class Bug378967Test extends AbstractXtextTests{

	@Override
	public void setUp() throws Exception {
		super.setUp();
		with(Bug378967TestLanguageStandaloneSetup.class);
	}

	@org.junit.Test
	public void test1() throws Exception {
		getModelAndExpect("1 after s",1);

	}
	@org.junit.Test
	public void test2() throws Exception {
		getModelAndExpect("2 after s",1);

	}
	@org.junit.Test
	public void test3() throws Exception {
		getModelAndExpect("3 after s",1);

	}
	@org.junit.Test
	public void test4() throws Exception {
		getModelAndExpect("4 after s",1);

	}
}

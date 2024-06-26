/*******************************************************************************
 * Copyright (c) 2008 itemis AG (http://www.itemis.eu) and others.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *******************************************************************************/
package org.eclipse.xtext.grammarinheritance;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.AbstractRule;
import org.eclipse.xtext.grammarinheritance.ametamodel.AmetamodelPackage;
import org.eclipse.xtext.grammarinheritance.services.InheritanceTest2LanguageGrammarAccess;
import org.eclipse.xtext.testing.AbstractXtextTests;
import org.junit.Test;

public class AnotherInheritanceTest extends AbstractXtextTests {

	@Override
	public void setUp() throws Exception {
		super.setUp();
		EPackage aMetamodel = AmetamodelPackage.eINSTANCE;
		EPackage.Registry.INSTANCE.put(aMetamodel.getNsURI(), aMetamodel);
		with(new InheritanceTest2LanguageStandaloneSetup());
	}

	@Test public void testSimple() throws Exception {
		InheritanceTest2LanguageGrammarAccess g = (InheritanceTest2LanguageGrammarAccess) getGrammarAccess();
		assertNotNull(g.getFQNAccess());
		AbstractRule fromFQN = g.getFQNAccess().getIDTerminalRuleCall_0().getRule();
		assertEquals(fromFQN, g.getIDRule());
		assertNotNull(g.getModelAccess());
	}
	
	@Test public void testFQNWithNumbers() throws Exception {
		Resource resource = getModelAndExpect("model a { ab.ab01 }", 1).eResource();
		String msg = resource.getErrors().get(0).getMessage();
		assertEquals("extraneous input '01' expecting '}'", msg);
	}
}

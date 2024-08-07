/*******************************************************************************
 * Copyright (c) 2015 itemis AG (http://www.itemis.eu) and others.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.xtend.core.tests.compiler

import org.junit.Test

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 */
class CompilerBug460691Test extends AbstractXtendCompilerTest {
	
	@Test def test_01() {
		'''
			class C implements java.util.Comparator<String> {
			    override compare(String s1, String s2) {
			    	return s1.compareTo(s2)
			    }
			}
		'''.assertCompilesTo('''
			import java.util.Comparator;
			
			@SuppressWarnings("all")
			public class C implements Comparator<String> {
			  @Override
			  public int compare(final String s1, final String s2) {
			    return s1.compareTo(s2);
			  }
			}
		''')
	}
	
}
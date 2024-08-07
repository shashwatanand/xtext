/*******************************************************************************
 * Copyright (c) 2014 itemis AG (http://www.itemis.eu) and others.
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
class CompilerBug441099Test extends AbstractXtendCompilerTest {
	
	@Test
	def test_01() {
		assertCompilesTo('''
			import org.eclipse.xtend.lib.annotations.Accessors
			import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor
			class C {
				@Accessors(PUBLIC_GETTER) 
				String string
				def dispatch m(String x) {
				}
				def m() {
					new D('')
				}
			}
			@FinalFieldsConstructor
			class D {
				val String x
			}
		''', '''
			import org.eclipse.xtend.lib.annotations.AccessorType;
			import org.eclipse.xtend.lib.annotations.Accessors;
			import org.eclipse.xtext.xbase.lib.Pure;
			import org.eclipse.xtext.xbase.lib.XbaseGenerated;
			
			@SuppressWarnings("all")
			public class C {
			  @Accessors(AccessorType.PUBLIC_GETTER)
			  private String string;
			
			  protected Object _m(final String x) {
			    return null;
			  }
			
			  public D m() {
			    return new D("");
			  }
			
			  @XbaseGenerated
			  public Object m(final String x) {
			    return _m(x);
			  }
			
			  @Pure
			  public String getString() {
			    return this.string;
			  }
			}
		''')
	}
	
}
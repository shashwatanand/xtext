/*******************************************************************************
 * Copyright (c) 2013, 2024 itemis AG (http://www.itemis.eu) and others.
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
class OverloadedExtensionCompilerTest extends AbstractXtendCompilerTest {
	
	@Test
	def test_01() {
		assertCompilesTo('''
			class C {
				def method(MyIterable<? extends Number> list) {
			        list.m[ process(it) ]
			    }
			    def void process(Number value) {}
			}
			interface MyIterable<T> extends Iterable<T> {
				def void m(Consumer<? super T> c)
			}
			interface Consumer<T> {
				def void consume(T t)
			}
		''', '''
			@SuppressWarnings("all")
			public class C {
			  public void method(final MyIterable<? extends Number> list) {
			    final Consumer<Number> _function = (Number it) -> {
			      this.process(it);
			    };
			    list.m(_function);
			  }
			
			  public void process(final Number value) {
			  }
			}
		''')
	}
	
	@Test
	def test_02() {
		assertCompilesTo('''
			class C {
				def method(MyIterable<? extends Number> list) {
			        list.forEach2[ process ]
			    }
			    def void process(Number value) {}
			}
			interface MyIterable<T> extends Iterable<T> {
				def void forEach2(Consumer<? super T> c)
			}
			interface Consumer<T> {
				def void consume(T t)
			}
		''', '''
			@SuppressWarnings("all")
			public class C {
			  public void method(final MyIterable<? extends Number> list) {
			    final Consumer<Number> _function = (Number it) -> {
			      this.process(it);
			    };
			    list.forEach2(_function);
			  }
			
			  public void process(final Number value) {
			  }
			}
		''')
	}
	
}



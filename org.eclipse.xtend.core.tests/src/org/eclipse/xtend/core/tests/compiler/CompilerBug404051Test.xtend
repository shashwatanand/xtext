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
 * @see CompilerBugTest#testBug404051_01 and friends
 */
class CompilerBug404051Test extends AbstractXtendCompilerTest {
	
	@Test
	def test_01() {
		assertCompilesTo('''
			class C {
				def m(Iterable<String> iterable) {
					iterable.flatMap[].sortBy [ hashCode ]
				}
				def <A,B> Iterable<? super B> flatMap(Iterable<? extends A> iterable, (A)=>B map) {
					return null
				}
			}
		''', '''
			import java.util.List;
			import org.eclipse.xtext.xbase.lib.Functions.Function1;
			import org.eclipse.xtext.xbase.lib.IterableExtensions;
			
			@SuppressWarnings("all")
			public class C {
			  public List<? super Object> m(final Iterable<String> iterable) {
			    final Function1<String, Object> _function = (String it) -> {
			      return null;
			    };
			    final Function1<Object, Integer> _function_1 = (Object it) -> {
			      return Integer.valueOf(it.hashCode());
			    };
			    return IterableExtensions.sortBy(this.<String, Object>flatMap(iterable, _function), _function_1);
			  }
			
			  public <A extends Object, B extends Object> Iterable<? super B> flatMap(final Iterable<? extends A> iterable, final Function1<? super A, ? extends B> map) {
			    return null;
			  }
			}
		''')
	}
	
	@Test
	def test_02() {
		assertCompilesTo('''
			class C {
				def m(Iterable<String> iterable) {
					iterable.flatMap[].sortBy [ hashCode ]
				}
				def <A,B extends Number> Iterable<? super B> flatMap(Iterable<? extends A> iterable, (A)=>B map) {
					return null
				}
			}
		''', '''
			import java.util.List;
			import org.eclipse.xtext.xbase.lib.Functions.Function1;
			import org.eclipse.xtext.xbase.lib.IterableExtensions;
			
			@SuppressWarnings("all")
			public class C {
			  public List<? super Number> m(final Iterable<String> iterable) {
			    final Function1<String, Number> _function = (String it) -> {
			      return null;
			    };
			    final Function1<Object, Integer> _function_1 = (Object it) -> {
			      return Integer.valueOf(it.hashCode());
			    };
			    return IterableExtensions.sortBy(this.<String, Number>flatMap(iterable, _function), _function_1);
			  }
			
			  public <A extends Object, B extends Number> Iterable<? super B> flatMap(final Iterable<? extends A> iterable, final Function1<? super A, ? extends B> map) {
			    return null;
			  }
			}
		''')
	}
	
	@Test
	def test_03() {
		assertCompilesTo('''
			class C {
				def m(Iterable<String> iterable) {
					iterable.flatMap[].sortBy [ intValue ]
				}
				def <A,B extends Number> Iterable<? extends B> flatMap(Iterable<? extends A> iterable, (A)=>B map) {
					return null
				}
			}
		''', '''
			import java.util.List;
			import org.eclipse.xtext.xbase.lib.Functions.Function1;
			import org.eclipse.xtext.xbase.lib.IterableExtensions;
			
			@SuppressWarnings("all")
			public class C {
			  public List<? extends Number> m(final Iterable<String> iterable) {
			    final Function1<String, Number> _function = (String it) -> {
			      return null;
			    };
			    final Function1<Number, Integer> _function_1 = (Number it) -> {
			      return Integer.valueOf(it.intValue());
			    };
			    return IterableExtensions.sortBy(this.<String, Number>flatMap(iterable, _function), _function_1);
			  }
			
			  public <A extends Object, B extends Number> Iterable<? extends B> flatMap(final Iterable<? extends A> iterable, final Function1<? super A, ? extends B> map) {
			    return null;
			  }
			}
		''')
	}
	
	@Test
	def test_04() {
		assertCompilesTo('''
			class C {
				def m() {
					#[].flatMap[ length ].sortBy [ intValue ]
				}
				def <A extends String,B extends Number> Iterable<? extends B> flatMap(Iterable<? extends A> iterable, (A)=>B map) {
					return null
				}
			}
		''', '''
			import java.util.Collections;
			import java.util.List;
			import org.eclipse.xtext.xbase.lib.CollectionLiterals;
			import org.eclipse.xtext.xbase.lib.Functions.Function1;
			import org.eclipse.xtext.xbase.lib.IterableExtensions;
			
			@SuppressWarnings("all")
			public class C {
			  public List<? extends Integer> m() {
			    final Function1<String, Integer> _function = (String it) -> {
			      return Integer.valueOf(it.length());
			    };
			    final Function1<Integer, Integer> _function_1 = (Integer it) -> {
			      return Integer.valueOf(it.intValue());
			    };
			    return IterableExtensions.sortBy(this.<String, Integer>flatMap(Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList()), _function), _function_1);
			  }
			
			  public <A extends String, B extends Number> Iterable<? extends B> flatMap(final Iterable<? extends A> iterable, final Function1<? super A, ? extends B> map) {
			    return null;
			  }
			}
		''')
	}
	
	@Test
	def test_05() {
		assertCompilesTo('''
			class C {
				def m() {
					#[].flatMap[].sortBy [ intValue ]
				}
				def <A extends String,B extends Number> Iterable<? extends B> flatMap(Iterable<? extends A> iterable, (A)=>B map) {
					return null
				}
			}
		''', '''
			import java.util.Collections;
			import java.util.List;
			import org.eclipse.xtext.xbase.lib.CollectionLiterals;
			import org.eclipse.xtext.xbase.lib.Functions.Function1;
			import org.eclipse.xtext.xbase.lib.IterableExtensions;
			
			@SuppressWarnings("all")
			public class C {
			  public List<? extends Number> m() {
			    final Function1<String, Number> _function = (String it) -> {
			      return null;
			    };
			    final Function1<Number, Integer> _function_1 = (Number it) -> {
			      return Integer.valueOf(it.intValue());
			    };
			    return IterableExtensions.sortBy(this.<String, Number>flatMap(Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList()), _function), _function_1);
			  }
			
			  public <A extends String, B extends Number> Iterable<? extends B> flatMap(final Iterable<? extends A> iterable, final Function1<? super A, ? extends B> map) {
			    return null;
			  }
			}
		''')
	}
	
	@Test
	def test_06() {
		assertCompilesTo('''
			class C {
				def m(Iterable<String> iterable) {
					iterable.flatMap[].sortBy [ length ]
				}
				def <A,B extends A> Iterable<? extends B> flatMap(Iterable<? extends A> iterable, (A)=>B map) {
					return null
				}
			}
		''', '''
			import java.util.List;
			import org.eclipse.xtext.xbase.lib.Functions.Function1;
			import org.eclipse.xtext.xbase.lib.IterableExtensions;
			
			@SuppressWarnings("all")
			public class C {
			  public List<? extends String> m(final Iterable<String> iterable) {
			    final Function1<String, String> _function = (String it) -> {
			      return null;
			    };
			    final Function1<String, Integer> _function_1 = (String it) -> {
			      return Integer.valueOf(it.length());
			    };
			    return IterableExtensions.sortBy(this.<String, String>flatMap(iterable, _function), _function_1);
			  }
			
			  public <A extends Object, B extends A> Iterable<? extends B> flatMap(final Iterable<? extends A> iterable, final Function1<? super A, ? extends B> map) {
			    return null;
			  }
			}
		''')
	}
	
	
}
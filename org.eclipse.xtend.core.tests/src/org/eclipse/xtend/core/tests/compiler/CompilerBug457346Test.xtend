/*******************************************************************************
 * Copyright (c) 2015, 2024 itemis AG (http://www.itemis.eu) and others.
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
class CompilerBug457346Test extends AbstractXtendCompilerTest {
	
	@Test
	def test_01() {
		assertCompilesTo('''
			import com.google.common.collect.Ordering
			class C {
				def m() {
					#['a', 'aa', 'aaa'].sortWith(Ordering.natural.onResultOf [ String s| s.length ].onResultOf[])
				}
			}
		''', '''
			import com.google.common.base.Function;
			import com.google.common.collect.Ordering;
			import java.util.Collections;
			import java.util.List;
			import org.eclipse.xtext.xbase.lib.CollectionLiterals;
			import org.eclipse.xtext.xbase.lib.IterableExtensions;
			
			@SuppressWarnings("all")
			public class C {
			  public List<String> m() {
			    final Function<String, Integer> _function = (String s) -> {
			      return Integer.valueOf(s.length());
			    };
			    final Function<String, String> _function_1 = (String it) -> {
			      return null;
			    };
			    return IterableExtensions.<String>sortWith(Collections.<String>unmodifiableList(CollectionLiterals.<String>newArrayList("a", "aa", "aaa")), Ordering.<Integer>natural().<String>onResultOf(_function).<String>onResultOf(_function_1));
			  }
			}
		''')
	}
	
	@Test
	def test_02() {
		assertCompilesTo('''
			import com.google.common.collect.Ordering
			class C {
				def m(Sortable<String> sortMe) {
					sortMe.sort(Ordering.natural.onResultOf [ String s| s.length ].onResultOf[])
				}
				
				interface Sortable<T> extends Iterable<T> {
					def void sort(java.util.Comparator<? super T> c)
				}
			}
		''', '''
			import com.google.common.base.Function;
			import com.google.common.collect.Ordering;
			import java.util.Comparator;
			
			@SuppressWarnings("all")
			public class C {
			  public interface Sortable<T extends Object> extends Iterable<T> {
			    void sort(final Comparator<? super T> c);
			  }
			
			  public void m(final C.Sortable<String> sortMe) {
			    final Function<String, Integer> _function = (String s) -> {
			      return Integer.valueOf(s.length());
			    };
			    final Function<String, String> _function_1 = (String it) -> {
			      return null;
			    };
			    sortMe.sort(Ordering.<Integer>natural().<String>onResultOf(_function).<String>onResultOf(_function_1));
			  }
			}
		''')
	}
	
	@Test
	def test_03() {
		assertCompilesTo('''
			import java.util.Comparator
			class C {
				def m(Sortable<String> sortMe) {
					sortMe.sort(MyComparator.comparingInt2 [String s| s.length].thenComparing2[String s | s])
				}
				
				interface Sortable<T> extends Iterable<T> {
					def void sort(java.util.Comparator<? super T> c)
				}
				static abstract class MyComparator<T> implements Comparator<T> {
					def static <T> MyComparator<T> comparingInt2(ToIntFunction<? super T> fun) {}
					def <U extends Comparable<? super U>> MyComparator<T> thenComparing2((T)=>U f) {}
				}
				interface ToIntFunction<T> {
					def int apply(T t)
				}
			}
		''', '''
			import java.util.Comparator;
			import org.eclipse.xtext.xbase.lib.Functions.Function1;
			
			@SuppressWarnings("all")
			public class C {
			  public interface Sortable<T extends Object> extends Iterable<T> {
			    void sort(final Comparator<? super T> c);
			  }
			
			  public static abstract class MyComparator<T extends Object> implements Comparator<T> {
			    public static <T extends Object> C.MyComparator<T> comparingInt2(final C.ToIntFunction<? super T> fun) {
			      return null;
			    }
			
			    public <U extends Comparable<? super U>> C.MyComparator<T> thenComparing2(final Function1<? super T, ? extends U> f) {
			      return null;
			    }
			  }
			
			  public interface ToIntFunction<T extends Object> {
			    int apply(final T t);
			  }
			
			  public void m(final C.Sortable<String> sortMe) {
			    final C.ToIntFunction<String> _function = (String s) -> {
			      return s.length();
			    };
			    final Function1<String, String> _function_1 = (String s) -> {
			      return s;
			    };
			    sortMe.sort(C.MyComparator.<String>comparingInt2(_function).<String>thenComparing2(_function_1));
			  }
			}
		''')
	}
	
	@Test
	def test_04() {
		assertCompilesTo('''
			import java.util.Comparator
			class C {
				def m(Sortable<String> sortMe) {
					sortMe.sort(MyComparator.comparingInt2 [String s| s.length].thenComparing2[toUpperCase])
				}
				
				interface Sortable<T> extends Iterable<T> {
					def void sort(java.util.Comparator<? super T> c)
				}
				static abstract class MyComparator<T> implements Comparator<T> {
					def static <T> MyComparator<T> comparingInt2(ToIntFunction<? super T> fun) {}
					def <U extends Comparable<? super U>> MyComparator<T> thenComparing2((T)=>U f) {}
				}
				interface ToIntFunction<T> {
					def int apply(T t)
				}
			}
		''', '''
			import java.util.Comparator;
			import org.eclipse.xtext.xbase.lib.Functions.Function1;
			
			@SuppressWarnings("all")
			public class C {
			  public interface Sortable<T extends Object> extends Iterable<T> {
			    void sort(final Comparator<? super T> c);
			  }
			
			  public static abstract class MyComparator<T extends Object> implements Comparator<T> {
			    public static <T extends Object> C.MyComparator<T> comparingInt2(final C.ToIntFunction<? super T> fun) {
			      return null;
			    }
			
			    public <U extends Comparable<? super U>> C.MyComparator<T> thenComparing2(final Function1<? super T, ? extends U> f) {
			      return null;
			    }
			  }
			
			  public interface ToIntFunction<T extends Object> {
			    int apply(final T t);
			  }
			
			  public void m(final C.Sortable<String> sortMe) {
			    final C.ToIntFunction<String> _function = (String s) -> {
			      return s.length();
			    };
			    final Function1<String, String> _function_1 = (String it) -> {
			      return it.toUpperCase();
			    };
			    sortMe.sort(C.MyComparator.<String>comparingInt2(_function).<String>thenComparing2(_function_1));
			  }
			}
		''')
	}
	
	@Test
	def test_05() {
		assertCompilesTo('''
			import java.util.Comparator
			class C {
				def m(Sortable<String> sortMe) {
					sortMe.sort(MyComparator.comparingInt2 [String s| s.length].thenComparing2[it])
				}
				
				interface Sortable<T> extends Iterable<T> {
					def void sort(java.util.Comparator<? super T> c)
				}
				static abstract class MyComparator<T> implements Comparator<T> {
					def static <T> MyComparator<T> comparingInt2(ToIntFunction<? super T> fun) {}
					def <U extends Comparable<? super U>> MyComparator<T> thenComparing2((T)=>U f) {}
				}
				interface ToIntFunction<T> {
					def int apply(T t)
				}
			}
		''', '''
			import java.util.Comparator;
			import org.eclipse.xtext.xbase.lib.Functions.Function1;
			
			@SuppressWarnings("all")
			public class C {
			  public interface Sortable<T extends Object> extends Iterable<T> {
			    void sort(final Comparator<? super T> c);
			  }
			
			  public static abstract class MyComparator<T extends Object> implements Comparator<T> {
			    public static <T extends Object> C.MyComparator<T> comparingInt2(final C.ToIntFunction<? super T> fun) {
			      return null;
			    }
			
			    public <U extends Comparable<? super U>> C.MyComparator<T> thenComparing2(final Function1<? super T, ? extends U> f) {
			      return null;
			    }
			  }
			
			  public interface ToIntFunction<T extends Object> {
			    int apply(final T t);
			  }
			
			  public void m(final C.Sortable<String> sortMe) {
			    final C.ToIntFunction<String> _function = (String s) -> {
			      return s.length();
			    };
			    final Function1<String, String> _function_1 = (String it) -> {
			      return it;
			    };
			    sortMe.sort(C.MyComparator.<String>comparingInt2(_function).<String>thenComparing2(_function_1));
			  }
			}
		''')
	}
	
	@Test
	def test_06() {
		assertCompilesTo('''
			class C {
				def m() {
					MyComparator.comparingInt [String s| s.length].thenComparing[it]
				}
				
				static abstract class MyComparator<T> {
					def static <T> MyComparator<T> comparingInt(ToIntFunction<? super T> fun) {}
					def <U extends Comparable<? super U>> MyComparator<T> thenComparing(
						Function<? super T, ? extends U> keyExtractor,
						MyComparator<? super U> keyComparator)
					def MyComparator<T> thenComparing(MyComparator<? super T> other)
					def <U extends Comparable<? super U>> MyComparator<T> thenComparing(
						Function<? super T, ? extends U> keyExtractor)
				}
				interface ToIntFunction<T> {
					def int apply(T t)
				}
				interface Function<T, R> {
					def R apply(T t)
				}
			}
		''', '''
			@SuppressWarnings("all")
			public class C {
			  public static abstract class MyComparator<T extends Object> {
			    public static <T extends Object> C.MyComparator<T> comparingInt(final C.ToIntFunction<? super T> fun) {
			      return null;
			    }
			
			    public abstract <U extends Comparable<? super U>> C.MyComparator<T> thenComparing(final C.Function<? super T, ? extends U> keyExtractor, final C.MyComparator<? super U> keyComparator);
			
			    public abstract C.MyComparator<T> thenComparing(final C.MyComparator<? super T> other);
			
			    public abstract <U extends Comparable<? super U>> C.MyComparator<T> thenComparing(final C.Function<? super T, ? extends U> keyExtractor);
			  }
			
			  public interface ToIntFunction<T extends Object> {
			    int apply(final T t);
			  }
			
			  public interface Function<T extends Object, R extends Object> {
			    R apply(final T t);
			  }
			
			  public C.MyComparator<String> m() {
			    final C.ToIntFunction<String> _function = (String s) -> {
			      return s.length();
			    };
			    final C.Function<String, String> _function_1 = (String it) -> {
			      return it;
			    };
			    return C.MyComparator.<String>comparingInt(_function).<String>thenComparing(_function_1);
			  }
			}
		''')
	}
	
}
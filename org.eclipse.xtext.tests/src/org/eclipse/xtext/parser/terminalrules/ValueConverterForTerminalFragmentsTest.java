/*******************************************************************************
 * Copyright (c) 2010 itemis AG (http://www.itemis.eu) and others.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.xtext.parser.terminalrules;

import org.eclipse.xtext.conversion.IValueConverter;
import org.eclipse.xtext.conversion.IValueConverterService;
import org.eclipse.xtext.conversion.ValueConverter;
import org.eclipse.xtext.conversion.impl.AbstractNullSafeConverter;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.testing.AbstractXtextTests;
import org.junit.Test;

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 */
public class ValueConverterForTerminalFragmentsTest extends AbstractXtextTests {

	@Override
	public void setUp() throws Exception {
		super.setUp();
		with(TerminalRulesTestLanguageStandaloneSetup.class);
	}
	
	@Test public void testExceptionWhenRegistered() {
		IValueConverterService converters = get(ConverterForFragment.class);
		try {
			converters.toValue("foo", "STRING", null);
		} catch(IllegalStateException e) {
			assertTrue(e.getMessage().contains("ESCAPED_CHAR"));
		}
	}
	
	public static class ConverterForFragment extends TerminalRuleTestLanguageConverters {
		@ValueConverter(rule = "ESCAPED_CHAR")
		public IValueConverter<String> forFragment() {
			return new AbstractNullSafeConverter<String>() {
				@Override
				protected String internalToValue(String string, INode node) {
					return string;
				}

				@Override
				protected String internalToString(String value) {
					return value;
				}
			};
		}
	}
	
}

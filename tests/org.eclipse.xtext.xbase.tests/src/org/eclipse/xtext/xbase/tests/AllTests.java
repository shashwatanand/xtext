package org.eclipse.xtext.xbase.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

/*
 * automatically generated by org.eclipse.emf.mwe.releng.GenerateTestSuite 
 * 
 * @generated 
 */
public class AllTests {
   public static Test suite() {
      TestSuite suite = new TestSuite("Tests for org.eclipse.xtext.xbase.tests");
      suite.addTestSuite(org.eclipse.xtext.xbase.tests.parser.XbaseParserTest.class);
      suite.addTestSuite(org.eclipse.xtext.xbase.tests.typing.ExpressionsTypeResolverTest.class);
      suite.addTestSuite(org.eclipse.xtext.xbase.tests.typing.TypesServiceTest.class);
      return suite;
   }
}

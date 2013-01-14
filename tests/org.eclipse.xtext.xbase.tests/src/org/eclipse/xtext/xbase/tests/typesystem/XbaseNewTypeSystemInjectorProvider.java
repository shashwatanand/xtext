/*
* generated by Xtext
*/
package org.eclipse.xtext.xbase.tests.typesystem;

import java.lang.reflect.AnnotatedElement;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.common.types.JvmAnnotationTarget;
import org.eclipse.xtext.common.types.access.ClasspathTypeProviderFactory;
import org.eclipse.xtext.common.types.access.impl.ClasspathTypeProvider;
import org.eclipse.xtext.common.types.access.impl.DeclaredTypeFactory;
import org.eclipse.xtext.common.types.util.FeatureOverridesService;
import org.eclipse.xtext.common.types.util.TypeArgumentContextProvider;
import org.eclipse.xtext.common.types.util.VisibilityService;
import org.eclipse.xtext.linking.LinkingScopeProviderBinding;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.IScopeProvider;
import org.eclipse.xtext.service.SingletonBinding;
import org.eclipse.xtext.xbase.XbaseStandaloneSetup;
import org.eclipse.xtext.xbase.compiler.TypeReferenceSerializer;
import org.eclipse.xtext.xbase.compiler.TypeReferenceSerializer2;
import org.eclipse.xtext.xbase.compiler.XbaseCompiler;
import org.eclipse.xtext.xbase.compiler.XbaseCompiler2;
import org.eclipse.xtext.xbase.interpreter.IExpressionInterpreter;
import org.eclipse.xtext.xbase.interpreter.impl.XbaseInterpreter2;
import org.eclipse.xtext.xbase.resource.BatchLinkableResource;
import org.eclipse.xtext.xbase.scoping.batch.XbaseBatchScopeProvider;
import org.eclipse.xtext.xbase.tests.XbaseInjectorProvider;
import org.eclipse.xtext.xbase.typesystem.legacy.LegacyFeatureOverridesService;
import org.eclipse.xtext.xbase.typesystem.legacy.LegacyTypeArgumentContextProvider;
import org.eclipse.xtext.xbase.typesystem.legacy.LegacyVisibilityService;
import org.eclipse.xtext.xbase.typesystem.legacy.XbaseBatchTypeProvider;
import org.eclipse.xtext.xbase.typing.XbaseTypeProvider;
import org.eclipse.xtext.xbase.validation.XbaseJavaValidator2;
import org.eclipse.xtext.xbase.validation.XbaseJavaValidator;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * An injector provider for plain Xbase tests with the reworked type system infrastructure.
 * 
 * @author Sebastian Zarnekow - Initial contribution and API
 */
@SuppressWarnings("deprecation")
public class XbaseNewTypeSystemInjectorProvider extends XbaseInjectorProvider {

	@Override
	protected Injector internalCreateInjector() {
		return new XbaseNewTypeSystemTestStandaloneSetup().createInjectorAndDoEMFRegistration();
	}

	public static class XbaseNewTypeSystemTestStandaloneSetup extends XbaseStandaloneSetup {
		@Override
		public Injector createInjector() {
			return Guice.createInjector(new XbaseNewTypeSystemTestRuntimeModule());
		}
	}

	public static class XbaseNewTypeSystemTestRuntimeModule extends XbaseTestRuntimeModule {

		@Override
		public Class<? extends IScopeProvider> bindIScopeProvider() {
			return DisabledXbaseScopeProvider.class;
		}

		@Override
		public void configureLinkingIScopeProvider(Binder binder) {
			binder.bind(IScopeProvider.class).annotatedWith(LinkingScopeProviderBinding.class)
					.to(XbaseBatchScopeProvider.class);
		}

		public Class<? extends ClasspathTypeProviderFactory> bindClasspathTypeProviderFactory() {
			return ClasspathTypeProviderFactoryWithoutAnnotationValues.class;
		}

		@Override
		public Class<? extends XtextResource> bindXtextResource() {
			return BatchLinkableResource.class;
		}

		public Class<? extends XbaseTypeProvider> bindXbaseTypeProvider() {
			return XbaseBatchTypeProvider.class;
		}
		
		@Override
		public Class<? extends IExpressionInterpreter> bindIExpressionInterpreter() {
			return XbaseInterpreter2.class;
		}
		
		public Class<? extends XbaseCompiler> bindCompiler() {
			return XbaseCompiler2.class;
		}
		
		@Override
		@SingletonBinding(eager=true)	
		public Class<? extends XbaseJavaValidator> bindXbaseJavaValidator() {
			return null;
		}
		
		@SingletonBinding(eager=true)	
		public Class<? extends XbaseJavaValidator2> bindDefaultXbaseValidator() {
			return XbaseJavaValidator2.class;
		}
		
		public Class<? extends VisibilityService> bindVisibilityService() {
			return LegacyVisibilityService.class;
		}
		
		public Class<? extends FeatureOverridesService> bindFeatureOverridesService() {
			return LegacyFeatureOverridesService.class;
		}
		
		@Override
		public Class<? extends TypeArgumentContextProvider> bindTypeArgumentContextProvider() {
			return LegacyTypeArgumentContextProvider.class;
		}
		
		public Class<? extends TypeReferenceSerializer> bindTypeReferenceSerializer() {
			return TypeReferenceSerializer2.class;
		}
		
	}
	
	public static class DisabledXbaseScopeProvider extends org.eclipse.xtext.xbase.scoping.XbaseScopeProvider {
		@Override
		public IScope getScope(EObject context, EReference reference) {
			throw new UnsupportedOperationException();
		}
	}

	public static class ClasspathTypeProviderFactoryWithoutAnnotationValues extends ClasspathTypeProviderFactory {

		private static boolean skipAnnotationValues = false;
		
		public static void skipAnnotationValues() {
			skipAnnotationValues = true;	
		}
		
		public static void readAnnotationValues() {
			skipAnnotationValues = false;	
		}
		
		@Inject
		public ClasspathTypeProviderFactoryWithoutAnnotationValues(ClassLoader classLoader) {
			super(classLoader);
		}

		@Override
		protected ClasspathTypeProvider createClasspathTypeProvider(ResourceSet resourceSet) {
			return new ClasspathTypeProvider(getClassLoader(), resourceSet, getIndexedJvmTypeAccess()) {
				@Override
				protected DeclaredTypeFactory createDeclaredTypeFactory() {
					return new DeclaredTypeFactory(getClassURIHelper()) {
						@Override
						protected void createAnnotationValues(AnnotatedElement annotated, JvmAnnotationTarget result) {
							if (skipAnnotationValues)
								return;
							super.createAnnotationValues(annotated, result);
						}
					};
				}
			};
		}
	}

}

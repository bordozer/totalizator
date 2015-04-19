package totalizator.config;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import totalizator.config.root.DevelopmentConfiguration;
import totalizator.config.root.RootContextConfig;
import totalizator.config.root.SecurityConfig;
import totalizator.config.root.TestConfiguration;
import totalizator.config.servlet.ServletContextConfig;

import javax.servlet.Filter;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[]{ RootContextConfig.class, DevelopmentConfiguration.class, TestConfiguration.class, SecurityConfig.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[]{ ServletContextConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[]{ "/" };
	}

	@Override
	protected Filter[] getServletFilters() {

		final CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding( "UTF-8" );
		characterEncodingFilter.setForceEncoding( true );

		return new Filter[]{ characterEncodingFilter };
	}
}

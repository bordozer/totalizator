package totalizator.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import totalizator.config.root.DevelopmentConfiguration;
import totalizator.config.root.RootContextConfig;
import totalizator.config.root.TestConfiguration;
import totalizator.config.servlet.ServletContextConfig;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class<?>[]{ RootContextConfig.class, DevelopmentConfiguration.class, TestConfiguration.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class<?>[]{ ServletContextConfig.class };
	}

	@Override
	protected String[] getServletMappings() {
		return new String[]{ "/" };
	}
}

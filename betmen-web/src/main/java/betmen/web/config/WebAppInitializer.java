package betmen.web.config;

import betmen.web.config.root.DevelopmentConfiguration;
import betmen.web.config.root.RootContextConfig;
import betmen.web.config.root.SecurityConfig;
import betmen.web.config.root.TestConfiguration;
import betmen.web.config.root.TestDataConfiguration;
import betmen.web.config.servlet.RequestListener;
import betmen.web.config.servlet.ServletContextConfig;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{
                DevelopmentConfiguration.class,
                RootContextConfig.class,
                SecurityConfig.class,
                TestConfiguration.class,
                TestDataConfiguration.class
        };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{ServletContextConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    public void onStartup(final ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);

        servletContext.addListener(new SessionListener());
        servletContext.addListener(new RequestListener());
    }

    @Override
    protected Filter[] getServletFilters() {

        final CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);

        return new Filter[]{characterEncodingFilter};
    }
}

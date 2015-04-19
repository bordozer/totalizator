package totalizator.config;

import org.springframework.core.annotation.Order;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;

@Order(2)
public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

	/*@Override
	protected void beforeSpringSecurityFilterChain( ServletContext servletContext ) {
		final FilterRegistration.Dynamic characterEncodingFilter = servletContext.addFilter( "encodingFilter", new CharacterEncodingFilter() );
		characterEncodingFilter.setInitParameter( "encoding", "UTF-8" );
		characterEncodingFilter.setInitParameter( "forceEncoding", "true" );
		characterEncodingFilter.addMappingForUrlPatterns( null, false, "*//*" );
	}*/
}

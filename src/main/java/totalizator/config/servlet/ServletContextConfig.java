package totalizator.config.servlet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
@EnableWebMvc
@ComponentScan("totalizator.app.controllers")
public class ServletContextConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addResourceHandlers( ResourceHandlerRegistry registry ) {
		registry.addResourceHandler( "/resources/**" ).addResourceLocations( "/resources/" );
	}

	@Bean
	public ViewResolver viewResolver() {

		final InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass( JstlView.class );
		viewResolver.setPrefix( "/WEB-INF/jsp" );
		viewResolver.setSuffix( ".jsp" );

		return viewResolver;
	}
}

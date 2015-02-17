package totalizator.config.root;

import com.allanditzel.springframework.security.web.csrf.CsrfTokenResponseHeaderBindingFilter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.csrf.CsrfFilter;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final Logger LOGGER = Logger.getLogger( AppSecurityConfig.class );

	@Autowired
	private  SecurityUserDetailsService userDetailsService;

	@Override
	protected void configure( AuthenticationManagerBuilder auth ) throws Exception {
		auth.userDetailsService( userDetailsService ).passwordEncoder( new BCryptPasswordEncoder() );
	}

	@Override
	protected void configure( HttpSecurity http ) throws Exception {

		CsrfTokenResponseHeaderBindingFilter csrfTokenFilter = new CsrfTokenResponseHeaderBindingFilter();
		http.addFilterAfter( csrfTokenFilter, CsrfFilter.class );

		http
				.authorizeRequests()
				.antMatchers( "/resources/public/**" ).permitAll()
				.antMatchers( "/resources/img/**" ).permitAll()
				.antMatchers( "/resources/bower_components/**" ).permitAll()
				.antMatchers( HttpMethod.POST, "/user" ).permitAll()
				.anyRequest().authenticated()
				.and()
				.formLogin()
				.defaultSuccessUrl( "/resources/calories-tracker.html" )
				.loginProcessingUrl( "/authenticate" )
				.usernameParameter( "username" )
				.passwordParameter( "password" )
//				.successHandler( new AjaxAuthenticationSuccessHandler( new SavedRequestAwareAuthenticationSuccessHandler() ) )
				.loginPage( "/resources/public/login.html" )
				.and()
				.httpBasic()
				.and()
				.logout()
				.logoutUrl( "/logout" )
				.logoutSuccessUrl( "/resources/public/login.html" )
				.permitAll();

		if ( "true".equals( System.getProperty( "httpsOnly" ) ) ) {
			LOGGER.info( "launching the application in HTTPS-only mode" );
			http.requiresChannel().anyRequest().requiresSecure();
		}
	}
}

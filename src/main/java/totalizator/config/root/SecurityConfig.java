package totalizator.config.root;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import totalizator.app.security.AjaxAuthenticationSuccessHandler;
import totalizator.app.security.SecurityUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	public static final String PORTAL_PAGE_URL = "/totalizator/";

	private static final String LOGIN_PAGE_URL = "/resources/public/login.html";

	private static final Logger LOGGER = Logger.getLogger( SecurityConfig.class );

	@Autowired
	private SecurityUserDetailsService userDetailsService;

	@Override
	protected void configure( AuthenticationManagerBuilder auth ) throws Exception {
		auth
			.eraseCredentials( true )
			.userDetailsService( userDetailsService )
			.passwordEncoder( new BCryptPasswordEncoder() )
		;
	}

	@Override
	protected void configure( HttpSecurity http ) throws Exception {

		http
				.csrf().disable()
				.authorizeRequests()
					.antMatchers( "/resources/public/**" ).permitAll()
					.antMatchers( "/resources/img*//**" ).permitAll()
					.antMatchers( "/resources/bower_components*//**" ).permitAll()
					.antMatchers( "/rest/translator/" ).permitAll()
					.antMatchers( HttpMethod.PUT, "/rest/users/create/" ).permitAll()
					.antMatchers( "/admin/**" ).hasRole( "ADMIN" )
					.anyRequest()
					.authenticated()
					.and()
				.formLogin()
					.defaultSuccessUrl( PORTAL_PAGE_URL )
					.loginProcessingUrl( "/authenticate" )
					.usernameParameter( "login" )
					.passwordParameter( "password" )
					.successHandler( new AjaxAuthenticationSuccessHandler( new SavedRequestAwareAuthenticationSuccessHandler() ) )
					.failureUrl( "/login?error" ) // TODO: implement
					.loginPage( LOGIN_PAGE_URL )
					.and()
					.httpBasic()
					.and()
				.logout()
					.logoutUrl( "/logout" )
					.logoutSuccessUrl( LOGIN_PAGE_URL )
					.deleteCookies( "JSESSIONID" )
					.permitAll()
					.and()
				.rememberMe()
					.rememberMeServices( rememberMeServices() )
//					.key( "_spring_security_remember_me" )
//					.tokenValiditySeconds( 1209600 )
		;
	}

	@Bean
	public TokenBasedRememberMeServices rememberMeServices() {

		final TokenBasedRememberMeServices meServices = new TokenBasedRememberMeServices( "myAppKey", userDetailsService );
		meServices.setCookieName( "USER_ID_AUTO_SIGN_IN_TOO" );
		meServices.setTokenValiditySeconds( 1209600 );

		return meServices;
	}
}

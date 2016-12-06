package betmen.web.config.root;

import betmen.web.security.AuthFailureHandler;
import betmen.web.security.AuthSuccessHandler;
import betmen.web.security.HttpAuthenticationEntryPoint;
import betmen.web.security.HttpLogoutSuccessHandler;
import betmen.web.security.SecurityUserDetailsService;
import betmen.web.utils.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
@ComponentScan(value = "betmen.web.security")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final int TOKEN_VALIDITY_SECONDS = 60 * 60 * 24 * 14; // 14 days
    private static final int MAXIMUM_SESSIONS = 1024;

    @Autowired
    private SecurityUserDetailsService securityUserDetailsService;

    @Autowired
    private HttpAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private AuthSuccessHandler authSuccessHandler;

    @Autowired
    private AuthFailureHandler authFailureHandler;

    @Autowired
    private HttpLogoutSuccessHandler logoutSuccessHandler;

    @Autowired
    private DriverManagerDataSource dataSource;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception {
        return super.userDetailsServiceBean();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(securityUserDetailsService);
        authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());

        return authenticationProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .eraseCredentials(true)
                .userDetailsService(securityUserDetailsService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/resources/img/favicon.png").permitAll()
                .antMatchers("/betmen/**").permitAll()
                .antMatchers("/rest/translator/").permitAll()
                .antMatchers("/rest/app/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/rest/users/create/").permitAll()
                .antMatchers("/rest/**").authenticated()
                .antMatchers("/admin/**").hasRole("ADMIN").anyRequest().authenticated()
                .and()
                .authenticationProvider(authenticationProvider())
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                .formLogin()
                .loginPage(Parameters.LOGIN_PAGE_URL)
                .permitAll()
                .loginProcessingUrl(Parameters.LOGIN_END_POINT)
                .usernameParameter(Parameters.USERNAME)
                .passwordParameter(Parameters.PASSWORD)
                .defaultSuccessUrl(Parameters.PORTAL_PAGE_URL)
                .successHandler(authSuccessHandler)
                .failureHandler(authFailureHandler)
                .and()
                .logout()
                .permitAll()
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutSuccessHandler)
                .logoutSuccessUrl(Parameters.LOGIN_PAGE_URL)
                .invalidateHttpSession(true)
                .and()
                .rememberMe()
                .tokenRepository(rememberMeTokenRepository())
                .rememberMeServices(rememberMeServices())
                .key(Parameters.REMEMBER_ME_KEY)
                .and()
                .sessionManagement()
                .maximumSessions(MAXIMUM_SESSIONS)
        ;
    }

    @Bean
    public TokenBasedRememberMeServices rememberMeServices() {
        final TokenBasedRememberMeServices rememberMeServices = new TokenBasedRememberMeServices(Parameters.REMEMBER_ME_KEY, securityUserDetailsService);
        rememberMeServices.setTokenValiditySeconds(TOKEN_VALIDITY_SECONDS);
        return rememberMeServices;
    }

    @Bean
    public PersistentTokenRepository rememberMeTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        return jdbcTokenRepository;
    }
}

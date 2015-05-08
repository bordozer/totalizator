package totalizator.app.security;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import totalizator.app.dao.UserDao;
import totalizator.app.models.User;
import totalizator.app.services.SecurityService;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Service
public class SecurityUserDetailsService implements UserDetailsService {

	private static final Logger LOGGER = Logger.getLogger( SecurityUserDetailsService.class );

	@Autowired
	private UserDao userRepository;

	@Autowired
	private SecurityService securityService;

	@Override
	public UserDetails loadUserByUsername( final String login ) throws UsernameNotFoundException {
		final User user = userRepository.findByLogin( login );

		if ( user == null ) {
			LOGGER.debug ( String.format( "================================= User login not found: %s =================================", login ) );

			throw new UsernameNotFoundException( String.format( "Username not found: %s", login ) );
		}

		final boolean isAdmin = securityService.isAdmin( user );
		final String role = isAdmin ? "ROLE_ADMIN" : "ROLE_USER";

		final List<GrantedAuthority> authorities = newArrayList();
		authorities.add( new SimpleGrantedAuthority( role ) );

		return new org.springframework.security.core.userdetails.User( login, user.getPassword(), authorities );
	}
}

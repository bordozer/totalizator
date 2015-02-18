package totalizator.app.security;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import totalizator.app.dao.UserRepository;
import totalizator.app.models.User;

import java.util.ArrayList;
import java.util.List;

@Service
public class SecurityUserDetailsService implements UserDetailsService {

	private static final Logger LOGGER = Logger.getLogger( SecurityUserDetailsService.class );

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername( final String username ) throws UsernameNotFoundException {
		final User user = userRepository.findUserByName( username );

		if ( user == null ) {
			final String message = String.format( "======================= Username not found: %s ======================= ", username );
			LOGGER.info( message );

			throw new UsernameNotFoundException( message );
		}

		final List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add( new SimpleGrantedAuthority( "ROLE_USER" ) );

		return new org.springframework.security.core.userdetails.User( username, user.getPassword(), authorities );
	}
}

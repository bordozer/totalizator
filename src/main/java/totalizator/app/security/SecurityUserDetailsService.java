package totalizator.app.security;

import org.apache.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SecurityUserDetailsService implements UserDetailsService {

	private static final Logger LOGGER = Logger.getLogger( SecurityUserDetailsService.class );

	@Override
	public UserDetails loadUserByUsername( final String username ) throws UsernameNotFoundException {

		final List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add( new SimpleGrantedAuthority( "ROLE_USER" ) );

		return new org.springframework.security.core.userdetails.User( username, "password", authorities );
	}
}

package totalizator.app.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class SecurityUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername( final String username ) throws UsernameNotFoundException {
		throw new UsernameNotFoundException( "There is no user yet" );
	}
}

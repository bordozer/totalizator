package betmen.web.security;

import betmen.core.entity.User;
import betmen.core.service.SecurityService;
import betmen.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Service
public class SecurityUserDetailsService implements UserDetailsService {

	@Autowired
	private UserService userService;

	@Autowired
	private SecurityService securityService;

	@Override
	public UserDetails loadUserByUsername(final String login) throws UsernameNotFoundException {

		final User user = userService.findByLogin(login);

		if (user == null) {
			throw new UsernameNotFoundException(String.format("Username not found: %s", login));
		}

		final boolean isAdmin = securityService.isAdmin(user);
		final String role = isAdmin ? "ROLE_ADMIN" : "ROLE_USER";

		final List<GrantedAuthority> authorities = newArrayList();
		authorities.add(new SimpleGrantedAuthority(role));

		return new org.springframework.security.core.userdetails.User(login, user.getPassword(), authorities);
	}
}

package totalizator.app.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import totalizator.app.dao.UserRepository;
import totalizator.app.models.User;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger LOGGER = Logger.getLogger( UserServiceImpl.class );

	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional
	public void createUser( final String login, final String name, final String password ) {

		// TODO: validation

		final User user = new User();
		user.setLogin( login );
		user.setName( name );
		user.setPassword( encodePassword( password ) );

		userRepository.save( user );

		LOGGER.debug( String.format( "New user: %s, ( name: %s, password: %s )", login, name, password ) );
	}

	@Override
	public User findUserByName( final String name ) {
		return userRepository.findUserByName( name );
	}

	private String encodePassword( final String password ) {
		return new BCryptPasswordEncoder().encode( password );
	}
}

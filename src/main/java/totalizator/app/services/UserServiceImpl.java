package totalizator.app.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import totalizator.app.dao.UserRepository;
import totalizator.app.models.User;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger LOGGER = Logger.getLogger( UserServiceImpl.class );

	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional
	public void createUser( final String login, final String name, final String password ) {

		// TODO: validation

		final User user = new User( login, name, encodePassword( password ) );

		userRepository.save( user );

		LOGGER.debug( String.format( "New user: %s, ( name: %s, password: %s )", login, name, password ) );
	}

	@Override
	@Transactional( readOnly = true )
	public List<User> loadAll() {
		return userRepository.loadAll();
	}

	@Override
	@Transactional
	public User save( final User entry ) {
		return userRepository.save( entry );
	}

	@Override
	@Transactional( readOnly = true )
	public User load( final int id ) {
		return userRepository.load( id );
	}

	@Override
	@Transactional
	public void delete( final int id ) {
		userRepository.delete( id );
	}

	@Override
	@Transactional( readOnly = true )
	public User findByName( final String name ) {
		return userRepository.findByName( name );
	}

	@Override
	public User findByLogin( final String login ) {
		return userRepository.findByLogin( login );
	}

	@Override
	public String encodePassword( final String password ) {
		return new BCryptPasswordEncoder().encode( password );
	}
}

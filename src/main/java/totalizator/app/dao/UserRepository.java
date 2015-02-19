package totalizator.app.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import totalizator.app.models.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserRepository {

	private static final Logger LOGGER = Logger.getLogger( UserRepository.class );

	@PersistenceContext
	private EntityManager em;

	public void save( final User user ) {
		em.merge( user );
	}

	public User findUserByName( final String username ) {
		final List<User> users = em.createNamedQuery( User.FIND_BY_USERNAME, User.class )
				.setParameter( "username", username )
				.getResultList();

		return users.size() == 1 ? users.get( 0 ) : null;
	}

	public User findUserByLogin( final String login ) {
		final List<User> users = em.createNamedQuery( User.FIND_BY_LOGIN, User.class )
				.setParameter( "login", login )
				.getResultList();

		return users.size() == 1 ? users.get( 0 ) : null;
	}
}

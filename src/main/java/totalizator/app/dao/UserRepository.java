package totalizator.app.dao;

import org.springframework.stereotype.Repository;
import totalizator.app.models.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserRepository {

	@PersistenceContext
	private EntityManager em;

	public void save( final User user ) {
		em.merge( user );
	}

	public User findUserByName( final String name ) {
		final List<User> users = em.createNamedQuery( User.FIND_BY_USERNAME, User.class )
				.setParameter( "username", name )
				.getResultList();

		return users.size() == 1 ? users.get( 0 ) : null;
	}
}

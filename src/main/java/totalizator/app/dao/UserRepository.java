package totalizator.app.dao;

import org.springframework.stereotype.Repository;
import totalizator.app.models.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class UserRepository {

	@PersistenceContext
	private EntityManager em;

	public void save( final User user ) {
		em.merge( user );
	}
}

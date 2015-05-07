package totalizator.app.dao;

import org.apache.log4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import totalizator.app.models.User;
import totalizator.app.services.GenericService;
import totalizator.app.services.NamedEntityGenericService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserRepository implements GenericService<User>, NamedEntityGenericService<User> {

	private static final Logger LOGGER = Logger.getLogger( UserRepository.class );
	private static final String CACHE_USER = "totalizator.app.cache.user";

	@PersistenceContext
	private EntityManager em;

	@Override
//	@Cacheable( value = CACHE_USER )
	public List<User> loadAll() {
		return em.createNamedQuery( User.LOAD_ALL, User.class )
				.getResultList();
	}

	@Override
	@CacheEvict( value = CACHE_USER, key="#id" )
	public User save( final User user ) {
		return em.merge( user );
	}

	@Override
	@Cacheable( value = CACHE_USER, key="#id" )
	public User load( final int id ) {
		return em.find( User.class, id );
	}

	@Override
	@CacheEvict( value = CACHE_USER, key="#id" )
	public void delete( final int id ) {
		em.remove( load( id ) );
	}

	@Override
//	@Cacheable( value = CACHE_USER )
	public User findByName( final String name ) {
		final List<User> users = em.createNamedQuery( User.FIND_BY_USERNAME, User.class )
				.setParameter( "username", name )
				.getResultList();

		return users.size() == 1 ? users.get( 0 ) : null;
	}

//	@Cacheable( value = CACHE_USER )
	public User findByLogin( final String login ) {
		final List<User> users = em.createNamedQuery( User.FIND_BY_LOGIN, User.class )
				.setParameter( "login", login )
				.getResultList();

		return users.size() == 1 ? users.get( 0 ) : null;
	}
}

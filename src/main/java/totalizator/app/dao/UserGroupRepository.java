package totalizator.app.dao;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;
import totalizator.app.models.User;
import totalizator.app.models.UserGroup;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserGroupRepository implements UserGroupDao {

	@PersistenceContext
	private EntityManager em;

	@Override
	@Cacheable( value = CACHE_QUERY )
	public List<UserGroup> loadAll() {
		return em.createNamedQuery( UserGroup.LOAD_ALL, UserGroup.class )
				.getResultList();
	}

	@Override
	@Cacheable( value = CACHE_ENTRY, key="#id" )
	public UserGroup load( final int id ) {
		return em.find( UserGroup.class, id );
	}

	@Override
	@Caching( evict = {
		@CacheEvict( value = CACHE_ENTRY, key="#entry.id" )
		, @CacheEvict( value = CACHE_QUERY, allEntries = true )
	} )
	public UserGroup save( final UserGroup entry ) {
		return em.merge( entry );
	}

	@Override
	@Caching( evict = {
		@CacheEvict( value = CACHE_ENTRY, key="#id" )
		, @CacheEvict( value = CACHE_QUERY, allEntries = true )
	} )
	public void delete( final int id ) {
		em.remove( load( id ) );
	}

	@Override
	@Cacheable( value = CACHE_QUERY )
	public List<UserGroup> loadAllOwned( final User user ) {
		return em.createNamedQuery( UserGroup.LOAD_ALL_USER_OWNS, UserGroup.class )
				.setParameter( "userId", user.getId() )
				.getResultList();
	}

	@Override
	@Cacheable( value = CACHE_QUERY )
	public List<UserGroup> loadAll( final User user ) {
		return null; // TODO
	}
}

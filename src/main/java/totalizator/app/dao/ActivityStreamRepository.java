package totalizator.app.dao;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;
import totalizator.app.models.ActivityStreamEntry;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class ActivityStreamRepository implements ActivityStreamDao {

	@PersistenceContext
	private EntityManager em;

	@Override
	@Cacheable( value = CACHE_QUERY )
	public List<ActivityStreamEntry> loadAll() {
		return em.createNamedQuery( ActivityStreamEntry.LOAD_ALL, ActivityStreamEntry.class )
				.getResultList();
	}

	@Override
	@Cacheable( value = CACHE_ENTRY, key="#id" )
	public ActivityStreamEntry load( final int id ) {
		return em.find( ActivityStreamEntry.class, id );
	}

	@Override
	@Caching( evict = {
		@CacheEvict( value = CACHE_ENTRY, key="#entry.id" )
		, @CacheEvict( value = CACHE_QUERY, allEntries = true )
	} )
	public ActivityStreamEntry save( final ActivityStreamEntry entry ) {
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
	public List<ActivityStreamEntry> loadAll( final int userId ) {

		return em.createNamedQuery( ActivityStreamEntry.LOAD_ALL_FOR_USER, ActivityStreamEntry.class )
				.setParameter( "userId", userId )
				.getResultList();
	}

	@Override
	public ActivityStreamEntry loadByActivityEntryId( final int activityEntryId ) {

		final List<ActivityStreamEntry> list = em.createNamedQuery( ActivityStreamEntry.LOAD_ALL_FOR_USER, ActivityStreamEntry.class )
				.setParameter( "activityEntryId", activityEntryId )
				.getResultList();

		return list.size() == 1 ? list.get( 0 ) : null;
	}
}

package totalizator.app.dao;

import org.apache.log4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;
import totalizator.app.models.Cup;
import totalizator.app.services.GenericService;
import totalizator.app.services.NamedEntityGenericService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CupRepository implements GenericService<Cup>, NamedEntityGenericService<Cup> {

	private static final Logger LOGGER = Logger.getLogger( CupRepository.class );

	private static final String CACHE_ENTRY = "totalizator.app.cache.cup";
	private static final String CACHE_QUERY = "totalizator.app.cache.cups";

	@PersistenceContext
	private EntityManager em;

	@Override
	@Cacheable( value = CACHE_QUERY )
	public List<Cup> loadAll() {
		return em.createNamedQuery( Cup.LOAD_ALL, Cup.class )
				.getResultList();
	}

	@Override
	@Cacheable( value = CACHE_ENTRY, key="#id" )
	public Cup load( final int id ) {
		return em.find( Cup.class, id );
	}

	@Override
	@Caching( evict = {
		@CacheEvict( value = CACHE_ENTRY, key="#entry.id" )
		, @CacheEvict( value = CACHE_QUERY, allEntries = true )
	} )
	public Cup save( final Cup entry ) {
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
	public Cup findByName( final String name ) {
		final List<Cup> cups = em.createNamedQuery( Cup.FIND_BY_NAME, Cup.class )
				.setParameter( "cupName", name )
				.getResultList();

		return cups.size() == 1 ? cups.get( 0 ) : null;
	}
}

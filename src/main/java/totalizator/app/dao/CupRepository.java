package totalizator.app.dao;

import org.apache.log4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
	private static final String CACHE_CUP = "totalizator.app.cache.cup";

	@PersistenceContext
	private EntityManager em;

	@Override
//	@Cacheable( value = CACHE_CUP )
	public List<Cup> loadAll() {
		return em.createNamedQuery( Cup.LOAD_ALL, Cup.class )
				.getResultList();
	}

	@Override
	@CacheEvict( value = CACHE_CUP, key="#id" )
	public Cup save( final Cup entry ) {
		return em.merge( entry );
	}

	@Override
	@Cacheable( value = CACHE_CUP, key="#id" )
	public Cup load( final int id ) {
		return em.find( Cup.class, id );
	}

	@Override
	@CacheEvict( value = CACHE_CUP, key="#id" )
	public void delete( final int id ) {
		em.remove( load( id ) );
	}

	@Override
//	@Cacheable( value = CACHE_CUP )
	public Cup findByName( final String name ) {
		final List<Cup> cups = em.createNamedQuery( Cup.FIND_BY_NAME, Cup.class )
				.setParameter( "cupName", name )
				.getResultList();

		return cups.size() == 1 ? cups.get( 0 ) : null;
	}
}

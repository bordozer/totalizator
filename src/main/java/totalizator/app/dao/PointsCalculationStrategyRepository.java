package totalizator.app.dao;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;
import totalizator.app.models.PointsCalculationStrategy;
import totalizator.app.services.score.CupScoresService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class PointsCalculationStrategyRepository implements PointsCalculationStrategyDao {

	@PersistenceContext
	private EntityManager em;

	@Override
	@Cacheable( value = CACHE_QUERY )
	public List<PointsCalculationStrategy> loadAll() {
		return em.createNamedQuery( PointsCalculationStrategy.LOAD_ALL, PointsCalculationStrategy.class )
				.getResultList();
	}

	@Override
	@Cacheable( value = CACHE_ENTRY, key="#id" )
	public PointsCalculationStrategy load( final int id ) {
		return em.find( PointsCalculationStrategy.class, id );
	}

	@Override
	@Caching( evict = {
		@CacheEvict( value = CACHE_ENTRY, key="#entry.id" )
		, @CacheEvict( value = CACHE_QUERY, allEntries = true )
		, @CacheEvict( value = CupScoresService.CACHE_QUERY, allEntries = true )
		, @CacheEvict( value = CupDao.CACHE_ENTRY, allEntries = true )
		, @CacheEvict( value = CupDao.CACHE_QUERY, allEntries = true )
		, @CacheEvict( value = MatchBetDao.CACHE_QUERY, allEntries = true )
	} )
	public PointsCalculationStrategy save( final PointsCalculationStrategy entry ) {
		return em.merge( entry );
	}

	@Override
	@Caching( evict = {
		@CacheEvict( value = CACHE_ENTRY, key="#id" )
		, @CacheEvict( value = CACHE_QUERY, allEntries = true )
		, @CacheEvict( value = CupScoresService.CACHE_QUERY, allEntries = true )
		, @CacheEvict( value = CupDao.CACHE_ENTRY, allEntries = true )
		, @CacheEvict( value = CupDao.CACHE_QUERY, allEntries = true )
	} )
	public void delete( final int id ) {
		em.remove( load( id ) );
	}
}

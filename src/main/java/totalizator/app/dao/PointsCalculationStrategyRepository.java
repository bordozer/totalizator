package totalizator.app.dao;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;
import totalizator.app.models.PointsCalculationStrategy;
import totalizator.app.services.score.UserMatchPointsCalculationService;
import totalizator.app.services.score.UserCupWinnersBonusCalculationService;
import totalizator.app.services.score.UserMatchBetPointsCalculationService;
import totalizator.app.services.score.MatchBonusPointsCalculationService;

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
		, @CacheEvict( value = UserMatchPointsCalculationService.CACHE_QUERY, allEntries = true )
		, @CacheEvict( value = UserMatchBetPointsCalculationService.CACHE_QUERY, allEntries = true )
		, @CacheEvict( value = UserCupWinnersBonusCalculationService.CACHE_QUERY, allEntries = true )
		, @CacheEvict( value = MatchBonusPointsCalculationService.CACHE_QUERY, allEntries = true )
		, @CacheEvict( value = CupDao.CACHE_ENTRY, allEntries = true )
		, @CacheEvict( value = CupDao.CACHE_QUERY, allEntries = true )
		, @CacheEvict( value = MatchBetDao.CACHE_ENTRY, allEntries = true )
		, @CacheEvict( value = MatchBetDao.CACHE_QUERY, allEntries = true )
		, @CacheEvict( value = MatchDao.CACHE_ENTRY, allEntries = true )
		, @CacheEvict( value = MatchDao.CACHE_QUERY, allEntries = true )
	} )
	public PointsCalculationStrategy save( final PointsCalculationStrategy entry ) {
		return em.merge( entry );
	}

	@Override
	@Caching( evict = {
		@CacheEvict( value = CACHE_ENTRY, key="#id" )
		, @CacheEvict( value = CACHE_QUERY, allEntries = true )
		, @CacheEvict( value = UserMatchPointsCalculationService.CACHE_QUERY, allEntries = true )
		, @CacheEvict( value = UserMatchBetPointsCalculationService.CACHE_QUERY, allEntries = true )
		, @CacheEvict( value = UserCupWinnersBonusCalculationService.CACHE_QUERY, allEntries = true )
		, @CacheEvict( value = MatchBonusPointsCalculationService.CACHE_QUERY, allEntries = true )
		, @CacheEvict( value = CupDao.CACHE_ENTRY, allEntries = true )
		, @CacheEvict( value = CupDao.CACHE_QUERY, allEntries = true )
		, @CacheEvict( value = MatchBetDao.CACHE_ENTRY, allEntries = true )
		, @CacheEvict( value = MatchBetDao.CACHE_QUERY, allEntries = true )
		, @CacheEvict( value = MatchDao.CACHE_ENTRY, allEntries = true )
		, @CacheEvict( value = MatchDao.CACHE_QUERY, allEntries = true )
	} )
	public void delete( final int id ) {
		em.remove( load( id ) );
	}
}

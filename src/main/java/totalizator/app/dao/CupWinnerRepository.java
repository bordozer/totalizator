package totalizator.app.dao;

import org.apache.log4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;
import totalizator.app.models.Cup;
import totalizator.app.models.CupWinner;
import totalizator.app.models.Team;
import totalizator.app.services.score.UserBetPointsCalculationService;
import totalizator.app.services.score.UserCupWinnersBonusCalculationService;
import totalizator.app.services.score.UserMatchBetPointsCalculationService;
import totalizator.app.services.score.MatchBonusPointsCalculationService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CupWinnerRepository implements CupWinnerDao {

	private static final Logger LOGGER = Logger.getLogger( CupWinnerRepository.class );

	@PersistenceContext
	private EntityManager em;

	@Override
	@Cacheable( value = CACHE_QUERY )
	public List<CupWinner> loadAll( final Cup cup ) {
		return em.createNamedQuery( CupWinner.LOAD_FOR_CUP, CupWinner.class )
				.setParameter( "cupId", cup.getId() )
				.getResultList();
	}

	@Override
	@Cacheable( value = CACHE_QUERY )
	public List<CupWinner> loadAll() {
		return em.createNamedQuery( CupWinner.LOAD_ALL, CupWinner.class )
				.getResultList();
	}

	@Override
	@Cacheable( value = CACHE_ENTRY, key="#id" )
	public CupWinner load( final int id ) {
		return em.find( CupWinner.class, id );
	}

	@Override
	@Caching( evict = {
		@CacheEvict( value = CACHE_ENTRY, key="#entry.id" )
		, @CacheEvict( value = CACHE_QUERY, allEntries = true )
		, @CacheEvict( value = UserBetPointsCalculationService.CACHE_QUERY, allEntries = true )
		, @CacheEvict( value = UserMatchBetPointsCalculationService.CACHE_QUERY, allEntries = true )
		, @CacheEvict( value = UserCupWinnersBonusCalculationService.CACHE_QUERY, allEntries = true )
		, @CacheEvict( value = MatchBonusPointsCalculationService.CACHE_QUERY, allEntries = true )
	} )
	public CupWinner save( final CupWinner entry ) {
		return em.merge( entry );
	}

	@Override
	@Caching( evict = {
		@CacheEvict( value = CACHE_ENTRY, key="#id" )
		, @CacheEvict( value = CACHE_QUERY, allEntries = true )
		, @CacheEvict( value = UserBetPointsCalculationService.CACHE_QUERY, allEntries = true )
		, @CacheEvict( value = UserMatchBetPointsCalculationService.CACHE_QUERY, allEntries = true )
		, @CacheEvict( value = UserCupWinnersBonusCalculationService.CACHE_QUERY, allEntries = true )
		, @CacheEvict( value = MatchBonusPointsCalculationService.CACHE_QUERY, allEntries = true )
	} )
	public void delete( final int id ) {
		em.remove( load( id ) );
	}

	@Override
	@Cacheable( value = CACHE_QUERY )
	public CupWinner load( final Cup cup, final Team team ) {

		final List<CupWinner> list = em.createNamedQuery( CupWinner.LOAD_FOR_CUP_AND_TEAM, CupWinner.class )
				.setParameter( "cupId", cup.getId() )
				.setParameter( "teamId", team.getId() )
				.getResultList();

		return list != null && list.size() > 0 ? list.get( 0 ) : null;
	}

	@Override
	@Caching( evict = {
		@CacheEvict( value = CACHE_QUERY, allEntries = true )
		, @CacheEvict( value = UserBetPointsCalculationService.CACHE_QUERY, allEntries = true )
		, @CacheEvict( value = UserMatchBetPointsCalculationService.CACHE_QUERY, allEntries = true )
		, @CacheEvict( value = UserCupWinnersBonusCalculationService.CACHE_QUERY, allEntries = true )
		, @CacheEvict( value = MatchBonusPointsCalculationService.CACHE_QUERY, allEntries = true )
	} )
	public void saveAll( final List<CupWinner> winners ) {

		for ( final CupWinner winner : winners ) {
			save( winner );
		}
	}
}

package totalizator.app.dao;

import org.apache.log4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;
import totalizator.app.models.Match;
import totalizator.app.models.MatchBet;
import totalizator.app.models.User;
import totalizator.app.services.GenericService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MatchBetRepository implements GenericService<MatchBet> {

	private static final Logger LOGGER = Logger.getLogger( MatchBetRepository.class );

	private static final String CACHE_ENTRY = "totalizator.app.cache.match-bet";
	private static final String CACHE_QUERY = "totalizator.app.cache.match-bet.query";

	@PersistenceContext
	private EntityManager em;

	@Override
	@Cacheable( value = CACHE_QUERY )
	public List<MatchBet> loadAll() {
		return em.createNamedQuery( MatchBet.LOAD_ALL, MatchBet.class )
				.getResultList();
	}

	@Cacheable( value = CACHE_QUERY )
	public List<MatchBet> loadAll( final User user ) {
		return em.createNamedQuery( MatchBet.LOAD_FOR_USER, MatchBet.class )
				.setParameter( "userId", user.getId() )
				.getResultList();
	}

	@Cacheable( value = CACHE_QUERY )
	public List<MatchBet> loadAll( final Match match ) {
		return em.createNamedQuery( MatchBet.LOAD_FOR_MATCH, MatchBet.class )
				.setParameter( "matchId", match.getId() )
				.getResultList();
	}

	@Cacheable( value = CACHE_QUERY )
	public MatchBet load( final User user, final Match match ) {
		final List<MatchBet> bets = em.createNamedQuery( MatchBet.LOAD_FOR_USER_AND_MATCH, MatchBet.class )
				.setParameter( "userId", user.getId() )
				.setParameter( "matchId", match.getId() )
				.getResultList();

		return bets.size() == 1 ? bets.get( 0 ) : null;
	}

	@Override
	@Cacheable( value = CACHE_ENTRY, key="#id" )
	public MatchBet load( final int id ) {
		return em.find( MatchBet.class, id );
	}

	@Override
	@Caching( evict = {
		@CacheEvict( value = CACHE_ENTRY, key="#entry.id" )
		, @CacheEvict( value = CACHE_QUERY, allEntries = true )
	} )
	public MatchBet save( final MatchBet entry ) {
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

	@Cacheable( value = CACHE_QUERY )
	public int betsCount( final Match match ) {
		final List<Long> bets = em.createNamedQuery( MatchBet.LOAD_MATCH_BETA_COUNT, Long.class )
				.setParameter( "matchId", match.getId() )
				.getResultList();

		return bets.size() == 1 ? bets.get( 0 ).intValue() : 0;
	}
}

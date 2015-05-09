package totalizator.app.dao;

import org.apache.log4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;
import totalizator.app.models.Cup;
import totalizator.app.models.Match;
import totalizator.app.models.Team;
import totalizator.app.services.score.CupScoresService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MatchRepository implements MatchDao {

	private static final Logger LOGGER = Logger.getLogger( MatchRepository.class );

	@PersistenceContext
	private EntityManager em;

	@Override
	@Cacheable( value = CACHE_QUERY )
	public List<Match> loadAll() {
		return em.createNamedQuery( Match.LOAD_ALL, Match.class )
				.getResultList();
	}

	@Override
	@Cacheable( value = CACHE_QUERY )
	public List<Match> loadAll( final Cup cup ) {
		return em.createNamedQuery( Match.FIND_BY_CUP, Match.class )
				.setParameter( "cupId", cup.getId() )
				.getResultList();
	}

	@Override
	@Cacheable( value = CACHE_ENTRY, key="#id" )
	public Match load( final int id ) {
		return em.find( Match.class, id );
	}

	@Override
	@Caching( evict = {
		@CacheEvict( value = CACHE_ENTRY, key="#entry.id" )
		, @CacheEvict( value = CACHE_QUERY, allEntries = true )
		, @CacheEvict( value = CupScoresService.CACHE_QUERY, allEntries = true )
		, @CacheEvict( value = MatchBetDao.CACHE_QUERY, allEntries = true )
	} )
	public Match save( final Match entry ) {
		return em.merge( entry );
	}

	@Override
	@Caching( evict = {
		@CacheEvict( value = CACHE_ENTRY, key="#id" )
		, @CacheEvict( value = CACHE_QUERY, allEntries = true )
		, @CacheEvict( value = CupScoresService.CACHE_QUERY, allEntries = true )
		, @CacheEvict( value = MatchBetDao.CACHE_QUERY, allEntries = true )
	} )
	public void delete( final int id ) {
		em.remove( load( id ) );
	}

	@Override
	@Cacheable( value = CACHE_QUERY )
	public List<Match> find( final Team team1, final Team team2 ) {
		return em.createNamedQuery( Match.FIND_BY_TEAMS, Match.class )
				.setParameter( "team1Id", team1.getId() )
				.setParameter( "team2Id", team2.getId() )
				.getResultList();
	}
}

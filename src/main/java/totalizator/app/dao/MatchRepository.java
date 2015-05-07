package totalizator.app.dao;

import org.apache.log4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import totalizator.app.models.Cup;
import totalizator.app.models.Match;
import totalizator.app.models.Team;
import totalizator.app.services.GenericService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class MatchRepository implements GenericService<Match> {

	private static final Logger LOGGER = Logger.getLogger( MatchRepository.class );
	private static final String CACHE_MATCH = "totalizator.app.cache.match";

	@PersistenceContext
	private EntityManager em;

	@Override
	@Cacheable( value = CACHE_MATCH )
	public List<Match> loadAll() {
		return em.createNamedQuery( Match.LOAD_ALL, Match.class )
				.getResultList();
	}

	@Cacheable( value = CACHE_MATCH )
	public List<Match> loadAll( final Cup cup ) {
		return em.createNamedQuery( Match.FIND_BY_CUP, Match.class )
				.setParameter( "cupId", cup.getId() )
				.getResultList();
	}

	@Override
	@CacheEvict( value = CACHE_MATCH, key="#id" )
	public Match save( final Match entry ) {
		return em.merge( entry );
	}

	@Override
	@Cacheable( value = CACHE_MATCH, key="#id" )
	public Match load( final int id ) {
		return em.find( Match.class, id );
	}

	@Override
	@CacheEvict( value = CACHE_MATCH, key="#id" )
	public void delete( final int id ) {
		em.remove( load( id ) );
	}

	@Cacheable( value = CACHE_MATCH )
	public List<Match> find( final Team team1, final Team team2 ) {
		return em.createNamedQuery( Match.FIND_BY_TEAMS, Match.class )
				.setParameter( "team1Id", team1.getId() )
				.setParameter( "team2Id", team2.getId() )
				.getResultList();
	}
}

package totalizator.app.dao;

import org.apache.log4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;
import totalizator.app.models.Category;
import totalizator.app.models.Team;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class TeamRepository implements TeamDao {

	private static final Logger LOGGER = Logger.getLogger( TeamRepository.class );

	@PersistenceContext
	private EntityManager em;

	@Override
	@Cacheable( value = CACHE_QUERY )
	public List<Team> loadAll() {
		return em.createNamedQuery( Team.LOAD_ALL, Team.class )
				.getResultList();
	}

	@Override
	@Cacheable( value = CACHE_QUERY )
	public List<Team> loadAll( final Category category ) {
		return em.createNamedQuery( Team.FIND_BY_CATEGORY, Team.class )
				.setParameter( "categoryId", category.getId() )
				.getResultList();
	}

	@Override
	@Cacheable( value = CACHE_ENTRY, key = "#id" )
	public Team load( final int id ) {
		return em.find( Team.class, id );
	}

	@Override
	@Caching( evict = {
			@CacheEvict( value = CACHE_ENTRY, key = "#entry.id" )
			, @CacheEvict( value = CACHE_QUERY, allEntries = true )
			, @CacheEvict( value = MatchDao.CACHE_QUERY, allEntries = true )
	} )
	public Team save( final Team entry ) {
		return em.merge( entry );
	}

	@Override
	@Caching( evict = {
			@CacheEvict( value = CACHE_ENTRY, key = "#id" )
			, @CacheEvict( value = CACHE_QUERY, allEntries = true )
			, @CacheEvict( value = MatchDao.CACHE_QUERY, allEntries = true )
	} )
	public void delete( final int id ) {
		em.remove( load( id ) );
	}

	@Override
	@Cacheable( value = CACHE_QUERY )
	public Team findByName( final Category category, final String name ) {

		final List<Team> teams = em.createNamedQuery( Team.FIND_BY_NAME, Team.class )
				.setParameter( "teamName", name )
				.getResultList();

		if ( teams.size() == 0 ) {
			return null;
		}

		for ( final Team team : teams ) {
			if ( team.getCategory().equals( category ) ) {
				return team;
			}
		}

		return null;
	}

	@Override
	public Team findByImportId( final Category category, final String teamImportId ) {

		final List<Team> teams = em.createNamedQuery( Team.FIND_BY_TEAM_IMPORT_ID, Team.class )
				.setParameter( "teamImportId", teamImportId )
				.getResultList();

		if ( teams.size() == 0 ) {
			return null;
		}

		for ( final Team team : teams ) {
			if ( team.getCategory().equals( category ) ) {
				return team;
			}
		}

		return null;
	}
}

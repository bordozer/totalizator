package totalizator.app.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import totalizator.app.models.Category;
import totalizator.app.models.Team;
import totalizator.app.services.GenericService;
import totalizator.app.services.NamedEntityGenericService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class TeamRepository implements GenericService<Team>, NamedEntityGenericService<Team> {

	private static final Logger LOGGER = Logger.getLogger( TeamRepository.class );

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<Team> loadAll() {
		return em.createNamedQuery( Team.LOAD_ALL, Team.class )
				.getResultList();
	}

	public List<Team> loadAll( final Category category ) {
		return em.createNamedQuery( Team.FIND_BY_CATEGORY, Team.class )
				.setParameter( "categoryId", category.getId() )
				.getResultList();
	}

	@Override
	public Team save( final Team entry ) {
		return em.merge( entry );
	}

	@Override
	public Team load( final int id ) {
		return em.find( Team.class, id );
	}

	@Override
	public void delete( final int id ) {
		em.remove( load( id ) );
	}

	@Override
	public Team findByName( final String name ) {
		final List<Team> teams = em.createNamedQuery( Team.FIND_BY_NAME, Team.class )
				.setParameter( "teamName", name )
				.getResultList();

		return teams.size() == 1 ? teams.get( 0 ) : null;
	}
}

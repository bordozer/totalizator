package totalizator.app.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import totalizator.app.models.Cup;
import totalizator.app.models.TeamBet;
import totalizator.app.models.User;
import totalizator.app.services.GenericService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class TeamBetRepository implements GenericService<TeamBet> {

	private static final Logger LOGGER = Logger.getLogger( TeamBetRepository.class );

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<TeamBet> loadAll() {
		return em.createNamedQuery( TeamBet.LOAD_ALL, TeamBet.class )
				.getResultList();
	}

	@Override
	public TeamBet load( final int id ) {
		return em.find( TeamBet.class, id );
	}

	@Override
	public TeamBet save( final TeamBet entry ) {
		return em.merge( entry );
	}

	@Override
	public void delete( final int id ) {
		em.remove( load( id ) );
	}

	public List<TeamBet> load( final Cup cup ) {
		return em.createNamedQuery( TeamBet.LOAD_ALL_FOR_CUP, TeamBet.class )
				.setParameter( "cupId", cup.getId() )
				.getResultList();
	}

	public List<TeamBet> load( final User user ) {
		return em.createNamedQuery( TeamBet.LOAD_ALL_FOR_CUP, TeamBet.class )
				.setParameter( "userId", user.getId() )
				.getResultList();
	}

	public List<TeamBet> load( final Cup cup, final User user ) {
		return em.createNamedQuery( TeamBet.LOAD_ALL_FOR_CUP, TeamBet.class )
				.setParameter( "cupId", cup.getId() )
				.setParameter( "userId", user.getId() )
				.getResultList();
	}
}

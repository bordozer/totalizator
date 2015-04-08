package totalizator.app.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import totalizator.app.enums.CupPosition;
import totalizator.app.models.Cup;
import totalizator.app.models.CupTeamBet;
import totalizator.app.models.Team;
import totalizator.app.models.User;
import totalizator.app.services.GenericService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CupTeamBetRepository implements GenericService<CupTeamBet> {

	private static final Logger LOGGER = Logger.getLogger( CupTeamBetRepository.class );

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<CupTeamBet> loadAll() {
		return em.createNamedQuery( CupTeamBet.LOAD_ALL, CupTeamBet.class )
				.getResultList();
	}

	@Override
	public CupTeamBet load( final int id ) {
		return em.find( CupTeamBet.class, id );
	}

	@Override
	public CupTeamBet save( final CupTeamBet entry ) {
		return em.merge( entry );
	}

	@Override
	public void delete( final int id ) {
		em.remove( load( id ) );
	}

	public List<CupTeamBet> load( final Cup cup ) {
		return em.createNamedQuery( CupTeamBet.LOAD_ALL_FOR_CUP, CupTeamBet.class )
				.setParameter( "cupId", cup.getId() )
				.getResultList();
	}

	public List<CupTeamBet> load( final User user ) {
		return em.createNamedQuery( CupTeamBet.LOAD_ALL_FOR_CUP, CupTeamBet.class )
				.setParameter( "userId", user.getId() )
				.getResultList();
	}

	public List<CupTeamBet> load( final Cup cup, final User user ) {
		return em.createNamedQuery( CupTeamBet.LOAD_ALL_FOR_CUP, CupTeamBet.class )
				.setParameter( "cupId", cup.getId() )
				.setParameter( "userId", user.getId() )
				.getResultList();
	}

	public List<CupTeamBet> load( final Cup cup, final User user, final CupPosition cupPosition ) {
		return em.createNamedQuery( CupTeamBet.LOAD_ALL_FOR_CUP, CupTeamBet.class )
				.setParameter( "cupId", cup.getId() )
				.setParameter( "userId", user.getId() )
				.setParameter( "cupPosition", cupPosition.getId() )
				.getResultList();
	}

	public List<CupTeamBet> load( final Cup cup, final Team team ) {
		return em.createNamedQuery( CupTeamBet.LOAD_ALL_FOR_CUP, CupTeamBet.class )
				.setParameter( "cupId", cup.getId() )
				.setParameter( "teamId", team.getId() )
				.getResultList();
	}

	public List<CupTeamBet> load( final Cup cup, final Team team, final User user ) {
		return em.createNamedQuery( CupTeamBet.LOAD_ALL_FOR_CUP, CupTeamBet.class )
				.setParameter( "cupId", cup.getId() )
				.setParameter( "teamId", team.getId() )
				.setParameter( "userId", user.getId() )
				.getResultList();
	}
}

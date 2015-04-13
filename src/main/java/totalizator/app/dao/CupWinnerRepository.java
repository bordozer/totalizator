package totalizator.app.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import totalizator.app.models.Cup;
import totalizator.app.models.CupWinner;
import totalizator.app.models.Team;
import totalizator.app.services.GenericService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CupWinnerRepository implements GenericService<CupWinner> {

	private static final Logger LOGGER = Logger.getLogger( CupWinnerRepository.class );

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<CupWinner> loadAll() {
		return em.createNamedQuery( CupWinner.LOAD_ALL, CupWinner.class )
				.getResultList();
	}

	@Override
	public CupWinner load( final int id ) {
		return em.find( CupWinner.class, id );
	}

	@Override
	public CupWinner save( final CupWinner entry ) {
		return em.merge( entry );
	}

	@Override
	public void delete( final int id ) {
		em.remove( load( id ) );
	}

	public List<CupWinner> loadAll( final Cup cup ) {
		return em.createNamedQuery( CupWinner.LOAD_FOR_CUP, CupWinner.class )
				.setParameter( "cupId", cup.getId() )
				.getResultList();
	}

	public List<CupWinner> loadAll( final Cup cup, final Team team ) {
		return em.createNamedQuery( CupWinner.LOAD_FOR_CUP_AND_TEAM, CupWinner.class )
				.setParameter( "cupId", cup.getId() )
				.setParameter( "teamId", team.getId() )
				.getResultList();
	}

	public void deleteAllWinners( final Cup cup ) {
		em.createNamedQuery( CupWinner.DELETE_ALL_CUP_WINNERS, CupWinner.class )
				.setParameter( "cupId", cup.getId() )
				.getResultList();
	}

	public void saveAll( final List<CupWinner> winners ) {
		for ( final CupWinner winner : winners ) {
			save( winner );
		}
	}
}

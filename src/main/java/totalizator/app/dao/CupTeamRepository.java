package totalizator.app.dao;

import org.springframework.stereotype.Repository;
import totalizator.app.models.CupTeam;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CupTeamRepository implements CupTeamDao {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<CupTeam> loadAll() {
		return em.createNamedQuery( CupTeam.LOAD_ALL, CupTeam.class )
				.getResultList();
	}

	@Override
	public CupTeam load( final int id ) {
		return em.find( CupTeam.class, id );
	}

	@Override
	public CupTeam save( final CupTeam entry ) {
		return em.merge( entry );
	}

	@Override
	public void delete( final int id ) {
		em.remove( load( id ) );
	}

	@Override
	public List<CupTeam> loadAll( final int cupId ) {
		return em.createNamedQuery( CupTeam.LOAD_ALL_CUP_TEAMS, CupTeam.class )
				.setParameter( "cupId", cupId )
				.getResultList();
	}

	@Override
	public CupTeam load( final int cupId, final int teamId ) {

		final List<CupTeam> list = em.createNamedQuery( CupTeam.LOAD_CUP_TEAM, CupTeam.class )
				.setParameter( "teamId", teamId )
				.setParameter( "cupId", cupId )
				.getResultList();

		return list.size() == 1 ? list.get( 0 ) : null;
	}
}

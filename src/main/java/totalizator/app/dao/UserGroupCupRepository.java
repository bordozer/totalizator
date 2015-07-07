package totalizator.app.dao;

import org.springframework.stereotype.Repository;
import totalizator.app.models.UserGroup;
import totalizator.app.models.UserGroupCup;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserGroupCupRepository implements UserGroupCupDao {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<UserGroupCup> loadAll() {
		return em.createNamedQuery( UserGroupCup.LOAD_ALL_USER_GROUP_CUPS, UserGroupCup.class )
				.getResultList();
	}

	@Override
	public UserGroupCup load( final int id ) {
		return em.find( UserGroupCup.class, id );
	}

	@Override
	public List<UserGroupCup> loadAll( final UserGroup userGroup ) {
		return em.createNamedQuery( UserGroupCup.LOAD_CUPS_FOR_USER_GROUP, UserGroupCup.class )
				.setParameter( "userGroupId", userGroup.getId() )
				 .getResultList();
	}

	@Override
	public UserGroupCup save( final UserGroupCup entry ) {
		return em.merge( entry );
	}

	@Override
	public void delete( final int id ) {
		em.remove( load( id ) );
	}

	@Override
	public void delete( final UserGroupCup userGroupCup ) {
		em.remove( userGroupCup );
	}

	@Override
	public void deleteAll( final UserGroup userGroup ) {
		for ( final UserGroupCup userGroupCup : loadAll( userGroup ) ) {
			this.delete( userGroupCup );
		}
	}
}

package totalizator.app.dao;

import org.springframework.stereotype.Repository;
import totalizator.app.models.UserGroup;
import totalizator.app.models.UserGroupMember;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserGroupMemberRepository implements UserGroupUserDao {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<UserGroupMember> loadAll() {
		return em.createNamedQuery( UserGroupMember.LOAD_ALL, UserGroupMember.class )
				.getResultList();
	}

	@Override
	public UserGroupMember load( final int id ) {
		return em.find( UserGroupMember.class, id );
	}

	@Override
	public UserGroupMember save( final UserGroupMember entry ) {
		return em.merge( entry );
	}

	@Override
	public void delete( final int id ) {
		em.remove( load( id ) );
	}

	@Override
	public List<UserGroupMember> loadUsers( final UserGroup userGroup ) {
		return em.createNamedQuery( UserGroupMember.LOAD_ALL_USER_GROUP_MEMBERS, UserGroupMember.class )
				.setParameter( "userGroupId", userGroup.getId() )
				 .getResultList();
	}
}

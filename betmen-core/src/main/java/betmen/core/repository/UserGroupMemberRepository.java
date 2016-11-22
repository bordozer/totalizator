package betmen.core.repository;

import betmen.core.entity.User;
import betmen.core.entity.UserGroup;
import betmen.core.entity.UserGroupMember;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserGroupMemberRepository implements UserGroupMemberDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<UserGroupMember> loadAll() {
        return em.createNamedQuery(UserGroupMember.LOAD_ALL, UserGroupMember.class)
                .getResultList();
    }

    @Override
    public UserGroupMember load(final int id) {
        return em.find(UserGroupMember.class, id);
    }

    @Override
    public UserGroupMember save(final UserGroupMember entry) {
        return em.merge(entry);
    }

    @Override
    public void delete(final int id) {
        em.remove(load(id));
    }

    @Override
    public List<UserGroupMember> loadUserGroupMembers(final UserGroup userGroup) {
        return em.createNamedQuery(UserGroupMember.LOAD_USER_GROUPS_MEMBERS, UserGroupMember.class)
                .setParameter("userGroupId", userGroup.getId())
                .getResultList();
    }

    @Override
    public List<UserGroupMember> loadUserGroupsWhereUserIsMember(final int userId) {
        return em.createNamedQuery(UserGroupMember.LOAD_USER_GROUPS_WHERE_USER_IS_MEMBER, UserGroupMember.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<UserGroupMember> loadUserGroupsWhereUserIsMember(final int userId, final int cupId) {
        return em.createNativeQuery(UserGroupMember.LOAD_USER_GROUPS_FOR_CUP_WHERE_WHERE_USER_IS_MEMBER_SQL, UserGroupMember.class)
                .setParameter("userId", userId)
                .setParameter("cupId", cupId)
                .getResultList();
    }

    @Override
    public UserGroupMember load(final UserGroup userGroup, final User user) {
        final List<UserGroupMember> resultList = em.createNamedQuery(UserGroupMember.LOAD_USER_GROUPS_MEMBER_ENTRY, UserGroupMember.class)
                .setParameter("userGroupId", userGroup.getId())
                .setParameter("userId", user.getId())
                .getResultList();

        return resultList == null || resultList.size() == 0 ? null : resultList.get(0);
    }

    @Override
    public void delete(final UserGroup userGroup, final User user) {
        final UserGroupMember userGroupMember = load(userGroup, user);

        if (userGroupMember != null) {
            em.remove(userGroupMember);
        }
    }

    @Override
    public void deleteAll(final UserGroup userGroup) {
        for (final UserGroupMember userGroupMember : loadUserGroupMembers(userGroup)) {
            delete(userGroup, userGroupMember.getUser());
        }
    }
}

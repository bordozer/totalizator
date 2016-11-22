package betmen.core.repository;

import betmen.core.entity.User;
import betmen.core.entity.UserGroupEntity;
import betmen.core.entity.UserGroupMemberEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserGroupMemberRepository implements UserGroupMemberDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<UserGroupMemberEntity> loadAll() {
        return em.createNamedQuery(UserGroupMemberEntity.LOAD_ALL, UserGroupMemberEntity.class)
                .getResultList();
    }

    @Override
    public UserGroupMemberEntity load(final int id) {
        return em.find(UserGroupMemberEntity.class, id);
    }

    @Override
    public UserGroupMemberEntity save(final UserGroupMemberEntity entry) {
        return em.merge(entry);
    }

    @Override
    public void delete(final int id) {
        em.remove(load(id));
    }

    @Override
    public List<UserGroupMemberEntity> loadUserGroupMembers(final UserGroupEntity userGroupEntity) {
        return em.createNamedQuery(UserGroupMemberEntity.LOAD_USER_GROUPS_MEMBERS, UserGroupMemberEntity.class)
                .setParameter("userGroupId", userGroupEntity.getId())
                .getResultList();
    }

    @Override
    public List<UserGroupMemberEntity> loadUserGroupsWhereUserIsMember(final int userId) {
        return em.createNamedQuery(UserGroupMemberEntity.LOAD_USER_GROUPS_WHERE_USER_IS_MEMBER, UserGroupMemberEntity.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<UserGroupMemberEntity> loadUserGroupsWhereUserIsMember(final int userId, final int cupId) {
        return em.createNativeQuery(UserGroupMemberEntity.LOAD_USER_GROUPS_FOR_CUP_WHERE_WHERE_USER_IS_MEMBER_SQL, UserGroupMemberEntity.class)
                .setParameter("userId", userId)
                .setParameter("cupId", cupId)
                .getResultList();
    }

    @Override
    public UserGroupMemberEntity load(final UserGroupEntity userGroupEntity, final User user) {
        final List<UserGroupMemberEntity> resultList = em.createNamedQuery(UserGroupMemberEntity.LOAD_USER_GROUPS_MEMBER_ENTRY, UserGroupMemberEntity.class)
                .setParameter("userGroupId", userGroupEntity.getId())
                .setParameter("userId", user.getId())
                .getResultList();

        return resultList == null || resultList.size() == 0 ? null : resultList.get(0);
    }

    @Override
    public void delete(final UserGroupEntity userGroupEntity, final User user) {
        final UserGroupMemberEntity userGroupMemberEntity = load(userGroupEntity, user);

        if (userGroupMemberEntity != null) {
            em.remove(userGroupMemberEntity);
        }
    }

    @Override
    public void deleteAll(final UserGroupEntity userGroupEntity) {
        for (final UserGroupMemberEntity userGroupMemberEntity : loadUserGroupMembers(userGroupEntity)) {
            delete(userGroupEntity, userGroupMemberEntity.getUser());
        }
    }
}

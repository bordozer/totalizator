package betmen.core.repository;

import betmen.core.entity.UserGroupEntity;
import betmen.core.entity.UserGroupCupEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserGroupCupRepository implements UserGroupCupDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<UserGroupCupEntity> loadAll() {
        return em.createNamedQuery(UserGroupCupEntity.LOAD_ALL_USER_GROUP_CUPS, UserGroupCupEntity.class)
                .getResultList();
    }

    @Override
    public UserGroupCupEntity load(final int id) {
        return em.find(UserGroupCupEntity.class, id);
    }

    @Override
    public List<UserGroupCupEntity> loadCups(final UserGroupEntity userGroupEntity) {
        return em.createNamedQuery(UserGroupCupEntity.LOAD_CUPS_FOR_USER_GROUP, UserGroupCupEntity.class)
                .setParameter("userGroupId", userGroupEntity.getId())
                .getResultList();
    }

    @Override
    public UserGroupCupEntity save(final UserGroupCupEntity entry) {
        return em.merge(entry);
    }

    @Override
    public void delete(final int id) {
        em.remove(load(id));
    }

    @Override
    public void delete(final UserGroupCupEntity userGroupCupEntity) {
        em.remove(userGroupCupEntity);
    }

    @Override
    public void deleteAll(final UserGroupEntity userGroupEntity) {
        for (final UserGroupCupEntity userGroupCupEntity : loadCups(userGroupEntity)) {
            this.delete(userGroupCupEntity);
        }
    }
}

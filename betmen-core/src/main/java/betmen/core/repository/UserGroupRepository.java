package betmen.core.repository;

import betmen.core.entity.UserGroupEntity;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserGroupRepository implements UserGroupDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Cacheable(value = CACHE_QUERY)
    public List<UserGroupEntity> loadAll() {
        return em.createNamedQuery(UserGroupEntity.LOAD_ALL, UserGroupEntity.class)
                .getResultList();
    }

    @Override
    @Cacheable(value = CACHE_ENTRY, key = "#id")
    public UserGroupEntity load(final int id) {
        return em.find(UserGroupEntity.class, id);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = CACHE_ENTRY, key = "#entry.id")
            , @CacheEvict(value = CACHE_QUERY, allEntries = true)
    })
    public UserGroupEntity save(final UserGroupEntity entry) {
        return em.merge(entry);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = CACHE_ENTRY, key = "#id")
            , @CacheEvict(value = CACHE_QUERY, allEntries = true)
    })
    public void delete(final int id) {
        em.remove(load(id));
    }

    @Override
    public List<UserGroupEntity> loadUserGroupsWhereUserIsOwner(final int userId, final int cupId) {
        return em.createNativeQuery(UserGroupEntity.LOAD_FOR_CUP_WHERE_USER_IS_OWNER, UserGroupEntity.class)
                .setParameter("userId", userId)
                .setParameter("cupId", cupId)
                .getResultList();
    }
}

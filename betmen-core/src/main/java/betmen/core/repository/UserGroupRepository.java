package betmen.core.repository;

import betmen.core.entity.UserGroup;
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
    public List<UserGroup> loadAll() {
        return em.createNamedQuery(UserGroup.LOAD_ALL, UserGroup.class)
                .getResultList();
    }

    @Override
    @Cacheable(value = CACHE_ENTRY, key = "#id")
    public UserGroup load(final int id) {
        return em.find(UserGroup.class, id);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = CACHE_ENTRY, key = "#entry.id")
            , @CacheEvict(value = CACHE_QUERY, allEntries = true)
    })
    public UserGroup save(final UserGroup entry) {
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
    @Cacheable(value = CACHE_QUERY)
    public List<UserGroup> loadUserGroupsWhereUserIsOwner(final int userId) {
        return em.createNamedQuery(UserGroup.LOAD_ALL_WHERE_USER_IS_OWNER, UserGroup.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<UserGroup> loadUserGroupsWhereUserIsOwner(final int userId, final int cupId) {
        return em.createNativeQuery(UserGroup.LOAD_FOR_CUP_WHERE_USER_IS_OWNER, UserGroup.class)
                .setParameter("userId", userId)
                .setParameter("cupId", cupId)
                .getResultList();
    }
}

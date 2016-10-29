package betmen.core.repository;

import betmen.core.entity.FavoriteCategory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class FavoriteCategoryRepository implements FavoriteCategoryDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Cacheable(value = CACHE_QUERY)
    public List<FavoriteCategory> loadAll() {
        return em.createNamedQuery(FavoriteCategory.LOAD_ALL, FavoriteCategory.class)
                .getResultList();
    }

    @Override
    @Cacheable(value = CACHE_ENTRY, key = "#id")
    public FavoriteCategory load(final int id) {
        return em.find(FavoriteCategory.class, id);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = CACHE_ENTRY, key = "#entry.id")
            , @CacheEvict(value = CACHE_QUERY, allEntries = true)
    })
    public FavoriteCategory save(final FavoriteCategory entry) {
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
    public List<FavoriteCategory> loadAllForUser(final int userId) {
        return em.createNamedQuery(FavoriteCategory.LOAD_FOR_USER, FavoriteCategory.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    @Cacheable(value = CACHE_QUERY)
    public FavoriteCategory find(final int userId, final int categoryId) {

        final List<FavoriteCategory> list = em.createNamedQuery(FavoriteCategory.LOAD_FOR_USER_AND_CATEGORY, FavoriteCategory.class)
                .setParameter("userId", userId)
                .setParameter("categoryId", categoryId)
                .getResultList();

        return list.size() == 1 ? list.get(0) : null;
    }
}

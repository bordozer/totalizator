package betmen.core.repository;

import betmen.core.entity.Category;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CategoryRepository implements CategoryDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Cacheable(value = CACHE_QUERY)
    public List<Category> loadAll() {
        return em.createNamedQuery(Category.LOAD_ALL, Category.class)
                .getResultList();
    }

    @Override
    @Cacheable(value = CACHE_ENTRY, key = "#id")
    public Category load(final int id) {
        return em.find(Category.class, id);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = CACHE_ENTRY, key = "#entry.id")
            , @CacheEvict(value = CACHE_QUERY, allEntries = true)
            , @CacheEvict(value = CupDao.CACHE_ENTRY, allEntries = true)
            , @CacheEvict(value = CupDao.CACHE_QUERY, allEntries = true)
    })
    public Category save(final Category entry) {
        return em.merge(entry);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = CACHE_ENTRY, key = "#id")
            , @CacheEvict(value = CACHE_QUERY, allEntries = true)
            , @CacheEvict(value = CupDao.CACHE_ENTRY, allEntries = true)
            , @CacheEvict(value = CupDao.CACHE_QUERY, allEntries = true)
    })
    public void delete(final int id) {
        em.remove(load(id));
    }

    @Override
    @Cacheable(value = CACHE_QUERY)
    public Category findByName(final String categoryName) {
        final List<Category> categories = em.createNamedQuery(Category.FIND_BY_NAME, Category.class)
                .setParameter("categoryName", categoryName)
                .getResultList();

        return categories.size() == 1 ? categories.get(0) : null;
    }

    @Override
    public List<Category> loadAll(final int sportKindId) {
        return em.createNamedQuery(Category.FIND_BY_SPORT_KIND, Category.class)
                .setParameter("sportKindId", sportKindId)
                .getResultList();
    }
}

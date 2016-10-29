package betmen.core.repository;

import betmen.core.entity.Cup;
import betmen.core.entity.PointsCalculationStrategy;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class CupRepository implements CupDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Cacheable(value = CACHE_QUERY)
    public List<Cup> loadAll() {
        return em.createNamedQuery(Cup.LOAD_ALL, Cup.class)
                .getResultList();
    }

    @Override
    @Cacheable(value = CACHE_ENTRY, key = "#id")
    public Cup load(final int id) {
        return em.find(Cup.class, id);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = CACHE_ENTRY, key = "#entry.id")
            , @CacheEvict(value = CACHE_QUERY, allEntries = true)
            , @CacheEvict(value = MatchBetDao.CACHE_ENTRY, allEntries = true)
            , @CacheEvict(value = MatchBetDao.CACHE_QUERY, allEntries = true)
            , @CacheEvict(value = MatchDao.CACHE_ENTRY, allEntries = true)
            , @CacheEvict(value = MatchDao.CACHE_QUERY, allEntries = true)
    })
    public Cup save(final Cup entry) {
        return em.merge(entry);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = CACHE_ENTRY, key = "#id")
            , @CacheEvict(value = CACHE_QUERY, allEntries = true)
            , @CacheEvict(value = MatchBetDao.CACHE_ENTRY, allEntries = true)
            , @CacheEvict(value = MatchBetDao.CACHE_QUERY, allEntries = true)
            , @CacheEvict(value = MatchDao.CACHE_ENTRY, allEntries = true)
            , @CacheEvict(value = MatchDao.CACHE_QUERY, allEntries = true)
    })
    public void delete(final int id) {
        em.remove(load(id));
    }

    @Override
    @Cacheable(value = CACHE_QUERY)
    public Cup findByName(final String name) {
        final List<Cup> cups = em.createNamedQuery(Cup.FIND_BY_NAME, Cup.class)
                .setParameter("cupName", name)
                .getResultList();

        return cups.size() == 1 ? cups.get(0) : null;
    }

    @Override
    @Cacheable(value = CACHE_QUERY)
    public List<Cup> loadCups(final PointsCalculationStrategy strategy) {

        return em.createNamedQuery(Cup.LOAD_ALL_USE_STRATEGY, Cup.class)
                .setParameter("strategyId", strategy.getId())
                .getResultList();
    }
}

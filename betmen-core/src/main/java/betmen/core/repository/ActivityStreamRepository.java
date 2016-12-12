package betmen.core.repository;

import betmen.core.entity.ActivityStreamEntry;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ActivityStreamRepository implements ActivityStreamDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Cacheable(value = CACHE_ENTRY, key = "#id")
    public ActivityStreamEntry load(final int id) {
        return em.find(ActivityStreamEntry.class, id);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = CACHE_ENTRY, key = "#entry.id")
            , @CacheEvict(value = CACHE_QUERY, allEntries = true)
    })
    public ActivityStreamEntry save(final ActivityStreamEntry entry) {
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
    public ActivityStreamEntry loadByActivityEntryId(final int activityEntryId) {

        final List<ActivityStreamEntry> list = em.createNamedQuery(ActivityStreamEntry.LOAD_ALL_FOR_USER, ActivityStreamEntry.class)
                .setParameter("activityEntryId", activityEntryId)
                .getResultList();

        return list.size() == 1 ? list.get(0) : null;
    }

    @Override
    public List<ActivityStreamEntry> loadAllEarlierThen(final LocalDateTime time) {

        return em.createNamedQuery(ActivityStreamEntry.LOAD_ALL_EARLIER_THEN, ActivityStreamEntry.class)
                .setParameter("activityTime", time)
                .getResultList();
    }

    @Override
    @Cacheable(value = CACHE_QUERY)
    public List<ActivityStreamEntry> loadAllForMatch(final int matchId) {

        return em.createNamedQuery(ActivityStreamEntry.LOAD_ALL_FOR_MATCH, ActivityStreamEntry.class)
                .setParameter("activityEntryId", matchId)
                .getResultList();
    }

    @Override
//	@Cacheable( value = CACHE_QUERY )
    public List<ActivityStreamEntry> loadAllForUser(final int userId, final int qty) {

        return em.createNamedQuery(ActivityStreamEntry.LOAD_ALL_FOR_USER, ActivityStreamEntry.class)
                .setParameter("userId", userId)
                .setFirstResult(0)
                .setMaxResults(qty)
                .getResultList();
    }
}

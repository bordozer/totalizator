package betmen.core.repository;

import betmen.core.entity.JobLog;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class JobLogRepository implements JobLogDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Cacheable(value = CACHE_QUERY)
    public List<JobLog> loadAll() {
        return em.createNamedQuery(JobLog.LOAD_ALL, JobLog.class)
                .getResultList();
    }

    @Override
    @Cacheable(value = CACHE_ENTRY, key = "#id")
    public JobLog load(final int id) {
        return em.find(JobLog.class, id);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = CACHE_ENTRY, key = "#entry.id")
            , @CacheEvict(value = CACHE_QUERY, allEntries = true)
    })
    public JobLog save(final JobLog entry) {
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
    public List<JobLog> loadAll(final int jobTaskId) {
        return em.createNamedQuery(JobLog.LOAD_ALL_FRO_JOB_TASK, JobLog.class)
                .setParameter("jobTaskId", jobTaskId)
                .getResultList();
    }
}

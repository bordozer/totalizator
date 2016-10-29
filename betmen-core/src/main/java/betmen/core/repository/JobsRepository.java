package betmen.core.repository;

import betmen.core.entity.Job;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class JobsRepository implements JobsDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Job> loadAll() {
        return em.createNamedQuery(Job.LOAD_ALL, Job.class)
                .getResultList();
    }

    @Override
    public Job load(final int id) {
        return em.find(Job.class, id);
    }

    @Override
    public Job save(final Job entry) {
        return em.merge(entry);
    }

    @Override
    public void delete(final int id) {
        em.remove(load(id));
    }
}

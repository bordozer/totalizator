package betmen.core.repository;

import betmen.core.entity.Cup;
import betmen.core.entity.Match;
import betmen.core.entity.MatchPoints;
import betmen.core.entity.User;
import betmen.core.entity.UserGroupEntity;
import betmen.core.service.points.CupPointsService;
import betmen.core.service.points.MatchPointsService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class MatchPointsRepository implements MatchPointsDao {

    @PersistenceContext
    private EntityManager em;


    @Override
    @Cacheable(value = CACHE_QUERY)
    public List<MatchPoints> loadAll() {
        return em.createNamedQuery(MatchPoints.LOAD_ALL, MatchPoints.class)
                .getResultList();
    }

    @Override
    @Cacheable(value = CACHE_ENTRY, key = "#id")
    public MatchPoints load(final int id) {
        return em.find(MatchPoints.class, id);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = CACHE_ENTRY, key = "#entry.id")
            , @CacheEvict(value = CACHE_QUERY, allEntries = true)
            , @CacheEvict(value = MatchPointsService.CACHE_QUERY, allEntries = true)
            , @CacheEvict(value = CupPointsService.CACHE_QUERY, allEntries = true)
    })
    public MatchPoints save(final MatchPoints entry) {
        return em.merge(entry);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = CACHE_ENTRY, key = "#id")
            , @CacheEvict(value = CACHE_QUERY, allEntries = true)
            , @CacheEvict(value = MatchPointsService.CACHE_QUERY, allEntries = true)
            , @CacheEvict(value = CupPointsService.CACHE_QUERY, allEntries = true)
    })
    public void delete(final int id) {
        em.remove(load(id));
    }

    @Override
    public void delete(final Match match) {

        em.createQuery("delete from MatchPoints where matchId = :matchId")
                .setParameter("matchId", match.getId())
                .executeUpdate();
    }

    @Override
    public void delete(final Cup cup) {

        em.createQuery("delete from MatchPoints where cupId = :cupId and userGroupId = :userGroupId")
                .setParameter("cupId", cup.getId())
                .executeUpdate();
    }

    @Override
    public void delete(final UserGroupEntity userGroupEntity) {

        em.createQuery("delete from MatchPoints where userGroupId = :userGroupId")
                .setParameter("userGroupId", userGroupEntity.getId())
                .executeUpdate();
    }

    @Override
    @Cacheable(value = CACHE_QUERY)
    public List<MatchPoints> loadAll(final Match match) {

        return em.createNamedQuery(MatchPoints.LOAD_ALL_FOR_MATCH, MatchPoints.class)
                .setParameter("matchId", match.getId())
                .getResultList();
    }

    @Override
    @Cacheable(value = CACHE_QUERY)
    public List<MatchPoints> loadAll(final Match match, final UserGroupEntity userGroupEntity) {

        return em.createNamedQuery(MatchPoints.LOAD_ALL_FOR_MATCH_AND_USER, MatchPoints.class)
                .setParameter("matchId", match.getId())
                .setParameter("userGroupId", userGroupEntity.getId())
                .getResultList();
    }

    @Override
    @Cacheable(value = CACHE_QUERY)
    public MatchPoints load(final User user, final Match match) {

        final List<MatchPoints> list = em.createNamedQuery(MatchPoints.LOAD_ALL_FOR_USER_AND_MATCH, MatchPoints.class)
                .setParameter("userId", user.getId())
                .setParameter("matchId", match.getId())
                .getResultList();

        return list.size() == 1 ? list.get(0) : null;
    }

    @Override
    @Cacheable(value = CACHE_QUERY)
    public MatchPoints load(final User user, final Match match, final UserGroupEntity userGroupEntity) {

        final List<MatchPoints> list = em.createNamedQuery(MatchPoints.LOAD_ALL_FOR_USER_AND_MATCH_AND_GROUP, MatchPoints.class)
                .setParameter("userId", user.getId())
                .setParameter("matchId", match.getId())
                .setParameter("userGroupId", userGroupEntity.getId())
                .getResultList();

        return list.size() == 1 ? list.get(0) : null;
    }

    @Override
    public List<MatchPoints> loadAll(final LocalDateTime timeFrom, final LocalDateTime timeTo) {

        return em.createNamedQuery(MatchPoints.LOAD_ALL_FOR_PERIOD, MatchPoints.class)
                .setParameter("timeFrom", timeFrom)
                .setParameter("timeTo", timeTo)
                .getResultList();
    }

    @Override
    @Cacheable(value = CACHE_QUERY)
    public MatchSummaryPoints loadSummary(final User user, final Cup cup) {

        final List list = em.createNamedQuery(MatchPoints.LOAD_SUMMARY_FOR_USER_AND_CUP)
                .setParameter("userId", user.getId())
                .setParameter("cupId", cup.getId())
                .getResultList();

        return getMatchSummaryPoints(list);
    }

    @Override
    @Cacheable(value = CACHE_QUERY)
    public MatchSummaryPoints loadSummary(final User user, final Cup cup, final UserGroupEntity userGroupEntity) {

        final List list = em.createNamedQuery(MatchPoints.LOAD_SUMMARY_FOR_USER_AND_CUP_AND_GROUP)
                .setParameter("userId", user.getId())
                .setParameter("cupId", cup.getId())
                .setParameter("userGroupId", userGroupEntity.getId())
                .getResultList();

        return getMatchSummaryPoints(list);
    }

    @Override
    public MatchSummaryPoints loadSummary(final User user, final Cup cup, final LocalDateTime timeFrom, final LocalDateTime timeTo) {

        final List list = em.createNamedQuery(MatchPoints.LOAD_SUMMARY_FOR_USER_FOR_CUP_FOR_PERIOD)
                .setParameter("timeFrom", timeFrom)
                .setParameter("timeTo", timeTo)
                .setParameter("userId", user.getId())
                .setParameter("cupId", cup.getId())
                .getResultList();

        return getMatchSummaryPoints(list);
    }

    private MatchSummaryPoints getMatchSummaryPoints(final List list) {

        if (list.size() != 1) {
            return null;
        }

        final Object[] result = (Object[]) list.get(0);

        if (result[0] == null) {
            return null;
        }

        return new MatchSummaryPoints((int) (long) result[0], (float) (double) result[1]);
    }
}

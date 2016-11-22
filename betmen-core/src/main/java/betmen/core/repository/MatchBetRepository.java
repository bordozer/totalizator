package betmen.core.repository;

import betmen.core.entity.Cup;
import betmen.core.entity.Match;
import betmen.core.entity.MatchBet;
import betmen.core.entity.User;
import org.apache.log4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class MatchBetRepository implements MatchBetDao {

    private static final Logger LOGGER = Logger.getLogger(MatchBetRepository.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    @Cacheable(value = CACHE_QUERY)
    public List<MatchBet> loadAll() {
        return em.createNamedQuery(MatchBet.LOAD_ALL, MatchBet.class)
                .getResultList();
    }

    @Override
    @Cacheable(value = CACHE_QUERY)
    public List<MatchBet> loadAll(final User user) {
        return em.createNamedQuery(MatchBet.LOAD_FOR_USER, MatchBet.class)
                .setParameter("userId", user.getId())
                .getResultList();
    }

    @Override
    public List<MatchBet> loadAll(final User user, final LocalDateTime timeFrom, final LocalDateTime timeTo) {
        return em.createQuery(
                "select mb from MatchBet as mb join mb.match as m where mb.user.id = :userId and m.beginningTime >= :timeFrom and m.beginningTime <= :timeTo order by m.beginningTime desc", MatchBet.class)
                .setParameter("userId", user.getId())
                .setParameter("timeFrom", timeFrom)
                .setParameter("timeTo", timeTo)
                .getResultList();
    }

    @Override
    @Cacheable(value = CACHE_QUERY)
    public List<MatchBet> loadAll(final Match match) {
        return em.createNamedQuery(MatchBet.LOAD_FOR_MATCH, MatchBet.class)
                .setParameter("matchId", match.getId())
                .getResultList();
    }

    @Override
    @Cacheable(value = CACHE_QUERY)
    public MatchBet load(final User user, final Match match) {
        final List<MatchBet> bets = em.createNamedQuery(MatchBet.LOAD_FOR_USER_AND_MATCH, MatchBet.class)
                .setParameter("userId", user.getId())
                .setParameter("matchId", match.getId())
                .getResultList();
        return bets.size() == 1 ? bets.get(0) : null;
    }

    @Override
    @Cacheable(value = CACHE_ENTRY, key = "#id")
    public MatchBet load(final int id) {
        return em.find(MatchBet.class, id);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = CACHE_ENTRY, key = "#entry.id")
            , @CacheEvict(value = CACHE_QUERY, allEntries = true)
    })
    public MatchBet save(final MatchBet entry) {
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
    @Cacheable(value = CACHE_QUERY, key = "#matchId")
    public int betsCount(final int matchId) {
        final List<Long> bets = em.createNamedQuery(MatchBet.LOAD_MATCH_BETS_COUNT, Long.class)
                .setParameter("matchId", matchId)
                .getResultList();
        return bets.size() == 1 ? bets.get(0).intValue() : 0;
    }

    @Override
    public int betsCount(final Cup cup, final User user) {
        final List<Long> bets = em.createNamedQuery(MatchBet.LOAD_COUNT_OF_CUP_MATCHES_WITH_USER_BET, Long.class)
                .setParameter("cupId", cup.getId())
                .setParameter("userId", user.getId())
                .getResultList();
        return bets.size() == 1 ? bets.get(0).intValue() : 0;
    }

    @Override
    public int userBetsCount(final int userId) {
        final List<Long> bets = em.createNamedQuery(MatchBet.LOAD_COUNT_OF_MATCHES_WITH_USER_BET, Long.class)
                .setParameter("userId", userId)
                .getResultList();
        return bets.size() == 1 ? bets.get(0).intValue() : 0;
    }

    @Override
    public int getMatchesCountAccessibleForBettingSince(final Cup cup, final User user, final LocalDateTime sinceTime) {
        final List<Long> bets = em.createNamedQuery(MatchBet.LOAD_COUNT_OF_CUP_MATCHES_ACCESSIBLE_FOR_BETTING_FOR_USER, Long.class)
                .setParameter("cupId", cup.getId())
                .setParameter("userId", user.getId())
                .setParameter("time", sinceTime)
                .getResultList();
        return bets.size() == 1 ? bets.get(0).intValue() : 0;
    }

    @Override
    public Match getFirstMatchWithoutBetSince(final Cup cup, final User user, final LocalDateTime sinceTime) {
        final TypedQuery<Match> query = em.createQuery(
                "select m from Match as m where m.cup.id = :cupId and m.beginningTime >= :time and m.id not in ( select mb.match.id from MatchBet mb where mb.user.id = :userId )", Match.class)
                .setParameter("cupId", cup.getId())
                .setParameter("userId", user.getId())
                .setParameter("time", sinceTime)
                .setFirstResult(0)
                .setMaxResults(1);
        final List<Match> list = query.getResultList();
        return list.size() == 1 ? list.get(0) : null;
    }
}

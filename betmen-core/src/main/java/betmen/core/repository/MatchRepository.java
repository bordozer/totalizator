package betmen.core.repository;

import betmen.core.entity.Cup;
import betmen.core.entity.Match;
import betmen.core.entity.Team;
import betmen.core.service.points.CupPointsService;
import betmen.core.service.points.MatchPointsService;
import betmen.core.utils.Constants;
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
public class MatchRepository implements MatchDao {

    private static final String CUP_ID = "cupId";

    @PersistenceContext
    private EntityManager em;

    @Override
    @Cacheable(value = CACHE_QUERY)
    public List<Match> loadAll() {

        return em.createNamedQuery(Match.LOAD_ALL, Match.class)
                .getResultList();
    }

    @Override
    @Cacheable(value = CACHE_QUERY)
    public List<Match> loadAll(final Cup cup) {

        return em.createNamedQuery(Match.FIND_BY_CUP, Match.class)
                .setParameter(CUP_ID, cup.getId())
                .getResultList();
    }

    @Override
    @Cacheable(value = CACHE_ENTRY, key = "#matchId")
    public Match load(final int matchId) {
        return em.find(Match.class, matchId);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = CACHE_ENTRY, key = "#match.id")
            , @CacheEvict(value = CACHE_QUERY, allEntries = true)
            , @CacheEvict(value = MatchBetDao.CACHE_ENTRY, allEntries = true)
            , @CacheEvict(value = MatchBetDao.CACHE_QUERY, allEntries = true)
            , @CacheEvict(value = MatchPointsDao.CACHE_ENTRY, allEntries = true)
            , @CacheEvict(value = MatchPointsDao.CACHE_QUERY, allEntries = true)
            , @CacheEvict(value = MatchPointsService.CACHE_QUERY, allEntries = true)
            , @CacheEvict(value = CupPointsService.CACHE_QUERY, allEntries = true)
            , @CacheEvict(value = Constants.USER_RATING_SERVICE_CACHE_QUERY, allEntries = true)
            , @CacheEvict(value = Constants.ACTIVITY_DTO_SERVICE_CACHE_ACTIVITY, allEntries = true)
    })
    public Match save(final Match match) {
        return em.merge(match);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = CACHE_ENTRY, key = "#matchId")
            , @CacheEvict(value = CACHE_QUERY, allEntries = true)
            , @CacheEvict(value = MatchBetDao.CACHE_ENTRY, allEntries = true)
            , @CacheEvict(value = MatchBetDao.CACHE_QUERY, allEntries = true)
            , @CacheEvict(value = MatchPointsDao.CACHE_ENTRY, allEntries = true)
            , @CacheEvict(value = MatchPointsDao.CACHE_QUERY, allEntries = true)
            , @CacheEvict(value = MatchPointsService.CACHE_QUERY, allEntries = true)
            , @CacheEvict(value = CupPointsService.CACHE_QUERY, allEntries = true)
            , @CacheEvict(value = Constants.USER_RATING_SERVICE_CACHE_QUERY, allEntries = true)
    })
    public void delete(final int matchId) {
        em.remove(load(matchId));
    }

    @Override
    @Cacheable(value = CACHE_QUERY)
    public List<Match> loadAll(final Team team1, final Team team2) {

        return em.createNamedQuery(Match.FIND_BY_TEAMS, Match.class)
                .setParameter("team1Id", team1.getId())
                .setParameter("team2Id", team2.getId())
                .getResultList();
    }

    @Override
    public List<Match> loadAll(final int cupId, final int team1Id, final int team2Id) {
        return em.createNamedQuery(Match.FIND_BY_CUP_AND_TEAMS, Match.class)
                .setParameter(CUP_ID, cupId)
                .setParameter("team1Id", team1Id)
                .setParameter("team2Id", team2Id)
                .getResultList();
    }

    @Override
    public List<Match> loadAllFinished(final int cupId, final int team1Id, final int team2Id) {
        return em.createNamedQuery(Match.FIND_BY_CUP_AND_TEAMS_FINISHED, Match.class)
                .setParameter(CUP_ID, cupId)
                .setParameter("team1Id", team1Id)
                .setParameter("team2Id", team2Id)
                .getResultList();
    }

    @Override
    public List<Match> loadAll(final Team team) {
        return em.createNamedQuery(Match.FIND_BY_TEAM, Match.class)
                .setParameter("teamId", team.getId())
                .getResultList();
    }

    @Override
    public int getMatchCount(final int teamId) {
        final List<Long> result = em.createNamedQuery(Match.LOAD_MATCH_COUNT_FOR_TEAM, Long.class)
                .setParameter("teamId", teamId)
                .getResultList();

        return result != null && !result.isEmpty() ? (int) (long) result.get(0) : 0;
    }

    @Override
    public List<Match> loadAll(final int cupId, final int teamId) {

        return em.createNamedQuery(Match.FIND_ALL_TEAM_MATCHES_FOR_CUP, Match.class)
                .setParameter(CUP_ID, cupId)
                .setParameter("teamId", teamId)
                .getResultList();
    }

    @Override
    public int getMatchCount(final Cup cup, final Team team) {

        final List<Long> result = em.createNamedQuery(Match.LOAD_MATCH_COUNT_FOR_CUP_AND_TEAM, Long.class)
                .setParameter(CUP_ID, cup.getId())
                .setParameter("teamId", team.getId())
                .getResultList();

        return result != null && !result.isEmpty() ? (int) (long) result.get(0) : 0;
    }

    @Override
    public int getFinishedMatchCount(final Cup cup, final Team team) {

        final List<Long> result = em.createNamedQuery(Match.LOAD_FINISHED_MATCH_COUNT_FOR_CUP_AND_TEAM, Long.class)
                .setParameter(CUP_ID, cup.getId())
                .setParameter("teamId", team.getId())
                .getResultList();

        return result != null && !result.isEmpty() ? (int) (long) result.get(0) : 0;
    }

    @Override
    public int getFutureMatchCount(final Cup cup) {
        final List<Long> result = em.createNamedQuery(Match.LOAD_FUTURE_MATCH_COUNT_FOR_CUP, Long.class)
                .setParameter(CUP_ID, cup.getId())
                .getResultList();

        return result != null && !result.isEmpty() ? (int) (long) result.get(0) : 0;
    }

    @Override
    public int getFutureMatchCount(final Cup cup, final Team team) {
        final List<Long> result = em.createNamedQuery(Match.LOAD_FUTURE_MATCH_COUNT_FOR_CUP_AND_TEAM, Long.class)
                .setParameter(CUP_ID, cup.getId())
                .setParameter("teamId", team.getId())
                .getResultList();

        return result != null && !result.isEmpty() ? (int) (long) result.get(0) : 0;
    }

    @Override
    public Match findByImportId(final String remoteGameId) {
        final List<Match> bets = em.createNamedQuery(Match.FIND_MATCH_BY_REMOTE_GAME_ID, Match.class)
                .setParameter("remoteGameId", remoteGameId)
                .getResultList();

        return bets.size() == 1 ? bets.get(0) : null;
    }

    @Override
    @Cacheable(value = CACHE_QUERY)
    public List<Match> loadAllBetween(final LocalDateTime timeFrom, final LocalDateTime timeTo) {

        return em.createNamedQuery(Match.FIND_MATCHES_BY_DATE, Match.class)
                .setParameter("timeFrom", timeFrom)
                .setParameter("timeTo", timeTo)
                .getResultList();
    }

    @Override
    public List<Match> loadAllBetween(final int cupId, final LocalDateTime timeFrom, final LocalDateTime timeTo) {

        return em.createNamedQuery(Match.FIND_CUP_MATCHES_BY_DATE, Match.class)
                .setParameter(CUP_ID, cupId)
                .setParameter("timeFrom", timeFrom)
                .setParameter("timeTo", timeTo)
                .getResultList();
    }

    @Override
    public List<Match> getStartedMatchCount(final Cup cup, final LocalDateTime timeFrom) {

        return em.createNamedQuery(Match.FIND_NOT_FINISHED_MATCHES_STARTED_TILL, Match.class)
                .setParameter(CUP_ID, cup.getId())
                .setParameter("time", timeFrom)
                .getResultList();
    }

    @Override
    public Match getNearestFutureMatch(final Cup cup, final LocalDateTime onTime) {
        final List<Match> result = em.createNamedQuery(Match.FIND_NOT_FINISHED_MATCHES_STARTED_AFTER, Match.class)
                .setParameter(CUP_ID, cup.getId())
                .setParameter("time", onTime)
                .getResultList();
        return !result.isEmpty() ? result.get(0) : null;
    }

    @Override
    public List<Match> loadAllNotFinished(final int cupId) {
        return em.createNamedQuery(Match.FIND_NOT_FINISHED_MATCHES, Match.class)
                .setParameter(CUP_ID, cupId)
                .getResultList();
    }

    @Override
    public List<Match> loadAllFinished(final int cupId) {
        return em.createNamedQuery(Match.FIND_FINISHED_MATCHES, Match.class)
                .setParameter(CUP_ID, cupId)
                .getResultList();
    }

    @Override
    public Match getFirstMatch(final Cup cup) {

        final TypedQuery<Match> query = em.createQuery(
                "select m from Match as m where m.cup.id = :cupId order by m.beginningTime asc", Match.class)
                .setParameter(CUP_ID, cup.getId())
                .setFirstResult(0)
                .setMaxResults(1);
        final List<Match> list = query.getResultList();
        return list.size() == 1 ? list.get(0) : null;
    }

    @Override
    public List<Match> getLastNMatches(final Cup cup, final Team team, final int n) {
        return em.createNamedQuery(Match.LOAD_LAST_TEAM_MATCHES, Match.class)
                .setParameter(CUP_ID, cup.getId())
                .setParameter("teamId", team.getId())
                .setFirstResult(0)
                .setMaxResults(n)
                .getResultList();
    }

    @Override
    public Match loadLastImportedMatch(final int cupId) {
        List<Match> list = em.createNamedQuery(Match.LAST_IMPORTED_MATCH, Match.class)
                .setParameter(CUP_ID, cupId)
                .setFirstResult(0)
                .setMaxResults(1)
                .getResultList();
        return list.size() == 1 ? list.get(0) : null;
    }
}

package betmen.core.repository;

import betmen.core.entity.Cup;
import betmen.core.entity.Match;
import betmen.core.entity.Team;

import java.time.LocalDateTime;
import java.util.List;

public interface MatchDao {

    String CACHE_ENTRY = "totalizator.app.cache.match";
    String CACHE_QUERY = "totalizator.app.cache.matches";

    List<Match> loadAll();

    Match load(final int matchId);

    Match save(Match match);

    void delete(final int matchId);

    List<Match> loadAll(final Cup cup);

    int getMatchCount(final int teamId);

    List<Match> loadAll(final int cupId, final int teamId);

    List<Match> loadAll(final Team team1, final Team team2);

    List<Match> loadAll(final int cupId, final int team1Id, final int team2Id);

    List<Match> loadAllFinished(final int cupId, final int team1Id, final int team2Id);

    List<Match> loadAll(final Team team);

    int getMatchCount(final Cup cup, final Team team);

    int getFinishedMatchCount(final Cup cup, final Team team);

    int getFutureMatchCount(final Cup cup);

    int getFutureMatchCount(final Cup cup, final Team team);

    Match findByImportId(final String remoteGameId);

    List<Match> loadAllBetween(final LocalDateTime timeFrom, final LocalDateTime timeTo);

    List<Match> loadAllBetween(final int cupId, final LocalDateTime timeFrom, final LocalDateTime timeTo);

    List<Match> getStartedMatchCount(final Cup cup, final LocalDateTime timeFrom);

    Match getNearestFutureMatch(final Cup cup, final LocalDateTime onTime);

    List<Match> loadAllNotFinished(final int cupId);

    List<Match> loadAllFinished(final int cupId);

    Match getFirstMatch(final Cup cup);

    List<Match> getLastNMatches(final Cup cup, final Team team, final int n);

    Match loadLastImportedMatch(final int cupId);
}

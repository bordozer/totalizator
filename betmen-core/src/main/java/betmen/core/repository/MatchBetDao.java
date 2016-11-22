package betmen.core.repository;

import betmen.core.entity.Cup;
import betmen.core.entity.Match;
import betmen.core.entity.MatchBet;
import betmen.core.entity.User;
import betmen.core.service.GenericService;

import java.time.LocalDateTime;
import java.util.List;

public interface MatchBetDao extends GenericService<MatchBet> {

    String CACHE_ENTRY = "totalizator.app.cache.match-bet";
    String CACHE_QUERY = "totalizator.app.cache.match-bet.query";

    List<MatchBet> loadAll(final User user);

    List<MatchBet> loadAll(final User user, final LocalDateTime timeFrom, final LocalDateTime timeTo);

    List<MatchBet> loadAll(final Match match);

    MatchBet load(final User user, final Match match);

    int betsCount(final int matchId);

    int betsCount(final Cup cup, final User user);

    int userBetsCount(final int userId);

    int getMatchesCountAccessibleForBettingSince(final Cup cup, final User user, final LocalDateTime sinceTime);

    Match getFirstMatchWithoutBetSince(final Cup cup, final User user, final LocalDateTime sinceTime);
}

package betmen.core.service.matches;

import betmen.core.model.ValidationResult;
import betmen.core.entity.Cup;
import betmen.core.entity.Match;
import betmen.core.entity.MatchBet;
import betmen.core.entity.User;
import betmen.core.entity.UserGroup;
import betmen.core.service.GenericService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface MatchBetsService extends GenericService<MatchBet> {

    List<MatchBet> loadAll(final User user);

    List<MatchBet> loadAll(final User user, final LocalDate date);

    List<MatchBet> loadAll(final Match match);

    List<MatchBet> loadAll(final Match match, final UserGroup userGroup);

    List<MatchBet> loadAll(final Cup cup, final User user);

    MatchBet load(final User user, final Match match);

    int betsCount(final int matchId);

    int betsCount(final Cup cup, final User user);

    int userBetsCount(final User user);

    ValidationResult validateBettingAllowed(final Match match, final User user);

    boolean canMatchBeBet(final Match match, final User user);

    boolean userCanSeeAnotherBets(final Match match, final User accessor);

    boolean isAllowedToShowMatchBets(final MatchBet matchBet, final User user);

    List<User> getUserWhoMadeBet(final Match match);

    int getMatchesCountAccessibleBorBetting(final Cup cup, final User user);

    Match getFirstMatchWithoutBet(final Cup cup, final User user);

    LocalDateTime getFirstMatchWithoutBetTime(final Cup cup, final User user);

    boolean delete(int userId, int matchBetId);
}

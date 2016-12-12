package betmen.core.service.activiries;

import betmen.core.entity.MatchBet;
import betmen.core.entity.User;
import betmen.core.entity.activities.AbstractActivityStreamEntry;

import java.util.List;

public interface ActivityStreamService {

    List<AbstractActivityStreamEntry> loadAllForLast(final int hours);

    List<AbstractActivityStreamEntry> loadAllForMatch(final int matchId);

    List<AbstractActivityStreamEntry> loadAllForUser(final int userId, final int qty);

    void matchBetCreated(final MatchBet matchBet);

    void matchBetChanged(final MatchBet matchBet, final int oldScore1, final int oldScore2);

    void matchBetDeleted(final User user, final int matchId, final int score1, final int score2);

    void matchFinished(final int matchId, final int score1, final int score2);

    void delete(final int id);

    void deleteAll();
}

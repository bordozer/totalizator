package totalizator.app.services.activiries;

import totalizator.app.models.MatchBet;
import totalizator.app.models.User;
import totalizator.app.models.activities.AbstractActivityStreamEntry;

import java.util.List;

public interface ActivityStreamService {

	List<AbstractActivityStreamEntry> loadAll();

	void matchBetCreated( final MatchBet matchBet );

	void matchBetChanged( final MatchBet matchBet, final int oldScore1, final int oldScore2 );

	void matchBetDeleted( final User user, final int matchId, final int score1, final int score2 );

	void matchFinished( final int matchId, final int score1, final int score2 );
}

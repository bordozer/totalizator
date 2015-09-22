package totalizator.app.services.activiries;

import totalizator.app.models.MatchBet;
import totalizator.app.models.User;
import totalizator.app.models.activities.AbstractActivityStreamEntry;

import java.util.List;

public interface ActivityStreamService {

	List<AbstractActivityStreamEntry> loadAll();

	void matchBetCreated( final MatchBet matchBet );

	void matchBetChanged( final MatchBet matchBet );

	void matchBetDeleted( final User user, final int matchId );
}

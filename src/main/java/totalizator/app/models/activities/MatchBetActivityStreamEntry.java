package totalizator.app.models.activities;

import totalizator.app.models.ActivityStreamEntry;
import totalizator.app.models.MatchBet;

public class MatchBetActivityStreamEntry extends AbstractActivityStreamEntry {

	private final MatchBet matchBet;

	public MatchBetActivityStreamEntry( final ActivityStreamEntry activityStreamEntry, final MatchBet matchBet ) {
		super( activityStreamEntry );

		this.matchBet = matchBet;
	}

	public MatchBet getMatchBet() {
		return matchBet;
	}
}

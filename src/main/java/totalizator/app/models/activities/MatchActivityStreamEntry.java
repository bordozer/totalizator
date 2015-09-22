package totalizator.app.models.activities;

import totalizator.app.models.ActivityStreamEntry;
import totalizator.app.models.Match;

public class MatchActivityStreamEntry extends AbstractActivityStreamEntry {

	private final Match match;

	public MatchActivityStreamEntry( final ActivityStreamEntry activityStreamEntry, final Match match ) {
		super( activityStreamEntry );
		this.match = match;
	}

	public Match getMatch() {
		return match;
	}
}

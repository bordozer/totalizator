package totalizator.app.models.activities;

import totalizator.app.models.ActivityStreamEntry;
import totalizator.app.models.activities.events.MatchBetEvent;

public class MatchBetActivityStreamEntry extends AbstractActivityStreamEntry {

	private final MatchBetEvent matchBetEvent;

	public MatchBetActivityStreamEntry( final ActivityStreamEntry activityStreamEntry, final MatchBetEvent matchBetEvent ) {

		super( activityStreamEntry );

		this.matchBetEvent = matchBetEvent;
	}

	public MatchBetEvent getMatchBetEvent() {
		return matchBetEvent;
	}
}

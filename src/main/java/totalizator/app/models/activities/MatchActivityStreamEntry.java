package totalizator.app.models.activities;

import totalizator.app.models.ActivityStreamEntry;
import totalizator.app.models.activities.events.MatchEvent;

public class MatchActivityStreamEntry extends AbstractActivityStreamEntry {

	private final MatchEvent matchEvent;

	public MatchActivityStreamEntry( final ActivityStreamEntry activityStreamEntry, final MatchEvent matchEvent ) {

		super( activityStreamEntry );

		this.matchEvent = matchEvent;
	}

	public MatchEvent getMatchEvent() {
		return matchEvent;
	}
}

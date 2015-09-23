package totalizator.app.models.activities;

import com.google.gson.Gson;
import totalizator.app.models.ActivityStreamEntry;
import totalizator.app.models.activities.events.MatchBetEvent;

public class MatchBetActivityStreamEntry extends AbstractActivityStreamEntry {

	private final MatchBetEvent matchBetEvent;

	public MatchBetActivityStreamEntry( final ActivityStreamEntry activityStreamEntry ) {

		super( activityStreamEntry );

		matchBetEvent = new Gson().fromJson( activityStreamEntry.getEventJson(), MatchBetEvent.class );
	}

	public MatchBetEvent getMatchBetEvent() {
		return matchBetEvent;
	}
}

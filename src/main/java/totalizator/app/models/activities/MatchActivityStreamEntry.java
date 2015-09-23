package totalizator.app.models.activities;

import com.google.gson.Gson;
import totalizator.app.models.ActivityStreamEntry;
import totalizator.app.models.activities.events.MatchEvent;

public class MatchActivityStreamEntry extends AbstractActivityStreamEntry {

	private final MatchEvent matchEvent;

	public MatchActivityStreamEntry( final ActivityStreamEntry activityStreamEntry ) {

		super( activityStreamEntry );

		matchEvent = new Gson().fromJson( activityStreamEntry.getEventJson(), MatchEvent.class );
	}

	public MatchEvent getMatchEvent() {
		return matchEvent;
	}
}

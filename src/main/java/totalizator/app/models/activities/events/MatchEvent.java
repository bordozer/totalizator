package totalizator.app.models.activities.events;

public class MatchEvent implements ActivityStreamEvent {

	private final int matchId;

	public MatchEvent( final int matchId ) {
		this.matchId = matchId;
	}

	public int getMatchId() {
		return matchId;
	}
}

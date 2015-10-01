package totalizator.app.services.matches.imports;

public class ImportGameResult {

	private final int matchId;
	private final boolean matchCreated;

	public ImportGameResult( final int matchId, final boolean matchCreated ) {
		this.matchId = matchId;
		this.matchCreated = matchCreated;
	}

	public int getMatchId() {
		return matchId;
	}

	public boolean isMatchCreated() {
		return matchCreated;
	}
}

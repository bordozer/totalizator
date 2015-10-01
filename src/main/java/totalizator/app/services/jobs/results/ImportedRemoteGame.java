package totalizator.app.services.jobs.results;

import totalizator.app.services.matches.imports.RemoteGame;

public class ImportedRemoteGame {

	private final boolean isNewGameCreated;
	private final int matchId;

	private final RemoteGame remoteGame;

	public ImportedRemoteGame( final RemoteGame remoteGame, final int matchId, final boolean isNewGameCreated ) {

		this.remoteGame = remoteGame;
		this.matchId = matchId;
		this.isNewGameCreated = isNewGameCreated;
	}

	public boolean isNewGameCreated() {
		return isNewGameCreated;
	}

	public int getMatchId() {
		return matchId;
	}

	public RemoteGame getRemoteGame() {
		return remoteGame;
	}
}

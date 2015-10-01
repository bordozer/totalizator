package totalizator.app.controllers.rest.admin.jobs.results;

import totalizator.app.dto.MatchDTO;
import totalizator.app.services.matches.imports.RemoteGame;

public class RemoteGameMatchPair {

	private final RemoteGame remoteGame;

	private boolean isNewGameCreated;
	private int matchId;

	private MatchDTO match;

	public RemoteGameMatchPair( final RemoteGame remoteGame ) {
		this.remoteGame = remoteGame;
	}

	public RemoteGame getRemoteGame() {
		return remoteGame;
	}

	public int getMatchId() {
		return matchId;
	}

	public void setMatchId( final int matchId ) {
		this.matchId = matchId;
	}

	public boolean isNewGameCreated() {
		return isNewGameCreated;
	}

	public void setIsNewGameCreated( final boolean isNewGameCreated ) {
		this.isNewGameCreated = isNewGameCreated;
	}

	public void setMatch( final MatchDTO match ) {
		this.match = match;
	}

	public MatchDTO getMatch() {
		return match;
	}
}

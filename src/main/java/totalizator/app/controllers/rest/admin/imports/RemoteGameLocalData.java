package totalizator.app.controllers.rest.admin.imports;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import totalizator.app.dto.MatchDTO;
import totalizator.app.dto.TeamDTO;

@JsonIgnoreProperties( ignoreUnknown = true )
public class RemoteGameLocalData {

	private TeamDTO team1;
	private TeamDTO team2;
	private MatchDTO match;

	public TeamDTO getTeam1() {
		return team1;
	}

	public void setTeam1( final TeamDTO team1 ) {
		this.team1 = team1;
	}

	public TeamDTO getTeam2() {
		return team2;
	}

	public void setTeam2( final TeamDTO team2 ) {
		this.team2 = team2;
	}

	public MatchDTO getMatch() {
		return match;
	}

	public void setMatch( final MatchDTO match ) {
		this.match = match;
	}
}

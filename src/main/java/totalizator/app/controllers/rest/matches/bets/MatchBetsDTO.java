package totalizator.app.controllers.rest.matches.bets;

import totalizator.app.dto.MatchBetDTO;
import totalizator.app.dto.MatchDTO;
import totalizator.app.dto.TeamDTO;

import java.util.List;

public class MatchBetsDTO {

	private int matchId;

	private List<MatchBetDTO> matchBets;
	private TeamDTO team1;
	private TeamDTO team2;

	private MatchDTO match;

	public int getMatchId() {
		return matchId;
	}

	public void setMatchId( final int matchId ) {
		this.matchId = matchId;
	}

	public List<MatchBetDTO> getMatchBets() {
		return matchBets;
	}

	public void setMatchBets( final List<MatchBetDTO> matchBets ) {
		this.matchBets = matchBets;
	}

	public void setTeam1( final TeamDTO team1 ) {
		this.team1 = team1;
	}

	public TeamDTO getTeam1() {
		return team1;
	}

	public void setTeam2( final TeamDTO team2 ) {
		this.team2 = team2;
	}

	public TeamDTO getTeam2() {
		return team2;
	}

	public MatchDTO getMatch() {
		return match;
	}

	public void setMatch( final MatchDTO match ) {
		this.match = match;
	}
}

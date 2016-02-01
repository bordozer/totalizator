package totalizator.app.controllers.rest.teams.standoffs;

import totalizator.app.dto.CupDTO;
import totalizator.app.dto.TeamDTO;

import java.util.List;

public class TeamsStandoffsDTO {

	private TeamDTO team1;
	private TeamDTO team2;
	private CupDTO cupToShow;
	private List<TeamsCupStandoffDTO> standoffsByCup;

	private TeamsLastGamesStat teamsLastGamesStat;

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

	public CupDTO getCupToShow() {
		return cupToShow;
	}

	public void setCupToShow( final CupDTO cupToShow ) {
		this.cupToShow = cupToShow;
	}

	public List<TeamsCupStandoffDTO> getStandoffsByCup() {
		return standoffsByCup;
	}

	public void setStandoffsByCup( final List<TeamsCupStandoffDTO> standoffsByCup ) {
		this.standoffsByCup = standoffsByCup;
	}

	public TeamsLastGamesStat getTeamsLastGamesStat() {
		return teamsLastGamesStat;
	}

	public void setTeamsLastGamesStat(final TeamsLastGamesStat teamsLastGamesStat) {
		this.teamsLastGamesStat = teamsLastGamesStat;
	}
}

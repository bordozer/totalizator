package totalizator.app.controllers.rest.admin.cups.edit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import totalizator.app.dto.TeamDTO;

class CupWinnerEditDTO {

	private int cupId;
	private int cupPosition;
	private int teamId;

	@JsonIgnore
	private TeamDTO team;

	public CupWinnerEditDTO() {
	}

	public CupWinnerEditDTO( final int cupId, final int cupPosition, final int teamId ) {
		this.cupId = cupId;
		this.cupPosition = cupPosition;
		this.teamId = teamId;
	}

	public int getCupId() {
		return cupId;
	}

	public void setCupId( final int cupId ) {
		this.cupId = cupId;
	}

	public int getCupPosition() {
		return cupPosition;
	}

	public void setCupPosition( final int cupPosition ) {
		this.cupPosition = cupPosition;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId( final int teamId ) {
		this.teamId = teamId;
	}

	@JsonProperty
	public TeamDTO getTeam() {
		return team;
	}

	@JsonIgnore
	public void setTeam( final TeamDTO team ) {
		this.team = team;
	}

	@Override
	public String toString() {
		return String.format( "%d %d %d ", cupId, cupPosition, teamId );
	}
}

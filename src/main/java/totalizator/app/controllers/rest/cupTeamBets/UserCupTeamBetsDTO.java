package totalizator.app.controllers.rest.cupTeamBets;

public class UserCupTeamBetsDTO {

	private int teamId;
	private int cupPosition;

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId( final int teamId ) {
		this.teamId = teamId;
	}

	public int getCupPosition() {
		return cupPosition;
	}

	public void setCupPosition( final int cupPosition ) {
		this.cupPosition = cupPosition;
	}
}

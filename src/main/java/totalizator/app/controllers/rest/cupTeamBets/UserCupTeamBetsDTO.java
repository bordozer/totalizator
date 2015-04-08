package totalizator.app.controllers.rest.cupTeamBets;

public class UserCupTeamBetsDTO {

	private int teamId;
	private int cupPositionId;

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId( final int teamId ) {
		this.teamId = teamId;
	}

	public int getCupPositionId() {
		return cupPositionId;
	}

	public void setCupPositionId( final int cupPositionId ) {
		this.cupPositionId = cupPositionId;
	}
}

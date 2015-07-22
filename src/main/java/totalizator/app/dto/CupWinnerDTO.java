package totalizator.app.dto;

public class CupWinnerDTO {

	private CupDTO cup;
	private int cupPosition;
	private TeamDTO team;

	public CupWinnerDTO( final CupDTO cup, final int cupPosition, final TeamDTO team ) {
		this.cup = cup;
		this.cupPosition = cupPosition;
		this.team = team;
	}

	public CupDTO getCup() {
		return cup;
	}
	public void setCup( final CupDTO cup ) {
		this.cup = cup;
	}
	public int getCupPosition() {
		return cupPosition;
	}
	public void setCupPosition( final int cupPosition ) {
		this.cupPosition = cupPosition;
	}
	public TeamDTO getTeam() {
		return team;
	}
	public void setTeam( final TeamDTO team ) {
		this.team = team;
	}
}

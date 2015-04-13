package totalizator.app.dto;

public class CupWinnerDTO {

	private int cupId;
	private int cupPosition;
	private int teamId;

	public CupWinnerDTO() {
	}

	public CupWinnerDTO( final int cupId, final int cupPosition, final int teamId ) {
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

	@Override
	public String toString() {
		return String.format( "%d %d %d ", cupId, cupPosition, teamId );
	}
}

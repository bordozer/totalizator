package totalizator.app.controllers.rest.teams.standoffs;

import totalizator.app.dto.CupDTO;

public class TeamsCupStandoffDTO {

	private CupDTO cup;

	private int score1;
	private int score2;

	public CupDTO getCup() {
		return cup;
	}

	public void setCup( final CupDTO cup ) {
		this.cup = cup;
	}

	public int getScore1() {
		return score1;
	}

	public void setScore1( final int score1 ) {
		this.score1 = score1;
	}

	public int getScore2() {
		return score2;
	}

	public void setScore2( final int score2 ) {
		this.score2 = score2;
	}
}

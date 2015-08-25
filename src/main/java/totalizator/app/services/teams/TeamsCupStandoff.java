package totalizator.app.services.teams;

import totalizator.app.models.Cup;

public class TeamsCupStandoff {

	private final Cup cup;

	private final int score1;
	private final int score2;

	public TeamsCupStandoff( final Cup cup, final int score1, final int score2 ) {
		this.cup = cup;
		this.score1 = score1;
		this.score2 = score2;
	}

	public Cup getCup() {
		return cup;
	}

	public int getScore1() {
		return score1;
	}

	public int getScore2() {
		return score2;
	}
}

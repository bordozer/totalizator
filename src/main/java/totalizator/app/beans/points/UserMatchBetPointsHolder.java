package totalizator.app.beans.points;

import totalizator.app.models.MatchBet;

public class UserMatchBetPointsHolder extends AbstractUserPointsHolder {

	private final MatchBet matchBet;
	private int points;

	public UserMatchBetPointsHolder( final MatchBet matchBet, final int points ) {
		super( matchBet.getUser() );

		this.matchBet = matchBet;
		this.points = points;
	}

	public MatchBet getMatchBet() {
		return matchBet;
	}

	@Override
	public float getPoints() {
		return points;
	}

	public int getMatchBetPoints() {
		return points;
	}

	public void setPoints( final int points ) {
		this.points = points;
	}

	@Override
	public String toString() {
		return String.format( "%s: %d", matchBet, points );
	}
}

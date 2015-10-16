package totalizator.app.dao;

public class MatchSummaryPoints {

	private final int matchPoints;
	private final float matchBonus;

	public MatchSummaryPoints( final int matchPoints, final float matchBonus ) {
		this.matchPoints = matchPoints;
		this.matchBonus = matchBonus;
	}

	public int getMatchPoints() {
		return matchPoints;
	}

	public float getMatchBonus() {
		return matchBonus;
	}
}

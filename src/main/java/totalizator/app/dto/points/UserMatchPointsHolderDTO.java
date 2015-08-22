package totalizator.app.dto.points;

public class UserMatchPointsHolderDTO {

	private float matchBetPoints;
	private float matchBonus;
	private final float summaryPoints;

	public UserMatchPointsHolderDTO( final int matchBetPoints ) {
		this.matchBetPoints = matchBetPoints;
		this.summaryPoints = matchBetPoints;
	}

	public UserMatchPointsHolderDTO( final float matchBetPoints, final float matchBonus ) {
		this.matchBetPoints = matchBetPoints;
		this.matchBonus = matchBonus;
		this.summaryPoints = matchBetPoints + matchBonus; // TODO: duplicated in UserBetPointsCalculationServiceImpl in comparator
	}

	public float getMatchBetPoints() {
		return matchBetPoints;
	}

	public void setMatchBetPoints( final int matchBetPoints ) {
		this.matchBetPoints = matchBetPoints;
	}

	public float getMatchBonus() {
		return matchBonus;
	}

	public void setMatchBonus( final float matchBonus ) {
		this.matchBonus = matchBonus;
	}

	public float getSummaryPoints() {
		return summaryPoints;
	}

	@Override
	public String toString() {
		return String.format( "%s + %s = %s", matchBetPoints, matchBonus, summaryPoints );
	}
}

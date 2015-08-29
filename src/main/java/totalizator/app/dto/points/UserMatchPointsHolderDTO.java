package totalizator.app.dto.points;

public class UserMatchPointsHolderDTO {

	private String matchBetPoints;
	private String matchBonus;
	private final String summaryPoints;

	private final float summary; // TODO: hack for sorting

	public UserMatchPointsHolderDTO( final int matchBetPoints ) {
		this.matchBetPoints = String.format( "%d", matchBetPoints );
		this.summaryPoints = String.format( "%d", matchBetPoints );

		this.summary = matchBetPoints;
	}

	public UserMatchPointsHolderDTO( final int matchBetPoints, final float matchBonus ) {
		this.matchBetPoints = String.format( "%d", matchBetPoints );
		this.matchBonus = String.format( "%.2f", matchBonus );
		this.summaryPoints = String.format( "%.2f", matchBetPoints + matchBonus );

		this.summary = matchBetPoints + matchBonus;
	}

	public String getMatchBetPoints() {
		return matchBetPoints;
	}

	public void setMatchBetPoints( final String matchBetPoints ) {
		this.matchBetPoints = matchBetPoints;
	}

	public String getMatchBonus() {
		return matchBonus;
	}

	public void setMatchBonus( final String matchBonus ) {
		this.matchBonus = matchBonus;
	}

	public String getSummaryPoints() {
		return summaryPoints;
	}

	public float getSummary() {
		return summary;
	}

	@Override
	public String toString() {
		return String.format( "%s + %s = %s", matchBetPoints, matchBonus, summaryPoints );
	}
}

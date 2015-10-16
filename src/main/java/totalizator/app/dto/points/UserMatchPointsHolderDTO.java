package totalizator.app.dto.points;

import totalizator.app.dto.UserDTO;

public class UserMatchPointsHolderDTO {

	private final UserDTO user;

	private String matchBetPoints;
	private String matchBonus;
	private final String summaryPoints;

	private final float summary; // TODO: hack for sorting

	public UserMatchPointsHolderDTO( final UserDTO user, final int matchBetPoints ) {
		this( user, matchBetPoints, 0 );
	}

	public UserMatchPointsHolderDTO( final UserDTO user, final int matchBetPoints, final float matchBonus ) {

		this.user = user;

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

	public UserDTO getUser() {
		return user;
	}

	@Override
	public String toString() {
		return String.format( "%s + %s = %s", matchBetPoints, matchBonus, summaryPoints );
	}
}

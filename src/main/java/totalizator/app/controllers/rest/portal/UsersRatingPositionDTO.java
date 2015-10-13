package totalizator.app.controllers.rest.portal;

import totalizator.app.dto.UserDTO;

public class UsersRatingPositionDTO {

	private final UserDTO user;
	private final String betPoints;
	private final String matchBonus;

	private final String pointsSummary;

	public UsersRatingPositionDTO( final UserDTO user, final int betPoints, final float matchBonus ) {

		this.user = user;

		this.betPoints = String.format( "%d", betPoints );
		this.matchBonus = String.format( "%.2f", matchBonus );

		this.pointsSummary = String.format( "%.2f", betPoints + matchBonus );
	}

	public UserDTO getUser() {
		return user;
	}

	public String getBetPoints() {
		return betPoints;
	}

	public String getMatchBonus() {
		return matchBonus;
	}

	public String getPointsSummary() {
		return pointsSummary;
	}
}

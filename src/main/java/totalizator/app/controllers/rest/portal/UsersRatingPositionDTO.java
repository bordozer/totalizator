package totalizator.app.controllers.rest.portal;

import totalizator.app.dto.UserDTO;

public class UsersRatingPositionDTO {

	private final UserDTO user;
	private final int betPoints;
	private final float matchBonus;

	public UsersRatingPositionDTO( final UserDTO user, final int betPoints, final float matchBonus ) {
		this.user = user;
		this.betPoints = betPoints;
		this.matchBonus = matchBonus;
	}

	public UserDTO getUser() {
		return user;
	}

	public int getBetPoints() {
		return betPoints;
	}

	public float getMatchBonus() {
		return matchBonus;
	}
}

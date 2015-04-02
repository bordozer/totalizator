package totalizator.app.controllers.rest.scores;

import totalizator.app.dto.UserDTO;

public class UserPointsDTO {

	private final UserDTO user;
	private final int points;

	public UserPointsDTO( final UserDTO user, final int points ) {
		this.user = user;
		this.points = points;
	}

	public UserDTO getUser() {
		return user;
	}

	public int getPoints() {
		return points;
	}
}

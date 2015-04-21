package totalizator.app.controllers.rest.scores;

import totalizator.app.beans.UserTitle;
import totalizator.app.dto.UserDTO;

public class UserPointsDTO {

	private final UserDTO user;
	private final int points;
	private final UserTitle userTitle;

	public UserPointsDTO( final UserDTO user, final int points, final UserTitle userTitle ) {
		this.user = user;
		this.points = points;
		this.userTitle = userTitle;
	}

	public UserDTO getUser() {
		return user;
	}

	public int getPoints() {
		return points;
	}

	public UserTitle getUserTitle() {
		return userTitle;
	}
}

package totalizator.app.controllers.rest.scores;

import totalizator.app.dto.UserDTO;

import java.util.List;

public class CupUsersScoresDTO {

	private UserDTO currentUser;
	private List<UserPointsDTO> userPoints;

	public UserDTO getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser( final UserDTO currentUser ) {
		this.currentUser = currentUser;
	}

	public List<UserPointsDTO> getUserPoints() {
		return userPoints;
	}

	public void setUserPoints( final List<UserPointsDTO> userPoints ) {
		this.userPoints = userPoints;
	}
}

package totalizator.app.controllers.rest.scores;

import totalizator.app.dto.UserDTO;

import java.util.List;

public class CupUsersScoresDTO {

	private UserDTO currentUser;
	private List<UserRatingPositionDTO> userRatingPositions;

	public UserDTO getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser( final UserDTO currentUser ) {
		this.currentUser = currentUser;
	}

	public List<UserRatingPositionDTO> getUserRatingPositions() {
		return userRatingPositions;
	}

	public void setUserRatingPositions( final List<UserRatingPositionDTO> userRatingPositions ) {
		this.userRatingPositions = userRatingPositions;
	}
}

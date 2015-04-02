package totalizator.app.controllers.rest.scores;

import totalizator.app.dto.UserDTO;

public class CupUsersScoresDTO {

	private UserDTO currentUser;

	public UserDTO getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser( final UserDTO currentUser ) {
		this.currentUser = currentUser;
	}
}

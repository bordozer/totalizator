package totalizator.app.controllers.rest.scores;

import totalizator.app.dto.UserDTO;
import totalizator.app.dto.points.UserMatchPointsHolderDTO;

import java.util.List;

public class UserMatchesPointsDTO {

	private final UserDTO user;
	private final List<UserMatchPointsHolderDTO> userMatchesPoints;

	public UserMatchesPointsDTO( final UserDTO user, final List<UserMatchPointsHolderDTO> userMatchesPoints ) {
		this.user = user;
		this.userMatchesPoints = userMatchesPoints;
	}

	public UserDTO getUser() {
		return user;
	}

	public List<UserMatchPointsHolderDTO> getUserMatchesPoints() {
		return userMatchesPoints;
	}
}

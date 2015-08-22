package totalizator.app.controllers.rest.scores;

import totalizator.app.dto.points.UserCupPointsHolderDTO;
import totalizator.app.dto.UserDTO;

public class UserRatingPositionDTO {

	private final UserDTO user;
	private final UserCupPointsHolderDTO userCupPointsHolder;

	public UserRatingPositionDTO( final UserDTO user, final UserCupPointsHolderDTO userCupPointsHolder ) {
		this.user = user;
		this.userCupPointsHolder = userCupPointsHolder;
	}

	public UserDTO getUser() {
		return user;
	}

	public UserCupPointsHolderDTO getUserCupPointsHolder() {
		return userCupPointsHolder;
	}
}

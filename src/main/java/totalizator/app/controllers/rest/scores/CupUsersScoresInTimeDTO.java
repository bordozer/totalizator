package totalizator.app.controllers.rest.scores;

import java.util.List;
import java.util.Map;

public class CupUsersScoresInTimeDTO {

	private final List<String> dates;
	private final Map<Integer, UserMatchesPointsDTO> usersPointsMap;

	public CupUsersScoresInTimeDTO( final List<String> dates, final Map<Integer, UserMatchesPointsDTO> usersPointsMap ) {
		this.dates = dates;
		this.usersPointsMap = usersPointsMap;
	}

	public List<String> getDates() {
		return dates;
	}

	public Map<Integer, UserMatchesPointsDTO> getUsersPointsMap() {
		return usersPointsMap;
	}
}

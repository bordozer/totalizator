package totalizator.app.services.points;

import totalizator.app.controllers.rest.scores.CupUsersScoresInTimeDTO;

public interface UserRatingService {

	String CACHE_QUERY = "totalizator.app.cache.userRatingService";

	CupUsersScoresInTimeDTO cupUsersScoresInTime( int cupId, int userGroupId );
}

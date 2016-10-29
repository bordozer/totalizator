package betmen.web.converters;

import betmen.dto.dto.points.scores.CupUsersScoresInTimeDTO;

public interface UserRatingService {

    CupUsersScoresInTimeDTO cupUsersScoresInTime(int cupId, int userGroupId);
}

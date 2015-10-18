package totalizator.app.services.points.calculation.match;

import totalizator.app.beans.points.UserMatchPointsHolder;
import totalizator.app.models.Match;
import totalizator.app.models.User;
import totalizator.app.models.UserGroup;

public interface UserMatchPointsCalculationService {

	UserMatchPointsHolder calculateUserMatchPoints( final Match match, final User user );

	UserMatchPointsHolder calculateUserMatchPoints( final Match match, final User user, final UserGroup userGroup );
}

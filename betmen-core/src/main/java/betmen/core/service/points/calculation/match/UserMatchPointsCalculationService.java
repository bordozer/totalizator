package betmen.core.service.points.calculation.match;

import betmen.core.model.points.UserMatchPointsHolder;
import betmen.core.entity.Match;
import betmen.core.entity.User;
import betmen.core.entity.UserGroup;

public interface UserMatchPointsCalculationService {

    UserMatchPointsHolder calculateUserMatchPoints(final Match match, final User user);

    UserMatchPointsHolder calculateUserMatchPoints(final Match match, final User user, final UserGroup userGroup);
}

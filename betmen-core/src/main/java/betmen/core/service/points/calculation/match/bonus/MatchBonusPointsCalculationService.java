package betmen.core.service.points.calculation.match.bonus;

import betmen.core.entity.Match;
import betmen.core.entity.UserGroupEntity;

public interface MatchBonusPointsCalculationService {

    float calculateMatchBonus(final Match match);

    float calculateMatchBonus(final Match match, final UserGroupEntity userGroupEntity);
}

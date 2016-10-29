package betmen.core.service.points.calculation.match.bonus;

import betmen.core.entity.Match;
import betmen.core.entity.UserGroup;

public interface MatchBonusPointsCalculationService {

    float calculateMatchBonus(final Match match);

    float calculateMatchBonus(final Match match, final UserGroup userGroup);
}

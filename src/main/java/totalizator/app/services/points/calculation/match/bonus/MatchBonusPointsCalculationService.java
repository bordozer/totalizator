package totalizator.app.services.points.calculation.match.bonus;

import totalizator.app.models.Match;
import totalizator.app.models.UserGroup;

public interface MatchBonusPointsCalculationService {

	float calculateMatchBonus( final Match match );

	float calculateMatchBonus( final Match match, final UserGroup userGroup );
}

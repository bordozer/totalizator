package totalizator.app.services.points.match.bonus;

import totalizator.app.models.Match;
import totalizator.app.models.UserGroup;

public interface MatchBonusPointsCalculationService {

	String CACHE_QUERY = "totalizator.app.cache.user-match-bonus-points-calculation.query";

	float calculateMatchBonus( final Match match );

	float calculateMatchBonus( final Match match, final UserGroup userGroup );
}

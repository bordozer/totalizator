package totalizator.app.services.score;

import totalizator.app.models.Match;

public interface MatchBonusPointsCalculationService {

	String CACHE_QUERY = "totalizator.app.cache.user-match-bonus-points-calculation.query";

	float calculateMatchBonus( final Match match );
}

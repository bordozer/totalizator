package totalizator.app.services.score;

import totalizator.app.models.Match;
import totalizator.app.models.User;

import java.util.List;

public interface MatchBonusPointsCalculationService {

	String CACHE_QUERY = "totalizator.app.cache.user-match-bonus-points-calculation.query";

	float calculateMatchBonus( final Match match, final List<User> users );
}

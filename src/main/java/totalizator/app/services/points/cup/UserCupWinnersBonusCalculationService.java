package totalizator.app.services.points.cup;

import totalizator.app.models.Cup;
import totalizator.app.models.Team;
import totalizator.app.models.User;

public interface UserCupWinnersBonusCalculationService {

	String CACHE_QUERY = "totalizator.app.cache.cup-bet-points-calculation.query";

	int getUserCupWinnersSummaryPoints( final Cup cup, final User user );

	int getUserCupWinnerPoints( final Cup cup, final Team team, final User user, final int cupPosition );
}

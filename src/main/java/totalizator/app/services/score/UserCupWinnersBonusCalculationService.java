package totalizator.app.services.score;

import totalizator.app.models.Cup;
import totalizator.app.models.Team;
import totalizator.app.models.User;

public interface UserCupWinnersBonusCalculationService {

	String CACHE_QUERY = "totalizator.app.cache.cup-bet-points-calculation.query";

	int getUserCupWinnersPoints( final Cup cup, final User user );

	int getUserCupWinnersPoints( final Cup cup, final Team team, final User user, final int cupPosition );
}

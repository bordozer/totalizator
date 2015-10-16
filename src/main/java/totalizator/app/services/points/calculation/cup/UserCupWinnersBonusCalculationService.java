package totalizator.app.services.points.calculation.cup;

import totalizator.app.models.Cup;
import totalizator.app.models.Team;
import totalizator.app.models.User;

public interface UserCupWinnersBonusCalculationService {

	int getUserCupWinnersSummaryPoints( final Cup cup, final User user );

	int getUserCupWinnerPoints( final Cup cup, final Team team, final User user, final int cupPosition );
}

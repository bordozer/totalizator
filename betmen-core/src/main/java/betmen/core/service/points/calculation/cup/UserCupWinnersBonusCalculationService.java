package betmen.core.service.points.calculation.cup;

import betmen.core.entity.Cup;
import betmen.core.entity.Team;
import betmen.core.entity.User;

public interface UserCupWinnersBonusCalculationService {

    int getUserCupWinnersSummaryPoints(final Cup cup, final User user);

    int getUserCupWinnerPoints(final Cup cup, final Team team, final User user, final int cupPosition);
}

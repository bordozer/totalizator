package betmen.core.service.points.calculation.match.points;

import betmen.core.model.points.UserMatchBetPointsHolder;
import betmen.core.entity.Cup;
import betmen.core.entity.MatchBet;
import betmen.core.entity.User;

import java.util.List;

public interface UserMatchBetPointsCalculationService {

    UserMatchBetPointsHolder getUserMatchBetPoints(final MatchBet matchBet);

    List<UserMatchBetPointsHolder> getUsersMatchBetsPointHolders(final Cup cup, final List<User> users);

    List<UserMatchBetPointsHolder> getUserPoints(final Cup cup, final User user);

    int getUserMatchBetPointsPositive(final Cup cup, final User user);

    int getUserMatchBetPointsNegative(final Cup cup, final User user);
}

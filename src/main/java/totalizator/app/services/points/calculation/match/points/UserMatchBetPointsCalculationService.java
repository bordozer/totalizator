package totalizator.app.services.points.calculation.match.points;

import totalizator.app.beans.points.UserMatchBetPointsHolder;
import totalizator.app.models.Cup;
import totalizator.app.models.MatchBet;
import totalizator.app.models.User;

import java.util.List;

public interface UserMatchBetPointsCalculationService {

	UserMatchBetPointsHolder getUserMatchBetPoints( final MatchBet matchBet );

	List<UserMatchBetPointsHolder> getUsersMatchBetsPointHolders( final Cup cup, final List<User> users );

	List<UserMatchBetPointsHolder> getUserPoints( final Cup cup, final User user );

	int getUserMatchBetPointsPositive( final Cup cup, final User user );

	int getUserMatchBetPointsNegative( final Cup cup, final User user );
}

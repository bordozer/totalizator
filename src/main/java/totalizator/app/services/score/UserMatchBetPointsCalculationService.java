package totalizator.app.services.score;

import org.springframework.cache.annotation.Cacheable;
import totalizator.app.beans.points.UserMatchBetPointsHolder;
import totalizator.app.models.Cup;
import totalizator.app.models.MatchBet;
import totalizator.app.models.User;

import java.util.List;

public interface UserMatchBetPointsCalculationService {

	String CACHE_QUERY = "totalizator.app.cache.user-match-points-calculation.query";

	UserMatchBetPointsHolder getUserMatchBetPoints( final MatchBet matchBet );

	List<UserMatchBetPointsHolder> getUsersMatchBetsPointHolders( final Cup cup, final List<User> users );

	List<UserMatchBetPointsHolder> getUserPoints( final Cup cup, final User user );

	@Cacheable( value = CACHE_QUERY )
	int getUserMatchBetPointsPositive( Cup cup, User user );

	@Cacheable( value = CACHE_QUERY )
	int getUserMatchBetPointsNegative( Cup cup, User user );
}

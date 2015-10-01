package totalizator.app.services.points;

import totalizator.app.beans.points.UserCupPointsHolder;
import totalizator.app.beans.points.UserMatchPointsHolder;
import totalizator.app.models.*;

import java.util.List;

public interface UserMatchPointsCalculationService {

	String CACHE_QUERY = "totalizator.app.cache.user-bet-points-calculation.query";

	UserMatchPointsHolder getUserMatchPoints( final Match match, final User user );

	UserMatchPointsHolder getUserMatchPoints( final Match match, final User user, final UserGroup userGroup );

	List<UserCupPointsHolder> getUsersCupPoints( final Cup cup );

	List<UserCupPointsHolder> getUsersCupPoints( final Cup cup, final UserGroup userGroup );
}

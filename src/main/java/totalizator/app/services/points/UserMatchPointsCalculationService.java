package totalizator.app.services.points;

import totalizator.app.beans.points.UserCupPointsHolder;
import totalizator.app.beans.points.UserMatchPointsHolder;
import totalizator.app.beans.points.UserSummaryPointsHolder;
import totalizator.app.models.Cup;
import totalizator.app.models.Match;
import totalizator.app.models.User;
import totalizator.app.models.UserGroup;

import java.time.LocalDate;
import java.util.List;

public interface UserMatchPointsCalculationService {

	String CACHE_QUERY = "totalizator.app.cache.user-bet-points-calculation.query";

	UserMatchPointsHolder getUserMatchPoints( final Match match, final User user );

	UserMatchPointsHolder getUserMatchPoints( final Match match, final User user, final UserGroup userGroup );

	UserSummaryPointsHolder getUserMatchPoints( final User user, final LocalDate date );

	List<UserSummaryPointsHolder> getUsersRatingOnDate( final LocalDate date );

	List<UserCupPointsHolder> getUsersCupPoints( final Cup cup );

	List<UserCupPointsHolder> getUsersCupPoints( final Cup cup, final UserGroup userGroup );
}

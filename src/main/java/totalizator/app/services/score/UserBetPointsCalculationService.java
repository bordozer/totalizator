package totalizator.app.services.score;

import totalizator.app.beans.points.UserCupPointsHolder;
import totalizator.app.beans.points.UserMatchPointsHolder;
import totalizator.app.models.Cup;
import totalizator.app.models.MatchBet;
import totalizator.app.models.UserGroup;

import java.util.List;

public interface UserBetPointsCalculationService {

	String CACHE_QUERY = "totalizator.app.cache.user-bet-points-calculation.query";

	UserMatchPointsHolder getUserMatchPoints( final MatchBet matchBet );

	List<UserCupPointsHolder> getUsersCupPoints( final Cup cup );

	List<UserCupPointsHolder> getUsersCupPoints( final Cup cup, final UserGroup userGroup );
}

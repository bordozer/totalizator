package totalizator.app.services.points;

import totalizator.app.beans.points.UserCupPointsHolder;
import totalizator.app.models.Cup;
import totalizator.app.models.UserGroup;

import java.util.List;

public interface CupPointsService {

	String CACHE_QUERY = "totalizator.app.cache.cupPointsService";

	List<UserCupPointsHolder> getUsersCupPoints( final Cup cup );

	List<UserCupPointsHolder> getUsersCupPoints( final Cup cup, final UserGroup userGroup );
}

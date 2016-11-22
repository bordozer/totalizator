package betmen.core.service.points;

import betmen.core.entity.UserGroupEntity;
import betmen.core.model.points.UserCupPointsHolder;
import betmen.core.entity.Cup;

import java.util.List;

public interface CupPointsService {

    String CACHE_QUERY = "totalizator.app.cache.cupPointsService";

    List<UserCupPointsHolder> getUsersCupPoints(final Cup cup);

    List<UserCupPointsHolder> getUsersCupPoints(final Cup cup, final UserGroupEntity userGroupEntity);
}

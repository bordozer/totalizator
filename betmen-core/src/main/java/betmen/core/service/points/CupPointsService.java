package betmen.core.service.points;

import betmen.core.model.points.UserCupPointsHolder;
import betmen.core.entity.Cup;
import betmen.core.entity.UserGroup;

import java.util.List;

public interface CupPointsService {

    String CACHE_QUERY = "totalizator.app.cache.cupPointsService";

    List<UserCupPointsHolder> getUsersCupPoints(final Cup cup);

    List<UserCupPointsHolder> getUsersCupPoints(final Cup cup, final UserGroup userGroup);
}

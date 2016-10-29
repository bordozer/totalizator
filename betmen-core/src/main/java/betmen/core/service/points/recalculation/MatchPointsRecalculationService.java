package betmen.core.service.points.recalculation;

import betmen.core.entity.Match;

public interface MatchPointsRecalculationService {

    void recalculate(final Match match);

}

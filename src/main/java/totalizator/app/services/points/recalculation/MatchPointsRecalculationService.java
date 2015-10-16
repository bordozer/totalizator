package totalizator.app.services.points.recalculation;

import totalizator.app.models.Match;

public interface MatchPointsRecalculationService {

	void recalculate( final Match match );

}

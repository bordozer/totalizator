package totalizator.app.services.matches;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.models.Match;
import totalizator.app.services.activiries.ActivityStreamService;
import totalizator.app.services.points.MatchPointsService;
import totalizator.app.services.points.recalculation.MatchPointsRecalculationService;

@Service
public class MatchUpdateServiceImpl implements MatchUpdateService {

	@Autowired
	private MatchService matchService;

	@Autowired
	private MatchPointsRecalculationService matchPointsRecalculationService;

	@Autowired
	private MatchPointsService matchPointsService;

	@Autowired
	private ActivityStreamService activityStreamService;

	@Override
	public Match update( final Match match ) {

		final Match saved = matchService.save( match );

		matchPointsService.delete( match );

		if ( match.isMatchFinished() ) {

			matchPointsRecalculationService.recalculate( match );

			activityStreamService.matchFinished( match.getId(), match.getScore1(), match.getScore2() );
		}

		return saved;
	}
}

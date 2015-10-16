package totalizator.app.services.points.recalculation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.models.Match;
import totalizator.app.services.CupService;
import totalizator.app.services.matches.MatchService;

import java.util.List;
import java.util.function.Consumer;

@Service
public class MatchPointsTotalRecalculationServiceImpl implements MatchPointsTotalRecalculationService {

	@Autowired
	private CupService cupService;

	@Autowired
	private MatchService matchService;

	@Autowired
	private MatchPointsRecalculationService matchPointsRecalculationService;

	private final MatchesPointsRecalculationMonitor monitor = new MatchesPointsRecalculationMonitor();

	@Override
	public void run() {

		if ( monitor.isInProgress() ) {
			return;
		}

		synchronized ( monitor ) {

			if ( monitor.isInProgress() ) {
				return;
			}

			monitor.startProgress();
		}

		final List<Match> matches = matchService.loadAll();

		monitor.setTotalSteps( matches.size() );

		matches
				.stream()
				.forEach( new Consumer<Match>() {
					@Override
					public void accept( final Match match ) {

						monitor.increase();

						if ( monitor.isBrokenByUser() ) {
							return;
						}

						matchPointsRecalculationService.recalculate( match );
					}
				} );

		monitor.reset();
	}

	@Override
	public void stop() {
		monitor.setBrokenByUser( true );
	}

	@Override
	public MatchesPointsRecalculationMonitor getMonitor() {
		return monitor;
	}
}

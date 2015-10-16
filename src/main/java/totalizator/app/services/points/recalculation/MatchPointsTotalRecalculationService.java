package totalizator.app.services.points.recalculation;

public interface MatchPointsTotalRecalculationService {

	void run();

	void stop();

	MatchesPointsRecalculationMonitor getMonitor();
}

package betmen.core.service.points.recalculation;

public interface MatchPointsTotalRecalculationService {

    void run();

    void stop();

    MatchesPointsRecalculationMonitor getMonitor();
}

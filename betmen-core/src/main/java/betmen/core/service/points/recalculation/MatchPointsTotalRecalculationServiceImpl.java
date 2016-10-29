package betmen.core.service.points.recalculation;

import betmen.core.entity.Match;
import betmen.core.service.CupService;
import betmen.core.service.matches.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        if (monitor.isInProgress()) {
            return;
        }
        synchronized (monitor) {
            if (monitor.isInProgress()) {
                return;
            }
            monitor.startProgress();
        }

        final List<Match> matches = matchService.loadAll();
        monitor.setTotalSteps(matches.size());
        matches.stream().forEach(match -> {
            monitor.increase();
            if (monitor.isBrokenByUser()) {
                return;
            }
            matchPointsRecalculationService.recalculate(match);
        });

        monitor.reset();
    }

    @Override
    public void stop() {
        monitor.setBrokenByUser(true);
    }

    @Override
    public MatchesPointsRecalculationMonitor getMonitor() {
        return monitor;
    }
}

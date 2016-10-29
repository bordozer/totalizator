package betmen.core.service.matches;

import betmen.core.entity.Match;
import betmen.core.service.activiries.ActivityStreamService;
import betmen.core.service.points.MatchPointsService;
import betmen.core.service.points.recalculation.MatchPointsRecalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Match update(final Match match) {

        final Match saved = matchService.save(match);

        matchPointsService.delete(match);

        if (match.isMatchFinished()) {

            matchPointsRecalculationService.recalculate(match);

            activityStreamService.matchFinished(match.getId(), match.getScore1(), match.getScore2());
        }

        return saved;
    }
}

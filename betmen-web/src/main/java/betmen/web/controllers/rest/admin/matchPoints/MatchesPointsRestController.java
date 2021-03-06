package betmen.web.controllers.rest.admin.matchPoints;

import betmen.core.service.points.recalculation.MatchPointsTotalRecalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/rest/matches-points-recalculation")
public class MatchesPointsRestController {

    @Autowired
    private MatchPointsTotalRecalculationService matchPointsTotalRecalculationService;

    @RequestMapping(method = RequestMethod.POST, value = "/start/")
    public void runMatchesPointsRecalculation() {
        matchPointsTotalRecalculationService.run();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/stop/")
    public void stop() {
        matchPointsTotalRecalculationService.stop();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/status/")
    public MatchPointsRecalculationStatusDTO matchesPointsRecalculationStatus() {
        return new MatchPointsRecalculationStatusDTO(matchPointsTotalRecalculationService.getMonitor());
    }
}

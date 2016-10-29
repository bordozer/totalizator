package betmen.web.controllers.ui.teams.matches;

import betmen.core.service.CupService;
import betmen.core.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/betmen/cups/{cupId}/matches/teams/{teamId}")
public class TeamMatchesController {

    public static final String MODEL_NAME = "teamMatchesMode";

    private static final String VIEW = "/TeamMatches";

    @Autowired
    private TeamService teamService;

    @Autowired
    private CupService cupService;

    @ModelAttribute(MODEL_NAME)
    public TeamMatchesModel preparePagingModel() {
        return new TeamMatchesModel();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String cupMatchesForTeam(
            final @PathVariable("cupId") int cupId
            , final @PathVariable("teamId") int teamId
            , final @ModelAttribute(MODEL_NAME) TeamMatchesModel model) {

        model.setCup(cupService.load(cupId));
        model.setTeam(teamService.load(teamId));

        return VIEW;
    }
}

package betmen.web.controllers.ui.teams.card;

import betmen.core.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/betmen/teams/{teamId}/")
public class TeamCardController {

    public static final String MODEL_NAME = "teamCardModel";

    private static final String VIEW = "/TeamCard";

    @Autowired
    private TeamService teamService;

    @ModelAttribute(MODEL_NAME)
    public TeamCardModel preparePagingModel() {
        return new TeamCardModel();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String portalPage(final @PathVariable("teamId") int teamId, final @ModelAttribute(MODEL_NAME) TeamCardModel model) {

        model.setTeam(teamService.load(teamId));

        return VIEW;
    }
}

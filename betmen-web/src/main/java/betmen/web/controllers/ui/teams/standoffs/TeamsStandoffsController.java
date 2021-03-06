package betmen.web.controllers.ui.teams.standoffs;

import betmen.core.entity.Team;
import betmen.core.service.TeamService;
import betmen.core.service.UserService;
import betmen.core.service.matches.MatchService;
import betmen.core.service.teams.TeamsStandoffService;
import betmen.web.converters.DTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
@RequestMapping("/betmen/teams/")
public class TeamsStandoffsController {

    public static final String MODEL_NAME = "teamsStandoffsModel";

    private static final String VIEW = "/TeamsStandoffs";

    @Autowired
    private TeamsStandoffService teamsStandoffService;

    @Autowired
    private UserService userService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private MatchService matchService;

    @Autowired
    private DTOService dtoService;

    @ModelAttribute(MODEL_NAME)
    public TeamsStandoffsModel preparePagingModel(final Principal principal) {
        return new TeamsStandoffsModel();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/standoff/{team1Id}/vs/{team2Id}/")
    public String teamsStandoff(final @PathVariable("team1Id") int team1Id, final @PathVariable("team2Id") int team2Id
            , final @ModelAttribute(MODEL_NAME) TeamsStandoffsModel model, final Principal principal) {

        final Team team1 = teamService.load(team1Id);
        final Team team2 = teamService.load(team2Id);

        model.setTeam1(team1);
        model.setTeam2(team2);

        return VIEW;
    }
}

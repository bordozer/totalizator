package betmen.web.controllers.rest.teams;

import betmen.core.entity.Category;
import betmen.core.entity.User;
import betmen.core.service.CategoryService;
import betmen.core.service.CupService;
import betmen.core.service.CupTeamService;
import betmen.core.service.TeamService;
import betmen.core.service.UserService;
import betmen.dto.dto.TeamDTO;
import betmen.web.converters.DTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/rest/teams")
public class TeamsRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private CupService cupService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private DTOService dtoService;
    @Autowired
    private CupTeamService cupTeamService;

    @RequestMapping(method = RequestMethod.GET, value = "/{teamId}/")
    public TeamDTO team(final @PathVariable("teamId") int teamId, final Principal principal) {
        return dtoService.transformTeam(teamService.load(teamId), getCurrentUser(principal));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/categories/{categoryId}/")
    public List<TeamDTO> categoryTeams(final @PathVariable("categoryId") int categoryId, final Principal principal) {
        final Category category = categoryService.load(categoryId);
        return dtoService.transformTeams(category, teamService.loadAll(category.getId()), getCurrentUser(principal));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/cup/{cupId}/")
    public List<TeamDTO> cupTeams(final @PathVariable("cupId") int cupId, final Principal principal) {
        final Category category = cupService.load(cupId).getCategory();
        return dtoService.transformTeams(category, teamService.loadAll(category.getId()), getCurrentUser(principal));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/cup/{cupId}/active/")
    public List<TeamDTO> cupTeamsActive(final @PathVariable("cupId") int cupId, final Principal principal) {
        return dtoService.transformTeams(cupService.load(cupId).getCategory(), cupTeamService.loadActiveForCup(cupId), getCurrentUser(principal));
    }

    private User getCurrentUser(final Principal principal) {
        return userService.findByLogin(principal.getName());
    }
}

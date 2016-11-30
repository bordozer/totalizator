package betmen.web.controllers.rest;

import betmen.core.entity.Cup;
import betmen.core.entity.Team;
import betmen.core.entity.User;
import betmen.core.service.CupService;
import betmen.core.service.CupTeamService;
import betmen.core.service.TeamService;
import betmen.core.service.UserService;
import betmen.dto.dto.CupTeamsDTO;
import betmen.web.converters.DTOService;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Sets.newHashSet;

@RestController
@RequestMapping("/rest/cups/{cupId}/teams")
public class CupTeamsRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private CupService cupService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private CupTeamService cupTeamService;
    @Autowired
    private DTOService dtoService;

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public CupTeamsDTO all(@PathVariable("cupId") final int cupId, @RequestParam(value = "letter", required = false) final String letter,
                           @RequestParam(value = "active", required = false) final boolean active, final Principal principal) {
        final User currentUser = userService.findByLogin(principal.getName());
        final Cup cup = cupService.loadAndAssertExists(cupId);
        final List<Team> teams = teamService.loadAll(cup.getCategory().getId()); // I need first letters of all teams
        final List<String> letters = teams.stream().map(this::getFirstLetter).collect(Collectors.toList());
        if (!active && StringUtils.isNotEmpty(letter)) {
            CollectionUtils.filter(teams, team -> StringUtils.isNotEmpty(team.getTeamName()) && team.getTeamName().substring(0, 1).equalsIgnoreCase(letter));
        }
        if (active) {
            return new CupTeamsDTO(dtoService.transformTeams(cup.getCategory(), cupTeamService.loadActiveForCup(cupId), currentUser), newHashSet(letters));
        }
        return new CupTeamsDTO(dtoService.transformTeams(cup.getCategory(), teams, currentUser), newHashSet(letters));
    }

    private String getFirstLetter(final Team team) {
        return StringUtils.isNotEmpty(team.getTeamName()) ? team.getTeamName().substring(0, 1) : "?";
    }
}

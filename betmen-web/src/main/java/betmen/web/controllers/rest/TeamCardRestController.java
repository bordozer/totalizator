package betmen.web.controllers.rest;

import betmen.core.entity.Cup;
import betmen.core.entity.CupWinner;
import betmen.core.entity.Team;
import betmen.core.entity.User;
import betmen.core.service.CupService;
import betmen.core.service.CupWinnerService;
import betmen.core.service.TeamService;
import betmen.core.service.UserService;
import betmen.core.service.matches.MatchService;
import betmen.dto.dto.TeamCardCupData;
import betmen.dto.dto.TeamCardDTO;
import betmen.web.converters.DTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

@RestController
@RequestMapping("/rest/teams/{teamId}")
public class TeamCardRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private CupService cupService;

    @Autowired
    private MatchService matchService;

    @Autowired
    private CupWinnerService cupWinnerService;

    @Autowired
    private DTOService dtoService;

    @RequestMapping(method = RequestMethod.GET, value = "/cup/{cupId}/statistics/")
    public TeamCardCupData cupTeamStatistics(final @PathVariable("teamId") int teamId, final @PathVariable("cupId") int cupId, final Principal principal) {
        final User currentUser = userService.findByLogin(principal.getName());
        final Team team = teamService.loadAndAssertExists(teamId);
        final Cup cup = cupService.loadAndAssertExists(cupId);
        return getTeamCardCupData(currentUser, team, cup);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/card/")
    public TeamCardDTO teamCard(final @PathVariable("teamId") int teamId, final Principal principal) {
        final User currentUser = userService.findByLogin(principal.getName());
        final Team team = teamService.loadAndAssertExists(teamId);

        final TeamCardDTO dto = new TeamCardDTO();
        dto.setTeam(dtoService.transformTeam(team, currentUser));

        final List<TeamCardCupData> cupDataMap = newArrayList();
        cupDataMap.addAll(cupService.loadPublic(team.getCategory()).stream()
                .map(cup -> getTeamCardCupData(currentUser, team, cup))
                .collect(Collectors.toList()));
        dto.setCardCupData(cupDataMap.stream()
                .filter(teamCardCupData -> teamCardCupData.getFutureMatchesCount() + teamCardCupData.getFinishedMatchCount() > 0)
                .collect(Collectors.toList()));
        return dto;
    }

    private TeamCardCupData getTeamCardCupData(final User currentUser, final Team team, final Cup cup) {
        final TeamCardCupData cupData = new TeamCardCupData(dtoService.transformCup(cup, currentUser));
        cupData.setFinishedMatchCount(matchService.getFinishedMatchCount(cup, team));
        cupData.setWonMatchCount(matchService.getWonMatchCount(cup, team));
        cupData.setFutureMatchesCount(matchService.getFutureMatchCount(cup, team));

        final CupWinner cupWinner = cupWinnerService.load(cup, team);
        if (cupWinner != null) {
            cupData.setCupWinner(dtoService.transformCupWinner(cupWinner, currentUser));
        }
        return cupData;
    }
}

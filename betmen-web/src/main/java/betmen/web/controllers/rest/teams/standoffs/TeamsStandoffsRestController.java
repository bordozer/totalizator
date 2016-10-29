package betmen.web.controllers.rest.teams.standoffs;

import betmen.core.entity.Cup;
import betmen.core.entity.Match;
import betmen.core.entity.Team;
import betmen.core.entity.User;
import betmen.core.service.CupService;
import betmen.core.service.TeamService;
import betmen.core.service.UserService;
import betmen.core.service.matches.MatchService;
import betmen.core.service.teams.TeamsCupStandoff;
import betmen.core.service.teams.TeamsStandoffService;
import betmen.web.converters.DTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/teams")
public class TeamsStandoffsRestController {

    @Autowired
    private TeamService teamService;
    @Autowired
    private UserService userService;
    @Autowired
    private MatchService matchService;
    @Autowired
    private CupService cupService;
    @Autowired
    private TeamsStandoffService teamsStandoffService;
    @Autowired
    private DTOService dtoService;

    @RequestMapping(method = RequestMethod.GET, value = "{team1Id}/vs/{team2Id}/")
    public TeamsStandoffsDTO all(@PathVariable("team1Id") final int team1Id, @PathVariable("team2Id") final int team2Id, final Principal principal) {
        final User currentUser = userService.findByLogin(principal.getName());
        final Team team1 = teamService.load(team1Id);
        final Team team2 = teamService.load(team2Id);
        final TeamsStandoffsDTO dto = new TeamsStandoffsDTO();
        dto.setTeam1(dtoService.transformTeam(team1, currentUser));
        dto.setTeam2(dtoService.transformTeam(team2, currentUser));
        final Cup cup = teamsStandoffService.getLastStandoffCup(team1, team2);
        if (cup == null) {
            return dto;
        }
        dto.setCupToShow(dtoService.transformCup(cup, userService.findByLogin(principal.getName())));
        final List<TeamsCupStandoffDTO> standoffByCup = getTeamsCupStandoffDTOs(team1, team2, currentUser);
        dto.setStandoffsByCup(standoffByCup);
        dto.setTeamsLastGamesStat(getTeamsLastGamesStat(team1, team2, currentUser));
        return dto;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/team1/{team1Id}/team2/{team2Id}/statistics/cup/{cupId}/")
    public TeamsCupStatistics matchAndBetTeamsStatistics(@PathVariable("team1Id") final int team1Id, @PathVariable("team2Id") final int team2Id,
                                                         @PathVariable("cupId") final int cupId) {
        final Cup cup = cupService.load(cupId);
        final Team team1 = teamService.load(team1Id);
        final Team team2 = teamService.load(team2Id);
        final TeamsCupStatistics result = new TeamsCupStatistics();

        final int team1MonMatchCount = matchService.getWonMatchCount(cup, team1);
        final int team1FinishedMatchCount = matchService.getFinishedMatchCount(cup, team1);
        result.setTeam1won(team1MonMatchCount);
        result.setTeam1lost(team1FinishedMatchCount - team1MonMatchCount);
        result.setTeam1Total(team1FinishedMatchCount);

        final int team2MonMatchCount = matchService.getWonMatchCount(cup, team2);
        final int team2FinishedMatchCount = matchService.getFinishedMatchCount(cup, team2);
        result.setTeam2won(team2MonMatchCount);
        result.setTeam2lost(team2FinishedMatchCount - team2MonMatchCount);
        result.setTeam2Total(team2FinishedMatchCount);

        return result;
    }

    private List<TeamsCupStandoffDTO> getTeamsCupStandoffDTOs(final Team team1, final Team team2, final User currentUser) {
        return teamsStandoffService.getTeamsStandoffByCups(team1, team2)
                .stream()
                .map(cupStandoff -> toDto(currentUser, cupStandoff))
                .collect(Collectors.toList());
    }

    private TeamsCupStandoffDTO toDto(final User currentUser, final TeamsCupStandoff cupStandoff) {
        Cup cup = cupStandoff.getCup();
        final TeamsCupStandoffDTO dto = new TeamsCupStandoffDTO();
        dto.setCup(dtoService.transformCup(cup, currentUser));
        dto.setScore1(cupStandoff.getScore1());
        dto.setScore2(cupStandoff.getScore2());
        return dto;
    }

    private TeamsLastGamesStat getTeamsLastGamesStat(final Team team1, final Team team2, final User currentUser) {
        final List<Cup> cups = cupService.loadPublicCurrent(team1.getCategory());
        if (CollectionUtils.isEmpty(cups)) {
            return new TeamsLastGamesStat();
        }
        final Cup cup = cups.get(0);
        final List<Match> team1Matches = matchService.getLastNMatches(cup, team1, 3);
        final List<Match> team2Matches = matchService.getLastNMatches(cup, team2, 3);
        final TeamsLastGamesStat result = new TeamsLastGamesStat();
        result.setCup(dtoService.transformCup(cup, currentUser));
        result.setTeam1CurrentCupLastGames(team1Matches.size());
        result.setTeam1CurrentCupLastGamesWon(team1Matches.stream()
                .filter(match -> matchService.isWinner(match, team1))
                .count()
        );
        result.setTeam2CurrentCupLastGames(team2Matches.size());
        result.setTeam2CurrentCupLastGamesWon(team2Matches.stream()
                .filter(match -> matchService.isWinner(match, team2))
                .count());
        return result;
    }
}

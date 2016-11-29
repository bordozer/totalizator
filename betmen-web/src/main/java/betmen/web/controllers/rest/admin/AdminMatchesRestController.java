package betmen.web.controllers.rest.admin;

import betmen.core.entity.Cup;
import betmen.core.entity.Match;
import betmen.core.entity.Team;
import betmen.core.entity.User;
import betmen.core.exception.BadRequestException;
import betmen.core.service.CupService;
import betmen.core.service.TeamService;
import betmen.core.service.UserService;
import betmen.core.service.matches.MatchBetsService;
import betmen.core.service.matches.MatchService;
import betmen.core.service.matches.MatchUpdateService;
import betmen.core.service.matches.MatchesAndBetsWidgetService;
import betmen.dto.dto.MatchSearchModelDto;
import betmen.dto.dto.admin.MatchEditDTO;
import betmen.web.converters.DTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/rest/matches")
public class AdminMatchesRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private MatchService matchService;
    @Autowired
    private MatchUpdateService matchUpdateService;
    @Autowired
    private MatchesAndBetsWidgetService matchesAndBetsWidgetService;
    @Autowired
    private CupService cupService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private MatchBetsService matchBetsService;
    @Autowired
    private DTOService dtoService;

    @RequestMapping(method = RequestMethod.GET, value = "/{matchId}")
    public MatchEditDTO getItem(final @PathVariable("matchId") int matchId, final Principal principal) {
        return convertToEditDto(matchService.loadAndAssertExists(matchId), getCurrentUser(principal));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public List<MatchEditDTO> getAllItems(final @Validated MatchSearchModelDto searchModel, final Principal principal) {
        final User currentUser = getCurrentUser(principal);
        return matchesAndBetsWidgetService.loadAll(dtoService.transformMatchSearchModel(searchModel)).stream()
                .map(match -> convertToEditDto(match, currentUser))
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/0")
    public MatchEditDTO createItem(final @Validated @RequestBody MatchEditDTO dto, final Principal principal) {
        validateDto(dto);
        final User currentUser = getCurrentUser(principal);
        final Match match = new Match();
        populateEntity(match, dto);
        return convertToEditDto(matchService.save(match), currentUser);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{matchId}")
    public MatchEditDTO save(final @PathVariable("matchId") int matchId, final @Validated @RequestBody MatchEditDTO dto, final Principal principal) {
        validateDto(dto);
        final User currentUser = getCurrentUser(principal);
        final Match match = matchService.loadAndAssertExists(dto.getMatchId());
        populateEntity(match, dto);
        return convertToEditDto(matchUpdateService.update(match), currentUser);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{matchId}")
    public boolean delete(final @PathVariable("matchId") int matchId) {
        if (matchId == 0) {
            return true;
        }
        Match match = matchService.loadAndAssertExists(matchId);
        matchService.delete(match.getId());
        return true;
    }

    private MatchEditDTO convertToEditDto(final Match match, final User currentUser) {
        final MatchEditDTO dto = new MatchEditDTO();
        dto.setMatchId(match.getId());
        dto.setCupId(match.getCup().getId());
        dto.setTeam1Id(match.getTeam1().getId());
        dto.setScore1(match.getScore1());
        dto.setTeam2Id(match.getTeam2().getId());
        dto.setScore2(match.getScore2());
        dto.setBeginningTime(match.getBeginningTime());
        dto.setMatchFinished(match.isMatchFinished());
        dto.setHomeTeamNumber(match.getHomeTeamNumber());
        dto.setMatchDescription(match.getDescription());
        dto.setRemoteGameId(match.getRemoteGameId());
        populateReadOnlyProperties(dto, match);
        return dto;
    }

    private void populateReadOnlyProperties(final MatchEditDTO dto, final Match match) {
        dto.setCategoryId(match.getCup().getCategory().getId());
        dto.setTeam1(dtoService.transformTeam(match.getTeam1()));
        dto.setTeam2(dtoService.transformTeam(match.getTeam2()));
        dto.setBetsCount(matchBetsService.betsCount(match.getId()));
    }

    private void populateEntity(final Match match, final MatchEditDTO matchEditDTO) {
        match.setCup(cupService.loadAndAssertExists(matchEditDTO.getCupId()));
        match.setTeam1(teamService.loadAndAssertExists(matchEditDTO.getTeam1Id()));
        match.setScore1(matchEditDTO.getScore1());
        match.setTeam2(teamService.loadAndAssertExists(matchEditDTO.getTeam2Id()));
        match.setScore2(matchEditDTO.getScore2());
        match.setBeginningTime(matchEditDTO.getBeginningTime());
        match.setMatchFinished(matchEditDTO.isMatchFinished());
        match.setHomeTeamNumber(matchEditDTO.getHomeTeamNumber());
        match.setDescription(matchEditDTO.getMatchDescription());
    }

    private void validateDto(final MatchEditDTO match) {
        int categoryId = match.getCategoryId();
        int cupId = match.getCupId();
        int team1Id = match.getTeam1Id();
        int team2Id = match.getTeam2Id();

        Cup cup = cupService.loadAndAssertExists(cupId);
        Team team1 = teamService.loadAndAssertExists(team1Id);
        Team team2 = teamService.loadAndAssertExists(team2Id);

        if (!team1.getCategory().equals(cup.getCategory())) {
            throw new BadRequestException(String.format("Team %s has category %s, but cup has %s", team1, team1.getCategory(), cup.getCategory()));
        }

        if (!team2.getCategory().equals(cup.getCategory())) {
            throw new BadRequestException(String.format("Team %s has category %s, but cup has %s", team2, team2.getCategory(), cup.getCategory()));
        }
    }

    private User getCurrentUser(final Principal principal) {
        return userService.findByLogin(principal.getName());
    }
}

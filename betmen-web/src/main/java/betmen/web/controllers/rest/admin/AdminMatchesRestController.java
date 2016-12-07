package betmen.web.controllers.rest.admin;

import betmen.core.entity.Cup;
import betmen.core.entity.Match;
import betmen.core.entity.Team;
import betmen.core.exception.BadRequestException;
import betmen.core.exception.UnprocessableEntityException;
import betmen.core.service.CupService;
import betmen.core.service.TeamService;
import betmen.core.service.matches.MatchBetsService;
import betmen.core.service.matches.MatchService;
import betmen.core.service.matches.MatchUpdateService;
import betmen.core.service.matches.MatchesAndBetsWidgetService;
import betmen.core.service.utils.DateTimeService;
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
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/rest/matches")
public class AdminMatchesRestController {

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
    private DateTimeService dateTimeService;
    @Autowired
    private DTOService dtoService;

    @RequestMapping(method = RequestMethod.GET, value = "/{matchId}")
    public MatchEditDTO getItem(@PathVariable("matchId") final int matchId) {
        return convertToEditDto(matchService.loadAndAssertExists(matchId));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public List<MatchEditDTO> getAllItems(@Validated final MatchSearchModelDto searchModel, final Principal principal) {
        return matchesAndBetsWidgetService.loadAll(dtoService.transformMatchSearchModel(searchModel)).stream()
                .map(this::convertToEditDto)
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/0")
    public MatchEditDTO createMatch(@Validated @RequestBody final MatchEditDTO dto, final Principal principal) {
        validateDto(dto);
        final Match match = new Match();
        populateEntity(match, dto);
        return convertToEditDto(matchService.save(match));
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{matchId}")
    public MatchEditDTO saveMatch(@PathVariable("matchId") final int matchId, @Validated @RequestBody final MatchEditDTO dto, final Principal principal) {
        validateDto(dto);
        final Match match = matchService.loadAndAssertExists(dto.getMatchId());
        populateEntity(match, dto);
        return convertToEditDto(matchUpdateService.update(match));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{matchId}")
    public boolean deleteMatch(@PathVariable("matchId") final int matchId) {
        if (matchId == 0) {
            return true;
        }
        Match match = matchService.loadAndAssertExists(matchId);
        matchService.delete(match.getId());
        return true;
    }

    private MatchEditDTO convertToEditDto(final Match match) {
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
        int cupId = match.getCupId();
        int team1Id = match.getTeam1Id();
        int team2Id = match.getTeam2Id();

        Cup cup = cupService.loadAndAssertExists(cupId);
        Team team1 = teamService.loadAndAssertExists(team1Id);
        Team team2 = teamService.loadAndAssertExists(team2Id);

        if (!team1.getCategory().equals(cup.getCategory())) {
            throw new BadRequestException(String.format("Team %s has category %s, but cup has %s", team1.getTeamName(), team1.getCategory().getCategoryName(), cup.getCategory().getCategoryName()));
        }

        if (!team2.getCategory().equals(cup.getCategory())) {
            throw new BadRequestException(String.format("Team %s has category %s, but cup has %s", team2.getTeamName(), team2.getCategory().getCategoryName(), cup.getCategory().getCategoryName()));
        }

        assertTeamHasNoMatchesOnDate(team1, dateTimeService.getToday(), match.getMatchId());
        assertTeamHasNoMatchesOnDate(team2, dateTimeService.getToday(), match.getMatchId());
    }

    private void assertTeamHasNoMatchesOnDate(final Team team, final LocalDate date, final int matchId) {
        List<Match> matches = matchService.loadAll(team, date);
        if (matches.stream().filter(match -> match.getId() != matchId).findAny().isPresent()) {
            throw new UnprocessableEntityException(String.format("Team %s already has match on %s", team, date));
        }
    }
}

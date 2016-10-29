package betmen.web.controllers.rest.admin.imports;

import betmen.core.entity.Cup;
import betmen.core.entity.Match;
import betmen.core.entity.Team;
import betmen.core.entity.User;
import betmen.core.service.CupService;
import betmen.core.service.TeamService;
import betmen.core.service.UserService;
import betmen.core.service.matches.imports.RemoteGame;
import betmen.core.service.matches.imports.RemoteGameDataImportService;
import betmen.core.service.remote.RemoteContentNullException;
import betmen.core.service.utils.DateTimeService;
import betmen.web.converters.DTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/rest/remote-games-import")
public class RemoteGameImportRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private CupService cupService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private RemoteGameDataImportService remoteGameDataImportService;
    @Autowired
    private DateTimeService dateTimeService;
    @Autowired
    private DTOService dtoService;

    @RequestMapping(method = RequestMethod.GET, value = "/collect-game-data-ids/")
    public List<RemoteGameDTO> collectRemoteGamesIds(final GameImportParametersDTO parametersDTO, final Principal principal) throws IOException, RemoteContentNullException {
        final LocalDate dateFrom = dateTimeService.parseDate(parametersDTO.getDateFrom());
        final LocalDate dateTo = dateTimeService.parseDate(parametersDTO.getDateTo());
        final Cup cup = cupService.load(parametersDTO.getCupId());
        return remoteGameDataImportService.preloadRemoteGames(dateFrom, dateTo, cup)
                .stream()
                .map(remoteGame -> toRemoteGameDto(principal, cup, remoteGame))
                .collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.GET, value = "/remote-game/{remoteGameId}/")
    public RemoteGameDTO loadRemoteGameData(final @PathVariable(value = "remoteGameId") String remoteGameId, final @RequestParam(value = "cupId") Integer cupId, final Principal principal) throws IOException, RemoteContentNullException {
        final Cup cup = cupService.load(cupId);
        final RemoteGame remoteGame = new RemoteGame(remoteGameId);
        remoteGame.setLoaded(true);
        remoteGameDataImportService.loadRemoteGame(remoteGame, cup);
        return toRemoteGameDto(principal, cup, remoteGame);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/remote-game/{remoteGameId}/")
    public void saveRemoteGameData(final @RequestParam(value = "cupId") Integer cupId, final @RequestBody RemoteGameDTO remoteGameDTO) {
        remoteGameDataImportService.importGame(cupService.load(cupId), toRemoteGame(remoteGameDTO));
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public Error handleException(final IOException exception) {
        return new Error("Remote game import server error: " + exception.getMessage());
    }

    private RemoteGameDTO toRemoteGameDto(final Principal principal, final Cup cup, final RemoteGame remoteGame) {
        final RemoteGameDTO remoteGameDTO = new RemoteGameDTO(remoteGame.getRemoteGameId());
        if (!remoteGame.isLoaded()) {
            return remoteGameDTO;
        }
        final Team team1 = teamService.findByImportId(cup.getCategory().getId(), remoteGame.getRemoteTeam1Id());
        remoteGameDTO.setTeam1Id(remoteGame.getRemoteTeam1Id());
        remoteGameDTO.setTeam1Name(team1 != null ? team1.getTeamName() : remoteGame.getRemoteTeam1Name());
        final Team team2 = teamService.findByImportId(cup.getCategory().getId(), remoteGame.getRemoteTeam2Id());
        remoteGameDTO.setTeam2Id(remoteGame.getRemoteTeam2Id());
        remoteGameDTO.setTeam2Name(team2 != null ? team2.getTeamName() : remoteGame.getRemoteTeam2Name());
        remoteGameDTO.setBeginningTime(dateTimeService.formatDateTime(remoteGame.getBeginningTime()));
        remoteGameDTO.setScore1(remoteGame.getScore1());
        remoteGameDTO.setScore2(remoteGame.getScore2());
        remoteGameDTO.setHomeTeamNumber(remoteGame.getHomeTeamNumber());
        remoteGameDTO.setFinished(remoteGame.isFinished());
        remoteGameDTO.setLoaded(remoteGame.isLoaded());
        remoteGameDTO.setRemoteGameLocalData(getRemoteGameLocalData(cup, remoteGameDTO, principal));
        return remoteGameDTO;
    }

    private RemoteGameLocalData getRemoteGameLocalData(final Cup cup, final RemoteGameDTO remoteGameDTO, final Principal principal) {
        final User currentUser = userService.findByName(principal.getName());
        final Match match = remoteGameDataImportService.findByRemoteGameId(remoteGameDTO.getRemoteGameId());

        if (match == null) {
            final RemoteGameLocalData result = new RemoteGameLocalData();
            final Team team1 = teamService.findByImportId(cup.getCategory().getId(), remoteGameDTO.getTeam1Id());
            if (team1 != null) {
                result.setTeam1(dtoService.transformTeam(team1, currentUser));
            }
            final Team team2 = teamService.findByImportId(cup.getCategory().getId(), remoteGameDTO.getTeam2Id());
            if (team2 != null) {
                result.setTeam2(dtoService.transformTeam(team2, currentUser));
            }
            return result;
        }

        final RemoteGameLocalData result = new RemoteGameLocalData();
        result.setTeam1(dtoService.transformTeam(match.getTeam1(), currentUser));
        result.setTeam2(dtoService.transformTeam(match.getTeam2(), currentUser));
        result.setMatch(dtoService.transformMatch(match, currentUser));

        return result;
    }

    private RemoteGame toRemoteGame(final @RequestBody RemoteGameDTO remoteGameDTO) {
        final RemoteGame remoteGame = new RemoteGame(remoteGameDTO.getRemoteGameId());
        remoteGame.setRemoteTeam1Id(remoteGameDTO.getTeam1Id());
        remoteGame.setRemoteTeam1Name(remoteGameDTO.getTeam1Name());
        remoteGame.setRemoteTeam2Id(remoteGameDTO.getTeam2Id());
        remoteGame.setRemoteTeam2Name(remoteGameDTO.getTeam2Name());
        remoteGame.setBeginningTime(dateTimeService.parseDateTime(remoteGameDTO.getBeginningTime()));
        remoteGame.setScore1(remoteGameDTO.getScore1());
        remoteGame.setScore2(remoteGameDTO.getScore2());
        remoteGame.setHomeTeamNumber(remoteGameDTO.getHomeTeamNumber());
        remoteGame.setFinished(remoteGameDTO.isFinished());
        return remoteGame;
    }
}

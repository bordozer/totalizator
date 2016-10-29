package betmen.web.controllers.rest.admin.jobs.results;

import betmen.core.entity.Match;
import betmen.core.entity.User;
import betmen.core.service.UserService;
import betmen.core.service.jobs.JobLogService;
import betmen.core.service.jobs.results.GamesImportJobLogResultJSON;
import betmen.core.service.jobs.results.ImportedRemoteGame;
import betmen.core.service.matches.MatchService;
import betmen.dto.dto.MatchDTO;
import betmen.web.converters.DTOService;
import betmen.web.converters.JobDTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/rest/jobs/custom-results")
public class AdminJobTaskCustomResultsRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private MatchService matchService;
    @Autowired
    private JobLogService jobLogService;
    @Autowired
    private DTOService dtoService;
    @Autowired
    private JobDTOService jobDTOService;

    @RequestMapping(method = RequestMethod.GET, value = "/games-import/logs/{jobTaskLogId}/imported-games/")
    public List<RemoteGameMatchPair> matchesForArray(final @PathVariable("jobTaskLogId") int jobTaskLogId, final Principal principal) {
        final User currentUser = getCurrentUser(principal);
        final GamesImportJobLogResultJSON resultJSON = jobDTOService.transformFromJobGamesImportSpecificResultJSON(jobLogService.load(jobTaskLogId));
        return resultJSON.getImportedRemoteGames()
                .stream()
                .map(importedRemoteGame -> toMatchPair(currentUser, importedRemoteGame))
                .collect(Collectors.toList());
    }

    private RemoteGameMatchPair toMatchPair(final User currentUser, final ImportedRemoteGame importedRemoteGame) {
        final RemoteGameMatchPair matchPair = new RemoteGameMatchPair(importedRemoteGame.getRemoteGame());
        matchPair.setNewGameCreated(importedRemoteGame.isNewGameCreated());
        matchPair.setMatchId(importedRemoteGame.getMatchId());
        final Match match = matchService.load(importedRemoteGame.getMatchId());
        if (match == null) {
            return matchPair;
        }
        final MatchDTO matchDTO = dtoService.transformMatch(match, currentUser);
        matchPair.setMatch(matchDTO);
        return matchPair;
    }

    private User getCurrentUser(final Principal principal) {
        return userService.findByLogin(principal.getName());
    }
}

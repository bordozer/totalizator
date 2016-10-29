package betmen.web.controllers.rest;

import betmen.core.entity.User;
import betmen.core.model.MatchSearchModel;
import betmen.core.service.CupService;
import betmen.core.service.UserService;
import betmen.core.service.matches.MatchService;
import betmen.core.service.matches.MatchesAndBetsWidgetService;
import betmen.dto.dto.MatchDTO;
import betmen.web.converters.DTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/rest/matches")
public class MatchesRestController {

    @Autowired
    private MatchService matchService;
    @Autowired
    private MatchesAndBetsWidgetService matchesAndBetsWidgetService;
    @Autowired
    private CupService cupService;
    @Autowired
    private UserService userService;
    @Autowired
    private DTOService dtoService;

    @RequestMapping(method = RequestMethod.GET, value = "/{matchId}/")
    public MatchDTO match(final @PathVariable("matchId") int matchId, final Principal principal) {
        return dtoService.transformMatch(matchService.loadAndAssertExists(matchId), getCurrentUser(principal));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public List<MatchDTO> matches(final MatchSearchModel dto, final Principal principal) {
        return dtoService.transformMatches(matchesAndBetsWidgetService.loadAll(dto), getCurrentUser(principal));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/cup/{cupId}/")
    public List<MatchDTO> cupMatches(@PathVariable("cupId") final int cupId, final Principal principal) {
        return dtoService.transformMatches(matchService.loadAll(cupService.loadAndAssertExists(cupId)), getCurrentUser(principal));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/cup/{cupId}/teams/{team1Id}/vs/{team2Id}/")
    public List<MatchDTO> cupTeamsMatches(@PathVariable("cupId") final int cupId, @PathVariable("team1Id") final int team1Id,
                                          @PathVariable("team2Id") final int team2Id, final Principal principal) {
        return dtoService.transformMatches(matchService.loadAll(cupId, team1Id, team2Id), getCurrentUser(principal));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/cup/{cupId}/teams/{team1Id}/vs/{team2Id}/finished/")
    public List<MatchDTO> cupTeamsMatchesFinished(@PathVariable("cupId") final int cupId, @PathVariable("team1Id") final int team1Id,
                                                  @PathVariable("team2Id") final int team2Id, final Principal principal) {
        return dtoService.transformMatches(matchService.loadAllFinished(cupId, team1Id, team2Id), getCurrentUser(principal));
    }

    private User getCurrentUser(final Principal principal) {
        return userService.findByLogin(principal.getName());
    }
}

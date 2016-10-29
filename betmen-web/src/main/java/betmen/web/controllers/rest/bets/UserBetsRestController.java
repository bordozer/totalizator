package betmen.web.controllers.rest.bets;

import betmen.core.entity.Cup;
import betmen.core.entity.Match;
import betmen.core.entity.MatchBet;
import betmen.core.entity.User;
import betmen.core.service.CupService;
import betmen.core.service.UserService;
import betmen.core.service.matches.MatchBetsService;
import betmen.core.service.matches.MatchService;
import betmen.dto.dto.BetDTO;
import betmen.web.converters.DTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/rest/bets/users/{userId}")
public class UserBetsRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private CupService cupService;
    @Autowired
    private MatchService matchService;
    @Autowired
    private MatchBetsService matchBetsService;
    @Autowired
    private DTOService dtoService;

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public List<BetDTO> userBets(@PathVariable("userId") final int userId, final Principal principal) {
        final User user = userService.load(userId);
        final List<MatchBet> bets = matchBetsService.loadAll(user);
        final User currentUser = userService.findByLogin(principal.getName());
        return getMatchBetDTOs(user, currentUser, bets);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/matches/{matchId}/")
    public List<BetDTO> userMatchBets(@PathVariable("userId") final int userId, @PathVariable("matchId") final int matchId, final Principal principal) {
        final User betsOfUser = userService.load(userId);
        final User currentUser = userService.findByLogin(principal.getName());
        final Match match = matchService.load(matchId);
        final List<MatchBet> bets = matchBetsService.loadAll(match);
        return getMatchBetDTOs(betsOfUser, currentUser, bets);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/cups/{cupId}/")
    public List<BetDTO> userCupBets(@PathVariable("userId") final int userId, @PathVariable("cupId") final int cupId, final Principal principal) {
        final User user = userService.load(userId);
        final Cup cup = cupService.load(cupId);
        final List<MatchBet> bets = matchBetsService.loadAll(cup, user);
        final User currentUser = userService.findByLogin(principal.getName());
        return getMatchBetDTOs(user, currentUser, bets);
    }

    private List<BetDTO> getMatchBetDTOs(final User user, final User accessor, final List<MatchBet> bets) {
        return dtoService.transformMatchBets(bets, user, accessor);
    }
}

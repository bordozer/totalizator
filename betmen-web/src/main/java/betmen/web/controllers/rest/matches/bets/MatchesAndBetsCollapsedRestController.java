package betmen.web.controllers.rest.matches.bets;

import betmen.core.model.MatchSearchModel;
import betmen.core.entity.Cup;
import betmen.core.entity.CupTeamBet;
import betmen.core.entity.Match;
import betmen.core.entity.User;
import betmen.core.service.CupBetsService;
import betmen.core.service.CupService;
import betmen.core.service.UserService;
import betmen.core.service.matches.MatchBetsService;
import betmen.core.service.matches.MatchService;
import betmen.core.service.utils.DateTimeService;
import betmen.dto.dto.MatchSearchModelDto;
import betmen.dto.dto.MatchesAndBetsCollapsedDTO;
import betmen.web.converters.DTOService;
import betmen.dto.dto.TeamDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/matches/bets/collapsed")
public class MatchesAndBetsCollapsedRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private CupService cupService;

    @Autowired
    private MatchService matchService;

    @Autowired
    private DateTimeService dateTimeService;

    @Autowired
    private CupBetsService cupBetsService;

    @Autowired
    private MatchBetsService matchBetsService;

    @Autowired
    private DTOService dtoService;

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public MatchesAndBetsCollapsedDTO matchesAndBetsCollapsed(final MatchSearchModelDto dto, final Principal principal) {

        MatchSearchModel filter = dtoService.transformMatchSearchModel(dto);
        final int userId = filter.getUserId();
        final User showBetsOfUser = userId > 0 ? userService.load(userId) : userService.findByLogin(principal.getName());
        final Cup cup = cupService.load(filter.getCupId());

        final MatchesAndBetsCollapsedDTO result = new MatchesAndBetsCollapsedDTO(dtoService.transformCup(cup, showBetsOfUser), dtoService.transformUser(showBetsOfUser));
        result.setMatchesCount(matchService.getMatchCount(cup.getId()));                // totally in cup
        result.setFutureMatchesCount(matchService.getFutureMatchCount(cup));    // not finished matches at all
        result.setTodayMatchesCount(matchService.loadAllOnDate(cup.getId(), filter.getFilterByDate()).size());
        result.setNowPlayingMatchesCount(matchService.getMatchNotFinishedYetMatches(cup).size());

        final Match nearestFutureMatch = matchService.getNearestFutureMatch(cup, dateTimeService.getNow());
        result.setFirstMatchTime(nearestFutureMatch != null ? nearestFutureMatch.getBeginningTime() : null);

        result.setUserBetsCount(matchBetsService.betsCount(cup, showBetsOfUser));
        result.setMatchesWithoutBetsCount(matchBetsService.getMatchesCountAccessibleBorBetting(cup, showBetsOfUser));
        result.setFirstMatchNoBetTime(matchBetsService.getFirstMatchWithoutBetTime(cup, showBetsOfUser));

        final boolean isCupBettingAllowed = cupBetsService.validateBettingAllowed(cup).isPassed();
        final boolean cupHasWinners = cup.getWinnersCount() > 0;
        final boolean userMadeAllCupWinnersBets = cupBetsService.load(cup, showBetsOfUser).size() == cup.getWinnersCount();
        result.setCupWinnersBetsIsAccessible(isCupBettingAllowed && cupHasWinners && !userMadeAllCupWinnersBets);
        result.setCupHasWinners(cupHasWinners);
        result.setUserCupWinnersBets(cupBetsService.load(cup, showBetsOfUser)
                .stream()
                .map(cupTeamBet -> dtoService.transformTeam(cupTeamBet.getTeam(), showBetsOfUser))
                .collect(Collectors.toList()));
        return result;
    }
}

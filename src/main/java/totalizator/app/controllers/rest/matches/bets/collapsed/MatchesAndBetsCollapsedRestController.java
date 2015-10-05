package totalizator.app.controllers.rest.matches.bets.collapsed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import totalizator.app.dto.MatchesBetSettingsDTO;
import totalizator.app.dto.TeamDTO;
import totalizator.app.models.Cup;
import totalizator.app.models.CupTeamBet;
import totalizator.app.models.Match;
import totalizator.app.models.User;
import totalizator.app.services.CupBetsService;
import totalizator.app.services.CupService;
import totalizator.app.services.DTOService;
import totalizator.app.services.UserService;
import totalizator.app.services.matches.MatchBetsService;
import totalizator.app.services.matches.MatchService;
import totalizator.app.services.utils.DateTimeService;

import java.security.Principal;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping( "/rest/matches/bets/collapsed" )
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

	@RequestMapping( method = RequestMethod.GET, value = "/" )
	public MatchesAndBetsCollapsedDTO matchesAndBetsCollapsed( final MatchesBetSettingsDTO filter, final Principal principal ) {

		final int userId = filter.getUserId();

		final User showBetsOfUser = userId > 0 ? userService.load( userId ) : userService.findByLogin( principal.getName() );

		final Cup cup = cupService.load( filter.getCupId() );

		final MatchesAndBetsCollapsedDTO result = new MatchesAndBetsCollapsedDTO( dtoService.transformCup( cup, showBetsOfUser ), dtoService.transformUser( showBetsOfUser ) );

		result.setMatchesCount( matchService.getMatchCount( cup ) ); 				// totally in cup
		result.setFutureMatchesCount( matchService.getFutureMatchCount( cup ) );	// not finished matches at all

		result.setTodayMatchesCount( matchService.loadAllOnDate( cup, dateTimeService.parseDate( filter.getFilterByDate() ) ).size() );
		result.setNowPlayingMatchesCount( matchService.getMatchNotFinishedYetMatches( cup ).size() );

		final Match nearestFutureMatch = matchService.getNearestFutureMatch( cup, dateTimeService.getNow() );
		result.setFirstMatchTime( nearestFutureMatch != null ? nearestFutureMatch.getBeginningTime() : null );

		result.setUserBetsCount( matchBetsService.betsCount( cup, showBetsOfUser ) );
		result.setMatchesWithoutBetsCount( matchBetsService.getMatchesCountAccessibleBorBetting( cup, showBetsOfUser ) );
		result.setFirstMatchNoBetTime( matchBetsService.getFirstMatchWithoutBetTime( cup, showBetsOfUser ) );

		final boolean isCupBettingAllowed = cupBetsService.validateBettingAllowed( cup ).isPassed();
		final boolean cupHasWinners = cup.getWinnersCount() > 0;
		final boolean userMadeAllCupWinnersBets = cupBetsService.load( cup, showBetsOfUser ).size() == cup.getWinnersCount();
		result.setCupWinnersBetsIsAccessible( isCupBettingAllowed && cupHasWinners && ! userMadeAllCupWinnersBets );

		result.setCupHasWinners( cupHasWinners );

		result.setUserCupWinnersBets( cupBetsService.load( cup, showBetsOfUser )
				.stream()
				.map( new Function<CupTeamBet, TeamDTO>() {
					@Override
					public TeamDTO apply( final CupTeamBet cupTeamBet ) {
						return dtoService.transformTeam( cupTeamBet.getTeam(), showBetsOfUser );
					}
				} )
				.collect( Collectors.toList() ) );

		return result;
	}
}

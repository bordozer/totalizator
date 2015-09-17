package totalizator.app.controllers.rest.cups.winners.bets;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.beans.AppContext;
import totalizator.app.dto.CupTeamBetDTO;
import totalizator.app.dto.TeamDTO;
import totalizator.app.models.*;
import totalizator.app.services.*;
import totalizator.app.services.utils.DateTimeService;
import totalizator.app.translator.Language;
import totalizator.app.translator.TranslatorService;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/rest/cups/{cupId}" )
public class CupWinnersBetsRestController {

	@Autowired
	private UserService userService;

	@Autowired
	private CupService cupService;

	@Autowired
	private CupBetsService cupBetsService;

	@Autowired
	private DTOService dtoService;

	@Autowired
	private TranslatorService translatorService;

	@Autowired
	private DateTimeService dateTimeService;

	@Autowired
	private CupWinnerService cupWinnerService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/winners/", produces = APPLICATION_JSON_VALUE )
	public List<TeamDTO> winners( final @PathVariable( "cupId" ) int cupId, final Principal principal ) {

		final User currentUser = userService.findByLogin( principal.getName() );

		final Cup cup = cupService.load( cupId );

		return dtoService.transformTeams( Lists.transform( cupWinnerService.loadAll( cup ), new Function<CupWinner, Team>() {
			@Override
			public Team apply( final CupWinner cupWinner ) {
				return cupWinner.getTeam();
			}
		} ), currentUser );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/winners/bets/", produces = APPLICATION_JSON_VALUE )
	public CupWinnersBetsDTO all( final @PathVariable( "cupId" ) int cupId, final HttpServletRequest request, final Principal principal ) {

		final User currentUser = userService.findByLogin( principal.getName() );

		final Cup cup = cupService.load( cupId );
		final Language language = AppContext.read( request.getSession() ).getLanguage();

		final CupWinnersBetsDTO result = new CupWinnersBetsDTO();
		result.setWinnersCount( cup.getWinnersCount() );

		final List<CupWinner> cupWinners = cupWinnerService.loadAll( cup );
		result.setWinners( dtoService.transformTeams( Lists.transform( cupWinners, new Function<CupWinner, Team>() {
			@Override
			public Team apply( final CupWinner cupWinner ) {
				return cupWinner.getTeam();
			}
		} ), currentUser ) );

		final boolean isCupBetsAreHiddenYet = !cupBetsService.isCupBettingFinished( cup );

		final List<User> users = getUsers( cup );

		final List<UserCupBetsDTO> usersCupBets = newArrayList();

		for ( final User user : users ) {

			final List<CupTeamBet> cupTeamBets = cupBetsService.load( cup, user );

			final UserCupBetsDTO userCupBetsDTO = new UserCupBetsDTO();
			userCupBetsDTO.setUser( dtoService.transformUser( user ) );

			final List<CupTeamBetDTO> userCupBets = dtoService.transformCupTeamBets( cupTeamBets, user );

			if ( isCupBetsAreHiddenYet ) {
				replaceTeamsWithFakeData( cup, userCupBets, language );
			}
			userCupBetsDTO.setUserCupBets( userCupBets );

			usersCupBets.add( userCupBetsDTO );
		}

		result.setUsersCupBets( usersCupBets );

		return result;
	}

	private void replaceTeamsWithFakeData( final Cup cup, final List<CupTeamBetDTO> userCupBets, final Language language ) {
		for ( final CupTeamBetDTO userCupBet : userCupBets ) {
			final TeamDTO team = userCupBet.getTeam();

			final TeamDTO fakeTeam = new TeamDTO();
			fakeTeam.setCategory( team.getCategory() );
			fakeTeam.setTeamId( 0 );
			fakeTeam.setTeamLogo( "/resources/img/team-logo-not-found.png" );
			fakeTeam.setTeamName( translatorService.translate( "Team name is hidden till $1"
					, language
					, dateTimeService.formatDateTimeUI( cup.getCupStartTime() )
			) );

			userCupBet.setTeam( fakeTeam );
			int i = 0;
		}
	}

	private List<User> getUsers( final Cup cup ) {

		final Set<User> usersSet = newHashSet();

		final List<CupTeamBet> cupBets = cupBetsService.load( cup );

		for ( final CupTeamBet bet : cupBets ) {
			usersSet.add( bet.getUser() );
		}

		final List<User> users = newArrayList( usersSet );

		Collections.sort( users, new Comparator<User>() {
			@Override
			public int compare( final User o1, final User o2 ) {
				return o1.getUsername().compareToIgnoreCase( o2.getUsername() );
			}
		} );

		return users;
	}
}

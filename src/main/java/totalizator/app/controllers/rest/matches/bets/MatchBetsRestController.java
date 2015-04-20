package totalizator.app.controllers.rest.matches.bets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.dto.MatchBetDTO;
import totalizator.app.dto.TeamDTO;
import totalizator.app.models.Match;
import totalizator.app.models.MatchBet;
import totalizator.app.models.Team;
import totalizator.app.models.User;
import totalizator.app.services.DTOService;
import totalizator.app.services.MatchBetsService;
import totalizator.app.services.MatchService;
import totalizator.app.services.UserService;

import java.security.Principal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/rest/matches" )
public class MatchBetsRestController {

	@Autowired
	private MatchService matchService;

	@Autowired
	private MatchBetsService matchBetsService;

	@Autowired
	private DTOService dtoService;

	@Autowired
	private UserService userService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/{matchId}/bets/", produces = APPLICATION_JSON_VALUE )
	public MatchBetsDTO matchBets( final @PathVariable( "matchId" ) int matchId, final Principal principal ) {

		final User currentUser = userService.findByLogin( principal.getName() );

		final Match match = matchService.load( matchId );

		final Team team1 = match.getTeam1();
		final Team team2 = match.getTeam2();

		final TeamDTO team1DTO = dtoService.transformTeam( team1 );
		final TeamDTO team2DTO = dtoService.transformTeam( team2 );

		final MatchBetsDTO result = new MatchBetsDTO();

		result.setMatchId( matchId );

		result.setMatch( dtoService.transformMatch( match, currentUser ) );

		result.setTeam1( team1DTO );
		result.setTeam2( team2DTO );

		result.setMatchBets( getMatchBetDTOs( match, currentUser ) );

		return result;
	}

	private List<MatchBetDTO> getMatchBetDTOs( final Match match, final User currentUser ) {

		final boolean canSeeAnotherBets = matchBetsService.userCanSeeAnotherBets( match, currentUser );

		final List<MatchBetDTO> matchBetsDTOs = newArrayList();

		final List<MatchBet> matchBets = matchBetsService.loadAll( match );
		for ( final MatchBet matchBet : matchBets ) {
			final MatchBetDTO matchBetDTO = dtoService.getMatchBetForMatch( match, matchBet.getUser() );
			if ( ! canSeeAnotherBets ) {
				matchBetDTO.getBet().setScore1( 0 );
				matchBetDTO.getBet().setScore2( 0 );
			}
			matchBetsDTOs.add( matchBetDTO );
		}

		Collections.sort( matchBetsDTOs, new Comparator<MatchBetDTO>() {
			@Override
			public int compare( final MatchBetDTO o1, final MatchBetDTO o2 ) {

				if ( o2.getPoints() + o1.getPoints() > 0 ) {
					return ( ( Integer ) o2.getPoints() ).compareTo( o1.getPoints() );
				}

				return o1.getBet().getUser().getUserName().compareToIgnoreCase( o2.getBet().getUser().getUserName() );
			}
		} );

		return matchBetsDTOs;
	}
}

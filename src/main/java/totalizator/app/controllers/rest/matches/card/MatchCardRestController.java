package totalizator.app.controllers.rest.matches.card;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.dto.MatchBetDTO;
import totalizator.app.dto.TeamDTO;
import totalizator.app.dto.points.UserMatchPointsHolderDTO;
import totalizator.app.models.*;
import totalizator.app.services.DTOService;
import totalizator.app.services.UserGroupService;
import totalizator.app.services.UserService;
import totalizator.app.services.matches.MatchBetsService;
import totalizator.app.services.matches.MatchService;

import java.security.Principal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/rest/matches" )
public class MatchCardRestController {

	@Autowired
	private UserService userService;

	@Autowired
	private MatchService matchService;

	@Autowired
	private MatchBetsService matchBetsService;

	@Autowired
	private DTOService dtoService;

	@Autowired
	private UserGroupService userGroupService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/{matchId}/bets/", produces = APPLICATION_JSON_VALUE )
	public MatchBetsDTO matchBets( final @PathVariable( "matchId" ) int matchId, final @RequestParam( value = "userGroupId", required = false ) int userGroupId, final Principal principal ) {

		final User currentUser = userService.findByLogin( principal.getName() );

		final Match match = matchService.load( matchId );

		final Team team1 = match.getTeam1();
		final Team team2 = match.getTeam2();

		final TeamDTO team1DTO = dtoService.transformTeam( team1, currentUser );
		final TeamDTO team2DTO = dtoService.transformTeam( team2, currentUser );

		final MatchBetsDTO result = new MatchBetsDTO();

		result.setMatchId( matchId );

		result.setMatch( dtoService.transformMatch( match, currentUser ) );

		result.setTeam1( team1DTO );
		result.setTeam2( team2DTO );

		result.setMatchBets( getMatchBetDTOs( match, currentUser, userGroupId ) );

		return result;
	}

	private List<MatchBetDTO> getMatchBetDTOs( final Match match, final User currentUser, final int userGroupId ) {

		final UserGroup userGroup = userGroupId > 0 ? userGroupService.load( userGroupId ) : null;

		final List<MatchBet> matchBets = matchBetsService.loadAll( match, userGroup );

		final List<MatchBetDTO> matchBetsDTOs = newArrayList();

		for ( final MatchBet matchBet : matchBets ) {

			final MatchBetDTO matchBetDTO = userGroup != null ? dtoService.getMatchBetForMatch( match, matchBet.getUser(), currentUser, userGroup ) : dtoService.getMatchBetForMatch( match, matchBet.getUser(), currentUser );

			if ( matchBetDTO.getBet().isSecuredBet() ) {
				matchBetDTO.getBet().setScore1( 0 );
				matchBetDTO.getBet().setScore2( 0 );
			}
			matchBetsDTOs.add( matchBetDTO );
		}

		Collections.sort( matchBetsDTOs, new Comparator<MatchBetDTO>() {

			@Override
			public int compare( final MatchBetDTO o1, final MatchBetDTO o2 ) {

				final UserMatchPointsHolderDTO pointsHolder1 = o2.getUserMatchPointsHolder();
				final UserMatchPointsHolderDTO pointsHolder2 = o1.getUserMatchPointsHolder();

				if ( pointsHolder1.getSummary() > pointsHolder2.getSummary() || pointsHolder1.getSummary() < pointsHolder2.getSummary() ) {
					return ( ( Float ) pointsHolder1.getSummary() ).compareTo( pointsHolder2.getSummary() );
				}

				return o1.getBet().getUser().getUserName().compareToIgnoreCase( o2.getBet().getUser().getUserName() );
			}
		} );

		return matchBetsDTOs;
	}
}

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
import totalizator.app.services.MatchBetsService;
import totalizator.app.services.MatchService;
import totalizator.app.services.TeamService;
import totalizator.app.services.UserService;

import java.security.Principal;
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
	private UserService userService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/{matchId}/bets/", produces = APPLICATION_JSON_VALUE )
	public MatchBetsDTO matchBets( final @PathVariable( "matchId" ) int matchId, final Principal principal ) {

		final Match match = matchService.load( matchId );

		final Team team1 = match.getTeam1();
		final Team team2 = match.getTeam2();

		final TeamDTO team1DTO = TeamService.TEAM_TO_TEAM_DTO_FUNCTION.apply( team1 );
		final TeamDTO team2DTO = TeamService.TEAM_TO_TEAM_DTO_FUNCTION.apply( team2 );

		final List<MatchBet> matchBets = matchBetsService.loadAll( match );

		final MatchBetsDTO result = new MatchBetsDTO();

		result.setMatchId( matchId );

		result.setTeam1( team1DTO );
		result.setTeam2( team2DTO );

		return result;
	}
}

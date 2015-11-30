package totalizator.app.controllers.rest.teams.standoffs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.models.Cup;
import totalizator.app.models.Team;
import totalizator.app.models.User;
import totalizator.app.services.CupService;
import totalizator.app.services.DTOService;
import totalizator.app.services.TeamService;
import totalizator.app.services.UserService;
import totalizator.app.services.matches.MatchService;
import totalizator.app.services.teams.TeamsCupStandoff;
import totalizator.app.services.teams.TeamsStandoffService;

import java.security.Principal;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/rest/teams" )
public class TeamsStandoffsRestController {

	@Autowired
	private TeamService teamService;

	@Autowired
	private UserService userService;

	@Autowired
	private MatchService matchService;

	@Autowired
	private CupService cupService;

	@Autowired
	private TeamsStandoffService teamsStandoffService;

	@Autowired
	private DTOService dtoService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "{team1Id}/vs/{team2Id}/", produces = APPLICATION_JSON_VALUE )
	public TeamsStandoffsDTO all( final @PathVariable( "team1Id" ) int team1Id, final @PathVariable( "team2Id" ) int team2Id, final Principal principal ) {

		final User currentUser = userService.findByLogin( principal.getName() );

		final Team team1 = teamService.load( team1Id );
		final Team team2 = teamService.load( team2Id );

		final TeamsStandoffsDTO dto = new TeamsStandoffsDTO();
		dto.setTeam1( dtoService.transformTeam( team1, currentUser ) );
		dto.setTeam2( dtoService.transformTeam( team2, currentUser ) );

		final Cup cup = teamsStandoffService.getLastStandoffCup( team1, team2 );
		if ( cup == null ) {
			return dto;
		}

		dto.setCupToShow( dtoService.transformCup( cup, userService.findByLogin( principal.getName() ) ) );

		final List<TeamsCupStandoffDTO> standoffByCup = getTeamsCupStandoffDTOs( team1, team2, currentUser );
		dto.setStandoffsByCup( standoffByCup );

		return dto;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/team1/{team1Id}/team2/{team2Id}/statistics/cup/{cupId}/", produces = APPLICATION_JSON_VALUE )
	public TeamsCupStatistics matchAndBetTeamsStatistics( final @PathVariable( "team1Id" ) int team1Id, final @PathVariable( "team2Id" ) int team2Id,
														 final @PathVariable( "cupId" ) int cupId, final Principal principal ) {

		final Cup cup = cupService.load( cupId );
		final Team team1 = teamService.load( team1Id );
		final Team team2 = teamService.load( team2Id );

		final TeamsCupStatistics result = new TeamsCupStatistics();

		final int team1MonMatchCount = matchService.getWonMatchCount( cup, team1 );
		final int team1FinishedMatchCount = matchService.getFinishedMatchCount( cup, team1 );
		result.setTeam1won( team1MonMatchCount );
		result.setTeam1lost( team1FinishedMatchCount - team1MonMatchCount );
		result.setTeam1Total( team1FinishedMatchCount );

		final int team2MonMatchCount = matchService.getWonMatchCount( cup, team2 );
		final int team2FinishedMatchCount = matchService.getFinishedMatchCount( cup, team2 );
		result.setTeam2won( team2MonMatchCount );
		result.setTeam2lost( team2FinishedMatchCount - team2MonMatchCount );
		result.setTeam2Total( team2FinishedMatchCount );

		return result;
	}

	private List<TeamsCupStandoffDTO> getTeamsCupStandoffDTOs( final Team team1, final Team team2, final User currentUser ) {

		return teamsStandoffService.getTeamsStandoffByCups( team1, team2 )
				.stream()
				.map( new Function<TeamsCupStandoff, TeamsCupStandoffDTO>() {

					@Override
					public TeamsCupStandoffDTO apply( final TeamsCupStandoff o ) {

						final TeamsCupStandoffDTO dto = new TeamsCupStandoffDTO();

						dto.setCup( dtoService.transformCup( o.getCup(), currentUser ) );
						dto.setScore1( o.getScore1() );
						dto.setScore2( o.getScore2() );

						return dto;
					}
				} ).collect( Collectors.toList() );
	}

	private User getCurrentUser( final Principal principal ) {
		return userService.findByLogin( principal.getName() );
	}
}

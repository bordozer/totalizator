package totalizator.app.controllers.rest.teams.card;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.models.Cup;
import totalizator.app.models.CupWinner;
import totalizator.app.models.Team;
import totalizator.app.models.User;
import totalizator.app.services.*;
import totalizator.app.services.matches.MatchService;

import java.security.Principal;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/rest/teams/{teamId}" )
public class TeamCardRestController {

	@Autowired
	private UserService userService;

	@Autowired
	private TeamService teamService;

	@Autowired
	private CupService cupService;

	@Autowired
	private MatchService matchService;

	@Autowired
	private CupWinnerService cupWinnerService;

	@Autowired
	private DTOService dtoService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/cup/{cupId}/statistics/", produces = APPLICATION_JSON_VALUE )
	public TeamCardCupData cupTeamStatistics( final @PathVariable( "teamId" ) int teamId, final @PathVariable( "cupId" ) int cupId, final Principal principal ) {

		final User currentUser = userService.findByLogin( principal.getName() );
		final Team team = teamService.load( teamId );
		final Cup cup = cupService.load( cupId );

		return getTeamCardCupData( currentUser, team, cup );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/card/", produces = APPLICATION_JSON_VALUE )
	public TeamCardDTO cupTeamsStatistics( final @PathVariable( "teamId" ) int teamId, final Principal principal ) {

		final User currentUser = userService.findByLogin( principal.getName() );
		final Team team = teamService.load( teamId );

		final TeamCardDTO dto = new TeamCardDTO();
		dto.setTeam( dtoService.transformTeam( team ) );

		final List<TeamCardCupData> cupDataMap = newArrayList();
		for ( final Cup cup : cupService.loadPublic( team.getCategory() ) ) {
			cupDataMap.add( getTeamCardCupData( currentUser, team, cup ) );
		}

		final List<TeamCardCupData> notNullCupData = cupDataMap.stream().filter( new Predicate<TeamCardCupData>() {
			@Override
			public boolean test( TeamCardCupData teamCardCupData ) {
				return teamCardCupData.getFutureMatchesCount() + teamCardCupData.getFinishedMatchCount() > 0;
			}
		} ).collect( Collectors.toList() );

		dto.setCardCupData( notNullCupData );

		return dto;
	}

	private TeamCardCupData getTeamCardCupData( final User currentUser, final Team team, final Cup cup ) {
		final TeamCardCupData cupData = new TeamCardCupData( dtoService.transformCup( cup, currentUser ) );

		cupData.setFinishedMatchCount( matchService.getFinishedMatchCount( cup, team ) );
		cupData.setWonMatchCount( matchService.getWonMatchCount( cup, team ) );
		cupData.setFutureMatchesCount( matchService.getFutureMatchCount( cup, team ) );

		final CupWinner cupWinner = cupWinnerService.load( cup, team );
		if ( cupWinner != null ) {
			cupData.setCupWinner( dtoService.transformCupWinner( cupWinner, currentUser ) );
		}
		return cupData;
	}
}

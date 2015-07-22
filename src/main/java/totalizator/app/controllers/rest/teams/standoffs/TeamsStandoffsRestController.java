package totalizator.app.controllers.rest.teams.standoffs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.models.Cup;
import totalizator.app.models.Match;
import totalizator.app.models.Team;
import totalizator.app.services.DTOService;
import totalizator.app.services.MatchService;
import totalizator.app.services.TeamService;
import totalizator.app.services.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Set;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newLinkedHashSet;
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
	private DTOService dtoService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "{team1Id}/vs/{team2Id}/", produces = APPLICATION_JSON_VALUE )
	public TeamsStandoffsDTO all( final @PathVariable( "team1Id" ) int team1Id, final @PathVariable( "team2Id" ) int team2Id, final Principal principal ) {

		final Team team1 = teamService.load( team1Id );
		final Team team2 = teamService.load( team2Id );

		final Set<Cup> cups = newLinkedHashSet();
		final List<Match> matches = matchService.loadAll( team1, team2 );
		for ( final Match match : matches ) {
			cups.add( match.getCup() );
		}

		return new TeamsStandoffsDTO( dtoService.transformCups( newArrayList( cups ), userService.findByLogin( principal.getName() ) ) );
	}
}

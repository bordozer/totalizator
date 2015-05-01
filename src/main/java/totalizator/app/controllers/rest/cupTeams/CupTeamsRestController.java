package totalizator.app.controllers.rest.cupTeams;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.dto.TeamDTO;
import totalizator.app.models.Cup;
import totalizator.app.models.Team;
import totalizator.app.services.CupService;
import totalizator.app.services.CupTeamService;
import totalizator.app.services.DTOService;
import totalizator.app.services.TeamService;

import java.security.Principal;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/rest/cups/{cupId}/teams" )
public class CupTeamsRestController {

	@Autowired
	private CupService cupService;

	@Autowired
	private TeamService teamService;

	@Autowired
	private CupTeamService cupTeamService;

	@Autowired
	private DTOService dtoService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	public CupTeamsDTO all( final @PathVariable( "cupId" ) int cupId
			, @RequestParam(value = "letter", required = false) final String letter
			, @RequestParam(value = "active", required = false) final boolean active
			, final Principal principal )
	{
		final Cup cup = cupService.load( cupId );

		final List<Team> teams = teamService.loadAll( cup.getCategory() );

		final List<String> letters = Lists.transform( newArrayList( teams ), new Function<Team, String>() {
			@Override
			public String apply( final Team team ) {
				return team.getTeamName().substring( 0, 1 );
			}
		} );

		if ( ! active && StringUtils.isNotEmpty( letter ) ) {
			CollectionUtils.filter( teams, new Predicate<Team>() {
				@Override
				public boolean evaluate( final Team team ) {
					return team.getTeamName().substring( 0, 1 ).equalsIgnoreCase( letter );
				}
			} );
		}

		if ( active ) {
			final List<Team> activeTeams = cupTeamService.loadActiveForCup( cupId );
			return new CupTeamsDTO( dtoService.transformTeams( activeTeams ), newHashSet( letters ) );
		}

		return new CupTeamsDTO( dtoService.transformTeams( teams ), newHashSet( letters ) );
	}
}

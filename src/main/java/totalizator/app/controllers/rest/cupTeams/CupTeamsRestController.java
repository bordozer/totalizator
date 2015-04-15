package totalizator.app.controllers.rest.cupTeams;

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
import totalizator.app.services.DTOService;
import totalizator.app.services.TeamService;

import java.security.Principal;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/rest/cups/{cupId}/teams" )
public class CupTeamsRestController {

	@Autowired
	private CupService cupService;

	@Autowired
	private TeamService teamService;

	@Autowired
	private DTOService dtoService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	public List<TeamDTO> all( final @PathVariable( "cupId" ) int cupId
			, @RequestParam(value = "letter", required = false) final String letter
			, final Principal principal )
	{
		final Cup cup = cupService.load( cupId );

		final List<Team> teams = teamService.loadAll( cup.getCategory() );

		if ( StringUtils.isNotEmpty( letter ) ) {
			CollectionUtils.filter( teams, new Predicate<Team>() {
				@Override
				public boolean evaluate( final Team team ) {
					return team.getTeamName().substring( 0, 1 ).equalsIgnoreCase( letter );
				}
			} );
		}

		return dtoService.transformTeams( teams );
	}
}

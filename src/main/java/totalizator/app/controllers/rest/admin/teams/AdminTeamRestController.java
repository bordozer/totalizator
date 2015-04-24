package totalizator.app.controllers.rest.admin.teams;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.models.Team;
import totalizator.app.services.CategoryService;
import totalizator.app.services.CupTeamService;
import totalizator.app.services.LogoService;
import totalizator.app.services.TeamService;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/admin/rest/teams" )
public class AdminTeamRestController {

	@Autowired
	private TeamService teamService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private CupTeamService cupTeamService;

	@Autowired
	private LogoService logoService;

	private static final Logger LOGGER = Logger.getLogger( AdminTeamRestController.class );

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	public List<TeamEditDTO> entries() {

		return Lists.transform( teamService.loadAll(), new Function<Team, TeamEditDTO>() {

			@Override
			public TeamEditDTO apply( final Team team ) {
				return new TeamEditDTO( team.getId(), team.getTeamName(), team.getCategory().getId(), true, logoService.getLogoURL( team ) );
			}
		} );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.PUT, value = "/0", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	public TeamEditDTO create( final @RequestBody TeamEditDTO teamEditDTO ) {
		// TODO: check if name exists
		final Team team = teamService.save( new Team( teamEditDTO.getTeamName(), categoryService.load( teamEditDTO.getCategoryId() ) ) );

		teamEditDTO.setTeamId( team.getId() );

		return teamEditDTO;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.PUT, value = "/{teamId}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	public TeamEditDTO edit( final @PathVariable( "teamId" ) int teamId, final @RequestBody TeamEditDTO teamEditDTO ) {
		// TODO: check if name exists
		final Team team = teamService.load( teamEditDTO.getTeamId() );
		team.setTeamName( teamEditDTO.getTeamName() );
		team.setCategory( categoryService.load( teamEditDTO.getCategoryId() ) );

		teamService.save( team );

		return teamEditDTO;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.DELETE, value = "/{teamId}" )
	public void delete( final @PathVariable( "teamId" ) int teamId ) throws IOException {

		if ( teamId == 0 ) {
			return;
		}

		final Team team = teamService.load( teamId );

		teamService.delete( teamId );
		logoService.deleteLogo( team );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.POST, value = "/{teamId}/cups/{cupId}/active/{isActive}/", produces = APPLICATION_JSON_VALUE )
	public void teamActivity( final @PathVariable( "teamId" ) int teamId, final @PathVariable( "cupId" ) int cupId, final @PathVariable( "isActive" ) boolean isActive ) {
		cupTeamService.saveCupTeam( cupId, teamId, isActive );
	}
}

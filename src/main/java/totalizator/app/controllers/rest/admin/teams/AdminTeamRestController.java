package totalizator.app.controllers.rest.admin.teams;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.dto.TeamDTO;
import totalizator.app.models.Category;
import totalizator.app.models.Team;
import totalizator.app.services.CategoryService;
import totalizator.app.services.TeamLogoService;
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
	private TeamLogoService teamLogoService;

	private static final Logger LOGGER = Logger.getLogger( AdminTeamRestController.class );

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	public List<TeamDTO> entries() {
		return Lists.transform( teamService.loadAll(), TeamService.TEAM_TO_TEAM_DTO_FUNCTION );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.PUT, value = "/0", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	public TeamDTO create( final @RequestBody TeamDTO teamDTO ) {
		// TODO: check if name exists
		final Team team = teamService.save( new Team( teamDTO.getTeamName(), categoryService.load( teamDTO.getCategoryId() ) ) );

		teamDTO.setTeamId( team.getId() );
		return teamDTO;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.PUT, value = "/{teamId}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	public TeamDTO edit( final @PathVariable( "teamId" ) int teamId, final @RequestBody TeamDTO teamDTO ) {
		// TODO: check if name exists
		final Team team = teamService.load( teamDTO.getTeamId() );
		team.setTeamName( teamDTO.getTeamName() );
		team.setCategory( categoryService.load( teamDTO.getCategoryId() ) );

		teamService.save( team );

		return teamDTO;
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
		teamLogoService.deleteLogo( team );
	}
}

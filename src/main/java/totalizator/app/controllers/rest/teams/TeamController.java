package totalizator.app.controllers.rest.teams;

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
import totalizator.app.services.TeamService;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/admin/teams" )
public class TeamController {

	@Autowired
	private TeamService teamService;

	@Autowired
	private CategoryService categoryService;

	private static final Logger LOGGER = Logger.getLogger( TeamController.class );

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	public List<TeamDTO> entries() {

		return Lists.transform( teamService.loadAll(), new Function<Team, TeamDTO>() {
			@Override
			public TeamDTO apply( final Team team ) {
				final Category category = categoryService.load( team.getCategoryId() );
				return new TeamDTO( team.getId(), team.getTeamName(), category.getId() );
			}
		} );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.PUT, value = "/0", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	public TeamDTO create( final @RequestBody TeamDTO teamDTO ) {
		// TODO: check if name exists
		final Team team = teamService.save( new Team( teamDTO.getTeamName(), teamDTO.getCategoryId() ) );

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
		team.setCategoryId( teamDTO.getCategoryId() );

		teamService.save( team );

		return teamDTO;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.DELETE, value = "/{teamId}" )
	public void delete( final @PathVariable( "teamId" ) int teamId ) {

		if ( teamId == 0 ) {
			return;
		}

		teamService.delete( teamId );
	}
}

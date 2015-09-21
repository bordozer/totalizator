package totalizator.app.controllers.rest.admin.teams;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import totalizator.app.models.Category;
import totalizator.app.models.Cup;
import totalizator.app.models.Team;
import totalizator.app.services.*;
import totalizator.app.services.matches.MatchService;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/admin/rest/teams" )
public class AdminTeamEditRestController {

	@Autowired
	private TeamService teamService;

	@Autowired
	private CupService cupService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private CupTeamService cupTeamService;

	@Autowired
	private MatchService matchService;

	@Autowired
	private LogoService logoService;

	private static final Logger LOGGER = Logger.getLogger( AdminTeamEditRestController.class );

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/cups/{cupId}", produces = APPLICATION_JSON_VALUE )
	public List<TeamEditDTO> cupTeams( final @PathVariable( "cupId" ) int cupId ) {

		final Cup cup = cupService.load( cupId );

		if ( cup == null ) {
			return Collections.emptyList();
		}

		return Lists.transform( teamService.loadAll( cup.getCategory() ), new Function<Team, TeamEditDTO>() {

			@Override
			public TeamEditDTO apply( final Team team ) {
				final TeamEditDTO dto = new TeamEditDTO( team.getId(), team.getTeamName(), team.getCategory().getId(), cupTeamService.exists( cup, team ), logoService.getLogoURL( team ) );
				dto.setMatchCount( matchService.getMatchCount( team ) );
				dto.setTeamImportId( team.getImportId() );

				return dto;
			}
		} );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.PUT, value = "/cups/{cupId}/0", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	public TeamEditDTO create( final @RequestBody TeamEditDTO teamEditDTO, final @PathVariable( "cupId" ) int cupId ) {
		// TODO: check if name exists, is teamImportID unique
		final Team team = new Team( teamEditDTO.getTeamName(), categoryService.load( teamEditDTO.getCategoryId() ) );
		team.setImportId( teamEditDTO.getTeamImportId() );

		final Team saved = teamService.save( team );

		final int teamId = saved.getId();

		teamActivity( teamId, cupId, true );

		teamEditDTO.setTeamId( teamId );

		return teamEditDTO;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.PUT, value = "/cups/{cupId}/{teamId}", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE )
	public TeamEditDTO edit( final @PathVariable( "teamId" ) int teamId, final @RequestBody TeamEditDTO teamEditDTO ) {
		// TODO: check if name exists, is teamImportID unique

		final Category category = categoryService.load( teamEditDTO.getCategoryId() );

		final Team team = teamService.load( teamEditDTO.getTeamId() );

		team.setTeamName( teamEditDTO.getTeamName() );
		team.setCategory( category );
		team.setImportId( teamEditDTO.getTeamImportId() );

		teamService.save( team );

		return teamEditDTO;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.DELETE, value = "/cups/{cupId}/{teamId}" )
	public void delete( final @PathVariable( "teamId" ) int teamId ) throws IOException {

		if ( teamId == 0 ) {
			return;
		}

		final Team team = teamService.load( teamId );

		teamService.delete( teamId ); // TODO: exception if team assigned to some match
		logoService.deleteLogo( team );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.POST, value = "/{teamId}/cups/{cupId}/active/{isActive}/", produces = APPLICATION_JSON_VALUE )
	public void teamActivity( final @PathVariable( "teamId" ) int teamId, final @PathVariable( "cupId" ) int cupId, final @PathVariable( "isActive" ) boolean isActive ) {
		cupTeamService.saveCupTeam( cupId, teamId, isActive );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.POST, value = "/{teamId}/logo/" )
	public void uploadLogo( final @PathVariable( "teamId" ) int teamId, final MultipartHttpServletRequest request ) throws IOException {

		final Team team = teamService.load( teamId );

		final Iterator<String> itr = request.getFileNames();
		if ( ! itr.hasNext() ) {
			return;
		}

		final MultipartFile logoFile = request.getFile( itr.next() );

		team.setLogoFileName( String.format( "team_logo_%d", team.getId() ) );
		teamService.save( team );

		logoFile.transferTo( logoService.getLogoFile( team ) );
	}
}

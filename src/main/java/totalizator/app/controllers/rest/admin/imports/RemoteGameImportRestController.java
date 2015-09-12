package totalizator.app.controllers.rest.admin.imports;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.models.Cup;
import totalizator.app.models.Match;
import totalizator.app.models.Team;
import totalizator.app.models.User;
import totalizator.app.services.*;
import totalizator.app.services.matches.imports.RemoteGame;
import totalizator.app.services.matches.imports.RemoteGameDataImportService;
import totalizator.app.services.utils.DateTimeService;
import totalizator.app.translator.TranslatorService;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/admin/rest/remote-games-import" )
public class RemoteGameImportRestController {

	@Autowired
	private UserService userService;

	@Autowired
	private CupService cupService;

	@Autowired
	private TeamService teamService;

	@Autowired
	private RemoteGameDataImportService remoteGameDataImportService;

	@Autowired
	private TranslatorService translatorService;

	@Autowired
	private DateTimeService dateTimeService;

	@Autowired
	private DTOService dtoService;

	private final Logger LOGGER = Logger.getLogger( RemoteGameImportRestController.class );

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/collect-game-data-ids/", produces = APPLICATION_JSON_VALUE )
	public List<NotLoadedRemoteGameDTO> collectRemoteGamesIds( final GameImportParametersDTO parametersDTO ) throws IOException {

		final LocalDate dateFrom = dateTimeService.parseDate( parametersDTO.getDateFrom() );
		final LocalDate dateTo = dateTimeService.parseDate( parametersDTO.getDateTo() );

		final Cup cup = cupService.load( parametersDTO.getCupId() );

		return remoteGameDataImportService.loadRemoteGameIds( dateFrom, dateTo, cup ).stream().map( new Function<String, NotLoadedRemoteGameDTO>() {

			@Override
			public NotLoadedRemoteGameDTO apply( final String remoteGameId ) {
				return new NotLoadedRemoteGameDTO( remoteGameId );
			}
		} ).collect( Collectors.toList() );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/remote-game/{remoteGameId}/", produces = APPLICATION_JSON_VALUE )
	public RemoteGameDTO loadRemoteGameData( final @PathVariable( "remoteGameId" ) String remoteGameId, final @RequestParam( value = "cupId" ) Integer cupId ) throws IOException {
		return getRemoteGameMapper().apply( remoteGameDataImportService.loadRemoteGame( remoteGameId, cupService.load( cupId ) ) );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/remote-game/local-data/", produces = APPLICATION_JSON_VALUE )
	public RemoteGameLocalData loadLocalDataForRemoteGame( final @RequestParam( value = "cupId" ) Integer cupId , final RemoteGameDTO remoteGameDTO, final Principal principal ) throws IOException {

		final User currentUser = userService.findByName( principal.getName() );
		final Cup cup = cupService.load( cupId );

		final RemoteGameLocalData result = new RemoteGameLocalData();

		final Team team1 = teamService.findByImportId( cup.getCategory(), remoteGameDTO.getTeam1Id() );
		if ( team1 != null ) {
			result.setTeam1( dtoService.transformTeam( team1 ) );
		}

		final Team team2 = teamService.findByImportId( cup.getCategory(), remoteGameDTO.getTeam2Id() );
		if ( team2 != null ) {
			result.setTeam2( dtoService.transformTeam( team2 ) );
		}

		if ( team1 != null && team2 != null ) {
			final Match match = remoteGameDataImportService.findMatchFor( cup, team1.getTeamName(), team2.getTeamName(), dateTimeService.parseDateTime( remoteGameDTO.getBeginningTime() ) );
			if ( match != null ) {
				result.setMatch( dtoService.transformMatch( match, currentUser ) );
			}
		}

		return result;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.PUT, value = "/remote-game/{remoteGameId}/", produces = APPLICATION_JSON_VALUE )
	public void saveRemoteGameData( final @RequestParam( value = "cupId" ) Integer cupId, final @RequestBody RemoteGameDTO remoteGameDTO ) {
		remoteGameDataImportService.importGame( cupService.load( cupId ), getRemoteGameDTOMapper().apply( remoteGameDTO ) );
	}

	private Function<RemoteGame, RemoteGameDTO> getRemoteGameMapper() {

		return new Function<RemoteGame, RemoteGameDTO>() {

			@Override
			public RemoteGameDTO apply( final RemoteGame remoteGame ) {

				final RemoteGameDTO remoteGameDTO = new RemoteGameDTO( remoteGame.getRemoteGameId() );

				remoteGameDTO.setTeam1Id( remoteGame.getRemoteTeam1Id() );
				remoteGameDTO.setTeam1Name( remoteGame.getRemoteTeam1Name() );
				remoteGameDTO.setTeam2Id( remoteGame.getRemoteTeam2Id() );
				remoteGameDTO.setTeam2Name( remoteGame.getRemoteTeam2Name() );

				remoteGameDTO.setBeginningTime( dateTimeService.formatDateTime( remoteGame.getBeginningTime() ) );
				remoteGameDTO.setScore1( remoteGame.getScore1() );
				remoteGameDTO.setScore2( remoteGame.getScore2() );
				remoteGameDTO.setHomeTeamNumber( remoteGame.getHomeTeamNumber() );
				remoteGameDTO.setFinished( remoteGame.isFinished() );

				return remoteGameDTO;
			}
		};
	}

	private Function<RemoteGameDTO, RemoteGame> getRemoteGameDTOMapper() {

		return new Function<RemoteGameDTO, RemoteGame>() {

			@Override
			public RemoteGame apply( final RemoteGameDTO remoteGameDTO ) {

				final RemoteGame remoteGame = new RemoteGame( remoteGameDTO.getRemoteGameId() );

				remoteGame.setRemoteTeam1Id( remoteGameDTO.getTeam1Name() );
				remoteGame.setRemoteTeam2Id( remoteGameDTO.getTeam2Name() );

				remoteGame.setBeginningTime( dateTimeService.parseDateTime( remoteGameDTO.getBeginningTime() ) );

				remoteGame.setScore1( remoteGameDTO.getScore1() );
				remoteGame.setScore2( remoteGameDTO.getScore2() );

				remoteGame.setHomeTeamNumber( remoteGameDTO.getHomeTeamNumber() );
				remoteGame.setFinished( remoteGameDTO.isFinished() );

				return remoteGame;
			}
		};
	}
}

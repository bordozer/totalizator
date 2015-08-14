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
import totalizator.app.services.CupService;
import totalizator.app.services.DTOService;
import totalizator.app.services.TeamService;
import totalizator.app.services.UserService;
import totalizator.app.services.matches.MatchService;
import totalizator.app.services.matches.imports.RemoteGame;
import totalizator.app.services.matches.imports.RemoteGameDataImportService;
import totalizator.app.services.utils.DateTimeService;
import totalizator.app.translator.Language;
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
	private MatchService matchService;

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

		return remoteGameDataImportService.loadRemoteGameIds( dateFrom, dateTo ).stream().map( new Function<String, NotLoadedRemoteGameDTO>() {

			@Override
			public NotLoadedRemoteGameDTO apply( final String remoteGameId ) {
				return new NotLoadedRemoteGameDTO( remoteGameId );
			}
		} ).collect( Collectors.toList() );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/remote-game/{remoteGameId}/", produces = APPLICATION_JSON_VALUE )
	public RemoteGameDTO loadRemoteGameData( final @PathVariable( "remoteGameId" ) String remoteGameId ) throws IOException {
		return getRemoteGameMapper().apply( remoteGameDataImportService.loadRemoteGame( remoteGameId ) );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/remote-game/local-data/", produces = APPLICATION_JSON_VALUE )
	public RemoteGameLocalData loadLocalDataForRemoteGame( final @RequestParam( value = "cupId" ) Integer cupId , final RemoteGameDTO remoteGameDTO, final Principal principal ) throws IOException {

		final User currentUser = userService.findByName( principal.getName() );
		final Cup cup = cupService.load( cupId );

		final RemoteGameLocalData result = new RemoteGameLocalData();

		final Team team1 = teamService.findByName( cup.getCategory(), remoteGameDTO.getTeam1Name() );
		if ( team1 != null ) {
			result.setTeam1( dtoService.transformTeam( team1 ) );
		}

		final Team team2 = teamService.findByName( cup.getCategory(), remoteGameDTO.getTeam2Name() );
		if ( team2 != null ) {
			result.setTeam2( dtoService.transformTeam( team2 ) );
		}

		final Match match = remoteGameDataImportService.findMatchFor( cup, remoteGameDTO.getTeam1Name(), remoteGameDTO.getTeam2Name(), dateTimeService.parseDateTime( remoteGameDTO.getBeginningTime() ) );
		if ( match != null ) {
			result.setMatch( dtoService.transformMatch( match, currentUser ) );
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

				remoteGameDTO.setTeam1Name( remoteGame.getTeam1Name() );
				remoteGameDTO.setTeam2Name( remoteGame.getTeam2Name() );

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

				remoteGame.setTeam1Name( remoteGameDTO.getTeam1Name() );
				remoteGame.setTeam2Name( remoteGameDTO.getTeam2Name() );

				remoteGame.setBeginningTime( dateTimeService.parseDateTime( remoteGameDTO.getBeginningTime() ) );

				remoteGame.setScore1( remoteGameDTO.getScore1() );
				remoteGame.setScore2( remoteGameDTO.getScore2() );

				remoteGame.setHomeTeamNumber( remoteGameDTO.getHomeTeamNumber() );
				remoteGame.setFinished( remoteGameDTO.isFinished() );

				return remoteGame;
			}
		};
	}












	/*@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/start/", produces = APPLICATION_JSON_VALUE )
	public ImportStatusDTO startImport( final @RequestParam( "cupId" ) int cupId ) {

		final Cup cup = cupService.load( cupId );
		if ( cup == null ) {
			throw new IllegalArgumentException( String.format( "Cup #%d not found", cupId ) );
		}

		if ( nbaGameDataImportService.isActive() ) {
			final ImportStatusDTO result = new ImportStatusDTO();
			result.setCupId( nbaGameDataImportService.getActiveImportCupId() );
			result.setImportActive( false );
			result.setImportStatusMessage( translatorService.translate( "Import is already going", getLanguage() ) );

			return result;
		}

		nbaGameDataImportService.start( cupId );

		new NBAGameImportTask( cup, nbaGameDataImportService ).start();

		final ImportStatusDTO result = new ImportStatusDTO();
		result.setCupId( cupId );
		result.setImportActive( true );
		result.setImportStatusMessage( translatorService.translate( "Import started", getLanguage() ) );

		return result;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/stop/", produces = APPLICATION_JSON_VALUE )
	public ImportStatusDTO stopImport() {

		final ImportStatusDTO result = new ImportStatusDTO();

		if ( !nbaGameDataImportService.isActive() ) {
			result.setCupId( nbaGameDataImportService.getActiveImportCupId() );
			result.setImportActive( false );
			result.setImportStatusMessage( translatorService.translate( "Import is not active", getLanguage() ) );

			return result;
		}

		nbaGameDataImportService.stop();

		result.setCupId( nbaGameDataImportService.getActiveImportCupId() );
		result.setImportActive( nbaGameDataImportService.isActive() );
		result.setImportStatusMessage( translatorService.translate( "Import has been stopped", getLanguage() ) );

		return result;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/state/", produces = APPLICATION_JSON_VALUE )
	public ImportStatusDTO importState() {

		final ImportStatusDTO result = new ImportStatusDTO();

		final boolean isActive = nbaGameDataImportService.isActive();

		result.setCupId( nbaGameDataImportService.getActiveImportCupId() );
		result.setImportActive( isActive );

		final GamesDataImportMonitor monitor = nbaGameDataImportService.getMonitor();
		final String error = StringUtils.isNotEmpty( monitor.getImportErrorMessage() ) ? String.format( " ( %s )", monitor.getImportErrorMessage() ) : "";

		final String stateMessage = String.format( "%s ( %s )%s"
				, translatorService.translate( monitor.getCurrentStatusMessage(), getLanguage() )
				, translatorService.translate( "$1 games are imported", getLanguage(), String.valueOf( monitor.getProcessedGamesCount() ) )
				, error
		);
		result.setImportStatusMessage( stateMessage );

		return result;
	}*/

	private Language getLanguage() {
		return translatorService.getDefaultLanguage();
	}
}

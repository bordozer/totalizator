package totalizator.app.controllers.rest.admin.imports.nba;

import org.apache.log4j.Logger;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.controllers.rest.admin.imports.GamesDataImportMonitor;
import totalizator.app.controllers.rest.admin.imports.ImportStatusDTO;
import totalizator.app.models.Cup;
import totalizator.app.services.CupService;
import totalizator.app.translator.Language;
import totalizator.app.translator.TranslatorService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping("/admin/rest/games-data-import/nba")
public class NBAImportRestController {

	private static final int INITIAL_GAME_ID = 21400001; // 21400001 - 21401230

	@Autowired
	private CupService cupService;

	@Autowired
	private NBAGameDataImportService nbaGameDataImportService;

	@Autowired
	private TranslatorService translatorService;

	private final Logger LOGGER = Logger.getLogger( NBAImportRestController.class );

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/start/", produces = APPLICATION_JSON_VALUE)
	public ImportStatusDTO startImport( final @RequestParam( "cupId" ) int cupId ) {

		final Cup cup = cupService.load( cupId );

		final ImportStatusDTO result = new ImportStatusDTO();

		if ( nbaGameDataImportService.isActive() ) {
			result.setImportActive( false );
			result.setImportStatusMessage( translatorService.translate( "Import is already going", getLanguage() ) );

			return result;
		}

		nbaGameDataImportService.start();

		final int initialGameId = INITIAL_GAME_ID; // TODO: save last imported!
		new NBAGameImportRunner( initialGameId, cup, nbaGameDataImportService ).start();

		result.setImportActive( true );
		result.setImportStatusMessage( translatorService.translate( "Import started", getLanguage() ) );

		return result;
	}

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/stop/", produces = APPLICATION_JSON_VALUE)
	public ImportStatusDTO stopImport() {

		final ImportStatusDTO result = new ImportStatusDTO();

		if ( ! nbaGameDataImportService.isActive() ) {
			result.setImportActive( false );
			result.setImportStatusMessage( translatorService.translate( "Import is not active", getLanguage() ) );

			return result;
		}

		nbaGameDataImportService.stop();

		result.setImportActive( true );
		result.setImportStatusMessage( translatorService.translate( "Import stopped", getLanguage() ) );

		return result;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/state/", produces = APPLICATION_JSON_VALUE )
	public ImportStatusDTO importState() {

		final ImportStatusDTO result = new ImportStatusDTO();

		final boolean isActive = nbaGameDataImportService.isActive();

		result.setImportActive( isActive );

		final GamesDataImportMonitor monitor = nbaGameDataImportService.getMonitor();
		final String error = StringUtils.isNotEmpty( monitor.getImportErrorMessage() ) ? String.format( " ( %s )", monitor.getImportErrorMessage() ) : "";
		final String stateMessage = String.format( "%s%s", translatorService.translate( monitor.getCurrentStatusMessage(), getLanguage() ), error );
		result.setImportStatusMessage( stateMessage );

		return result;
	}

	private Language getLanguage() {
		return Language.RU; // TODO: language!
	}
}

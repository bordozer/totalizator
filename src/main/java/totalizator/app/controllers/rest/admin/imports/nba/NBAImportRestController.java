package totalizator.app.controllers.rest.admin.imports.nba;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import totalizator.app.controllers.rest.admin.imports.GamesDataImportMonitor;
import totalizator.app.translator.Language;
import totalizator.app.translator.TranslatorService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping("/admin/rest/games-data-import/nba")
public class NBAImportRestController {

	private static final int INITIAL_GAME_ID = 21401230; // TODO: add 00 at start!

	@Autowired
	private NBAImportService nbaImportService;

	@Autowired
	private TranslatorService translatorService;

	private final Logger LOGGER = Logger.getLogger( NBAImportRestController.class );

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/start/", produces = APPLICATION_JSON_VALUE)
	public ImportDTO startImport() {

		final ImportDTO result = new ImportDTO();

		if ( nbaImportService.isImportingNow() ) {
			result.setRequestSuccessful( false );
			result.setImportMessage( translatorService.translate( "Import is already going", getLanguage() ) );

			return result;
		}

		final int gameId = INITIAL_GAME_ID;

		nbaImportService.startImport();

		new NBAGameImporter( gameId, nbaImportService ).start();

		result.setRequestSuccessful( true );
		result.setImportMessage( translatorService.translate( "Import started", getLanguage() ) );

		return result;
	}

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/stop/", produces = APPLICATION_JSON_VALUE)
	public ImportDTO stopImport() {

		final ImportDTO result = new ImportDTO();

		if ( ! nbaImportService.isImportingNow() ) {
			result.setRequestSuccessful( false );
			result.setRequestSuccessful( false );
			result.setImportMessage( translatorService.translate( "Import is not active", getLanguage() ) );

			return result;
		}

		nbaImportService.stopImport();

		result.setRequestSuccessful( true );
		result.setImportMessage( translatorService.translate( "Import stopped", getLanguage() ) );

		return result;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/state/", produces = APPLICATION_JSON_VALUE )
	public ImportDTO isImportingNow() {

		final ImportDTO result = new ImportDTO();

		final boolean isImportingNow = nbaImportService.isImportingNow();

		result.setRequestSuccessful( isImportingNow );

		if ( isImportingNow ) {
			result.setImportMessage( translatorService.translate( "Import is going", getLanguage() ) );
		} else {
			result.setImportMessage( translatorService.translate( "Import is stopped", getLanguage() ) );
		}

		return result;
	}

	private Language getLanguage() {
		return Language.RU; // TODO: language!
	}
}

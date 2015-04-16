package totalizator.app.controllers.rest.admin.imports.nba;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.controllers.rest.admin.imports.ImportDTO;
import totalizator.app.models.Cup;
import totalizator.app.services.CupService;
import totalizator.app.translator.Language;
import totalizator.app.translator.TranslatorService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping("/admin/rest/games-data-import/nba")
public class NBAImportRestController {

	private static final int INITIAL_GAME_ID = 21401225;

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
	public ImportDTO startImport( final @RequestParam( "cupId" ) int cupId ) {

		final Cup cup = cupService.load( cupId );

		final ImportDTO result = new ImportDTO();

		if ( nbaGameDataImportService.isImportingNow() ) {
			result.setRequestSuccessful( false );
			result.setImportMessage( translatorService.translate( "Import is already going", getLanguage() ) );

			return result;
		}

		nbaGameDataImportService.startImport();

		final int initialGameId = INITIAL_GAME_ID; // TODO: save last imported!
		new NBAGameImportRunner( initialGameId, cup, nbaGameDataImportService ).start();

		result.setRequestSuccessful( true );
		result.setImportMessage( translatorService.translate( "Import started", getLanguage() ) );

		return result;
	}

	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	@RequestMapping(method = RequestMethod.GET, value = "/stop/", produces = APPLICATION_JSON_VALUE)
	public ImportDTO stopImport() {

		final ImportDTO result = new ImportDTO();

		if ( ! nbaGameDataImportService.isImportingNow() ) {
			result.setRequestSuccessful( false );
			result.setRequestSuccessful( false );
			result.setImportMessage( translatorService.translate( "Import is not active", getLanguage() ) );

			return result;
		}

		nbaGameDataImportService.stopImport();

		result.setRequestSuccessful( true );
		result.setImportMessage( translatorService.translate( "Import stopped", getLanguage() ) );

		return result;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/state/", produces = APPLICATION_JSON_VALUE )
	public ImportDTO isImportingNow() {

		final ImportDTO result = new ImportDTO();

		final boolean isImportingNow = nbaGameDataImportService.isImportingNow();

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

package totalizator.app.controllers.rest.admin.imports.nba;

import org.apache.log4j.Logger;
import totalizator.app.controllers.rest.admin.imports.RemoteGameDataImportService;
import totalizator.app.models.Cup;

import java.io.IOException;

public class NBAGameImportRunner extends Thread {

	private final int initialGameId;
	private Cup cup;

	private final RemoteGameDataImportService remoteGameDataImportService;

	private final Logger LOGGER = Logger.getLogger( NBAGameImportRunner.class );

	public NBAGameImportRunner( final int initialGameId, final Cup cup, final RemoteGameDataImportService remoteGameDataImportService ) {
		this.initialGameId = initialGameId;
		this.cup = cup;
		this.remoteGameDataImportService = remoteGameDataImportService;
	}

	@Override
	public void run() {

		int gameId = initialGameId;

		while ( true ) {

			final boolean result = makeImport( gameId );
			if ( ! result ) {
				remoteGameDataImportService.stopImport();
				break;
			}

			if ( ! remoteGameDataImportService.isImportingNow() ) {
				break;
			}

			gameId++;
		}
	}

	private boolean makeImport( final int gameId ) {

		final String nbaGameId = String.format( "%010d", gameId );

		try {
			final boolean isGameImported = remoteGameDataImportService.importGame( cup, nbaGameId );

			LOGGER.error( String.format( "Remote game import finished. GameID: %s", nbaGameId ) );

			return isGameImported;
		} catch ( IOException e ) {
			LOGGER.error( String.format( "Error during import starting. GameID: %s", nbaGameId ), e );
		}

		return false;
	}
}

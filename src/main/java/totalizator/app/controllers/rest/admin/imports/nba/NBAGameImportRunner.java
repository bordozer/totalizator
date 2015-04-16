package totalizator.app.controllers.rest.admin.imports.nba;

import org.apache.log4j.Logger;
import totalizator.app.controllers.rest.admin.imports.RemoteGameDataImportService;

import java.io.IOException;

public class NBAGameImportRunner extends Thread {

	private final int initialGameId;
	private final RemoteGameDataImportService remoteGameDataImportService;

	private final Logger LOGGER = Logger.getLogger( NBAGameImportRunner.class );

	public NBAGameImportRunner( final int initialGameId, final RemoteGameDataImportService remoteGameDataImportService ) {
		this.initialGameId = initialGameId;
		this.remoteGameDataImportService = remoteGameDataImportService;
	}

	@Override
	public void run() {

		final String gameId = String.format( "%010d", initialGameId );

		try {
			final boolean isGameImported = remoteGameDataImportService.importGame( gameId );
			LOGGER.error( String.format( "Remote game import finished. GameID: %s", gameId ) );
		} catch ( IOException e ) {
			LOGGER.error( String.format( "Error during import starting. GameID: %s", gameId ), e );
		}
	}
}

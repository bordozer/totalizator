package totalizator.app.controllers.rest.admin.imports.nba;

import org.apache.log4j.Logger;

import java.io.IOException;

public class NBAGameImporter extends Thread {

	private final int gameId;
	private final NBAImportService nbaImportService;

	private final Logger LOGGER = Logger.getLogger( NBAGameImporter.class );

	public NBAGameImporter( final int gameId, final NBAImportService nbaImportService ) {
		this.gameId = gameId;
		this.nbaImportService = nbaImportService;
	}

	@Override
	public void run() {
		try {
			final boolean isGameImported = nbaImportService.importGame( gameId );
			LOGGER.error( String.format( "Remote game import finished. GameID: %02d", gameId ) );
		} catch ( IOException e ) {
			LOGGER.error( String.format( "Error during import starting. GameID: %02d", gameId ), e );
		}
	}
}

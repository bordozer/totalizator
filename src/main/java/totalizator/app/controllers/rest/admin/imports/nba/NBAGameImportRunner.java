package totalizator.app.controllers.rest.admin.imports.nba;

import org.apache.log4j.Logger;
import totalizator.app.controllers.rest.admin.imports.RemoteGameDataImportService;
import totalizator.app.models.Cup;

import java.time.LocalDateTime;

public class NBAGameImportRunner extends Thread {

	private Cup cup;

	private final RemoteGameDataImportService remoteGameDataImportService;

	private final Logger LOGGER = Logger.getLogger( NBAGameImportRunner.class );

	public NBAGameImportRunner( final Cup cup, final RemoteGameDataImportService remoteGameDataImportService ) {
		this.cup = cup;
		this.remoteGameDataImportService = remoteGameDataImportService;
	}

	@Override
	public void run() {

		final LocalDateTime cupStartTime = this.cup.getCupStartTime();
		final int year = cupStartTime.getYear();

		int gameId = calculateCupInitialGameId( year );

		while ( true ) {

			final boolean result = makeImport( gameId );
			if ( !result ) {
				remoteGameDataImportService.finish();
				break;
			}

			if ( !remoteGameDataImportService.isActive() ) {
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
		} catch ( final Throwable e ) {

			remoteGameDataImportService.error( e.getMessage() );

			LOGGER.error( String.format( "Error during import starting. GameID: %s", nbaGameId ), e );
		}

		return false;
	}

	private int calculateCupInitialGameId( final int year ) {
		// NBA's games ids are in diapason 21400001 - 21401230
		// All star game id 0031400001
		// where 14 is a first cup's year ( 2014/2015 )
		return 20000001 + ( year - 2000 ) * 100000;
	}
}

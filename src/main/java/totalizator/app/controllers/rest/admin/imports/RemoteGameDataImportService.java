package totalizator.app.controllers.rest.admin.imports;

import totalizator.app.models.Cup;

import java.io.IOException;

public interface RemoteGameDataImportService {

	void start( final int cupId );

	void stop();

	void finish();

	void error( final String message );

	boolean isActive();

	boolean importGame( final Cup cup, final String gameId ) throws IOException;

	int getActiveImportCupId();
}

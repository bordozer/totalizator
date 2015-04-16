package totalizator.app.controllers.rest.admin.imports;

import totalizator.app.models.Cup;

import java.io.IOException;

public interface RemoteGameDataImportService {

	void startImport();

	void stopImport();

	boolean isImportingNow();

	boolean importGame( final Cup cup, final String gameId ) throws IOException;
}

package totalizator.app.controllers.rest.admin.imports;

import java.io.IOException;

public interface RemoteGameDataImportService {

	void startImport();

	void stopImport();

	boolean isImportingNow();

	boolean importGame( final String gameId ) throws IOException;
}

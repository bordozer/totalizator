package totalizator.app.controllers.rest.admin.imports.nba;

import java.io.IOException;

public interface NBAImportService {

	void startImport();

	void stopImport();

	boolean isImportingNow();

	boolean importGame( final int gameId ) throws IOException;
}

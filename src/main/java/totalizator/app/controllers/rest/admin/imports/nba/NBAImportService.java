package totalizator.app.controllers.rest.admin.imports.nba;

public interface NBAImportService {

	void startImport();

	void stopImport();

	boolean isImportingNow();
}

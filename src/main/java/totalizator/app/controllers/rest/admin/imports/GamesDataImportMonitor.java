package totalizator.app.controllers.rest.admin.imports;

public class GamesDataImportMonitor {

	private boolean importActive;

	public boolean isImportActive() {
		return importActive;
	}

	public void setImportActive( final boolean importActive ) {
		this.importActive = importActive;
	}

	public void activate() {
		importActive = true;
	}

	public void deactivate() {
		importActive = false;
	}
}

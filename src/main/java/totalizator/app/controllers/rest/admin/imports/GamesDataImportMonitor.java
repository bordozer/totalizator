package totalizator.app.controllers.rest.admin.imports;

public class GamesDataImportMonitor {

	private ImportState importState;
	private String currentStatusMessage;
	private String customMessage;

	public ImportState getImportState() {
		return importState;
	}

	public void setImportState( final ImportState importState ) {
		this.importState = importState;
	}

	public String getCurrentStatusMessage() {
		return currentStatusMessage;
	}

	public void setCurrentStatusMessage( final String currentStatusMessage ) {
		this.currentStatusMessage = currentStatusMessage;
	}

	public boolean isActive() {
		return importState.isActive();
	}

	public void start() {
		importState = ImportState.IN_PROGRESS;
	}

	public void stop() {
		importState = ImportState.STOPPED;
	}

	public void finish() {
		importState = ImportState.FINISHED;
	}

	public void error( final String message ) {
		customMessage = message;
		importState = ImportState.ERROR;
	}

	public String getCustomMessage() {
		return customMessage;
	}

	public void setCustomMessage( final String customMessage ) {
		this.customMessage = customMessage;
	}
}

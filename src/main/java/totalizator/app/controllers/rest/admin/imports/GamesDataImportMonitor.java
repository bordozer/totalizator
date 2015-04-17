package totalizator.app.controllers.rest.admin.imports;

public class GamesDataImportMonitor {

	private int cupId;

	private ImportState importState = ImportState.NOT_STARTED;
	private String importErrorMessage;

	public int getCupId() {
		return cupId;
	}

	public void setCupId( final int cupId ) {
		this.cupId = cupId;
	}

	public ImportState getImportState() {
		return importState;
	}

	public void setImportState( final ImportState importState ) {
		this.importState = importState;
	}

	public String getCurrentStatusMessage() {
		return importState.getDescription();
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

	public void error( final String error ) {
		importErrorMessage = error;
		importState = ImportState.ERROR;
	}

	public String getImportErrorMessage() {
		return importErrorMessage;
	}

	public void setImportErrorMessage( final String importErrorMessage ) {
		this.importErrorMessage = importErrorMessage;
	}
}

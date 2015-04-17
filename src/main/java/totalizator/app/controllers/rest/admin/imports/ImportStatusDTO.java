package totalizator.app.controllers.rest.admin.imports;

public class ImportStatusDTO {

	private boolean importActive;
	private String importStatusMessage;

	public boolean isImportActive() {
		return importActive;
	}

	public void setImportActive( final boolean importActive ) {
		this.importActive = importActive;
	}

	public String getImportStatusMessage() {
		return importStatusMessage;
	}

	public void setImportStatusMessage( final String importStatusMessage ) {
		this.importStatusMessage = importStatusMessage;
	}
}


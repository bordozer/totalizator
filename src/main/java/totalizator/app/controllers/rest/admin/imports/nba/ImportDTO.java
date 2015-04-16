package totalizator.app.controllers.rest.admin.imports.nba;

public class ImportDTO {

	private boolean importSuccessful;
	private String importMessage;

	public boolean isImportSuccessful() {
		return importSuccessful;
	}

	public void setImportSuccessful( final boolean importSuccessful ) {
		this.importSuccessful = importSuccessful;
	}

	public String getImportMessage() {
		return importMessage;
	}

	public void setImportMessage( final String importMessage ) {
		this.importMessage = importMessage;
	}
}


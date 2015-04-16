package totalizator.app.controllers.rest.admin.imports;

public class ImportDTO {

	private boolean requestSuccessful;
	private String importMessage;

	public boolean isRequestSuccessful() {
		return requestSuccessful;
	}

	public void setRequestSuccessful( final boolean requestSuccessful ) {
		this.requestSuccessful = requestSuccessful;
	}

	public String getImportMessage() {
		return importMessage;
	}

	public void setImportMessage( final String importMessage ) {
		this.importMessage = importMessage;
	}
}


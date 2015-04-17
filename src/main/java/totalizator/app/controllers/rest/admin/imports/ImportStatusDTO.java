package totalizator.app.controllers.rest.admin.imports;

public class ImportStatusDTO {

	private int cupId;

	private boolean importActive;
	private String importStatusMessage;

	public void setCupId( final int cupId ) {
		this.cupId = cupId;
	}

	public int getCupId() {
		return cupId;
	}

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


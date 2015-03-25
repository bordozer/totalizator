package totalizator.app.controllers.ui.cups;

public class CupModel {

	private String userName;
	private int cupId;

	public void setUserName( final String userName ) {
		this.userName = userName;
	}

	public String getUserName() {
		return userName;
	}

	public void setCupId( final int cupId ) {
		this.cupId = cupId;
	}

	public int getCupId() {
		return cupId;
	}

	@Override
	public String toString() {
		return String.format( "%d", cupId );
	}
}

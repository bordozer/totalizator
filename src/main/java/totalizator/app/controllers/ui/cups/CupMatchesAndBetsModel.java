package totalizator.app.controllers.ui.cups;

public class CupMatchesAndBetsModel {

	private String userName;
	private int cupId;
	private String cupName;

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

	public String getCupName() {
		return cupName;
	}

	public void setCupName( final String cupName ) {
		this.cupName = cupName;
	}

	@Override
	public String toString() {
		return String.format( "%d", cupId );
	}
}

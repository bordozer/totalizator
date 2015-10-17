package totalizator.app.controllers.rest.user.card;

public class UserCardParametersDTO {

	private String onDate;

	public String getOnDate() {
		return onDate;
	}

	public void setOnDate( final String onDate ) {
		this.onDate = onDate;
	}

	@Override
	public String toString() {
		return String.format( "%s", onDate );
	}
}

package totalizator.app.controllers.ui.cups;

import totalizator.app.controllers.ui.AbstractPageModel;
import totalizator.app.models.User;

public class CupMatchesAndBetsModel extends AbstractPageModel {

	private int cupId;
	private String cupName;

	protected CupMatchesAndBetsModel( final User user ) {
		super( user );
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

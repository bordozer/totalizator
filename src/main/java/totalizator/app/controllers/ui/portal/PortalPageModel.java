package totalizator.app.controllers.ui.portal;

import totalizator.app.controllers.ui.AbstractPageModel;

public class PortalPageModel extends AbstractPageModel {

	private String onDate;

	public PortalPageModel( final String today ) {
		onDate = today;
	}

	public String getOnDate() {
		return onDate;
	}

	public void setOnDate( final String onDate ) {
		this.onDate = onDate;
	}
}

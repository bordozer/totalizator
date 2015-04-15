package totalizator.app.controllers.ui.admin.matches;

import totalizator.app.controllers.ui.AbstractPageModel;
import totalizator.app.models.User;

public class AdminMatchesModel extends AbstractPageModel {

	private int categoryId;
	private int cupId;

	protected AdminMatchesModel( final User currentUser ) {
		super( currentUser );
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId( final int categoryId ) {
		this.categoryId = categoryId;
	}

	public int getCupId() {
		return cupId;
	}

	public void setCupId( final int cupId ) {
		this.cupId = cupId;
	}
}

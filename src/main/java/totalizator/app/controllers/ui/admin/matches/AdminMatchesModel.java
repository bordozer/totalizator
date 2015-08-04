package totalizator.app.controllers.ui.admin.matches;

import totalizator.app.controllers.ui.AbstractPageModel;

public class AdminMatchesModel extends AbstractPageModel {

	private int categoryId;
	private int cupId;

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

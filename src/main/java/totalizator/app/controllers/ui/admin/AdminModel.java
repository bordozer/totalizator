package totalizator.app.controllers.ui.admin;

import totalizator.app.controllers.ui.AbstractPageModel;

public class AdminModel extends AbstractPageModel {

	private int categoryId;
	private int cupId;

	public void setCategoryId( final int categoryId ) {
		this.categoryId = categoryId;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCupId( final int cupId ) {
		this.cupId = cupId;
	}

	public int getCupId() {
		return cupId;
	}
}

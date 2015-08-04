package totalizator.app.controllers.ui.categories;

import totalizator.app.controllers.ui.AbstractPageModel;
import totalizator.app.models.Category;

public class CategoryModel extends AbstractPageModel {

	private Category category;

	public Category getCategory() {
		return category;
	}

	public void setCategory( final Category category ) {
		this.category = category;
	}
}

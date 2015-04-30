package totalizator.app.controllers.ui.categories;

import totalizator.app.controllers.ui.AbstractPageModel;
import totalizator.app.models.Category;
import totalizator.app.models.User;

public class CategoryModel extends AbstractPageModel {

	private Category category;

	CategoryModel( final User currentUser ) {
		super( currentUser );
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory( final Category category ) {
		this.category = category;
	}
}

package totalizator.app.models;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import static totalizator.app.models.Category.FIND_CATEGORY_BY_NAME;
import static totalizator.app.models.Category.LOAD_ALL_CATEGORIES;

@Entity
@Table( name = "categories" )
@NamedQueries( {
		@NamedQuery(
				name = LOAD_ALL_CATEGORIES,
				query = "select c from Category c order by categoryName"
		),
		/*@NamedQuery(
				name = FIND_CATEGORY_BY_ID,
				query = "select c from Category c where id: id"
		),*/
		@NamedQuery(
				name = FIND_CATEGORY_BY_NAME,
				query = "select c from Category c where categoryName: categoryName"
		)
} )
public class Category extends AbstractEntity {

	public static final String LOAD_ALL_CATEGORIES = "categories.loadAllCategories";
//	public static final String FIND_CATEGORY_BY_ID = "categories.findCategoryById";
	public static final String FIND_CATEGORY_BY_NAME = "categories.findCategoryByName";

	private String categoryName;

	public Category() {
	}

	public Category( final String categoryName ) {
		this.categoryName = categoryName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName( final String categoryName ) {
		this.categoryName = categoryName;
	}

	@Override
	public String toString() {
		return String.format( "#%d: %s", getId(), categoryName );
	}
}

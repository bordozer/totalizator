package totalizator.app.controllers.rest.admin.categories;

public class CategoryEditDTO {

	private int categoryId;
	private String categoryName;

	public CategoryEditDTO() {
	}

	public CategoryEditDTO( final int categoryId, final String categoryName ) {
		this.categoryId = categoryId;
		this.categoryName = categoryName;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId( final int categoryId ) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName( final String categoryName ) {
		this.categoryName = categoryName;
	}

	@Override
	public String toString() {
		return String.format( "#%d: %s", categoryId, categoryName );
	}
}
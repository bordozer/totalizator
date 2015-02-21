package totalizator.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class CategoryDTO {

	private int categoryId;
	private String categoryName;

	public CategoryDTO() {
	}

	public CategoryDTO( int categoryId, String categoryName ) {
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
}

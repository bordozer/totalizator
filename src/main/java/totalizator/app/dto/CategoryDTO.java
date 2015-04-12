package totalizator.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class CategoryDTO {

	private int categoryId;
	private String categoryName;
	private String logoUrl;

	public CategoryDTO() {
	}

	public CategoryDTO( final int categoryId, final String categoryName ) {
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

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl( final String logoUrl ) {
		this.logoUrl = logoUrl;
	}

	@Override
	public String toString() {
		return String.format( "#%d: %s", categoryId, categoryName );
	}
}

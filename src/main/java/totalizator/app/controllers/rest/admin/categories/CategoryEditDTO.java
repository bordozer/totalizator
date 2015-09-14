package totalizator.app.controllers.rest.admin.categories;

public class CategoryEditDTO {

	private int categoryId;
	private String categoryName;
	private String logoUrl;

	private String categoryImportId;
	private int remoteGameImportStrategyTypeId;

	private int sportKindId;

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

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl( final String logoUrl ) {
		this.logoUrl = logoUrl;
	}

	public String getCategoryImportId() {
		return categoryImportId;
	}

	public void setCategoryImportId( final String categoryImportId ) {
		this.categoryImportId = categoryImportId;
	}

	public int getRemoteGameImportStrategyTypeId() {
		return remoteGameImportStrategyTypeId;
	}

	public void setRemoteGameImportStrategyTypeId( final int remoteGameImportStrategyTypeId ) {
		this.remoteGameImportStrategyTypeId = remoteGameImportStrategyTypeId;
	}

	public int getSportKindId() {
		return sportKindId;
	}

	public void setSportKindId( final int sportKindId ) {
		this.sportKindId = sportKindId;
	}

	@Override
	public String toString() {
		return String.format( "#%d: %s", categoryId, categoryName );
	}
}

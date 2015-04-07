package totalizator.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class CupDTO {

	private int cupId;
	private String cupName;
	private CategoryDTO category;
	private boolean showOnPortalPage;

	public CupDTO() {
	}

	public CupDTO( final int cupId, final String cupName, final CategoryDTO category ) {
		this.cupId = cupId;
		this.cupName = cupName;
		this.category = category;
	}

	public int getCupId() {
		return cupId;
	}

	public void setCupId( final int cupId ) {
		this.cupId = cupId;
	}

	public String getCupName() {
		return cupName;
	}

	public void setCupName( final String cupName ) {
		this.cupName = cupName;
	}

	public CategoryDTO getCategory() {
		return category;
	}

	public void setCategory( final CategoryDTO category ) {
		this.category = category;
	}

	public boolean isShowOnPortalPage() {
		return showOnPortalPage;
	}

	public void setShowOnPortalPage( final boolean showOnPortalPage ) {
		this.showOnPortalPage = showOnPortalPage;
	}

	@Override
	public String toString() {
		return String.format( "#%d %s", cupId, cupName );
	}
}

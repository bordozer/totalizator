package totalizator.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class CupDTO {

	private int cupId;
	private String cupName;
	private int categoryId;

	public CupDTO() {
	}

	public CupDTO( final int cupId, final String cupName, final int categoryId ) {
		this.cupId = cupId;
		this.cupName = cupName;
		this.categoryId = categoryId;
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

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId( final int categoryId ) {
		this.categoryId = categoryId;
	}
}

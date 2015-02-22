package totalizator.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class CupDTO {

	private int cupId;
	private String cupName;

	private CategoryDTO categoryDTO;

	public CupDTO() {
	}

	public CupDTO( final int cupId, final String cupName, final CategoryDTO categoryDTO ) {
		this.cupId = cupId;
		this.cupName = cupName;
		this.categoryDTO = categoryDTO;
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

	public CategoryDTO getCategoryDTO() {
		return categoryDTO;
	}

	public void setCategoryDTO( final CategoryDTO categoryDTO ) {
		this.categoryDTO = categoryDTO;
	}
}

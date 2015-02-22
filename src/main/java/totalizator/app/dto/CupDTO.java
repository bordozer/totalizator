package totalizator.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class CupDTO {

	private int cupId;
	private String cupName;

	public CupDTO() {
	}

	public CupDTO( final int cupId, final String cupName ) {
		this.cupId = cupId;
		this.cupName = cupName;
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
}

package totalizator.app.controllers.rest.admin.imports;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class GameImportParametersDTO {

	private String dateFrom;
	private String dateTo;

	private int cupId;

	public int getCupId() {
		return cupId;
	}

	public void setCupId( final int cupId ) {
		this.cupId = cupId;
	}

	public String getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom( final String dateFrom ) {
		this.dateFrom = dateFrom;
	}

	public String getDateTo() {
		return dateTo;
	}

	public void setDateTo( final String dateTo ) {
		this.dateTo = dateTo;
	}
}

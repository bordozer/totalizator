package totalizator.app.controllers.rest.admin.imports;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties( ignoreUnknown = true )
public class GameImportParametersDTO {

	private String dateFrom;
	private String dateTo;

	private int gameImportStrategyTypeId;

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

	public int getGameImportStrategyTypeId() {
		return gameImportStrategyTypeId;
	}

	public void setGameImportStrategyTypeId( final int gameImportStrategyTypeId ) {
		this.gameImportStrategyTypeId = gameImportStrategyTypeId;
	}
}

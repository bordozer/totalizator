package totalizator.app.controllers.rest.admin.cups;

import totalizator.app.dto.CupDTO;

public class CupForGameImportDTO {

	private final CupDTO cup;

	private final int timePeriodType;

	public CupForGameImportDTO( final CupDTO cup, final int timePeriodType ) {
		this.timePeriodType = timePeriodType;
		this.cup = cup;
	}

	public CupDTO getCup() {
		return cup;
	}

	public int getTimePeriodType() {
		return timePeriodType;
	}
}

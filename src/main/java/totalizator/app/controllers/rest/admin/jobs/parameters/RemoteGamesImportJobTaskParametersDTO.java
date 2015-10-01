package totalizator.app.controllers.rest.admin.jobs.parameters;

import com.google.gson.Gson;

public class RemoteGamesImportJobTaskParametersDTO extends AbstractJobTaskParametersDTO {

	private int cupId;
	private TimePeriodDTO timePeriod;

	public int getCupId() {
		return cupId;
	}

	public void setCupId( final int cupId ) {
		this.cupId = cupId;
	}

	public TimePeriodDTO getTimePeriod() {
		return timePeriod;
	}

	public void setTimePeriod( final TimePeriodDTO timePeriod ) {
		this.timePeriod = timePeriod;
	}

	@Override
	public String toString() {
		return new Gson().toJson( this, this.getClass() );
	}
}

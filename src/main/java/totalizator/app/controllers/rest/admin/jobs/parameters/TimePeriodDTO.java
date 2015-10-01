package totalizator.app.controllers.rest.admin.jobs.parameters;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import totalizator.app.dto.serialization.DateDeserializer;
import totalizator.app.dto.serialization.DateSerializer;

import java.time.LocalDate;

public class TimePeriodDTO {

	private int timePeriodType;

	private LocalDate dateFrom;
	private LocalDate dateTo;

	private int daysOffset;
	private int monthsOffset;

	public int getTimePeriodType() {
		return timePeriodType;
	}

	public void setTimePeriodType( final int timePeriodType ) {
		this.timePeriodType = timePeriodType;
	}

	@JsonSerialize( using = DateSerializer.class )
	public LocalDate getDateFrom() {
		return dateFrom;
	}

	@JsonDeserialize( using = DateDeserializer.class )
	public void setDateFrom( final LocalDate dateFrom ) {
		this.dateFrom = dateFrom;
	}

	@JsonSerialize( using = DateSerializer.class )
	public LocalDate getDateTo() {
		return dateTo;
	}

	@JsonDeserialize( using = DateDeserializer.class )
	public void setDateTo( final LocalDate dateTo ) {
		this.dateTo = dateTo;
	}

	public int getDaysOffset() {
		return daysOffset;
	}

	public void setDaysOffset( final int daysOffset ) {
		this.daysOffset = daysOffset;
	}

	public int getMonthsOffset() {
		return monthsOffset;
	}

	public void setMonthsOffset( final int monthsOffset ) {
		this.monthsOffset = monthsOffset;
	}
}

package totalizator.app.models.converters;

import totalizator.app.services.jobs.executors.JobExecutionState;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class JobExecutionStateConverter implements AttributeConverter<JobExecutionState, Integer> {

	@Override
	public Integer convertToDatabaseColumn( final JobExecutionState attribute ) {
		return attribute.getId();
	}

	@Override
	public JobExecutionState convertToEntityAttribute( final Integer dbData ) {
		return JobExecutionState.getById( dbData );
	}
}

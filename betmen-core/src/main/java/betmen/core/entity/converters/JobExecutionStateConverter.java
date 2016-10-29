package betmen.core.entity.converters;

import betmen.core.service.jobs.executors.JobExecutionState;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class JobExecutionStateConverter implements AttributeConverter<JobExecutionState, Integer> {

    @Override
    public Integer convertToDatabaseColumn(final JobExecutionState attribute) {
        return attribute.getId();
    }

    @Override
    public JobExecutionState convertToEntityAttribute(final Integer dbData) {
        return JobExecutionState.getById(dbData);
    }
}

package betmen.core.entity.converters;

import betmen.core.service.jobs.JobTaskType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class JobTaskTypeConverter implements AttributeConverter<JobTaskType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(final JobTaskType attribute) {
        return attribute.getId();
    }

    @Override
    public JobTaskType convertToEntityAttribute(final Integer dbData) {
        return JobTaskType.getById(dbData);
    }
}

package totalizator.app.models.converters;

import totalizator.app.models.activities.ActivityStreamEntryType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ActivityStreamEntryTypeConverter implements AttributeConverter<ActivityStreamEntryType, Integer> {

	@Override
	public Integer convertToDatabaseColumn( final ActivityStreamEntryType attribute ) {
		return attribute.getId();
	}

	@Override
	public ActivityStreamEntryType convertToEntityAttribute( final Integer dbData ) {
		return ActivityStreamEntryType.getById( dbData );
	}
}

package betmen.web.converters;

import betmen.dto.dto.ActivityStreamDTO;
import betmen.core.entity.User;
import betmen.core.entity.activities.AbstractActivityStreamEntry;
import betmen.core.entity.activities.ActivityStreamEntryType;

import java.util.List;

public interface ActivitiesDTOService {

    List<ActivityStreamDTO> transformActivities(final List<AbstractActivityStreamEntry> activities, final User currentUser);

    List<ActivityStreamDTO> transformActivities(final List<AbstractActivityStreamEntry> activities, final User currentUser, final List<ActivityStreamEntryType> excludedTypes);
}

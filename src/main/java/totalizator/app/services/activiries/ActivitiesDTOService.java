package totalizator.app.services.activiries;

import totalizator.app.controllers.rest.activities.ActivityStreamDTO;
import totalizator.app.models.User;
import totalizator.app.models.activities.AbstractActivityStreamEntry;
import totalizator.app.models.activities.ActivityStreamEntryType;

import java.util.List;

public interface ActivitiesDTOService {

	List<ActivityStreamDTO> transformActivities( final List<AbstractActivityStreamEntry> activities, final User currentUser );

	List<ActivityStreamDTO> transformActivities( final List<AbstractActivityStreamEntry> activities, final User currentUser, final List<ActivityStreamEntryType> excludedTypes );
}

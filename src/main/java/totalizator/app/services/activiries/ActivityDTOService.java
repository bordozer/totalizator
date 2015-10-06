package totalizator.app.services.activiries;

import totalizator.app.controllers.rest.activities.ActivityStreamDTO;
import totalizator.app.models.User;
import totalizator.app.models.activities.AbstractActivityStreamEntry;

public interface ActivityDTOService {

	String CACHE_ACTIVITY = "totalizator.app.cache.activityDTO";

	ActivityStreamDTO transformActivity( final AbstractActivityStreamEntry activity, final User currentUser );
}

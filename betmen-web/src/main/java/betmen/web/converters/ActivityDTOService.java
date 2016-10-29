package betmen.web.converters;

import betmen.dto.dto.ActivityStreamDTO;
import betmen.core.entity.User;
import betmen.core.entity.activities.AbstractActivityStreamEntry;

public interface ActivityDTOService {

    ActivityStreamDTO transformActivity(final AbstractActivityStreamEntry activity, final User currentUser);
}

package betmen.web.converters;

import betmen.core.entity.User;
import betmen.core.entity.activities.AbstractActivityStreamEntry;
import betmen.dto.dto.ActivityStreamDTO;

public interface ActivityDTOService {

    ActivityStreamDTO transformActivity(final AbstractActivityStreamEntry activity, final User currentUser);
}

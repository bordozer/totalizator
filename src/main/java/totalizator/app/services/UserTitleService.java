package totalizator.app.services;

import totalizator.app.beans.UserTitle;
import totalizator.app.models.User;

public interface UserTitleService {

	UserTitle getUserTitle( final User user );
}

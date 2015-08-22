package totalizator.app.services;

import totalizator.app.beans.UserTitle;
import totalizator.app.models.Cup;
import totalizator.app.models.User;

@Deprecated
public interface UserTitleService {

	UserTitle getUserTitle( final User user, final Cup cup );
}

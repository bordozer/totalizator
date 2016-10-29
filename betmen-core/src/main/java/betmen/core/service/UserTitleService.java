package betmen.core.service;

import betmen.core.model.UserTitle;
import betmen.core.entity.Cup;
import betmen.core.entity.User;

@Deprecated
public interface UserTitleService {

    UserTitle getUserTitle(final User user, final Cup cup);
}

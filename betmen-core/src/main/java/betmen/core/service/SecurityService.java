package betmen.core.service;

import betmen.core.entity.User;

public interface SecurityService {

    boolean isAdmin(final User user);

    boolean isAdmin(final int userId);
}

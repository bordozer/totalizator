package totalizator.app.services;

import totalizator.app.models.User;

public interface SecurityService {

	boolean isAdmin( final User user );

	boolean isAdmin( final int userId );
}

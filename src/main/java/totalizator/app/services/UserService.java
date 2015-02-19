package totalizator.app.services;

import totalizator.app.models.User;

public interface UserService {

	void createUser( final String login, final String name, final String password );

	User findUserByName( final String name );

	User findUserByLogin( final String login );
}

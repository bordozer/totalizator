package totalizator.app.services;

import totalizator.app.models.User;

public interface UserService extends GenericService<User>, NamedEntityGenericService<User>{

	void createUser( final String login, final String name, final String password );

	User findByLogin( final String login );

	String encodePassword( final String password );

	void updateUserPassword( final User user, final String password );
}

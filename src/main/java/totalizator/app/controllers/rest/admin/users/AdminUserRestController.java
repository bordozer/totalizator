package totalizator.app.controllers.rest.admin.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import totalizator.app.models.User;
import totalizator.app.services.UserService;

@RestController
@RequestMapping( "/admin/rest/users" )
public class AdminUserRestController {

	@Autowired
	private UserService userService;

	@RequestMapping( method = RequestMethod.GET, value = "/{userId}/" )
	public UserPasswordChangeResult adminChangeUserPassword( final @PathVariable int userId, final @RequestParam String password ) {

		final User user = userService.load( userId );

		userService.updateUserPassword( user, password );

		return new UserPasswordChangeResult( user, password );
	}

	private class UserPasswordChangeResult {

		private final String name;
		private final String login;
		private final String password;

		private UserPasswordChangeResult( final User user, final String password ) {
			this.name = user.getUsername();
			this.login = user.getLogin();
			this.password = password;
		}

		public String getName() {
			return name;
		}

		public String getLogin() {
			return login;
		}

		public String getPassword() {
			return password;
		}
	}
}

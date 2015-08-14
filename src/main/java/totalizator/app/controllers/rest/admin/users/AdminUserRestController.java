package totalizator.app.controllers.rest.admin.users;

import org.apache.commons.lang.StringUtils;
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

		final UserPasswordChangeResult result = new UserPasswordChangeResult( user, password );

		if ( StringUtils.isEmpty( password ) ) {

			result.setResult( "ERROR!!! Password can not be empty" );

			return result;
		}

		if ( password.equals( user.getUsername() ) ) {

			result.setResult( "ERROR!!! Public user name can not be used as password" );

			return result;
		}

		userService.updateUserPassword( user, password );

		return result;
	}

	private class UserPasswordChangeResult {

		private final String name;
		private final String login;
		private final String password;
		private String result = "OK";

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

		public String getResult() {
			return result;
		}

		public void setResult( final String result ) {
			this.result = result;
		}
	}
}

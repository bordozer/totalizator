package totalizator.app.controllers.ui;

import com.google.gson.Gson;
import totalizator.app.dto.UserDTO;
import totalizator.app.models.User;

public abstract class AbstractPageModel {

	private final User currentUser;

	protected AbstractPageModel( final User currentUser ) {
		this.currentUser = currentUser;
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public String getCurrentUserJSON() {
		return new Gson().toJson( new UserDTO( currentUser ) ); // TODO: use DTOService.transformUser()!
	}
}

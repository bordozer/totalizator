package totalizator.app.controllers.rest.admin.main;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import totalizator.app.dto.UserDTO;

@JsonIgnoreProperties( ignoreUnknown = true )
public class AdminMainPageDTO {

	private int id = 0;

	private UserDTO userDTO;

	public int getId() {
		return id;
	}

	public void setId( final int id ) {
		this.id = id;
	}

	public UserDTO getUserDTO() {
		return userDTO;
	}

	public void setUserDTO( final UserDTO userDTO ) {
		this.userDTO = userDTO;
	}
}

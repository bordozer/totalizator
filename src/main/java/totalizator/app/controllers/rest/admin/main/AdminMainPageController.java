package totalizator.app.controllers.rest.admin.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import totalizator.app.dto.UserDTO;
import totalizator.app.models.User;
import totalizator.app.services.UserService;

import java.security.Principal;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping( "/admin/rest" )
public class AdminMainPageController {

	@Autowired
	private UserService userService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	public AdminMainPageDTO adminMainPage( final Principal principal ) {

		final User user = userService.findByLogin( principal.getName() );

		final AdminMainPageDTO dto = new AdminMainPageDTO();
		dto.setUserDTO( new UserDTO( user.getId(), user.getUsername() ) );

		return dto;
	}
}

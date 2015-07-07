package totalizator.app.controllers.rest.groups;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.dto.UserDTO;
import totalizator.app.dto.UserGroupDTO;
import totalizator.app.services.DTOService;
import totalizator.app.services.UserGroupService;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping("/rest/user-groups/")
public class UserGroupsRestController {

	@Autowired
	private UserGroupService userGroupService;

	@Autowired
	private DTOService dtoService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	public List<UserGroupDTO> allUserGroups() {
		return dtoService.transformUserGroups( userGroupService.loadAll() );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/{userGroupId}/members/", produces = APPLICATION_JSON_VALUE )
	public List<UserDTO> allMembersOfUserGroup( final @PathVariable( "userGroupId" ) int userGroupId ) {
		return dtoService.transformUsers( userGroupService.loadGroupMembers( userGroupService.load( userGroupId ) ) );
	}
}

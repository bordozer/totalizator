package totalizator.app.controllers.rest.user.groups;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.dto.UserGroupDTO;
import totalizator.app.models.User;
import totalizator.app.models.UserGroup;
import totalizator.app.services.DTOService;
import totalizator.app.services.UserGroupService;
import totalizator.app.services.UserService;
import totalizator.app.services.utils.DateTimeService;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping("/rest/users/{userId}/groups")
public class User_UserGroupsRestController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserGroupService userGroupService;

	@Autowired
	private DateTimeService dateTimeService;

	@Autowired
	private DTOService dtoService;

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/owner/", produces = APPLICATION_JSON_VALUE )
	public List<UserGroupDTO> allGroupsWhereUserIsOwner( final @PathVariable( "userId" ) int userId ) {
		return dtoService.transformUserGroups( userGroupService.loadAllWhereIsOwner( userService.load( userId ) ) );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/member/", produces = APPLICATION_JSON_VALUE )
	public List<UserGroupDTO> loadAllWhereIsMember( final @PathVariable( "userId" ) int userId ) {
		return dtoService.transformUserGroups( userGroupService.loadAllWhereIsMember( userService.load( userId ) ) );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.PUT, value = "/owner/0", produces = APPLICATION_JSON_VALUE )
	public UserGroupDTO create( final @RequestBody UserGroupDTO dto, final @PathVariable( "userId" ) int userId ) {

		final UserGroup userGroup = save( userId, dto, new UserGroup() );
		dto.setUserGroupId( userGroup.getId() );

		return dto;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.PUT, value = "/owner/{userGroupId}", produces = APPLICATION_JSON_VALUE )
	public UserGroupDTO save( final @RequestBody UserGroupDTO dto, final @PathVariable( "userId" ) int userId, final @PathVariable( "userGroupId" ) int userGroupId ) {

		save( userId, dto, userGroupService.load( userGroupId ) );

		return dto;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.DELETE, value = "/{userGroupId}" )
	public void delete( final @PathVariable( "userId" ) int userId, final @PathVariable( "userGroupId" ) int userGroupId ) {
		userGroupService.delete( userGroupId );
	}

	private UserGroup save( final int userId, final UserGroupDTO dto, final UserGroup userGroup ) {

		final User groupOwner = userService.load( userId );

		userGroup.setOwner( groupOwner );
		userGroup.setGroupName( dto.getUserGroupName() );
		userGroup.setCreationTime( dateTimeService.getNow() );

		return userGroupService.save( userGroup, dto.getCupIds() );
	}
}

package totalizator.app.controllers.rest.user.groups;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import totalizator.app.dto.UserGroupDTO;
import totalizator.app.models.Cup;
import totalizator.app.models.User;
import totalizator.app.models.UserGroup;
import totalizator.app.services.DTOService;
import totalizator.app.services.UserGroupService;
import totalizator.app.services.UserService;
import totalizator.app.services.utils.DateTimeService;

import java.security.Principal;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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
	public List<UserGroupDTO> loadUserGroupsWhereUserIsOwner( final @PathVariable( "userId" ) int userId, final Principal principal ) {
		return dtoService.transformUserGroups( userGroupService.loadUserGroupsWhereUserIsOwner( userService.load( userId ) ), getUser( principal ) );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/member/", produces = APPLICATION_JSON_VALUE )
	public List<UserGroupDTO> loadUserGroupsWhereUserIsMember( final @PathVariable( "userId" ) int userId, final Principal principal ) {
		return dtoService.transformUserGroups( userGroupService.loadUserGroupsWhereUserIsMember( userService.load( userId ) ), getUser( principal ) );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.GET, value = "/", produces = APPLICATION_JSON_VALUE )
	public List<UserGroupEditDTO> userGroupList( final @PathVariable( "userId" ) int userId ) {

		final Function<Cup, Integer> cupMapper = new Function<Cup, Integer>() {
			@Override
			public Integer apply( final Cup cup ) {
				return cup.getId();
			}
		};

		final List<UserGroup> userGroups = userGroupService.loadUserGroupsWhereUserIsOwner( userService.load( userId ) );

		final Function<UserGroup, UserGroupEditDTO> mapper = new Function<UserGroup, UserGroupEditDTO>() {
			@Override
			public UserGroupEditDTO apply( final UserGroup userGroup ) {

				final UserGroupEditDTO dto = new UserGroupEditDTO();
				dto.setUserGroupId( userGroup.getId() );
				dto.setUserGroupName( userGroup.getGroupName() );

				dto.setCupIds( userGroupService.loadCups( userGroup ).stream().map( cupMapper ).collect( Collectors.toList() ) );

				return dto;
			}
		};
		return userGroups.stream().map( mapper ).collect( Collectors.toList() );
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.PUT, value = "/0", produces = APPLICATION_JSON_VALUE )
	public UserGroupEditDTO create( final @RequestBody UserGroupEditDTO dto, final @PathVariable( "userId" ) int userId ) {
		// TODO: validate!
		final UserGroup userGroup = save( userId, dto, new UserGroup() );
		dto.setUserGroupId( userGroup.getId() );

		return dto;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.PUT, value = "/{userGroupId}", produces = APPLICATION_JSON_VALUE )
	public UserGroupEditDTO save( final @RequestBody UserGroupEditDTO dto, final @PathVariable( "userId" ) int userId, final @PathVariable( "userGroupId" ) int userGroupId ) {
		// TODO: validate!
		save( userId, dto, userGroupService.load( userGroupId ) );

		return dto;
	}

	@ResponseStatus( HttpStatus.OK )
	@ResponseBody
	@RequestMapping( method = RequestMethod.DELETE, value = "/{userGroupId}" )
	public void delete( final @PathVariable( "userId" ) int userId, final @PathVariable( "userGroupId" ) int userGroupId ) {
		userGroupService.delete( userGroupId );
	}

	private UserGroup save( final int userId, final UserGroupEditDTO dto, final UserGroup userGroup ) {

		final User groupOwner = userService.load( userId );

		userGroup.setOwner( groupOwner );
		userGroup.setGroupName( dto.getUserGroupName() );
		userGroup.setCreationTime( dateTimeService.getNow() );

		return userGroupService.save( userGroup, dto.getCupIds() );
	}

	private User getUser( final Principal principal ) {
		return userService.findByLogin( principal.getName() );
	}
}

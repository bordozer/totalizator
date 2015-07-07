package totalizator.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import totalizator.app.dao.UserGroupCupDao;
import totalizator.app.dao.UserGroupDao;
import totalizator.app.dao.UserGroupMemberDao;
import totalizator.app.models.*;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class UserGroupServiceImpl implements UserGroupService {

	@Autowired
	private UserGroupDao userGroupRepository;

	@Autowired
	private UserGroupCupDao userGroupCupRepository;

	@Autowired
	private UserGroupMemberDao userGroupMemberRepository;

	@Autowired
	private CupService cupService;

	@Override
	@Transactional( readOnly = true )
	public List<UserGroup> loadAll() {
		return userGroupRepository.loadAll();
	}

	@Override
	@Transactional( readOnly = true )
	public UserGroup load( final int id ) {
		return userGroupRepository.load( id );
	}

	@Override
	@Transactional
	public UserGroup save( final UserGroup entry ) {
		return userGroupRepository.save( entry );
	}

	@Override
	@Transactional
	public UserGroup save( final UserGroup userGroup, final List<Integer> cupIds ) {

		final UserGroup saved = this.save( userGroup );

		userGroupCupRepository.deleteAll( saved );

		for ( final int cupId : cupIds ) {
			userGroupCupRepository.save( new UserGroupCup( saved, cupService.load( cupId ) ) );
		}

		return saved;
	}

	@Override
	@Transactional
	public void delete( final int id ) {
		userGroupCupRepository.deleteAll( load( id ) );
		userGroupRepository.delete( id );
	}

	@Override
	@Transactional( readOnly = true )
	public List<UserGroup> loadUserGroupsWhereUserIsOwner( final User user ) {
		return userGroupRepository.loadUserGroupsWhereUserIsOwner( user );
	}

	@Override
	@Transactional( readOnly = true )
	public List<UserGroup> loadUserGroupsWhereUserIsMember( final User user ) {

		final Function<UserGroupMember, UserGroup> mapper = new Function<UserGroupMember, UserGroup>() {
			@Override
			public UserGroup apply( final UserGroupMember userGroupMember ) {
				return userGroupMember.getUserGroup();
			}
		};

		final List<UserGroupMember> memberInGroups = userGroupMemberRepository.loadUserGroupsWhereUserIsMember( user );

		return memberInGroups.stream().map( mapper ).collect( Collectors.toList() );
	}

	@Override
	@Transactional( readOnly = true )
	public List<User> loadUserGroupMembers( final UserGroup userGroup ) {

		final Function<UserGroupMember, User> mapper = new Function<UserGroupMember, User>() {

			@Override
			public User apply( final UserGroupMember userGroupMember ) {
				return userGroupMember.getUser();
			}
		};

		final List<UserGroupMember> userGroupMembers = userGroupMemberRepository.loadUserGroupMembers( userGroup );

		return userGroupMembers.stream().map( mapper ).collect( Collectors.toList() );
	}

	@Override
	public List<Cup> loadCups( final UserGroup userGroup ) {

		final Function<UserGroupCup, Cup> mapper = new Function<UserGroupCup, Cup>() {
			@Override
			public Cup apply( final UserGroupCup userGroupCup ) {
				return userGroupCup.getCup();
			}
		};

		final List<UserGroupCup> userGroupCups = userGroupCupRepository.loadCups( userGroup );

		return userGroupCups.stream().map( mapper ).collect( Collectors.toList() );
	}

	@Transactional( readOnly = true )
	@Cacheable( value = GenericService.CACHE_PERMANENT )
	public boolean isUserOwnerOfGroup( final UserGroup userGroup, final User user ) {

		for ( final UserGroup group : loadUserGroupsWhereUserIsOwner( user ) ) {
			if ( group.getOwner().equals( user ) ) {
				return true;
			}
		}

		return false;
	}
}

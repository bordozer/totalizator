package totalizator.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import totalizator.app.dao.UserGroupDao;
import totalizator.app.models.User;
import totalizator.app.models.UserGroup;

import java.util.List;

@Service
public class UserGroupServiceImpl implements UserGroupService {

	@Autowired
	private UserGroupDao userGroupRepository;

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
	public void delete( final int id ) {
		userGroupRepository.delete( id );
	}

	@Override
	@Transactional( readOnly = true )
	public List<UserGroup> loadAllOwned( final User user ) {
		return userGroupRepository.loadAllOwned( user );
	}

	@Override
	@Transactional( readOnly = true )
	public List<UserGroup> loadAll( final User user ) {
		return userGroupRepository.loadAll( user );
	}

	@Transactional( readOnly = true )
	@Cacheable( value = GenericService.CACHE_PERMANENT )
	public boolean isOwner( final UserGroup userGroup, final User user ) {

		for ( final UserGroup group : loadAllOwned( user ) ) {
			if ( group.getOwner().equals( user ) ) {
				return true;
			}
		}

		return false;
	}
}

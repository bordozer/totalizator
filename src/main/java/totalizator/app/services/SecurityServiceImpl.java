package totalizator.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.models.User;

@Service
public class SecurityServiceImpl implements SecurityService {

	@Autowired
	private SystemVarsService systemVarsService;

	@Override
	public boolean isAdmin( final User user ) {
		return isAdmin( user.getId() );
	}

	@Override
	public boolean isAdmin( final int userId ) {
		for ( final int adminId : systemVarsService.getAdminIds() ) {
			if ( userId == adminId ) {
				return true;
			}
		}

		return false;
	}
}

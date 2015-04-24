package totalizator.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.dao.CupTeamRepository;
import totalizator.app.models.CupTeam;

@Service
public class CupTeamServiceImpl implements CupTeamService {

	@Autowired
	private CupTeamRepository cupTeamRepository;

	@Autowired
	private CupService cupService;

	@Autowired
	private TeamService teamService;

	@Override
	public void saveCupTeam( final int cupId, final int teamId, final boolean isActive ) {

		final CupTeam cupTeam = cupTeamRepository.load( cupId, teamId );

		final boolean exists = cupTeam != null;

		if ( isActive && ! exists ) {
			createNewEntry( cupId, teamId );
			return;
		}

		if ( ! isActive && exists ) {
			deleteExistingEntry( cupTeam );
			return;
		}
	}

	private void createNewEntry( final int cupId, final int teamId ) {
		cupTeamRepository.save( new CupTeam( cupService.load( cupId ), teamService.load( teamId ) ) );
	}

	private void deleteExistingEntry( final CupTeam cupTeam ) {
		cupTeamRepository.delete( cupTeam.getId() );
	}
}

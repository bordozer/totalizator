package totalizator.app.services;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import totalizator.app.dao.CupTeamRepository;
import totalizator.app.models.Cup;
import totalizator.app.models.CupTeam;
import totalizator.app.models.Team;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Service
public class CupTeamServiceImpl implements CupTeamService {

	@Autowired
	private CupTeamRepository cupTeamRepository;

	@Autowired
	private CupService cupService;

	@Autowired
	private TeamService teamService;

	@Override
	@Transactional
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

	@Override
	@Transactional( readOnly = true )
	public boolean exists( final Cup cup, final Team team ) {
		return cupTeamRepository.load( cup.getId(), team.getId() ) != null;
	}

	@Override
	public List<Team> loadActiveForCup( final int cupId ) {

		return Lists.transform( cupTeamRepository.loadAll( cupId ), new Function<CupTeam, Team>() {
			@Override
			public Team apply( final CupTeam cupTeam ) {
				return cupTeam.getTeam();
			}
		} );
	}

	private void createNewEntry( final int cupId, final int teamId ) {
		cupTeamRepository.save( new CupTeam( cupService.load( cupId ), teamService.load( teamId ) ) );
	}

	private void deleteExistingEntry( final CupTeam cupTeam ) {
		cupTeamRepository.delete( cupTeam.getId() );
	}
}

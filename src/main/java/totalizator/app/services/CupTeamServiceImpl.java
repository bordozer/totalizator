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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
		return exists( cup.getId(), team.getId() );
	}

	@Override
	public boolean exists( final int cupId, final int teamId ) {
		return cupTeamRepository.load( cupId, teamId ) != null;
	}

	@Override
	public List<Team> loadActiveForCup( final int cupId ) {

		final List<CupTeam> cupTeams = cupTeamRepository.loadAll( cupId );
		Collections.sort( cupTeams, new Comparator<CupTeam>() {
			@Override
			public int compare( final CupTeam o1, final CupTeam o2 ) {
				return o1.getTeam().getTeamName().compareToIgnoreCase( o2.getTeam().getTeamName() );
			}
		} );

		return Lists.transform( cupTeams, new Function<CupTeam, Team>() {
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
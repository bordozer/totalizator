package totalizator.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import totalizator.app.dao.TeamDao;
import totalizator.app.models.Category;
import totalizator.app.models.Team;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Service
public class TeamServiceImpl implements TeamService {

	@Autowired
	private TeamDao teamRepository;

	@Autowired
	private CupTeamService cupTeamService;

	@Override
	@Transactional( readOnly = true )
	public List<Team> loadAll() {
		return newArrayList( teamRepository.loadAll() );
	}

	@Override
	public List<Team> loadAll( final Category category ) {
		return newArrayList( teamRepository.loadAll( category ) );
	}

	@Override
	@Transactional
	public Team save( final Team entry ) {
		return teamRepository.save( entry );
	}

	@Override
	@Transactional( readOnly = true )
	public Team load( final int teamId ) {
		return teamRepository.load( teamId );
	}

	@Override
	@Transactional
	public void delete( final int teamId ) {
		cupTeamService.clearFor( teamId );
		teamRepository.delete( teamId );
	}

	@Override
	@Transactional( readOnly = true )
	public Team findByName( final Category category, final String name ) {
		return teamRepository.findByName( category, name );
	}

	@Override
	public Team findByImportId( final Category category, final String teamImportId ) {
		return teamRepository.findByImportId( category, teamImportId );
	}
}

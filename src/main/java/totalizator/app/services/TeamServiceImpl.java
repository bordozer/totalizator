package totalizator.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import totalizator.app.dao.TeamRepository;
import totalizator.app.models.Team;

import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {

	@Autowired
	private TeamRepository teamRepository;

	@Override
	@Transactional( readOnly = true )
	public List<Team> loadAll() {
		return teamRepository.loadAll();
	}

	@Override
	@Transactional
	public Team save( final Team entry ) {
		return teamRepository.save( entry );
	}

	@Override
	@Transactional( readOnly = true )
	public Team load( final int id ) {
		return teamRepository.load( id );
	}

	@Override
	@Transactional
	public void delete( final int id ) {
		teamRepository.delete( id );
	}

	@Override
	@Transactional( readOnly = true )
	public Team findByName( final String name ) {
		return teamRepository.findByName( name );
	}
}

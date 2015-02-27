package totalizator.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.dao.TeamRepository;
import totalizator.app.models.Team;

import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {

	@Autowired
	private TeamRepository teamRepository;

	@Override
	public List<Team> loadAll() {
		return teamRepository.loadAll();
	}

	@Override
	public Team save( final Team entry ) {
		return teamRepository.save( entry );
	}

	@Override
	public Team load( final int id ) {
		return teamRepository.load( id );
	}

	@Override
	public void delete( final int id ) {
		teamRepository.delete( id );
	}

	@Override
	public Team findByName( final String name ) {
		return teamRepository.findByName( name );
	}
}

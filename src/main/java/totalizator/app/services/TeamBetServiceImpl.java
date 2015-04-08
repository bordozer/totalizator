package totalizator.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import totalizator.app.dao.TeamBetRepository;
import totalizator.app.models.Cup;
import totalizator.app.models.TeamBet;
import totalizator.app.models.User;

import java.util.List;

@Service
public class TeamBetServiceImpl implements TeamBetService {

	@Autowired
	private TeamBetRepository teamBetRepository;

	@Override
	@Transactional( readOnly = true )
	public List<TeamBet> loadAll() {
		return teamBetRepository.loadAll();
	}

	@Override
	@Transactional( readOnly = true )
	public TeamBet load( final int id ) {
		return teamBetRepository.load( id );
	}

	@Override
	@Transactional
	public TeamBet save( final TeamBet entry ) {
		return teamBetRepository.save( entry );
	}

	@Override
	@Transactional
	public void delete( final int id ) {
		teamBetRepository.delete( id );
	}

	@Override
	@Transactional( readOnly = true )
	public List<TeamBet> load( final Cup cup ) {
		return teamBetRepository.load( cup );
	}

	@Override
	@Transactional( readOnly = true )
	public List<TeamBet> load( final User user ) {
		return teamBetRepository.load( user );
	}

	@Override
	@Transactional( readOnly = true )
	public List<TeamBet> load( final Cup cup, final User user ) {
		return teamBetRepository.load( cup, user );
	}
}

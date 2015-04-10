package totalizator.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import totalizator.app.dao.CupTeamBetRepository;
import totalizator.app.models.Cup;
import totalizator.app.models.CupTeamBet;
import totalizator.app.models.Team;
import totalizator.app.models.User;

import java.util.List;

@Service
public class CupBetsServiceImpl implements CupBetsService {

	@Autowired
	private CupTeamBetRepository cupTeamBetRepository;

	@Override
	@Transactional( readOnly = true )
	public List<CupTeamBet> loadAll() {
		return cupTeamBetRepository.loadAll();
	}

	@Override
	@Transactional( readOnly = true )
	public CupTeamBet load( final int id ) {
		return cupTeamBetRepository.load( id );
	}

	@Override
	@Transactional
	public CupTeamBet save( final CupTeamBet entry ) {
		return cupTeamBetRepository.save( entry );
	}

	@Override
	@Transactional
	public void delete( final int id ) {
		cupTeamBetRepository.delete( id );
	}

	@Override
	@Transactional( readOnly = true )
	public List<CupTeamBet> load( final Cup cup, final User user ) {
		return cupTeamBetRepository.load( cup, user );
	}

	@Override
	public CupTeamBet load( final Cup cup, final User user, final int cupPosition ) {
		return cupTeamBetRepository.load( cup, user, cupPosition );
	}

	@Override
	public CupTeamBet load( final Cup cup, final User user, final Team team ) {
		return cupTeamBetRepository.load( cup, team, user );
	}
}

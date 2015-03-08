package totalizator.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import totalizator.app.dao.MatchBetRepository;
import totalizator.app.models.Match;
import totalizator.app.models.MatchBet;
import totalizator.app.models.User;

import java.util.List;

@Service
public class MatchBetsServiceImpl implements MatchBetsService {

	@Autowired
	private MatchBetRepository matchBetRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private MatchService matchService;

	@Override
	@Transactional( readOnly = true )
	public List<MatchBet> loadAll() {
		return matchBetRepository.loadAll();
	}

	@Override
	@Transactional( readOnly = true )
	public List<MatchBet> loadAll( final User user ) {
		return matchBetRepository.loadAll( user );
	}

	@Override
	@Transactional( readOnly = true )
	public List<MatchBet> loadAll( final Match match ) {
		return matchBetRepository.loadAll( match );
	}

	@Override
	public List<MatchBet> loadAll( final User user, final Match match ) {
		return matchBetRepository.loadAll( user, match );
	}

	@Override
	public List<MatchBet> loadAll( final int userId, final int matchId ) {
		return loadAll( userService.load( userId ), matchService.load( matchId ) );
	}

	@Override
	@Transactional( readOnly = true )
	public MatchBet load( final int id ) {
		return matchBetRepository.load( id );
	}

	@Override
	@Transactional
	public MatchBet save( final MatchBet entry ) {
		return matchBetRepository.save( entry );
	}

	@Override
	@Transactional
	public void delete( final int id ) {
		matchBetRepository.delete( id );
	}
}

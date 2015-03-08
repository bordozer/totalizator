package totalizator.app.services;

import org.springframework.stereotype.Service;
import totalizator.app.models.Match;
import totalizator.app.models.MatchBet;
import totalizator.app.models.User;

import java.util.List;

@Service
public class MatchBetsServiceImpl implements MatchBetsService {

	@Override
	public List<MatchBet> loadAll() {
		return null;
	}

	@Override
	public List<MatchBet> loadAll( final User user ) {
		return null;
	}

	@Override
	public List<MatchBet> loadAll( final Match match ) {
		return null;
	}

	@Override
	public MatchBet save( final MatchBet entry ) {
		return null;
	}

	@Override
	public MatchBet load( final int id ) {
		return null;
	}

	@Override
	public void delete( final int id ) {

	}
}

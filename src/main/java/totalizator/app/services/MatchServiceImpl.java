package totalizator.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import totalizator.app.dao.MatchRepository;
import totalizator.app.models.Match;

import java.util.List;

@Service
public class MatchServiceImpl implements MatchService {

	@Autowired
	private MatchRepository matchRepository;

	@Override
	@Transactional( readOnly = true )
	public List<Match> loadAll() {
		return matchRepository.loadAll();
	}

	@Override
	@Transactional
	public Match save( final Match entry ) {
		return matchRepository.save( entry );
	}

	@Override
	@Transactional( readOnly = true )
	public Match load( final int id ) {
		return matchRepository.load( id );
	}

	@Override
	@Transactional
	public void delete( final int id ) {
		matchRepository.delete( id );
	}
}

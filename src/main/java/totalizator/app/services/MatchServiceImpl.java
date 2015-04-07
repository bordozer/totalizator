package totalizator.app.services;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import totalizator.app.dao.MatchRepository;
import totalizator.app.dto.MatchesBetSettingsDTO;
import totalizator.app.models.Cup;
import totalizator.app.models.Match;

import java.util.List;

@Service
public class MatchServiceImpl implements MatchService {

	@Autowired
	private MatchRepository matchRepository;

	@Autowired
	private CupService cupService;

	@Autowired
	private TeamService teamService;

	private static final Logger LOGGER = Logger.getLogger( MatchServiceImpl.class );

	@Override
	@Transactional( readOnly = true )
	public List<Match> loadAll() {
		return matchRepository.loadAll();
	}

	@Override
	public List<Match> loadAll( final Cup cup ) {
		return matchRepository.loadAll( cup );
	}

	@Override
	public List<Match> loadAll( final MatchesBetSettingsDTO dto ) {

		final List<Match> matches = loadAll();

		if ( dto.getCategoryId() > 0 ) {
			CollectionUtils.filter( matches, new Predicate<Match>() {
				@Override
				public boolean evaluate( final Match match ) {
					return match.getCup().getCategory().getId() == dto.getCategoryId();
				}
			} );
		}

		if ( dto.getCupId() > 0 ) {
			CollectionUtils.filter( matches, new Predicate<Match>() {
				@Override
				public boolean evaluate( final Match match ) {
					return match.getCup().getId() == dto.getCupId();
				}
			} );
		}

		if ( dto.getTeamId() > 0 ) {
			CollectionUtils.filter( matches, new Predicate<Match>() {
				@Override
				public boolean evaluate( final Match match ) {
					return match.getTeam1().getId() == dto.getTeamId() || match.getTeam2().getId() == dto.getTeamId();
				}
			} );
		}

		if ( !dto.isShowFutureMatches() ) {
			CollectionUtils.filter( matches, new Predicate<Match>() {
				@Override
				public boolean evaluate( final Match match ) {
					return match.isMatchFinished();
				}
			} );
		}

		if ( !dto.isShowFinished() ) {
			CollectionUtils.filter( matches, new Predicate<Match>() {
				@Override
				public boolean evaluate( final Match match ) {
					return ! match.isMatchFinished();
				}
			} );
		}

		return matches;
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

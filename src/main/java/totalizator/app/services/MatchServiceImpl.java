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
import totalizator.app.models.MatchBet;
import totalizator.app.models.Team;
import totalizator.app.services.utils.DateTimeService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class MatchServiceImpl implements MatchService {

	@Autowired
	private MatchRepository matchRepository;

	@Autowired
	private DateTimeService dateTimeService;

	@Autowired
	private MatchBetsService matchBetsService;

	private static final Logger LOGGER = Logger.getLogger( MatchServiceImpl.class );

	@Override
	@Transactional( readOnly = true )
	public List<Match> loadAll() {
		return sort( matchRepository.loadAll() );
	}

	@Override
	public List<Match> loadAll( final Cup cup ) {
		return sort( matchRepository.loadAll( cup ) );
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
					return ( match.getTeam1().getId() == dto.getTeamId() ) || ( match.getTeam2().getId() == dto.getTeamId() );
				}
			} );
		}

		if ( dto.getTeam2Id() > 0 ) {
			CollectionUtils.filter( matches, new Predicate<Match>() {
				@Override
				public boolean evaluate( final Match match ) {
					return match.getTeam1().getId() == dto.getTeam2Id() || match.getTeam2().getId() == dto.getTeam2Id();
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

		if ( dto.isFilterByDateEnable() ) {

			final String _filterByDate = dto.getFilterByDate();
			final LocalDateTime filterByDate = dateTimeService.parseDate( _filterByDate );

			CollectionUtils.filter( matches, new Predicate<Match>() {
				@Override
				public boolean evaluate( final Match match ) {
					return dateTimeService.hasTheSameDate( match.getBeginningTime(), filterByDate );
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
		final List<MatchBet> bets = matchBetsService.loadAll( load( id ) );
		for ( final MatchBet bet : bets ) {
			matchBetsService.delete( bet.getId() );
		}
		matchRepository.delete( id );
	}

	@Override
	public boolean isMatchStarted( final Match match ) {
		return dateTimeService.getNow().isAfter( match.getBeginningTime() );
	}

	@Override
	public boolean isMatchFinished( final Match match ) {
		return match.isMatchFinished();
	}

	@Override
	public Match find( final Team team1, final Team team2, final LocalDateTime localDateTime ) {
		final List<Match> matches = matchRepository.find( team1, team2 );

		CollectionUtils.filter( matches, new Predicate<Match>() {
			@Override
			public boolean evaluate( final Match match ) {
				return match.getBeginningTime().equals( localDateTime );
			}
		} );

		return matches.size() > 0 ? matches.get( 0 ) : null;
	}

	@Override
	public List<Match> find( final Team team1, final Team team2 ) {
		return matchRepository.find( team1, team2 );
	}

	private List<Match> sort( final List<Match> matches ) {
		Collections.sort( matches, new Comparator<Match>() {
			@Override
			public int compare( final Match o1, final Match o2 ) {
				return o2.getBeginningTime().compareTo( o1.getBeginningTime() );
			}
		} );
		return matches;
	}
}

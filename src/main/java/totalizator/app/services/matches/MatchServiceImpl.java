package totalizator.app.services.matches;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import totalizator.app.dao.MatchDao;
import totalizator.app.models.Cup;
import totalizator.app.models.Match;
import totalizator.app.models.MatchBet;
import totalizator.app.models.Team;
import totalizator.app.services.activiries.ActivityStreamService;
import totalizator.app.services.points.recalculation.MatchPointsRecalculationService;
import totalizator.app.services.utils.DateTimeService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Service
public class MatchServiceImpl implements MatchService {

	@Autowired
	private MatchDao matchRepository;

	@Autowired
	private DateTimeService dateTimeService;

	@Autowired
	private MatchBetsService matchBetsService;

	@Autowired
	private ActivityStreamService activityStreamService;

	@Autowired
	private MatchPointsRecalculationService matchPointsRecalculationService;

	private static final Logger LOGGER = Logger.getLogger( MatchServiceImpl.class );

	@Override
	@Transactional( readOnly = true )
	public List<Match> loadAll() {
		return sort( newArrayList( matchRepository.loadAll() ) );
	}

	@Override
	public List<Match> loadAll( final Cup cup ) {
		return sort( newArrayList( matchRepository.loadAll( cup ) ) );
	}

	@Override
	public List<Match> loadAllNotFinished( final int cupId ) {
		return matchRepository.loadAllNotFinished( cupId );
	}

	@Override
	public List<Match> loadAllFinished( final int cupId ) {
		return matchRepository.loadAllFinished( cupId );
	}

	@Override
	public List<Match> loadAll( final int cupId, final int teamId ) {
		return sort( newArrayList( matchRepository.loadAll( cupId, teamId ) ) );
	}

	@Override
	public List<Match> loadAllFinished( final int cupId, final int teamId ) {

		final List<Match> matches = matchRepository.loadAll( cupId, teamId );

		matches
				.stream()
				.filter( new java.util.function.Predicate<Match>() {

					@Override
					public boolean test( final Match match ) {
						return isMatchFinished( match );
					}
				} );

		return sort( newArrayList( matches ) );
	}

	@Override
	public List<Match> loadAll( final Team team ) {
		return matchRepository.loadAll( team );
	}

	@Override
	public int getMatchCount( Team team ) {
		return matchRepository.getMatchCount( team );
	}

	@Override
	@Transactional
	public Match save( final Match entry ) {

		final Match match = matchRepository.save( entry );

		matchPointsRecalculationService.recalculate( match );

		if ( match.isMatchFinished() ) {
			activityStreamService.matchFinished( match.getId(), match.getScore1(), match.getScore2() );
		}

		return match;
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
	@Transactional( readOnly = true )
	public boolean isMatchFinished( final Match match ) {
		return match.isMatchFinished();
	}

	@Override
	public Match find( final Cup cup, final Team team1, final Team team2, final LocalDateTime localDateTime ) {
		final List<Match> matches = loadAll( cup.getId(), team1.getId(), team2.getId() );

		CollectionUtils.filter( matches, new Predicate<Match>() {
			@Override
			public boolean evaluate( final Match match ) {
				return match.getBeginningTime().equals( localDateTime );
			}
		} );

		return matches.size() > 0 ? matches.get( 0 ) : null;
	}

	@Override
	public List<Match> loadAll( final Team team1, final Team team2 ) {
		return newArrayList( matchRepository.loadAll( team1, team2 ) );
	}

	@Override
	public List<Match> loadAll( final int cupId, final int team1Id, final int team2Id ) {
		return newArrayList( matchRepository.loadAll( cupId, team1Id, team2Id ) );
	}

	@Override
	public boolean isWinner( final Match match, final Team team ) {

		if ( match.getTeam1().equals( team ) ) {
			return match.getScore1() > match.getScore2();
		}

		return match.getScore1() < match.getScore2();
	}

	@Override
	public int getMatchCount( final Cup cup ) {
		return matchRepository.getMatchCount( cup );
	}

	@Override
	public int getMatchCount( final Cup cup, final Team team ) {
		return matchRepository.getMatchCount( cup, team );
	}

	@Override
	public int getFinishedMatchCount( final Cup cup, final Team team ) {
		return matchRepository.getFinishedMatchCount( cup, team );
	}

	@Override
	public int getWonMatchCount( final Cup cup, final Team team ) {
		int result = 0;

		final List<Match> matches = loadAllFinished( cup.getId(), team.getId() );

		for ( final Match match : matches ) {
			if ( isWinner( match, team ) ) {
				result += 1;
			}
		}

		return result;
	}

	@Override
	public int getFutureMatchCount( final Cup cup ) {
		return matchRepository.getFutureMatchCount( cup );
	}

	@Override
	public int getFutureMatchCount( Cup cup, Team team ) {
		return matchRepository.getFutureMatchCount( cup, team );
	}

	@Override
	public Match findByImportId( final String remoteGameId ) {
		return matchRepository.findByImportId( remoteGameId );
	}

	@Override
	public List<Match> loadAllBetween( final LocalDateTime timeFrom, final LocalDateTime timeTo ) {
		return matchRepository.loadAllBetween( timeFrom, timeTo );
	}

	@Override
	public List<Match> loadAllBetween( final int cupId, final LocalDateTime timeFrom, final LocalDateTime timeTo ) {
		return matchRepository.loadAllBetween( cupId, timeFrom, timeTo );
	}

	@Override
	public List<Match> loadAllOnDate( final LocalDate date ) {
		return loadAllBetween( dateTimeService.getFirstSecondOf( date ), dateTimeService.getLastSecondOf( date ) );
	}

	@Override
	public List<Match> loadAllOnDate( final int cupId, final LocalDate date ) {
		return loadAllBetween( cupId, dateTimeService.getFirstSecondOf( date ), dateTimeService.getLastSecondOf( date ) );
	}

	@Override
	public List<Match> getMatchNotFinishedYetMatches( final Cup cup ) {
		return matchRepository.getStartedMatchCount( cup, dateTimeService.getNow() );
	}

	@Override
	public Match getNearestFutureMatch( final Cup cup, final LocalDateTime onTime ) {
		return matchRepository.getNearestFutureMatch( cup, onTime );
	}

	@Override
	public Match getFirstMatch( final Cup cup ) {
		return matchRepository.getFirstMatch( cup );
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

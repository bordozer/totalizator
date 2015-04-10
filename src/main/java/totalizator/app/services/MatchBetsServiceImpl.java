package totalizator.app.services;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import totalizator.app.dao.MatchBetRepository;
import totalizator.app.models.Cup;
import totalizator.app.models.Match;
import totalizator.app.models.MatchBet;
import totalizator.app.models.User;
import totalizator.app.services.utils.DateTimeService;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class MatchBetsServiceImpl implements MatchBetsService {

	public static final int STOP_BETTING_BEFORE_MATCH_BEGINNING_MIN = -120; // TODO: settings

	@Autowired
	private MatchBetRepository matchBetRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private MatchService matchService;

	@Autowired
	private DateTimeService dateTimeService;

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
	@Transactional( readOnly = true )
	public List<MatchBet> loadAll( final Cup cup, final User user ) {

		final List<MatchBet> bets = matchBetRepository.loadAll( user );

		CollectionUtils.filter( bets, new Predicate<MatchBet>() {
			@Override
			public boolean evaluate( final MatchBet matchBet ) {
				return matchBet.getMatch().getCup().equals( cup );
			}
		} );

		return bets;
	}

	@Override
	public MatchBet load( final User user, final Match match ) {
		return matchBetRepository.load( user, match );
	}

	@Override
	public MatchBet load( final int userId, final int matchId ) {
		return load( userService.load( userId ), matchService.load( matchId ) );
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

	@Override
	public int betsCount( final Match match ) {
		return matchBetRepository.betsCount( match );
	}

	@Override
	public boolean isMatchBettingAllowed( final Match match, final User user ) {

		if ( match.isMatchFinished() ) {
			return false;
		}

		return dateTimeService.getNow().isBefore( getMatchLastBettingSecond( match ) );
	}

	private LocalDateTime getMatchLastBettingSecond( final Match match ) {
		return dateTimeService.offset( match.getBeginningTime(), Calendar.MINUTE, STOP_BETTING_BEFORE_MATCH_BEGINNING_MIN );
	}
}

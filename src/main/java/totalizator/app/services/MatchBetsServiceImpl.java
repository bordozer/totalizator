package totalizator.app.services;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import totalizator.app.beans.ValidationResult;
import totalizator.app.dao.MatchBetRepository;
import totalizator.app.models.Cup;
import totalizator.app.models.Match;
import totalizator.app.models.MatchBet;
import totalizator.app.models.User;
import totalizator.app.services.utils.DateTimeService;
import totalizator.app.translator.Language;
import totalizator.app.translator.TranslatorService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MatchBetsServiceImpl implements MatchBetsService {

	public static final int STOP_BETTING_BEFORE_MATCH_BEGINNING_HOURS = 1; // TODO: mode to the settings

	@Autowired
	private MatchBetRepository matchBetRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private MatchService matchService;

	@Autowired
	private DateTimeService dateTimeService;

	@Autowired
	private TranslatorService translatorService;

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
	public ValidationResult validateBettingAllowed( final Match match, final User user ) {

		final Language language = Language.RU; // TODO: language!

		if ( match.getCup().isFinished() ) {
			return ValidationResult.fail( translatorService.translate( "Cup $1 is finished", language, match.getCup().getCupName() ) );
		}

		if ( ! match.getCup().isReadyForMatchBets() ) {
			return ValidationResult.fail( translatorService.translate( "Cup $1 is not open for game bets at this moment", language, match.getCup().getCupName() ) );
		}

		if ( match.isMatchFinished() ) {
			return ValidationResult.fail( translatorService.translate( "Match is finished", language ) );
		}

		if ( isTooLateForMatchBetting( match ) ) {
			return ValidationResult.fail( translatorService.translate( "It_s too late for betting. The betting was possible till $1", language, dateTimeService.formatDateTimeUI( getMatchLastBettingSecond( match ) ) ) );
		}

		return ValidationResult.pass();
	}

	@Override
	public boolean isBettingAllowed( final Match match, final User user ) {
		return validateBettingAllowed( match, user ).isPassed();
	}

	private boolean isTooLateForMatchBetting( final Match match ) {
		return dateTimeService.getNow().isAfter( getMatchLastBettingSecond( match ) );
	}

	private LocalDateTime getMatchLastBettingSecond( final Match match ) {
		return dateTimeService.minusHours( match.getBeginningTime(), STOP_BETTING_BEFORE_MATCH_BEGINNING_HOURS );
	}
}

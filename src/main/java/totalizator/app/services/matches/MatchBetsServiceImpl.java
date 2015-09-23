package totalizator.app.services.matches;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import totalizator.app.beans.ValidationResult;
import totalizator.app.dao.MatchBetDao;
import totalizator.app.models.*;
import totalizator.app.services.CupBetsService;
import totalizator.app.services.CupService;
import totalizator.app.services.UserGroupService;
import totalizator.app.services.UserService;
import totalizator.app.services.activiries.ActivityStreamService;
import totalizator.app.services.utils.DateTimeService;
import totalizator.app.translator.Language;
import totalizator.app.translator.TranslatorService;

import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

@Service
public class MatchBetsServiceImpl implements MatchBetsService {

	@Autowired
	private MatchBetDao matchBetRepository;

	@Autowired
	private CupService cupService;

	@Autowired
	private UserService userService;

	@Autowired
	private MatchService matchService;

	@Autowired
	private DateTimeService dateTimeService;

	@Autowired
	private TranslatorService translatorService;

	@Autowired
	private CupBetsService cupBetsService;

	@Autowired
	private UserGroupService userGroupService;

	@Autowired
	private ActivityStreamService activityStreamService;

	@Override
	@Transactional( readOnly = true )
	public List<MatchBet> loadAll() {
		return newArrayList( matchBetRepository.loadAll() );
	}

	@Override
	@Transactional( readOnly = true )
	public List<MatchBet> loadAll( final User user ) {
		return newArrayList( matchBetRepository.loadAll( user ) );
	}

	@Override
	@Transactional( readOnly = true )
	public List<MatchBet> loadAll( final Match match ) {
		return newArrayList( matchBetRepository.loadAll( match ) );
	}

	@Override
	@Transactional( readOnly = true )
	public List<MatchBet> loadAll( final Match match, final UserGroup userGroup ) {

		return loadAll( match ).stream().filter( new java.util.function.Predicate<MatchBet>() {

			@Override
			public boolean test( final MatchBet matchBet ) {
				return userGroupService.isUserMemberOfGroup( userGroup, matchBet.getUser() );
			}
		} ).collect( Collectors.toList() );
	}

	@Override
	@Transactional( readOnly = true )
	public List<MatchBet> loadAll( final Cup cup, final User user ) {

		final List<MatchBet> bets = loadAll( user );

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

		final int entryId = entry.getId();

		final MatchBet savedBet = matchBetRepository.load( entryId );

		final MatchBet matchBet = matchBetRepository.save( entry );

		if ( entryId == 0 ) {
			activityStreamService.matchBetCreated( matchBet );
		} else {
			final int oldScore1 = savedBet.getBetScore1();
			final int oldScore2 = savedBet.getBetScore2();

			activityStreamService.matchBetChanged( matchBet, oldScore1, oldScore2 );
		}

		return matchBet;
	}

	@Override
	@Transactional
	public void delete( final int id ) {

		final MatchBet matchBet = load( id );
		final Match match = matchBet.getMatch();
		final User user = matchBet.getUser();

		matchBetRepository.delete( id );

		activityStreamService.matchBetDeleted( user, match.getId(), matchBet.getBetScore1(), matchBet.getBetScore2() );
	}

	@Override
	public int betsCount( final Match match ) {
		return matchBetRepository.betsCount( match );
	}

	@Override
	public ValidationResult validateBettingAllowed( final Match match, final User user ) {

		final Language language = translatorService.getDefaultLanguage(); // TODO: read user language

		final Cup cup = match.getCup();

		if ( cupService.isCupFinished( cup ) ) {
			return ValidationResult.fail( translatorService.translate( "Cup $1 is finished", language, cup.getCupName() ) );
		}

		if ( cupBetsService.isMatchBettingFinished( cup ) ) {
			return ValidationResult.fail( translatorService.translate( "Cup $1 is not open for game bets at this moment", language, cup.getCupName() ) );
		}

		if ( matchService.isMatchFinished( match ) ) {
			return ValidationResult.fail( translatorService.translate( "Match finished", language ) );
		}

		if ( matchService.isMatchStarted( match ) ) {
			return ValidationResult.fail( translatorService.translate( "Match betting is not allowed after match start ( $1 )", language, dateTimeService.formatDateTimeUI( match.getBeginningTime() ) ) );
		}

		return ValidationResult.pass();
	}

	@Override
	public boolean canMatchBeBet( final Match match, final User user ) {
		return validateBettingAllowed( match, user ).isPassed();
	}

	@Override
	public boolean userCanSeeAnotherBets( final Match match, final User accessor ) {
		return matchService.isMatchStarted( match ) || matchService.isMatchFinished( match ); // TODO: yes, looks weird, I know
	}

	@Override
	public boolean isAllowedToShowMatchBets( final MatchBet matchBet, final User user ) {

		if ( matchBet.getUser().equals( user ) ) {
			return true;
		}

		return userCanSeeAnotherBets( matchBet.getMatch(), user );
	}
}

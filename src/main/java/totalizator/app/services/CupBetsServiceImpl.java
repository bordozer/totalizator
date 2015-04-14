package totalizator.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import totalizator.app.beans.ValidationResult;
import totalizator.app.dao.CupTeamBetRepository;
import totalizator.app.models.Cup;
import totalizator.app.models.CupTeamBet;
import totalizator.app.models.Team;
import totalizator.app.models.User;
import totalizator.app.services.utils.DateTimeService;
import totalizator.app.translator.Language;
import totalizator.app.translator.TranslatorService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CupBetsServiceImpl implements CupBetsService {

	private static final int STOP_BETTING_BEFORE_CUP_BEGINNING_HOURS = 1; // TODO: mode to the settings
	@Autowired
	private CupTeamBetRepository cupTeamBetRepository;

	@Autowired
	private DateTimeService dateTimeService;

	@Autowired
	private TranslatorService translatorService;

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

	@Override
	public boolean isCupBettingFinished( final Cup cup ) {
		return dateTimeService.getNow().isAfter( getCupLastBettingSecond( cup ) );
	}

	@Override
	public boolean isMatchBettingFinished( final Cup cup ) {
		return cup.isFinished();
	}

	@Override
	public ValidationResult validateBettingAllowed( final Cup cup, final User user ) {

		final Language language = Language.RU; // TODO: language!

		if ( cup.isFinished() ) {
			return ValidationResult.fail( translatorService.translate( "Cup $1 is finished", language, cup.getCupName() ) );
		}

		if ( isCupBettingFinished( cup ) ) {
			return ValidationResult.fail( translatorService.translate( "It_s too late for betting. The betting was possible till $1", language, dateTimeService.formatDateTimeUI( getCupLastBettingSecond( cup ) ) ) );
		}

		return ValidationResult.pass();
	}

	@Override
	public boolean canCupBeBet( final Cup cup, final User user ) {
		return validateBettingAllowed( cup, user ).isPassed();
	}

	private LocalDateTime getCupLastBettingSecond( final Cup cup ) {
		return dateTimeService.minusHours( cup.getCupStartTime(), STOP_BETTING_BEFORE_CUP_BEGINNING_HOURS );
	}
}

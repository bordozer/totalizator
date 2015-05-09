package totalizator.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import totalizator.app.beans.ValidationResult;
import totalizator.app.dao.CupTeamBetDao;
import totalizator.app.models.Cup;
import totalizator.app.models.CupTeamBet;
import totalizator.app.models.Team;
import totalizator.app.models.User;
import totalizator.app.services.utils.DateTimeService;
import totalizator.app.translator.Language;
import totalizator.app.translator.TranslatorService;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Service
public class CupBetsServiceImpl implements CupBetsService {


	@Autowired
	private CupService cupService;

	@Autowired
	private CupTeamBetDao cupTeamBetRepository;

	@Autowired
	private DateTimeService dateTimeService;

	@Autowired
	private TranslatorService translatorService;

	@Override
	@Transactional( readOnly = true )
	public List<CupTeamBet> loadAll() {
		return newArrayList( cupTeamBetRepository.loadAll() );
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
	public List<CupTeamBet> load( final Cup cup ) {
		return newArrayList( cupTeamBetRepository.load( cup ) );
	}

	@Override
	@Transactional( readOnly = true )
	public List<CupTeamBet> load( final Cup cup, final User user ) {
		return newArrayList( cupTeamBetRepository.load( cup, user ) );
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
		return cupService.isCupStarted( cup );
	}

	@Override
	public boolean isMatchBettingFinished( final Cup cup ) {
		return cupService.isCupFinished( cup );
	}

	@Override
	public ValidationResult validateBettingAllowed( final Cup cup ) {

		final Language language = Language.RU; // TODO: language!

		if ( cupService.isCupFinished( cup ) ) {
			return ValidationResult.fail( translatorService.translate( "Cup $1 is finished", language, cup.getCupName() ) );
		}

		if ( isCupBettingFinished( cup ) ) {
			return ValidationResult.fail( translatorService.translate( "Cup betting is not allowed after cup start ( $1 )", language, dateTimeService.formatDateTimeUI( cup.getCupStartTime() ) ) );
		}

		return ValidationResult.pass();
	}

	@Override
	public boolean canCupBeBet( final Cup cup, final User user ) {
		return validateBettingAllowed( cup ).isPassed();
	}
}

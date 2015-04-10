package totalizator.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import totalizator.app.dao.CupTeamBetRepository;
import totalizator.app.models.Cup;
import totalizator.app.models.CupTeamBet;
import totalizator.app.models.Team;
import totalizator.app.models.User;
import totalizator.app.services.utils.DateTimeService;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

@Service
public class CupBetsServiceImpl implements CupBetsService {

	private static final int STOP_BETTING_BEFORE_MATCH_BEGINNING_HOURS = 24;
	@Autowired
	private CupTeamBetRepository cupTeamBetRepository;

	@Autowired
	private DateTimeService dateTimeService;

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
	public boolean isCupBettingAllowed( final Cup cup, final User user ) {
		return dateTimeService.getNow().isBefore( getCupLastBettingSecond( cup ) );
	}

	private LocalDateTime getCupLastBettingSecond( final Cup cup ) {
		return dateTimeService.offset( cup.getCupStartTime(), Calendar.HOUR, STOP_BETTING_BEFORE_MATCH_BEGINNING_HOURS );
	}
}

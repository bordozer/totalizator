package totalizator.app.services;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import totalizator.app.dao.MatchBetRepository;
import totalizator.app.dto.BetDTO;
import totalizator.app.dto.MatchBetDTO;
import totalizator.app.dto.MatchDTO;
import totalizator.app.dto.UserDTO;
import totalizator.app.models.Cup;
import totalizator.app.models.Match;
import totalizator.app.models.MatchBet;
import totalizator.app.models.User;
import totalizator.app.services.utils.DateTimeService;

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
	public boolean isBettingAllowed( final Match match, final User user ) {

		if ( match.isMatchFinished() ) {
			return false;
		}

		final Date bettingIsAllowedTill = dateTimeService.offset( match.getBeginningTime(), Calendar.MINUTE, STOP_BETTING_BEFORE_MATCH_BEGINNING_MIN );
		return dateTimeService.getNow().getTime() < bettingIsAllowedTill.getTime();
	}

	@Override
	public List<MatchBetDTO> transform( final List<Match> matches, final User user ) {
		return Lists.transform( matches, new Function<Match, MatchBetDTO>() {
			@Override
			public MatchBetDTO apply( final Match match ) {
				return transform( match, user );
			}
		} );
	}

	@Override
	public MatchBetDTO transform( final Match match, final User user ) {

		final Function<Match, MatchBetDTO> function = new Function<Match, MatchBetDTO>() {

			@Override
			public MatchBetDTO apply( final Match match ) {
				final MatchDTO matchDTO = matchService.initDTOFromModel( match );

				final MatchBetDTO matchBetDTO = new MatchBetDTO( matchDTO );
				matchBetDTO.setBettingAllowed( isBettingAllowed( match, user ) );

				final MatchBet matchBet = load( user, match );

				if ( matchBet == null ) {
					return matchBetDTO;
				}

				final BetDTO betDTO = getBetDTO( matchBet, user );

				matchBetDTO.setBet( betDTO );

				return matchBetDTO;
			}
		};

		return function.apply( match );
	}

	@Override
	public BetDTO getBetDTO( final MatchBet matchBet, final User user ) {

		final MatchDTO matchDTO = matchService.initDTOFromModel( matchBet.getMatch() );

		final BetDTO betDTO = new BetDTO( matchDTO, new UserDTO( user ) );
		betDTO.setMatchBetId( matchBet.getId() );
		betDTO.setScore1( matchBet.getBetScore1() );
		betDTO.setScore2( matchBet.getBetScore2() );

		return betDTO;
	}
}

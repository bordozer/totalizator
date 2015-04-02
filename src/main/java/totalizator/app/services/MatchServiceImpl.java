package totalizator.app.services;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import totalizator.app.dao.MatchRepository;
import totalizator.app.dto.MatchDTO;
import totalizator.app.dto.MatchesBetSettingsDTO;
import totalizator.app.models.Cup;
import totalizator.app.models.Match;

import java.io.IOException;
import java.util.List;

@Service
public class MatchServiceImpl implements MatchService {

	@Autowired
	private MatchRepository matchRepository;

	@Autowired
	private CupService cupService;

	@Autowired
	private TeamService teamService;

	@Autowired
	private TeamLogoService teamLogoService;

	private static final Logger LOGGER = Logger.getLogger( MatchServiceImpl.class );

	@Override
	@Transactional( readOnly = true )
	public List<Match> loadAll() {
		return matchRepository.loadAll();
	}

	/*@Override
	@Transactional( readOnly = true )
	public List<Match> loadOpen() {
		return matchRepository.loadAll(); // TODO: filter finished
	}*/

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

	@Override
	public void initModelFromDTO( final MatchDTO matchDTO, final Match match ) {
		match.setCup( cupService.load( matchDTO.getCupId() ) );

		match.setTeam1( teamService.load( matchDTO.getTeam1Id() ) );
		match.setScore1( matchDTO.getScore1() );

		match.setTeam2( teamService.load( matchDTO.getTeam2Id() ) );
		match.setScore2( matchDTO.getScore2() );

		match.setBeginningTime( matchDTO.getBeginningTime() );

		match.setMatchFinished( matchDTO.isMatchFinished() );
	}

	@Override
	public MatchDTO initDTOFromModel( final Match match ) {
		final MatchDTO dto = new MatchDTO();

		dto.setMatchId( match.getId() );
		dto.setCategoryId( match.getCup().getCategory().getId() );
		dto.setCupId( match.getCup().getId() );

		dto.setTeam1Id( match.getTeam1().getId() );
		dto.setScore1( match.getScore1() );
		dto.setTeam1Logo( teamLogoService.getTeamLogoURL( match.getTeam1() ) );

		dto.setTeam2Id( match.getTeam2().getId() );
		dto.setScore2( match.getScore2() );
		dto.setTeam2Logo( teamLogoService.getTeamLogoURL( match.getTeam2() ) );

		dto.setBeginningTime( match.getBeginningTime() );

		dto.setMatchFinished( match.isMatchFinished() );

		return dto;
	}
}

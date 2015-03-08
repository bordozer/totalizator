package totalizator.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import totalizator.app.dao.MatchRepository;
import totalizator.app.dto.MatchDTO;
import totalizator.app.models.Match;

import java.util.List;

@Service
public class MatchServiceImpl implements MatchService {

	@Autowired
	private MatchRepository matchRepository;

	@Autowired
	private CupService cupService;

	@Autowired
	private TeamService teamService;

	@Override
	@Transactional( readOnly = true )
	public List<Match> loadAll() {
		return matchRepository.loadAll();
	}

	@Override
	@Transactional( readOnly = true )
	public List<Match> loadOpen() {
		return matchRepository.loadAll(); // TODO: filter finished
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
		match.setLastBetTime( matchDTO.getLastBetTime() );
	}

	@Override
	public MatchDTO initDTOFromModel( final Match match ) {
		final MatchDTO dto = new MatchDTO();

		dto.setMatchId( match.getId() );
		dto.setCategoryId( match.getCup().getCategory().getId() );
		dto.setCupId( match.getCup().getId() );

		dto.setTeam1Id( match.getTeam1().getId() );
		dto.setScore1( match.getScore1() );

		dto.setTeam2Id( match.getTeam2().getId() );
		dto.setScore2( match.getScore2() );

		dto.setLastBetTime( match.getLastBetTime() );

		return dto;
	}
}

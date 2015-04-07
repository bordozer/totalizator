package totalizator.app.services;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import totalizator.app.dto.*;
import totalizator.app.models.*;

import java.util.List;

@Service
public class DTOServiceImpl implements DTOService {

	@Autowired
	private TeamLogoService teamLogoService;

	@Override
	public UserDTO transformUser( final User user ) {
		return userFunction().apply( user );
	}

	@Override
	public List<UserDTO> transformUsers( final List<User> users ) {
		return Lists.transform( users, userFunction() );
	}

	@Override
	public CategoryDTO transformCategory( final Category category ) {
		return categoryFunction().apply( category );
	}

	@Override
	public List<CategoryDTO> transformCategories( final List<Category> users ) {
		return Lists.transform( users, categoryFunction() );
	}

	@Override
	public CupDTO transformCup( final Cup cup ) {
		return cupFunction().apply( cup );
	}

	@Override
	public List<CupDTO> transformCups( final List<Cup> cups ) {
		return Lists.transform( cups, cupFunction() );
	}

	@Override
	public TeamDTO transformTeam( final Team team ) {
		return teamFunction().apply( team );
	}

	@Override
	public List<TeamDTO> transformTeams( final List<Team> teams ) {
		return Lists.transform( teams, teamFunction() );
	}

	@Override
	public MatchDTO transformMatch( final Match match ) {
		return matchFunction().apply( match );
	}

	@Override
	public List<MatchDTO> transformMatch( final List<Match> matches ) {
		return Lists.transform( matches, matchFunction() );
	}

	private Function<User, UserDTO> userFunction() {

		return new Function<User, UserDTO>() {

			@Override
			public UserDTO apply( final User user ) {
				return new UserDTO( user );
			}
		};
	}

	private Function<Category, CategoryDTO> categoryFunction() {

		return new Function<Category, CategoryDTO>() {

			@Override
			public CategoryDTO apply( final Category category ) {
				return new CategoryDTO( category.getId(), category.getCategoryName() );
			}
		};
	}

	private Function<Cup, CupDTO> cupFunction() {

		return new Function<Cup, CupDTO>() {

			@Override
			public CupDTO apply( final Cup cup ) {
				final CupDTO cupDTO = new CupDTO( cup.getId(), cup.getCupName(), cup.getCategory().getId() );
				cupDTO.setShowOnPortalPage( cup.isShowOnPortalPage() );

				return cupDTO;
			}
		};
	}

	private Function<Team, TeamDTO> teamFunction() {

		return new Function<Team, TeamDTO>() {

			@Override
			public TeamDTO apply( final Team team ) {
				final Category category = team.getCategory();
				final String teamLogoURL = teamLogoService.getTeamLogoURL( team );

				return new TeamDTO( team.getId(), team.getTeamName(), category.getId(), teamLogoURL );
			}
		};
	}

	private Function<Match, MatchDTO> matchFunction() {

		return new Function<Match, MatchDTO>() {

			@Override
			public MatchDTO apply( final Match match ) {
				final MatchDTO dto = new MatchDTO();

				dto.setMatchId( match.getId() );
				dto.setCategoryId( match.getCup().getCategory().getId() );
				dto.setCupId( match.getCup().getId() );

				dto.setTeam1Id( match.getTeam1().getId() );
				dto.setScore1( match.getScore1() );

				dto.setTeam2Id( match.getTeam2().getId() );
				dto.setScore2( match.getScore2() );

				dto.setBeginningTime( match.getBeginningTime() );

				dto.setMatchFinished( match.isMatchFinished() );

				return dto;
			}
		};
	}
}

package totalizator.app.controllers.rest.scores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import totalizator.app.beans.points.UserCupPointsHolder;
import totalizator.app.beans.points.UserSummaryPointsHolder;
import totalizator.app.dto.UserDTO;
import totalizator.app.dto.points.UserMatchPointsHolderDTO;
import totalizator.app.models.Cup;
import totalizator.app.models.Match;
import totalizator.app.models.User;
import totalizator.app.services.CupService;
import totalizator.app.services.DTOService;
import totalizator.app.services.UserGroupService;
import totalizator.app.services.UserService;
import totalizator.app.services.matches.MatchService;
import totalizator.app.services.points.CupPointsService;
import totalizator.app.services.points.MatchPointsService;
import totalizator.app.services.utils.DateTimeService;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Maps.newLinkedHashMap;
import static com.google.inject.internal.util.$Lists.newArrayList;

@RestController
@RequestMapping( "/rest/cups/{cupId}" )
public class CupUsersScoresRestController {

	public static final int BARCHART_IN_TIME_DAYS = 7;

	@Autowired
	private UserService userService;

	@Autowired
	private CupService cupService;

	@Autowired
	private MatchService matchService;

	@Autowired
	private CupPointsService cupPointsService;

	@Autowired
	private UserGroupService userGroupService;

	@Autowired
	private DateTimeService dateTimeService;

	@Autowired
	private MatchPointsService matchPointsService;

	@Autowired
	private DTOService dtoService;

	@RequestMapping( method = RequestMethod.GET, value = "/scores/" )
	public CupUsersScoresDTO cupUsersScores( final @PathVariable( "cupId" ) int cupId, final @RequestParam( value = "userGroupId", required = false ) int userGroupId, final Principal principal ) {

		final User currentUser = userService.findByLogin( principal.getName() );
		final Cup cup = cupService.load( cupId );

		final CupUsersScoresDTO result = new CupUsersScoresDTO();

		result.setCurrentUser( dtoService.transformUser( currentUser ) );

		result.setUserRatingPositions( getUsersPoints( cup, userGroupId ) );

		return result;
	}

	@RequestMapping( method = RequestMethod.GET, value = "/scores/in-time/" )
	public CupUsersScoresInTimeDTO cupUsersScoresInTime( final @PathVariable( "cupId" ) int cupId, final @RequestParam( value = "userGroupId", required = false ) int userGroupId ) {

		final Cup cup = cupService.load( cupId );

		final List<Match> matches = matchService.loadAllFinished( cup.getId() );

		if ( matches == null || matches.size() == 0 ) {
			return new CupUsersScoresInTimeDTO( newArrayList(), newHashMap() );
		}

		final List<User> users = userGroupId != 0 ? userGroupService.loadUserGroupMembers( userGroupService.load( userGroupId ) ) : userService.loadAll();
		final List<LocalDate> dates = getBarchartDates( matches );

		final LocalDate startDate = matchService.getFirstMatch( cup ).getBeginningTime().toLocalDate();

		final Map<Integer, UserMatchesPointsDTO> map = newLinkedHashMap();

		users
				.stream()
				.forEach( new Consumer<User>() {
					@Override
					public void accept( final User user ) {

						final UserDTO userDTO = dtoService.transformUser( user );

						final List<UserMatchPointsHolderDTO> list = newArrayList();

						dates
								.stream()
								.forEach( new Consumer<LocalDate>() {
									@Override
									public void accept( final LocalDate date ) {
										final UserSummaryPointsHolder usersRatingOnDate = matchPointsService.getUserRating( user, cup, startDate, date );

										if ( usersRatingOnDate == null ) {
											list.add( new UserMatchPointsHolderDTO( userDTO, 0, 0 ) );
											return;
										}

										list.add( new UserMatchPointsHolderDTO( userDTO, usersRatingOnDate.getBetPoints(), usersRatingOnDate.getMatchBonus() ) );
									}
								} );

						if ( list.size() > 0 ) {
							map.put( user.getId(), new UserMatchesPointsDTO( userDTO, list ) );
						}
					}
				} );

		final List<String> datesString = dates
				.stream()
				.map( new Function<LocalDate, String>() {
					@Override
					public String apply( final LocalDate date ) {
						return dateTimeService.formatDate( date );
					}
				} )
				.collect( Collectors.toList() );

		final Iterator<Integer> iterator = map.keySet().iterator();
		while ( iterator.hasNext() ) {

			final int userId = iterator.next();

			final List<UserMatchPointsHolderDTO> userMatchPointsHolderDTOs = map.get( userId ).getUserMatchesPoints();

			final Double summaryPoints = userMatchPointsHolderDTOs
					.stream()
					.collect( Collectors.summingDouble( UserMatchPointsHolderDTO::getSummary ) );

			if ( summaryPoints == 0 ) {
				iterator.remove();
			}
		}

		return new CupUsersScoresInTimeDTO( datesString, map );
	}

	private List<LocalDate> getBarchartDates( final List<Match> matches ) {

		final List<LocalDate> dates = matches
				.stream()
				.map( new Function<Match, LocalDate>() {
					@Override
					public LocalDate apply( final Match match ) {
						return match.getBeginningTime().toLocalDate();
					}
				} )
				.distinct()
				.sorted( new Comparator<LocalDate>() {
					@Override
					public int compare( final LocalDate o1, final LocalDate o2 ) {
						return o1.compareTo( o2 );
					}
				} )
				.collect( Collectors.toList() );

		if ( dates.size() <= BARCHART_IN_TIME_DAYS ) {
			return dates;
		}

		return dates.subList( dates.size() - BARCHART_IN_TIME_DAYS, dates.size() );
	}

	private List<UserRatingPositionDTO> getUsersPoints( final Cup cup, final int userGroupId ) {

		return getUsersCupPoints( cup, userGroupId )
				.stream()
				.map( mapper() )
				.collect( Collectors.toList() );
	}

	private List<UserCupPointsHolder> getUsersCupPoints( final Cup cup, final int userGroupId ) {

		if ( userGroupId == 0 ) {
			return cupPointsService.getUsersCupPoints( cup );
		}

		return cupPointsService.getUsersCupPoints( cup, userGroupService.load( userGroupId ) );
	}

	private Function<UserCupPointsHolder, UserRatingPositionDTO> mapper() {

		return new Function<UserCupPointsHolder, UserRatingPositionDTO>() {

			@Override
			public UserRatingPositionDTO apply( final UserCupPointsHolder userCupPointsHolder ) {
				return new UserRatingPositionDTO( dtoService.transformUser( userCupPointsHolder.getUser() ), dtoService.transformCupPoints( userCupPointsHolder ) );
			}
		};
	}
}

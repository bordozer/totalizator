package totalizator.app.services.points;

import totalizator.app.beans.points.UserSummaryPointsHolder;
import totalizator.app.models.*;
import totalizator.app.services.GenericService;

import java.time.LocalDate;
import java.util.List;

public interface MatchPointsService extends GenericService<MatchPoints> {

	String CACHE_QUERY = "totalizator.app.cache.matchPointsService";

	void delete( final Match match );

	void delete( final UserGroup userGroup );

	void delete( final Cup cup );

	List<MatchPoints> loadAll( final Match match );

	List<MatchPoints> loadAll( final Match match, final UserGroup userGroup );

	MatchPoints load( final User user, final Match match );

	MatchPoints load( final User user, final Match match, final UserGroup userGroup );

	UserSummaryPointsHolder load( final User user, final Cup cup );

	UserSummaryPointsHolder load( final User user, final Cup cup, final UserGroup userGroup );

	List<UserSummaryPointsHolder> getUsersRating( final LocalDate date );

	List<UserSummaryPointsHolder> getUsersRating(final LocalDate dateFrom, final LocalDate dateTo);

	UserSummaryPointsHolder getUserRating( final User user, final Cup cup, final LocalDate date );

	UserSummaryPointsHolder getUserRating( final User user, final Cup cup, final LocalDate dateFrom, final LocalDate dateTo );
}

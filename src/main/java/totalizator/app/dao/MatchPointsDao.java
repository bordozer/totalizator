package totalizator.app.dao;

import totalizator.app.models.*;
import totalizator.app.services.GenericService;

import java.time.LocalDateTime;
import java.util.List;

public interface MatchPointsDao extends GenericService<MatchPoints> {

	String CACHE_ENTRY = "totalizator.app.cache.matchPoint";
	String CACHE_QUERY = "totalizator.app.cache.matchPoints";

	void delete( final Match match );

	void delete( final Cup cup );

	void delete( final UserGroup userGroup );

	List<MatchPoints> loadAll( final Match match );

	List<MatchPoints> loadAll( final Match match, final UserGroup userGroup );

	List<MatchPoints> loadAll( final LocalDateTime timeFrom, final LocalDateTime timeTo );

	MatchPoints load( final User user, final Match match );

	MatchPoints load( final User user, final Match match, final UserGroup userGroup );

	MatchSummaryPoints loadSummary( final User user, final Cup cup );

	MatchSummaryPoints loadSummary( final User user, final Cup cup, final UserGroup userGroup );

	MatchSummaryPoints loadSummary( final User user, final Cup cup, final LocalDateTime timeFrom, final LocalDateTime timeTo );
}

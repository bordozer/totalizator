package totalizator.app.services.matches;

import totalizator.app.models.Cup;
import totalizator.app.models.Match;
import totalizator.app.models.Team;
import totalizator.app.services.GenericService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface MatchService extends GenericService<Match> {

	List<Match> loadAll( final Cup cup );

	List<Match> loadAll( final int cupId, final int teamId );

	List<Match> loadAllNotFinished( final int cupId );

	List<Match> loadAllFinished( final int cupId );

	List<Match> loadAllFinished( final int cupId, final int teamId );

	List<Match> loadAll( final Team team );

	int getMatchCount( final Team team );

	boolean isMatchStarted( final Match match );

	boolean isMatchFinished( final Match match );

	Match find( final Cup cup, final Team team1, final Team team2, final LocalDateTime localDateTime );

	List<Match> loadAll( final Team team1, final Team team2 );

	List<Match> loadAll( final int cupId, final int team1Id, final int team2Id );

	List<Match> loadAllFinished( final int cupId, final int team1Id, final int team2Id );

	boolean isWinner( final Match match, final Team team );

	int getMatchCount( final Cup cup );

	int getMatchCount( final Cup cup, final Team team );

	int getFinishedMatchCount( final Cup cup, final Team team );

	int getWonMatchCount( final Cup cup, final Team team );

	int getFutureMatchCount( final Cup cup );

	int getFutureMatchCount( final Cup cup, final Team team );

	Match findByImportId( final String remoteGameId );

	List<Match> loadAllBetween( final LocalDateTime timeFrom, final LocalDateTime timeTo );

	List<Match> loadAllOnDate( final LocalDate date );

	List<Match> loadAllOnDate( final int cupId, final LocalDate date );

	List<Match> loadAllBetween( final int cupId, final LocalDateTime timeFrom, final LocalDateTime timeTo );

	List<Match> getMatchNotFinishedYetMatches( final Cup cup );

	Match getNearestFutureMatch( final Cup cup, final LocalDateTime onTime );

	Match getFirstMatch( final Cup cup );
}

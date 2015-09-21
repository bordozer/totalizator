package totalizator.app.services.matches.imports;

import java.time.LocalDateTime;

public class RemoteGame implements Comparable {

	private final String remoteGameId;

	private String remoteTeam1Id;
	private String remoteTeam1Name;
	private String remoteTeam2Id;
	private String remoteTeam2Name;

	private LocalDateTime beginningTime;

	private int score1;
	private int score2;

	private boolean finished;

	private int homeTeamNumber;

	private boolean loaded;

	public RemoteGame( final String remoteGameId ) {
		this.remoteGameId = remoteGameId;
	}

	public String getRemoteGameId() {
		return remoteGameId;
	}

	public String getRemoteTeam1Id() {
		return remoteTeam1Id;
	}

	public void setRemoteTeam1Id( final String remoteTeam1Id ) {
		this.remoteTeam1Id = remoteTeam1Id;
	}

	public String getRemoteTeam1Name() {
		return remoteTeam1Name;
	}

	public void setRemoteTeam1Name( final String remoteTeam1Name ) {
		this.remoteTeam1Name = remoteTeam1Name;
	}

	public String getRemoteTeam2Id() {
		return remoteTeam2Id;
	}

	public void setRemoteTeam2Id( final String remoteTeam2Id ) {
		this.remoteTeam2Id = remoteTeam2Id;
	}

	public String getRemoteTeam2Name() {
		return remoteTeam2Name;
	}

	public void setRemoteTeam2Name( final String remoteTeam2Name ) {
		this.remoteTeam2Name = remoteTeam2Name;
	}

	public LocalDateTime getBeginningTime() {
		return beginningTime;
	}

	public void setBeginningTime( final LocalDateTime beginningTime ) {
		this.beginningTime = beginningTime;
	}

	public void setScore1( final int score1 ) {
		this.score1 = score1;
	}

	public int getScore1() {
		return score1;
	}

	public void setScore2( final int score2 ) {
		this.score2 = score2;
	}

	public int getScore2() {
		return score2;
	}

	public void setFinished( final boolean finished ) {
		this.finished = finished;
	}

	public boolean isFinished() {
		return finished;
	}

	public int getHomeTeamNumber() {
		return homeTeamNumber;
	}

	public void setHomeTeamNumber( final int homeTeamNumber ) {
		this.homeTeamNumber = homeTeamNumber;
	}

	public boolean isLoaded() {
		return loaded;
	}

	public void setLoaded( final boolean loaded ) {
		this.loaded = loaded;
	}

	@Override
	public String toString() {
		return String.format( "#%s: %s vs %s ( %s )", remoteGameId, remoteTeam1Id, remoteTeam2Id, beginningTime );
	}

	@Override
	public int compareTo( final Object o ) {
		final RemoteGame remoteGame = ( RemoteGame ) o;
		return remoteGameId.compareTo( remoteGame.getRemoteGameId() );
	}
}

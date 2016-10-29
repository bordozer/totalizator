package betmen.core.entity.activities.events;

public class MatchEvent implements ActivityStreamEvent {

    private final int matchId;

    private final int score1;
    private final int score2;

    public MatchEvent(final int matchId, final int score1, final int score2) {
        this.matchId = matchId;
        this.score1 = score1;
        this.score2 = score2;
    }

    public int getMatchId() {
        return matchId;
    }

    public int getScore1() {
        return score1;
    }

    public int getScore2() {
        return score2;
    }
}

package betmen.core.entity.activities.events;

public class MatchBetEvent implements ActivityStreamEvent {

    private final int matchId;

    private final int score1;
    private final int score2;

    private final int oldScore1;
    private final int oldScore2;

    public MatchBetEvent(final int matchId, final int score1, final int score2, final int oldScore1, final int oldScore2) {

        this.matchId = matchId;

        this.score1 = score1;
        this.score2 = score2;

        this.oldScore1 = oldScore1;
        this.oldScore2 = oldScore2;
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

    public int getOldScore1() {
        return oldScore1;
    }

    public int getOldScore2() {
        return oldScore2;
    }
}

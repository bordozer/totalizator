package totalizator.app.controllers.rest.teams.standoffs;

import totalizator.app.dto.CupDTO;

public class TeamsLastGamesStat {

    private CupDTO cup;

    private int team1CurrentCupLastGames;
    private long team1CurrentCupLastGamesWon;

    private int team2CurrentCupLastGames;
    private long team2CurrentCupLastGamesWon;

    public CupDTO getCup() {
        return cup;
    }

    public void setCup(final CupDTO cup) {
        this.cup = cup;
    }

    public int getTeam1CurrentCupLastGames() {
        return team1CurrentCupLastGames;
    }

    public void setTeam1CurrentCupLastGames(final int team1CurrentCupLastGames) {
        this.team1CurrentCupLastGames = team1CurrentCupLastGames;
    }

    public long getTeam1CurrentCupLastGamesWon() {
        return team1CurrentCupLastGamesWon;
    }

    public void setTeam1CurrentCupLastGamesWon(final long team1CurrentCupLastGamesWon) {
        this.team1CurrentCupLastGamesWon = team1CurrentCupLastGamesWon;
    }

    public int getTeam2CurrentCupLastGames() {
        return team2CurrentCupLastGames;
    }

    public void setTeam2CurrentCupLastGames(final int team2CurrentCupLastGames) {
        this.team2CurrentCupLastGames = team2CurrentCupLastGames;
    }

    public long getTeam2CurrentCupLastGamesWon() {
        return team2CurrentCupLastGamesWon;
    }

    public void setTeam2CurrentCupLastGamesWon(final long team2CurrentCupLastGamesWon) {
        this.team2CurrentCupLastGamesWon = team2CurrentCupLastGamesWon;
    }
}

package betmen.dto.dto;

public class CupTeamBetDTO {

    private CupDTO cup;
    private TeamDTO team;
    private UserDTO user;

    private int cupPosition;
    private int points;

    private boolean stillActive;

    public CupDTO getCup() {
        return cup;
    }

    public void setCup(final CupDTO cup) {
        this.cup = cup;
    }

    public TeamDTO getTeam() {
        return team;
    }

    public void setTeam(final TeamDTO team) {
        this.team = team;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(final UserDTO user) {
        this.user = user;
    }

    public int getCupPosition() {
        return cupPosition;
    }

    public void setCupPosition(final int cupPosition) {
        this.cupPosition = cupPosition;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(final int points) {
        this.points = points;
    }

    public boolean isStillActive() {
        return stillActive;
    }

    public void setStillActive(final boolean stillActive) {
        this.stillActive = stillActive;
    }
}

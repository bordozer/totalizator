package betmen.rests.common.routes;

public enum BetRoutes implements Route {

    MATCHES_AND_BETS("/rest/matches/bets/") // M'n'B widget
    , MATCH_AND_USER_BET("/rest/matches/{matchId}/bet-of-user/{userId}/"), MATCH_BETS_COUNT("/{matchId}/bets/count/"), SAVE_BET("/{matchId}/bets/{score1}/{score2}/"), DELETE_BET("/{matchId}/bets/{matchBetId}");

    private final String route;

    BetRoutes(final String route) {
        this.route = route;
    }

    @Override
    public String getRoute() {
        return route;
    }
}

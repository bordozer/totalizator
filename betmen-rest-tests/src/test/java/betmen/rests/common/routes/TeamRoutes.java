package betmen.rests.common.routes;

public enum TeamRoutes implements Route {

    TEAM_CARD("/rest/teams/{teamId}/card/"),
    TEAM_CARD_STATISTICS("/rest/teams/{teamId}/cup/{cupId}/statistics/")
    ;

    private final String route;

    TeamRoutes(final String route) {
        this.route = route;
    }

    @Override
    public String getRoute() {
        return route;
    }
}

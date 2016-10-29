package betmen.rests.common.routes;

public enum TeamsRoutes implements Route {

    CUP_TEAMS("/rest/cups/{cupId}/teams/?active={active}")
    , CUP_TEAMS_STARTED_WITH_LETTER("/rest/cups/{cupId}/teams/?letter={letter}")
    ;

    private final String route;

    TeamsRoutes(final String route) {
        this.route = route;
    }

    @Override
    public String getRoute() {
        return route;
    }
}

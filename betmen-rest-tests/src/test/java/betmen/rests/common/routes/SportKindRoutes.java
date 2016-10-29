package betmen.rests.common.routes;

public enum SportKindRoutes implements Route {
    SPORT_KINDS_LIST("/rest/sport-kinds/"),
    SPORT_KIND("/rest/sport-kinds/{sportKindId}/")
    ;

    private final String route;

    SportKindRoutes(final String route) {
        this.route = route;
    }

    @Override
    public String getRoute() {
        return route;
    }
}

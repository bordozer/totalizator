package betmen.rests.common.routes;

public enum SystemRoutes implements Route {
    PORTAL_PAGE("/rest/portal-page/?portalPageDate={portalPageDate}"),
    TRANSLATOR("/rest/translator/?{translations}")
    ;

    private final String route;

    SystemRoutes(final String route) {
        this.route = route;
    }

    @Override
    public String getRoute() {
        return route;
    }
}

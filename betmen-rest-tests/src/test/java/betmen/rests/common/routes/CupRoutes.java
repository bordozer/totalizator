package betmen.rests.common.routes;

public enum CupRoutes implements Route {

    CUPS("/rest/cups/") // Endpoint is deprecated on BE
    , PUBLIC_CUPS("/rest/cups/public/")
    , PUBLIC_CURRENT_CUPS("/rest/cups/public/current/")
    , CUP("/rest/cups/{cupId}/")
    ;

    private final String route;

    CupRoutes(final String route) {
        this.route = route;
    }

    @Override
    public String getRoute() {
        return route;
    }
}

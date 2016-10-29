package betmen.rests.common.routes;

public enum AuthRoutes implements Route {
    LOGIN("/authenticate?login={login}&password={password}"),
    LOGOUT("/logout");

    private final String route;

    AuthRoutes(final String route) {
        this.route = route;
    }

    @Override
    public String getRoute() {
        return route;
    }
}

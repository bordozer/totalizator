package betmen.rests.common.routes;

public enum UserRoutes implements Route {

    USER_GET("/rest/users/{userId}/"),
    USER_CURRENT("/rest/users/current/"),
    USER_CREATE("/rest/users/create/"),
    USER_UPDATE("/rest/users/{userId}/"),
    USER_IS_ADMIN("/rest/users/{userId}/is-admin/"),
    USERS_LIST("/rest/users/");

    private final String route;

    UserRoutes(final String route) {
        this.route = route;
    }

    @Override
    public String getRoute() {
        return route;
    }
}

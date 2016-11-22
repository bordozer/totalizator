package betmen.rests.common.routes;

public enum UserRoutes implements Route {

    USER_GET("/rest/users/{userId}/"),
    USER_CURRENT("/rest/users/current/"),
    USER_CREATE("/rest/users/create/"),
    USER_UPDATE("/rest/users/{userId}/"),
    USER_IS_ADMIN("/rest/users/{userId}/is-admin/"),
    ALL_USERS("/rest/users/"),
    USER_LIST("/rest/users/list/")
    ;

    private final String route;

    UserRoutes(final String route) {
        this.route = route;
    }

    @Override
    public String getRoute() {
        return route;
    }
}

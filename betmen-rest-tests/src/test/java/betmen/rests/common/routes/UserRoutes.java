package betmen.rests.common.routes;

public enum UserRoutes implements Route {

    USER_GET("/rest/users/{userId}/"),
    USER_CURRENT("/rest/users/current/"),
    USER_CREATE("/rest/users/create/"),
    USER_UPDATE("/rest/users/{userId}/"),
    USER_CARD("/rest/users/{userId}/card/?onDate={onDate}"),
    USER_CARD_WITH_CUP("/rest/users/{userId}/card/?onDate={onDate}&cupId={cupId}"),
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

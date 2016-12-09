package betmen.rests.common.routes;

public enum UserRoutes implements Route {

    USER_CREATE("/rest/users/create/"),
    USER_SETTINGS_GET("/rest/users/settings/"),
    USER_SETTINGS_MODIFY("/rest/users/settings/"),
    USER_CARD("/rest/users/{userId}/card/?onDate={onDate}"),
    USER_CARD_WITH_CUP("/rest/users/{userId}/card/?onDate={onDate}&cupId={cupId}"),
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

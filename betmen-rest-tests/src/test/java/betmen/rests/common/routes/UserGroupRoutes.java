package betmen.rests.common.routes;

public enum UserGroupRoutes implements Route {
    CURRENT_USER_GROUPS("/rest/user-groups/management/"),
    USER_GROUP_CREATE("/rest/user-groups/management/0"),
    USER_GROUP_MODIFY("/rest/user-groups/management/{userGroupId}"),
    USER_GROUP_DELETE("/rest/user-groups/management/{userGroupId}"),
    ;

    private final String route;

    UserGroupRoutes(final String route) {
        this.route = route;
    }

    @Override
    public String getRoute() {
        return route;
    }
}

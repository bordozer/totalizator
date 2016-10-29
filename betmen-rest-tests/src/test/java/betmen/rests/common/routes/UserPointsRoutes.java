package betmen.rests.common.routes;

public enum UserPointsRoutes implements Route {

    CUP_POINTS("/rest/cups/{cupId}/scores/?userGroupId={userGroupId}"),
    CUP_POINTS_IN_TIME("/rest/cups/{cupId}/scores/in-time/?userGroupId={userGroupId}"),
    USERS_RATING("/rest/users/rating/?dateFrom={dateFrom}&dateTo={dateTo}")
    ;

    private final String route;

    UserPointsRoutes(final String route) {
        this.route = route;
    }

    @Override
    public String getRoute() {
        return route;
    }
}

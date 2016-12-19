package betmen.rests.common.routes;

public enum SystemRoutes implements Route {
    PORTAL_PAGE("/rest/portal-page/?portalPageDate={portalPageDate}"),
    TRANSLATOR("/rest/translator/?{translations}"),
    WHO_AM_I("/rest/app/who-am-i/"),
    AUTHENTICATED("/rest/app/authenticated/"),
    APP_CONTEXT("/rest/app/"),
    IS_CURRENT_USER_ADMIN("/rest/app/who-am-i/is-admin/"),
    PORTAL_PAGE_ACTIVITIES("/rest/activity-stream/portal/"),
    MATCH_ACTIVITIES("/rest/activity-stream/matches/{matchId}/"),
    USER_ACTIVITIES("/rest/activity-stream/users/{userId}/"),
    ACTIVITY_STREAM_CLEANUP("/admin/rest/common/activities/cleanup/"),
    FAVORITE_CATEGORIES_BETS_STATISTICS("/rest/portal-page/favorites/statistics/?onDate={onDate}"),
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

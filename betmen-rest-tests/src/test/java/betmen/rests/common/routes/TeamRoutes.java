package betmen.rests.common.routes;

public enum TeamRoutes implements Route {

    TEAM_CARD("/rest/teams/{teamId}/card/"),
    TEAM_CARD_STATISTICS("/rest/teams/{teamId}/cup/{cupId}/statistics/"),
    GET_TEAM("/rest/teams/{teamId}/"),
    GET_ALL_CATEGORY_TEAMS("/rest/teams/categories/{categoryId}/"),
    GET_ALL_CUP_TEAMS("/rest/teams/cup/{cupId}/"),
    GET_ALL_CUP_ACTIVE_TEAMS("/rest/teams/cup/{cupId}/active/"),
    ;

    private final String route;

    TeamRoutes(final String route) {
        this.route = route;
    }

    @Override
    public String getRoute() {
        return route;
    }
}

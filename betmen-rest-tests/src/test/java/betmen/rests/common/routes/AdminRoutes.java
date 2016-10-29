package betmen.rests.common.routes;

public enum AdminRoutes implements Route {

    SPORT_KINDS("/admin/rest/sport-kinds/")
    , SPORT_KIND_GET("/admin/rest/sport-kinds/{sportKindId}")
    , SPORT_KIND_CREATE("/admin/rest/sport-kinds/0")
    , SPORT_KIND_UPDATE("/admin/rest/sport-kinds/{sportKindId}")
    , SPORT_KIND_DELETE("/admin/rest/sport-kinds/{sportKindId}")

    , POINTS_CALCULATION_STRATEGIES("/admin/rest/points-calculation-strategies/")
    , POINTS_CALCULATION_STRATEGY_GET("/admin/rest/points-calculation-strategies/{pcsId}")
    , POINTS_CALCULATION_STRATEGY_CREATE("/admin/rest/points-calculation-strategies/0")
    , POINTS_CALCULATION_STRATEGY_UPDATE("/admin/rest/points-calculation-strategies/{pcsId}")
    , POINTS_CALCULATION_STRATEGY_DELETE("/admin/rest/points-calculation-strategies/{pcsId}")

    , CATEGORIES("/admin/rest/categories/")
    , CATEGORY_GET("/admin/rest/categories/{categoryId}")
    , CATEGORY_CREATE("/admin/rest/categories/0")
    , CATEGORY_UPDATE("/admin/rest/categories/{categoryId}")
    , CATEGORY_DELETE("/admin/rest/categories/{categoryId}")

    , TEAMS_OF_CATEGORY("/admin/rest/teams/categories/{categoryId}/")
    , TEAM_GET("/admin/rest/teams/{teamId}/")
    , TEAM_CREATE("/admin/rest/teams/0?cupId={cupId}")
    , TEAM_UPDATE("/admin/rest/teams/{teamId}")
    , TEAM_DELETE("/admin/rest/teams/{teamId}")
    , TEAM_CUP_ACTIVITY("/admin/rest/teams/{teamId}/cups/{cupId}/active/{active}/")

    , CUPS_EDIT("/admin/rest/cups/edit/")
    , CUP_EDIT_GET("/admin/rest/cups/edit/{cupId}")
    , CUP_CREATE("/admin/rest/cups/edit/0")
    , CUP_UPDATE("/admin/rest/cups/edit/{cupId}")
    , CUP_DELETE("/admin/rest/cups/edit/{cupId}")

    , CUPS_ALL("/admin/rest/cups/")
    , CUPS_ALL_CURRENT("/admin/rest/cups/current/")
    , CUPS_ALL_IMPORTABLE("/admin/rest/cups/configured-for-remote-games-import/?sportKindId={sportKindId}")
    , CUPS_ALL_CURRENT_IMPORTABLE("/admin/rest/cups/configured-for-remote-games-import/current/?sportKindId={sportKindId}")
    , CUPS_OF_CATEGORY("/admin/rest/cups/for-category/{categoryId}/")
    , CUP("/admin/rest/cups/{cupId}/")

    , MATCHES_AND_BETS_WIDGET("/admin/rest/matches/?" +
            "categoryId={categoryId}" +
            "&cupId={cupId}" +
            "&showFutureMatches={showFutureMatches}" +
            "&showFinished={showFinished}" +
            "&filterByDateEnable={filterByDateEnable}" +
            "&filterByDate={filterByDate}" +
            "&sorting={sorting}" +
            "&userId={userId}" +
            "&teamId={teamId}" +
            "&team2Id={team2Id}")
    , MATCH_GET("/admin/rest/matches/{matchId}")
    , MATCH_CREATE("/admin/rest/matches/0")
    , MATCH_UPDATE("/admin/rest/matches/{matchId}")
    , MATCH_DELETE("/admin/rest/matches/{matchId}")

    ;

    private final String route;

    AdminRoutes(final String route) {
        this.route = route;
    }

    @Override
    public String getRoute() {
        return route;
    }
}

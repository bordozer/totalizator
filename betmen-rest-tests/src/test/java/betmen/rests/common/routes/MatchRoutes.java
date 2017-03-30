package betmen.rests.common.routes;

public enum MatchRoutes implements Route {

    MATCHES_AND_BETS_WIDGET("/rest/matches/bets/?" +
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
    , GET_MATCH("/rest/matches/{matchId}/")
    , GET_CUP_MATCHES("/rest/matches/cup/{cupId}/")
    , GET_CUP_TEAMS_MATCHES("/rest/matches/cup/{cupId}/teams/{team1Id}/vs/{team2Id}/")
    , GET_CUP_TEAMS_MATCHES_FINISHED("/rest/matches/cup/{cupId}/teams/{team1Id}/vs/{team2Id}/finished/")
    , GET_MATCH_BET("/rest/matches/{matchId}/bet-of-user/{userId}/")
    , MAKE_MATCH_BET("/rest/matches/{matchId}/bets/{score1}/{score2}/")
    , DELETE_MATCH_BET("/rest/matches/{matchId}/bets/")
    , MATCH_MESSAGES("/rest/messages/matches/{matchId}/")
    , USER_MESSAGES("/rest/messages/users/{userId}/")
    , CREATE_MATCH_MESSAGES("/rest/messages/matches/0")
    , UPDATE_MATCH_MESSAGES("/rest/messages/matches/{matchId}")
    , DELETE_MATCH_MESSAGES("/rest/messages/matches/{matchId}")
    ;

    private final String route;

    MatchRoutes(final String route) {
        this.route = route;
    }

    @Override
    public String getRoute() {
        return route;
    }
}

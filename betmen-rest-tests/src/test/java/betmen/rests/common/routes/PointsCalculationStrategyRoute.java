package betmen.rests.common.routes;

public enum PointsCalculationStrategyRoute implements Route {

    POINTS_CALCULATION_STRATEGY_LIST("/rest/points-calculation-strategies/"),
    POINTS_CALCULATION_STRATEGY("/rest/points-calculation-strategies/{pcsId}/cups/");

    private final String route;

    PointsCalculationStrategyRoute(final String route) {
        this.route = route;
    }

    @Override
    public String getRoute() {
        return route;
    }
}

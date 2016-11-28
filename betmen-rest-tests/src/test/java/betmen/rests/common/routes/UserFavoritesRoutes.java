package betmen.rests.common.routes;

public enum UserFavoritesRoutes implements Route {
    GET_ALL_FAVORITE_CATEGORIES("/rest/favorites/categories/"),
    ADD_CATEGORY_TO_FAVORITES("/rest/favorites/categories/{categoryId}/"),
    REMOVE_CATEGORY_FROM_FAVORITES("/rest/favorites/categories/{categoryId}/"),
    ;

    private final String route;

    UserFavoritesRoutes(final String route) {
        this.route = route;
    }

    @Override
    public String getRoute() {
        return route;
    }
}

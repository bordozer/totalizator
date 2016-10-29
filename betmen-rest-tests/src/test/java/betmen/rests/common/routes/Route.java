package betmen.rests.common.routes;

public interface Route {

    String SERVER = System.getProperty("host", "http://localhost:9093");

    String getRoute();

    static String buildRoute(final Route route) {
        return String.format("%s%s", SERVER, route.getRoute());
    }
}

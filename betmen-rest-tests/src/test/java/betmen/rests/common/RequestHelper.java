package betmen.rests.common;

import betmen.rests.common.routes.Route;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.parsing.Parser;
import com.jayway.restassured.response.Response;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static betmen.rests.common.routes.Route.buildRoute;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.IsNot.not;

public class RequestHelper {

    private static final Logger LOGGER = Logger.getLogger(RequestHelper.class);

    public static Response doGet(final Route route) {
        return doGet(route, HttpStatus.SC_OK);
    }

    public static Response doGet(final Route route, final int expectedStatusCode) {
        return doGet(route, Collections.emptyMap(), expectedStatusCode);
    }

    public static Response doPlainPost(final Route route, final Map<String, Object> params) {
        return doPlainPost(route, params, HttpServletResponse.SC_OK);
    }

    public static Response doPlainPost(final Route route, final Map<String, Object> params, final int expectedStatusCode) {
        return doJsonPost(route, null, params, expectedStatusCode);
    }

    public static Response doJsonPost(final Route route, final Object dto) {
        return doJsonPost(route, dto, Collections.emptyMap(), HttpServletResponse.SC_OK);
    }

    public static Response doJsonPost(final Route route, final Object dto, final Map<String, Object> params) {
        return doJsonPost(route, dto, params, HttpServletResponse.SC_OK);
    }

    public static Response doJsonPut(final Route route, final Object dto) {
        return doJsonPut(route, dto, Collections.emptyMap(), HttpServletResponse.SC_OK);
    }

    public static Response doJsonPut(final Route route, final Object dto, final int expectedStatusCode) {
        return doJsonPut(route, dto, Collections.emptyMap(), expectedStatusCode);
    }

    public static Response doJsonPut(final Route route, final Object dto, final Map<String, Object> params) {
        return doJsonPut(route, dto, params, HttpServletResponse.SC_OK);
    }

    public static Response doDelete(final Route route, final Map<String, Object> params) {
        return doDelete(route, params, HttpServletResponse.SC_OK);
    }

    public static Response doGet(final Route route, final Map<String, Object> params, final int expectedStatusCode) {
        logRestTestName(route, null);
        return given()
                .when()
                .pathParameters(params)
                .response().then().statusCode(expectedStatusCode)
                .log().ifStatusCodeMatches(not(equalTo(expectedStatusCode)))
                .get(buildRoute(route));
    }

    public static Response doJsonPost(final Route route, final Object dto, final Map<String, Object> params, final int expectedStatusCode) {
        logRestTestName(route, dto);
        RestAssured.defaultParser = Parser.JSON;
        return given()
                .log()
                .ifValidationFails()
                .pathParameters(params)
                .contentType(ContentType.JSON)
                .body(toJson(dto))
                .when()
                .response().log().ifStatusCodeMatches(not(equalTo(expectedStatusCode)))
                .then().statusCode(expectedStatusCode)
                .post(buildRoute(route));
    }

    public static Response doJsonPut(final Route route, final Object dto, final Map<String, Object> params, final int expectedStatusCode) {
        logRestTestName(route, dto);
        RestAssured.defaultParser = Parser.JSON;
        return given()
                .log()
                .ifValidationFails()
                .pathParameters(params)
                .contentType(ContentType.JSON)
                .body(toJson(dto))
                .when()
                .response().log().ifStatusCodeMatches(not(equalTo(expectedStatusCode)))
                .then().statusCode(expectedStatusCode)
                .put(buildRoute(route));
    }

    public static Response doDelete(final Route route, final Map<String, Object> params, final int expectedStatusCode) {
        logRestTestName(route, null);
        return given()
                .header("Content-Type", "application/json")
                .log()
                .ifValidationFails()
                .pathParameters(params)
                .when()
                .response().log().ifStatusCodeMatches(not(equalTo(expectedStatusCode)))
                .then().statusCode(expectedStatusCode)
                .delete(buildRoute(route));
    }

    private static String toJson(final Object dto) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new DtoDateTimeSerializer());
        gsonBuilder.registerTypeAdapter(LocalDate.class, new DtoDateSerializer());
        Gson gson = gsonBuilder.create();
        return gson.toJson(dto);
    }

    private static void logRestTestName(final Route route, final Object dto) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement restTestElement = Arrays.stream(stackTrace)
                .filter(trace -> trace.getClassName().contains("betmen.rests.tests.SportKindsRestTest"))
                .findFirst()
                .orElse(null);
        if (restTestElement == null) {
//            LOGGER.debug("Rest test name cannot be determined from stack trace");
            return;
        }
        String dtoJson = dto != null ? toJson(dto) : StringUtils.EMPTY;
        LOGGER.debug(String.format("%s.%s(), route: '%s', json: %s",
                restTestElement.getFileName().replace(".java", ""),
                restTestElement.getMethodName(),
                route.getRoute(),
                dtoJson
        ));
    }
}

package betmen.rests.utils.helpers;

import betmen.dto.dto.AppDTO;
import betmen.dto.dto.UserDTO;
import betmen.rests.common.RequestHelper;
import betmen.rests.common.ResponseStatus;
import betmen.rests.common.routes.SystemRoutes;
import com.jayway.restassured.response.Response;

public class CommonEndPointHandler {

    public static final String WHO_AM_I_ANONYMOUS = "Anonymous";

    public static UserDTO whoAmI() {
        return RequestHelper.doGet(SystemRoutes.WHO_AM_I, ResponseStatus.OK.getCode()).as(UserDTO.class);
    }

    public static boolean isAuthenticated() {
        return RequestHelper.doGet(SystemRoutes.AUTHENTICATED, ResponseStatus.OK.getCode()).as(Boolean.class);
    }

    public static AppDTO getAppContext() {
        return RequestHelper.doGet(SystemRoutes.APP_CONTEXT, ResponseStatus.OK.getCode()).as(AppDTO.class);
    }

    public static boolean isCurrentUserAdmin() {
        return isCurrentUserAdmin(ResponseStatus.OK).as(Boolean.class);
    }

    public static Response isCurrentUserAdmin(final ResponseStatus responseStatus) {
        return RequestHelper.doGet(SystemRoutes.IS_CURRENT_USER_ADMIN, responseStatus.getCode());
    }
}

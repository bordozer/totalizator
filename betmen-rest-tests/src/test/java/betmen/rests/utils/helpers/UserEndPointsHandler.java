package betmen.rests.utils.helpers;

import betmen.dto.dto.UserDTO;
import betmen.dto.dto.UserListItemDTO;
import betmen.dto.edit.UserEditDTO;
import betmen.rests.common.RequestHelper;
import betmen.rests.common.ResponseStatus;
import betmen.rests.common.routes.UserRoutes;
import betmen.rests.utils.RestTestConstants;
import com.jayway.restassured.response.Response;
import org.apache.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class UserEndPointsHandler {

    public static UserEditDTO getUserEditData(final int userId) {
        return RequestHelper.doGet(UserRoutes.USER_GET, getParams(userId), HttpStatus.SC_OK).as(UserEditDTO.class);
    }

    public static UserEditDTO getCurrentUserEditData() {
        return RequestHelper.doGet(UserRoutes.USER_CURRENT, HttpStatus.SC_OK).as(UserEditDTO.class);
    }

    public static UserEditDTO updateUserData(final UserEditDTO dto) {
        return RequestHelper.doJsonPost(UserRoutes.USER_UPDATE, dto, getParams(dto.getUserId()), HttpStatus.SC_OK).as(UserEditDTO.class);
    }

    public static boolean isUserAdmin(final int userId) {
        return RequestHelper.doGet(UserRoutes.USER_IS_ADMIN, getParams(userId), HttpStatus.SC_OK).as(Boolean.class);
    }

    public static Map<String, Object> getParams(final int userId) {
        return Collections.singletonMap(RestTestConstants.USER_ID, userId);
    }

    public static List<UserDTO> getAllUsers() {
        return Arrays.asList(getAllUsers(ResponseStatus.OK).as(UserDTO[].class));
    }

    public static Response getAllUsers(final ResponseStatus responseStatus) {
        return RequestHelper.doGet(UserRoutes.ALL_USERS, responseStatus.getCode());
    }

    public static List<UserListItemDTO> getUserList() {
        return Arrays.asList(getUserList(ResponseStatus.OK).as(UserListItemDTO[].class));
    }

    public static Response getUserList(final ResponseStatus responseStatus) {
        return RequestHelper.doGet(UserRoutes.USER_LIST, responseStatus.getCode());

    }
}

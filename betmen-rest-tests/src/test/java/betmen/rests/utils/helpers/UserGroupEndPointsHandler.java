package betmen.rests.utils.helpers;

import betmen.dto.dto.UserGroupEditDTO;
import betmen.rests.common.RequestHelper;
import betmen.rests.common.ResponseStatus;
import betmen.rests.common.routes.UserGroupRoutes;
import betmen.rests.utils.RestTestConstants;
import com.jayway.restassured.response.Response;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class UserGroupEndPointsHandler {

    public static List<UserGroupEditDTO> getAllCurrentUserGroups() {
        return Arrays.asList(getAllCurrentUserGroups(ResponseStatus.OK).as(UserGroupEditDTO[].class));
    }

    public static Response getAllCurrentUserGroups(final ResponseStatus responseStatus) {
        return RequestHelper.doGet(UserGroupRoutes.CURRENT_USER_GROUPS, responseStatus.getCode());
    }

    public static UserGroupEditDTO create(final UserGroupEditDTO dto) {
        Assert.isTrue(dto.getUserGroupId() == 0, "ID should be zero");
        return create(dto, ResponseStatus.OK).as(UserGroupEditDTO.class);
    }

    public static Response create(final UserGroupEditDTO dto, final ResponseStatus expectedStatusCode) {
        Assert.isTrue(dto.getUserGroupId() == 0, "ID should be zero");
        return RequestHelper.doJsonPut(UserGroupRoutes.USER_GROUP_CREATE, dto, expectedStatusCode.getCode());
    }

    public static UserGroupEditDTO update(final UserGroupEditDTO dto) {
        Assert.isTrue(dto.getUserGroupId() > 0, "ID should be positive");
        return update(dto, ResponseStatus.OK).as(UserGroupEditDTO.class);
    }

    public static Response update(final UserGroupEditDTO dto, final ResponseStatus expectedStatusCode) {
        Assert.isTrue(dto.getUserGroupId() > 0, "ID should be positive");
        return RequestHelper.doJsonPut(UserGroupRoutes.USER_GROUP_MODIFY, dto, params(dto.getUserGroupId()), expectedStatusCode.getCode());
    }

    public static boolean delete(final int userGroupId) {
        return delete(userGroupId, ResponseStatus.OK).as(Boolean.class);
    }

    public static Response delete(final int userGroupId, final ResponseStatus expectedStatusCode) {
        return RequestHelper.doDelete(UserGroupRoutes.USER_GROUP_DELETE, params(userGroupId), expectedStatusCode.getCode());
    }

    private static Map<String, Object> params(final int userGroupId) {
        return Collections.singletonMap(RestTestConstants.USER_GROUP_ID, userGroupId);
    }
}

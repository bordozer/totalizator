package betmen.rests.utils.helpers;

import betmen.dto.dto.UserDTO;
import betmen.rests.common.RequestHelper;
import betmen.rests.common.ResponseStatus;
import betmen.rests.common.routes.SystemRoutes;

public class CommonEndPointHandler {

    public static final String WHO_AM_I_ANONYMOUS = "Anonymous";

    public static UserDTO whoAmI() {
        return RequestHelper.doGet(SystemRoutes.WHO_AM_I, ResponseStatus.OK.getCode()).as(UserDTO.class);
    }
}

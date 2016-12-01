package betmen.rests.utils.helpers;

import betmen.dto.dto.portal.PortalPageDTO;
import betmen.rests.common.RequestHelper;
import betmen.rests.common.ResponseStatus;
import betmen.rests.common.routes.SystemRoutes;
import betmen.rests.utils.RestTestConstants;

import java.util.Collections;
import java.util.Map;

public class PortalPageEndPointHandler {

    public static PortalPageDTO getPortalPageCups(final PortalPageDTO dto) {
        return RequestHelper.doGet(SystemRoutes.PORTAL_PAGE, portalPageParams(dto.getPortalPageDate()), ResponseStatus.OK.getCode()).as(PortalPageDTO.class);
    }

    private static Map<String, Object> portalPageParams(final String date) {
        return Collections.singletonMap(RestTestConstants.PORTAL_PAGE_DATE, date);
    }
}

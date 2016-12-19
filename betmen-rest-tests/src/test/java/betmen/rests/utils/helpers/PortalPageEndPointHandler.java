package betmen.rests.utils.helpers;

import betmen.dto.dto.portal.FavoriteCategoriesBetStatisticsDTO;
import betmen.dto.dto.portal.PortalPageDTO;
import betmen.rests.common.RequestHelper;
import betmen.rests.common.ResponseStatus;
import betmen.rests.common.routes.SystemRoutes;
import betmen.rests.utils.DateTimeUtils;
import betmen.rests.utils.RestTestConstants;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

public class PortalPageEndPointHandler {

    public static PortalPageDTO getPortalPageCups(final PortalPageDTO dto) {
        return RequestHelper.doGet(SystemRoutes.PORTAL_PAGE, portalPageParams(dto.getPortalPageDate()), ResponseStatus.OK.getCode()).as(PortalPageDTO.class);
    }

    public static FavoriteCategoriesBetStatisticsDTO getPortalPageFavoritesCategoriesBetsStatistics(final LocalDate date) {
        return RequestHelper.doGet(SystemRoutes.FAVORITE_CATEGORIES_BETS_STATISTICS, onDateParams(DateTimeUtils.formatDate(date)), ResponseStatus.OK.getCode())
                .as(FavoriteCategoriesBetStatisticsDTO.class);
    }

    private static Map<String, Object> portalPageParams(final String date) {
        return Collections.singletonMap(RestTestConstants.PORTAL_PAGE_DATE, date);
    }

    private static Map<String, Object> onDateParams(final String date) {
        return Collections.singletonMap(RestTestConstants.ON_DATE, date);
    }
}

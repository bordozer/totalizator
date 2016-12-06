package betmen.rests.utils.helpers;

import betmen.dto.dto.CategoryDTO;
import betmen.dto.dto.portal.PortalDefineFavoritesDTO;
import betmen.rests.common.RequestHelper;
import betmen.rests.common.ResponseStatus;
import betmen.rests.common.routes.UserFavoritesRoutes;
import betmen.rests.utils.ParameterUtils;
import com.jayway.restassured.response.Response;

import java.util.Arrays;
import java.util.List;

public class UserFavoritesEndPointsHandler {

    public static List<CategoryDTO> getAllFavoriteCategories() {
        return Arrays.asList(getAllFavoriteCategories(ResponseStatus.OK).as(CategoryDTO[].class));
    }

    public static Response getAllFavoriteCategories(final ResponseStatus responseStatus) {
        return RequestHelper.doGet(UserFavoritesRoutes.GET_ALL_FAVORITE_CATEGORIES, responseStatus.getCode());
    }

    public static void addCategoryToFavorites(final int categoryId) {
        addCategoryToFavorites(categoryId, ResponseStatus.OK);
    }

    public static Response addCategoryToFavorites(final int categoryId, final ResponseStatus responseStatus) {
        return RequestHelper.doPlainPost(UserFavoritesRoutes.ADD_CATEGORY_TO_FAVORITES, ParameterUtils.categoryParams(categoryId), responseStatus.getCode());
    }

    public static void removeCategoryFromFavorites(final int categoryId) {
        removeCategoryFromFavorites(categoryId, ResponseStatus.OK);
    }

    public static Response removeCategoryFromFavorites(final int categoryId, final ResponseStatus responseStatus) {
        return RequestHelper.doDelete(UserFavoritesRoutes.REMOVE_CATEGORY_FROM_FAVORITES, ParameterUtils.categoryParams(categoryId), responseStatus.getCode());
    }

    public static List<PortalDefineFavoritesDTO> defineFavorites() {
        return Arrays.asList(RequestHelper.doGet(UserFavoritesRoutes.DEFINE_FAVORITES, ResponseStatus.OK.getCode()).as(PortalDefineFavoritesDTO[].class));
    }
}

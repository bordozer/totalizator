package betmen.rests.utils.helpers.admin;

import betmen.dto.dto.admin.CategoryEditDTO;
import betmen.rests.common.RequestHelper;
import betmen.rests.common.routes.AdminRoutes;
import betmen.rests.utils.ParameterUtils;
import com.jayway.restassured.response.Response;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

public class AdminCategoryEndPointsHandler {

    private static final int SC_OK = HttpServletResponse.SC_OK;
    private static final String ID_SHOULD_BE_ZERO = "ID should be zero";
    private static final String ID_SHOULD_BE_POSITIVE = "ID should be positive";

    public static List<CategoryEditDTO> getItems() {
        return Arrays.asList(RequestHelper.doGet(AdminRoutes.CATEGORIES).as(CategoryEditDTO[].class));
    }

    public static CategoryEditDTO get(final int categoryId) {
        return RequestHelper.doGet(AdminRoutes.CATEGORY_GET, ParameterUtils.categoryParams(categoryId), SC_OK).as(CategoryEditDTO.class);
    }

    public static Response get(final int categoryId, final int expectedStatusCode) {
        return RequestHelper.doGet(AdminRoutes.CATEGORY_GET, ParameterUtils.categoryParams(categoryId), expectedStatusCode);
    }

    public static Response create(final CategoryEditDTO dto, final int expectedStatusCode) {
        Assert.isTrue(dto.getCategoryId() == 0, ID_SHOULD_BE_ZERO);
        return RequestHelper.doJsonPut(AdminRoutes.CATEGORY_CREATE, dto, expectedStatusCode);
    }

    public static CategoryEditDTO create(final CategoryEditDTO dto) {
        Assert.isTrue(dto.getCategoryId() == 0, ID_SHOULD_BE_ZERO);
        return RequestHelper.doJsonPut(AdminRoutes.CATEGORY_CREATE, dto).as(CategoryEditDTO.class);
    }

    public static CategoryEditDTO update(final CategoryEditDTO dto) {
        Assert.isTrue(dto.getCategoryId() > 0, ID_SHOULD_BE_POSITIVE);
        return RequestHelper.doJsonPut(AdminRoutes.CATEGORY_UPDATE, dto, ParameterUtils.categoryParams(dto.getCategoryId())).as(CategoryEditDTO.class);
    }

    public static Response update(final CategoryEditDTO dto, final int expectedStatusCode) {
        Assert.isTrue(dto.getCategoryId() > 0, ID_SHOULD_BE_POSITIVE);
        return RequestHelper.doJsonPut(AdminRoutes.CATEGORY_UPDATE, dto, ParameterUtils.categoryParams(dto.getCategoryId()), expectedStatusCode);
    }

    public static Response delete(final int categoryId, final int expectedStatusCode) {
        return RequestHelper.doDelete(AdminRoutes.CATEGORY_DELETE, ParameterUtils.categoryParams(categoryId), expectedStatusCode);
    }

    public static boolean delete(final int categoryId) {
        return RequestHelper.doDelete(AdminRoutes.CATEGORY_DELETE, ParameterUtils.categoryParams(categoryId), SC_OK).as(Boolean.class);
    }
}

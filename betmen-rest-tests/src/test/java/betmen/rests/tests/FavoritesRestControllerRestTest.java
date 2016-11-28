package betmen.rests.tests;

import betmen.dto.dto.CategoryDTO;
import betmen.dto.dto.admin.CategoryEditDTO;
import betmen.dto.dto.admin.PointsCalculationStrategyEditDTO;
import betmen.rests.common.ResponseStatus;
import betmen.rests.utils.ComparisonUtils;
import betmen.rests.utils.data.generator.AdminTestDataGenerator;
import betmen.rests.utils.helpers.AuthEndPointsHandler;
import betmen.rests.utils.helpers.UserFavoritesEndPointsHandler;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

public class FavoritesRestControllerRestTest {

    private static final int NOT_EXISTING_CATEGORY_ID = 7899887;
    private CategoryEditDTO category1;
    private CategoryEditDTO category2;
    private CategoryEditDTO category3;

    @BeforeClass
    public void classInit() {
        AuthEndPointsHandler.loginAsAdmin();
        PointsCalculationStrategyEditDTO pcs = AdminTestDataGenerator.createPointsStrategy();

        category1 = AdminTestDataGenerator.createCategory();
        category2 = AdminTestDataGenerator.createCategory();
        category3 = AdminTestDataGenerator.createCategory();
    }

    @BeforeMethod
    public void testInit() {
        AuthEndPointsHandler.logout();
    }

    @Test
    public void shouldNotAllowToOperateWithFavoritesForUnregisteredUser() {
        UserFavoritesEndPointsHandler.getAllFavoriteCategories(ResponseStatus.UNAUTHORIZED);

        UserFavoritesEndPointsHandler.addCategoryToFavorites(NOT_EXISTING_CATEGORY_ID, ResponseStatus.UNAUTHORIZED);
        UserFavoritesEndPointsHandler.addCategoryToFavorites(category1.getCategoryId(), ResponseStatus.UNAUTHORIZED);

        UserFavoritesEndPointsHandler.removeCategoryFromFavorites(NOT_EXISTING_CATEGORY_ID, ResponseStatus.UNAUTHORIZED);
        UserFavoritesEndPointsHandler.removeCategoryFromFavorites(category1.getCategoryId(), ResponseStatus.UNAUTHORIZED);
    }

    @Test
    public void shouldReturnEmptyListIfCurrentUserDoesNotHaveFavoriteCategories() {
        AuthEndPointsHandler.registerNewUserAndLogin();
        List<CategoryDTO> categories = UserFavoritesEndPointsHandler.getAllFavoriteCategories();
        assertThat(categories, notNullValue());
        assertThat(categories, hasSize(0));
    }

    @Test
    public void shouldAddExistingCategoryToFavorites() {
        AuthEndPointsHandler.registerNewUserAndLogin();

        UserFavoritesEndPointsHandler.addCategoryToFavorites(category1.getCategoryId());
        List<CategoryDTO> categories2 = UserFavoritesEndPointsHandler.getAllFavoriteCategories();
        assertThat(categories2, hasSize(1));
        ComparisonUtils.assertEqual(categories2.get(0), category1);
    }

    @Test
    public void shouldNotAddNotExistingCategoryToFavorites() {
        AuthEndPointsHandler.registerNewUserAndLogin();
        UserFavoritesEndPointsHandler.addCategoryToFavorites(NOT_EXISTING_CATEGORY_ID, ResponseStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldRemoveExistingFavoriteCategoryFromFavorites() {
        int categoryId = category2.getCategoryId();
        AuthEndPointsHandler.registerNewUserAndLogin();

        UserFavoritesEndPointsHandler.addCategoryToFavorites(categoryId);
        List<CategoryDTO> categories2 = UserFavoritesEndPointsHandler.getAllFavoriteCategories();
        assertThat(categories2, hasSize(1));
        ComparisonUtils.assertEqual(categories2.get(0), category2);

        UserFavoritesEndPointsHandler.removeCategoryFromFavorites(categoryId);
        List<CategoryDTO> categories3 = UserFavoritesEndPointsHandler.getAllFavoriteCategories();
        assertThat(categories3, hasSize(0));
    }

    @Test
    public void shouldNotRemoveNotExistingCategoryFromFavorites() {
        AuthEndPointsHandler.registerNewUserAndLogin();

        UserFavoritesEndPointsHandler.removeCategoryFromFavorites(NOT_EXISTING_CATEGORY_ID, ResponseStatus.UNPROCESSABLE_ENTITY);
        List<CategoryDTO> categories3 = UserFavoritesEndPointsHandler.getAllFavoriteCategories();
        assertThat(categories3, hasSize(0));
    }

    @Test
    public void shouldRemoveNotExistingFavoriteCategoryFromFavorites() {
        AuthEndPointsHandler.registerNewUserAndLogin();
        UserFavoritesEndPointsHandler.removeCategoryFromFavorites(category3.getCategoryId());
    }
}

package betmen.rests.stories;

import betmen.dto.dto.FavoriteCategoryDTO;
import betmen.dto.dto.admin.CategoryEditDTO;
import betmen.dto.dto.admin.SportKindEditDTO;
import betmen.dto.dto.portal.PortalDefineFavoritesDTO;
import betmen.rests.utils.ComparisonUtils;
import betmen.rests.utils.data.DataCleanUpUtils;
import betmen.rests.utils.data.generator.AdminTestDataGenerator;
import betmen.rests.utils.helpers.AuthEndPointsHandler;
import betmen.rests.utils.helpers.UserFavoritesEndPointsHandler;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

public class FavoritesCategoriesStory extends AbstractCommonStepsStory {

    private SportKindEditDTO sport1;
    private CategoryEditDTO sport1Category1;
    private CategoryEditDTO sport1Category2;

    private SportKindEditDTO sport2;
    private CategoryEditDTO sport2Category1;
    private CategoryEditDTO sport2Category2;
    private CategoryEditDTO sport2Category3;

    @Given("The system has no categories")
    public void beforeScenario() {
        DataCleanUpUtils.cleanupAll();
    }

    @When("New user registered and logged in")
    public void user1RegisteredAndLoggedIn() {
        AuthEndPointsHandler.registerNewUserAndLogin();
    }

    @Then("User requests categories and gets an empty list")
    public void userGetsEmptyListOfCategories() {
        List<PortalDefineFavoritesDTO> items = UserFavoritesEndPointsHandler.defineFavorites();
        assertThat(items, notNullValue());
        assertThat(items, hasSize(0));
    }

    @When("Creates one sport with two categories, another one with three ones and another one without ones")
    public void adminCreatesCategories() {
        AuthEndPointsHandler.loginAsAdmin();

        sport1 = AdminTestDataGenerator.createSport();
        sport1Category1 = AdminTestDataGenerator.createCategory(sport1.getSportKindId());
        sport1Category2 = AdminTestDataGenerator.createCategory(sport1.getSportKindId());

        sport2 = AdminTestDataGenerator.createSport();
        sport2Category1 = AdminTestDataGenerator.createCategory(sport2.getSportKindId());
        sport2Category2 = AdminTestDataGenerator.createCategory(sport2.getSportKindId());
        sport2Category3 = AdminTestDataGenerator.createCategory(sport2.getSportKindId());

        SportKindEditDTO sport3 = AdminTestDataGenerator.createSport(); // no categories
    }

    @When("User1 adds two categories to favorites")
    public void user1AddsTwoCategoriesToFavorites() {
        UserFavoritesEndPointsHandler.addCategoryToFavorites(sport1Category1.getCategoryId());
        UserFavoritesEndPointsHandler.addCategoryToFavorites(sport2Category3.getCategoryId());
    }

    @When("User2 adds two categories to favorites")
    public void user2AddsTwoCategoriesToFavorites() {
        UserFavoritesEndPointsHandler.addCategoryToFavorites(sport2Category1.getCategoryId());
        UserFavoritesEndPointsHandler.addCategoryToFavorites(sport2Category3.getCategoryId());
    }

    @Then("User1 requests categories and gets not empty list of system categories")
    public void user1RequestsCategories() {
        verifyCategories(true, false, true);
    }

    @Then("User2 requests categories and gets not empty list of system categories")
    public void user2RequestsCategories() {
        verifyCategories(false, true, true);
    }

    private void verifyCategories(final boolean isSport1Cat1Fav, final boolean isSport2Cat1Fav, final boolean isSport2Cat3Fav) {
        // when
        List<PortalDefineFavoritesDTO> items = UserFavoritesEndPointsHandler.defineFavorites();

        // then
        assertThat(items, notNullValue());
        assertThat(items, hasSize(2));

        PortalDefineFavoritesDTO sp1 = items.get(0);
        assertThat(sp1, notNullValue());
        ComparisonUtils.assertTheSame(sp1.getSport(), sport1);

        List<FavoriteCategoryDTO> categories1 = sp1.getCategories();
        assertThat(categories1, notNullValue());
        assertThat(categories1, hasSize(2));

        ComparisonUtils.assertTheSame(categories1.get(0), sport1Category1);
        ComparisonUtils.assertTheSame(categories1.get(1), sport1Category2);

        assertThat(categories1.get(0).isFavoriteCategory(), is(isSport1Cat1Fav));
        assertThat(categories1.get(1).isFavoriteCategory(), is(false));

        // ====

        PortalDefineFavoritesDTO sp2 = items.get(1);
        assertThat(sp2, notNullValue());
        ComparisonUtils.assertTheSame(sp2.getSport(), sport2);

        List<FavoriteCategoryDTO> categories2 = sp2.getCategories();
        assertThat(categories2, notNullValue());
        assertThat(categories2, hasSize(3));

        ComparisonUtils.assertTheSame(categories2.get(0), sport2Category1);
        ComparisonUtils.assertTheSame(categories2.get(1), sport2Category2);
        ComparisonUtils.assertTheSame(categories2.get(2), sport2Category3);

        assertThat(categories2.get(0).isFavoriteCategory(), is(isSport2Cat1Fav));
        assertThat(categories2.get(1).isFavoriteCategory(), is(false));
        assertThat(categories2.get(2).isFavoriteCategory(), is(isSport2Cat3Fav));
    }
}

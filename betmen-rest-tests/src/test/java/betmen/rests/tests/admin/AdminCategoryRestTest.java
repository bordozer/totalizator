package betmen.rests.tests.admin;

import betmen.dto.dto.admin.CategoryEditDTO;
import betmen.dto.dto.admin.SportKindEditDTO;
import betmen.dto.dto.error.FieldErrorsResponse;
import betmen.rests.utils.RandomUtils;
import betmen.rests.utils.data.builders.CategoryEditDtoBuilder;
import betmen.rests.utils.data.builders.SportKindEditDtoBuilder;
import betmen.rests.utils.data.generator.AdminTestDataGenerator;
import betmen.rests.utils.helpers.AuthEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminCategoryEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminSportEndPointsHandler;
import com.jayway.restassured.response.Response;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;

public class AdminCategoryRestTest {

    private static final int IMPORT_STRATEGY_TYPE_ID_1 = 1;
    public static final int IMPORT_STRATEGY_TYPE_ID_2 = 2;

    @BeforeMethod
    public void setup() {
        AuthEndPointsHandler.loginAsAdmin();
    }

    @AfterMethod
    public void afterTest() {
        AuthEndPointsHandler.logout();
    }

    @Test
    public void shouldCreateIfMandatoryDataProvided() {
        SportKindEditDTO sportKind = createSport();

        String name = RandomUtils.categoryName();
        CategoryEditDTO dto = new CategoryEditDTO();
        dto.setSportKindId(sportKind.getSportKindId());
        dto.setCategoryName(name);
        dto.setRemoteGameImportStrategyTypeId(IMPORT_STRATEGY_TYPE_ID_1); // TODO: move enum to DTO

        CategoryEditDTO category = AdminCategoryEndPointsHandler.create(dto);
        assertThat(category, notNullValue());
        assertThat(category.getCategoryId() > 0, is(Boolean.TRUE));
        assertThat(category.getSportKindId() > 0, is(Boolean.TRUE));
        assertThat(category.getCategoryName(), is(name));
        assertThat(category.getRemoteGameImportStrategyTypeId(), is(IMPORT_STRATEGY_TYPE_ID_1));

        final List<CategoryEditDTO> categories = getItems();
        assertThat(category, isIn(categories));
    }

    @Test
    public void shouldGetIfItemExists() {
        CategoryEditDTO created = AdminTestDataGenerator.createCategory();
        CategoryEditDTO got = AdminCategoryEndPointsHandler.get(created.getCategoryId());

        assertThat(got, notNullValue());
        assertThat(got.getSportKindId(), is(created.getSportKindId()));
        assertThat(got.getCategoryName(), is(created.getCategoryName()));
        assertThat(got.getRemoteGameImportStrategyTypeId(), is(created.getRemoteGameImportStrategyTypeId()));
        assertThat(got.getLogoUrl(), is(created.getLogoUrl()));
        assertThat(got.getCategoryImportId(), is(created.getCategoryImportId()));
    }

    @Test
    public void shouldGetItems() {
        final List<CategoryEditDTO> items = getItems();
        assertThat(items, notNullValue());
    }

    @Test
    public void shouldNotCreateIfNameIsEmpty() {
        assertNotSaved(StringUtils.EMPTY);
    }

    @Test
    public void shouldNotCreateIfNameIsBlank() {
        assertNotSaved(" ");
    }

    @Test
    public void shouldThr0wExceptionIfWrongEntityRequested() {
        AdminCategoryEndPointsHandler.get(10002, HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldNotCreateIfNameAlreadyExists() {
        SportKindEditDTO sportKind = createSport();

        String name = RandomUtils.categoryName();
        CategoryEditDTO dto1 = new CategoryEditDTO();
        dto1.setSportKindId(sportKind.getSportKindId());
        dto1.setCategoryName(name);

        CategoryEditDTO category1 = AdminCategoryEndPointsHandler.create(dto1);

        int sizeBefore = getItems().size();

        CategoryEditDTO dto2 = new CategoryEditDTO();
        dto2.setSportKindId(sportKind.getSportKindId());
        dto2.setCategoryName(name);

        AdminCategoryEndPointsHandler.create(dto1, HttpStatus.SC_UNPROCESSABLE_ENTITY);

        int sizeAfter = getItems().size();
        assertThat(sizeAfter, is(sizeBefore));
    }

    @Test
    public void shouldNotCreateIfSportIsWrong() {
        CategoryEditDTO dto1 = new CategoryEditDTO();
        dto1.setSportKindId(99999);
        dto1.setCategoryName(RandomUtils.categoryName());

        AdminCategoryEndPointsHandler.create(dto1, HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldNotUpdateIfNameAlreadyExists() {
        SportKindEditDTO sportKind = createSport();

        String name1 = RandomUtils.categoryName();

        CategoryEditDTO dto1 = new CategoryEditDTO();
        dto1.setSportKindId(sportKind.getSportKindId());
        dto1.setCategoryName(name1);

        CategoryEditDTO created1 = AdminCategoryEndPointsHandler.create(dto1);
        AdminCategoryEndPointsHandler.update(created1);

        CategoryEditDTO dto2 = new CategoryEditDTO();
        dto2.setSportKindId(sportKind.getSportKindId());
        dto2.setCategoryName(RandomUtils.categoryName());

        CategoryEditDTO created2 = AdminCategoryEndPointsHandler.create(dto2);
        created2.setCategoryName(name1);
        AdminCategoryEndPointsHandler.update(created2, HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldUpdateIfNameProvided() {
        SportKindEditDTO sport1 = createSport();
        SportKindEditDTO sport2 = createSport();

        String originalName = RandomUtils.categoryName();
        int originalSportId = sport1.getSportKindId();
        int originalImportStrategyTypeId = IMPORT_STRATEGY_TYPE_ID_1;
        String originalLogo = "some_logo";
        String originalImportId = "IMPORT_ID";

        CategoryEditDTO dto = new CategoryEditDTO();
        dto.setSportKindId(originalSportId);
        dto.setCategoryName(originalName);
        dto.setRemoteGameImportStrategyTypeId(originalImportStrategyTypeId);
        dto.setLogoUrl(originalLogo);
        dto.setCategoryImportId(originalImportId);

        CategoryEditDTO created = AdminCategoryEndPointsHandler.create(dto);
        int categoryId = created.getCategoryId();
        String logoPath = String.format("/resources/categories/%d/logo/", categoryId);

        assertThat(created, notNullValue());
        assertThat(created.getSportKindId(), is(originalSportId));
        assertThat(created.getCategoryName(), is(originalName));
        assertThat(created.getRemoteGameImportStrategyTypeId(), is(originalImportStrategyTypeId));
        assertThat(created.getLogoUrl(), is(logoPath));
        assertThat(created.getCategoryImportId(), nullValue());

        String newName = RandomUtils.categoryName();
        int newSportId = sport2.getSportKindId();
        int newImportStrategyTypeId = IMPORT_STRATEGY_TYPE_ID_2;
        String newLogo = "another_logo";
        String newImportId = "IMPORT_ID_2";

        created.setSportKindId(newSportId);
        created.setCategoryName(newName);
        created.setRemoteGameImportStrategyTypeId(newImportStrategyTypeId);
        created.setLogoUrl(newLogo);
        created.setCategoryImportId(newImportId);

        CategoryEditDTO updated = AdminCategoryEndPointsHandler.update(created);

        assertThat(updated, notNullValue());
        assertThat(updated.getSportKindId(), is(newSportId));
        assertThat(updated.getCategoryName(), is(newName));
        assertThat(updated.getRemoteGameImportStrategyTypeId(), is(newImportStrategyTypeId));
        assertThat(updated.getLogoUrl(), is(logoPath));
        assertThat(updated.getCategoryImportId(), nullValue());

        final List<CategoryEditDTO> items = getItems();
        assertThat(updated, isIn(items));
    }

    @Test
    public void shouldNotUpdateIfSavingFails() {
        SportKindEditDTO sport = createSport();

        String name = RandomUtils.categoryName();
        int sportKindId = sport.getSportKindId();
        int importStrategyTypeId1 = IMPORT_STRATEGY_TYPE_ID_1;
        String logo = "some_logo";
        String importId = "IMPORT_ID";

        CategoryEditDTO dto = new CategoryEditDTO();
        dto.setSportKindId(sportKindId);
        dto.setCategoryName(name);
        dto.setRemoteGameImportStrategyTypeId(importStrategyTypeId1);
        dto.setLogoUrl(logo);
        dto.setCategoryImportId(importId);

        CategoryEditDTO created = AdminCategoryEndPointsHandler.create(dto);
        int categoryId = created.getCategoryId();
        String logoPath = String.format("/resources/categories/%d/logo/", categoryId);

        created.setSportKindId(999);
        created.setCategoryName("");
        created.setRemoteGameImportStrategyTypeId(88);
        created.setLogoUrl("");
        created.setCategoryImportId("");

        Response response = AdminCategoryEndPointsHandler.update(created, HttpServletResponse.SC_BAD_REQUEST);

        CategoryEditDTO category = AdminCategoryEndPointsHandler.get(categoryId);

        assertThat(category, notNullValue());
        assertThat(category.getSportKindId(), is(sportKindId));
        assertThat(category.getCategoryName(), is(name));
        assertThat(category.getRemoteGameImportStrategyTypeId(), is(importStrategyTypeId1));
        assertThat(category.getLogoUrl(), is(logoPath));
        assertThat(category.getCategoryImportId(), nullValue());
    }

    @Test
    public void shouldNotDeleteIfCategoryIdIsZero() {
        assertThat(AdminCategoryEndPointsHandler.delete(0), is(Boolean.TRUE));
    }

    @Test(enabled = false)
    public void shouldNotDeleteIfCategoryIsAssignedToCup() {
        SportKindEditDTO sport = AdminSportEndPointsHandler.create(SportKindEditDtoBuilder.construct());
        CategoryEditDTO category = AdminCategoryEndPointsHandler.create(CategoryEditDtoBuilder.construct(sport.getSportKindId()));
        // TODO: create cup and assign category to it
        Response response = AdminCategoryEndPointsHandler.delete(category.getCategoryId(), HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldDeleteIfCategoryHasNoAssignedCups() {
        SportKindEditDTO sport = AdminSportEndPointsHandler.create(SportKindEditDtoBuilder.construct());
        CategoryEditDTO category = AdminCategoryEndPointsHandler.create(CategoryEditDtoBuilder.construct(sport.getSportKindId()));
        assertThat(AdminCategoryEndPointsHandler.delete(category.getCategoryId()), is(Boolean.TRUE));
    }

    @Test
    public void shouldNotDeleteIfWrongId() {
        AdminCategoryEndPointsHandler.delete(9999, HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    private void assertNotSaved(final String name) {
        int sizeBefore = getItems().size();

        CategoryEditDTO dto = new CategoryEditDTO();
        dto.setCategoryName(name);

        Response response = AdminCategoryEndPointsHandler.create(dto, HttpServletResponse.SC_BAD_REQUEST);
        FieldErrorsResponse errors = response.as(FieldErrorsResponse.class);
        assertThat(errors.containsError("categoryName", "errors.name_should_not_be_blank"), is(Boolean.TRUE));
        assertThat(errors.containsError("categoryName", "errors.name_has_wrong_length"), is(Boolean.TRUE));
        assertThat(errors.containsError("sportKindId", "must be greater than or equal to 1"), is(Boolean.TRUE));

        assertThat(getItems().size(), is(sizeBefore));
    }

    private SportKindEditDTO createSport() {
        return AdminTestDataGenerator.createSport();
    }

    private List<CategoryEditDTO> getItems() {
        return AdminCategoryEndPointsHandler.getItems();
    }
}

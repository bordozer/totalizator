package betmen.rests.tests.admin;

import betmen.dto.dto.admin.CategoryEditDTO;
import betmen.dto.dto.admin.SportKindEditDTO;
import betmen.dto.dto.error.FieldErrorsResponse;
import betmen.rests.common.ResponseStatus;
import betmen.rests.utils.RandomUtils;
import betmen.rests.utils.data.DataCleanUpUtils;
import betmen.rests.utils.data.generator.AdminTestDataGenerator;
import betmen.rests.utils.helpers.AuthEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminSportEndPointsHandler;
import com.jayway.restassured.response.Response;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;

public class AdminSportKindRestTest {

    @BeforeMethod
    public void setup() {
        AuthEndPointsHandler.loginAsAdmin();
    }

    @AfterMethod
    public void afterTest() {
        AuthEndPointsHandler.logout();
    }

    @Test
    public void shouldGetSports() {
        final List<SportKindEditDTO> items = getItems();
        assertThat(items, notNullValue());
        // TODO: check ordering
    }

    @Test
    public void shouldThr0wExceptionIfWrongEntityRequested() {
        AdminSportEndPointsHandler.get(10002, ResponseStatus.UNPROCESSABLE_ENTITY);
    }

    /*@Test
    public void shouldNotCreateIfNameIsNull() {
        assertNotSaved(null);
    }*/

    @Test
    public void shouldNotCreateIfNameIsEmpty() {
        assertNotSaved(StringUtils.EMPTY);
    }

    @Test
    public void shouldNotCreateIfNameIsBlank() {
        assertNotSaved(" ");
    }

    @Test
    public void shouldNotCreateIfNameAlreadyExists() {
        String name = RandomUtils.sportName();
        SportKindEditDTO dto1 = new SportKindEditDTO();
        dto1.setSportKindName(name);

        SportKindEditDTO sportKind = AdminSportEndPointsHandler.create(dto1);

        int sizeBefore = getItems().size();

        SportKindEditDTO dto2 = new SportKindEditDTO();
        dto2.setSportKindName(name);

        AdminSportEndPointsHandler.create(dto2, HttpStatus.SC_UNPROCESSABLE_ENTITY);

        int sizeAfter = getItems().size();
        assertThat(sizeAfter, is(sizeBefore));
    }

    @Test
    public void shouldCreateIfMandatoryDataProvided() {
        String name = RandomUtils.sportName();
        SportKindEditDTO dto = new SportKindEditDTO();
        dto.setSportKindName(name);

        SportKindEditDTO sportKind = AdminSportEndPointsHandler.create(dto);
        assertThat(sportKind, notNullValue());
        assertThat(sportKind.getSportKindId() > 0, is(Boolean.TRUE));
        assertThat(sportKind.getSportKindName(), is(name));

        final List<SportKindEditDTO> sports = getItems();
        assertThat(sportKind, isIn(sports));
    }

    @Test
    public void shouldNotUpdateIfNameAlreadyExists() {
        String name1 = RandomUtils.sportName();

        SportKindEditDTO dto1 = new SportKindEditDTO();
        dto1.setSportKindName(name1);

        SportKindEditDTO created1 = AdminSportEndPointsHandler.create(dto1);
        AdminSportEndPointsHandler.update(created1); // can update item

        SportKindEditDTO dto2 = new SportKindEditDTO();
        dto2.setSportKindName(RandomUtils.sportName());

        SportKindEditDTO created2 = AdminSportEndPointsHandler.create(dto2);
        created2.setSportKindName(name1); // set name 1
        AdminSportEndPointsHandler.update(created2, HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldUpdateIfNameProvided() {
        String oldName = RandomUtils.sportName();
        String newName = RandomUtils.sportName();

        SportKindEditDTO dto = new SportKindEditDTO();
        dto.setSportKindName(oldName);

        SportKindEditDTO created = AdminSportEndPointsHandler.create(dto);
        created.setSportKindName(newName);

        SportKindEditDTO updated = AdminSportEndPointsHandler.update(created);

        assertThat(updated, notNullValue());
        assertThat(updated.getSportKindId(), is(created.getSportKindId()));
        assertThat(updated.getSportKindName(), is(newName));

        final List<SportKindEditDTO> items = getItems();
        assertThat(updated, isIn(items));
    }

    @Test
    public void shouldNotUpdateIfSavingFails() {
        String oldName = RandomUtils.sportName();
        String emptyName = StringUtils.EMPTY;

        SportKindEditDTO dto = new SportKindEditDTO();
        dto.setSportKindName(oldName);

        SportKindEditDTO createdSport = AdminSportEndPointsHandler.create(dto);

        SportKindEditDTO editedSport = AdminSportEndPointsHandler.update(createdSport);
        editedSport.setSportKindId(createdSport.getSportKindId());
        editedSport.setSportKindName(emptyName);

        Response response = AdminSportEndPointsHandler.update(editedSport, HttpServletResponse.SC_BAD_REQUEST);
        FieldErrorsResponse errors = response.as(FieldErrorsResponse.class);
        assertThat(errors.containsError("sportKindName", "errors.name_should_not_be_blank"), is(Boolean.TRUE));
        assertThat(errors.containsError("sportKindName", "errors.name_has_wrong_length"), is(Boolean.TRUE));

        final List<SportKindEditDTO> sports = getItems();
        assertThat(sports, hasItem(createdSport));
    }

    @Test
    public void shouldNotUpdateIfWrongId() {
        int sizeBefore = getItems().size();

        SportKindEditDTO dto = new SportKindEditDTO();
        dto.setSportKindId(1024);
        dto.setSportKindName("Some name");

        Response sportKind = AdminSportEndPointsHandler.update(dto, HttpStatus.SC_UNPROCESSABLE_ENTITY);

        int sizeAfter = getItems().size();
        assertThat(sizeAfter, is(sizeBefore));
    }

    @Test
    public void shouldNotDeleteIfSportIdIsZero() {
        assertThat(AdminSportEndPointsHandler.delete(0), is(Boolean.TRUE));
    }

    @Test
    public void shouldNotDeleteIfSportIsAssignedToCategory() {
        CategoryEditDTO category = AdminTestDataGenerator.createCategory();
        AdminSportEndPointsHandler.delete(category.getSportKindId(), HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldDeleteIfSportHasNoAssignedCategories() {
        SportKindEditDTO sport = AdminTestDataGenerator.createSport();
        assertThat(AdminSportEndPointsHandler.delete(sport.getSportKindId()), is(Boolean.TRUE));
    }

    @Test
    public void shouldEvictCacheAfterDeletion() {
        DataCleanUpUtils.cleanupAll();
        AuthEndPointsHandler.loginAsAdmin();
        SportKindEditDTO sport = AdminTestDataGenerator.createSport();
        assertThat(AdminSportEndPointsHandler.delete(sport.getSportKindId()), is(Boolean.TRUE));
        AdminSportEndPointsHandler.get(sport.getSportKindId(), ResponseStatus.UNPROCESSABLE_ENTITY);
        List<SportKindEditDTO> items = AdminSportEndPointsHandler.getItems();
        assertThat(items, hasSize(0));
    }

    @Test
    public void shouldNotDeleteIfWrongId() {
        AdminSportEndPointsHandler.delete(9999, HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    private void assertNotSaved(final String name) {
        int sizeBefore = getItems().size();

        SportKindEditDTO dto = new SportKindEditDTO();
        dto.setSportKindName(name);

        Response response = AdminSportEndPointsHandler.create(dto, HttpServletResponse.SC_BAD_REQUEST);
        FieldErrorsResponse errors = response.as(FieldErrorsResponse.class);
        assertThat(errors.containsError("sportKindName", "errors.name_should_not_be_blank"), is(Boolean.TRUE));
        assertThat(errors.containsError("sportKindName", "errors.name_has_wrong_length"), is(Boolean.TRUE));

        int sizeAfter = getItems().size();
        assertThat(sizeAfter, is(sizeBefore));
    }

    private List<SportKindEditDTO> getItems() {
        return AdminSportEndPointsHandler.getItems();
    }

}

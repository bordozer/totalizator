package betmen.rests.tests.admin;

import betmen.dto.dto.error.FieldErrorsResponse;
import betmen.dto.dto.admin.CupEditDTO;
import betmen.dto.dto.admin.PointsCalculationStrategyEditDTO;
import betmen.rests.common.ResponseStatus;
import betmen.rests.utils.RandomUtils;
import betmen.rests.utils.data.DataCleanUpUtils;
import betmen.rests.utils.helpers.AuthEndPointsHandler;
import betmen.rests.utils.data.builders.PointsCalculationStrategyEditDtoBuilder;
import betmen.rests.utils.helpers.admin.AdminPointsStrategyEndPointsHandler;
import betmen.rests.utils.data.generator.AdminTestDataGenerator;
import com.jayway.restassured.response.Response;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class PointsCalculationStrategyRestTest {

    @BeforeMethod
    public void setup() {
        AuthEndPointsHandler.loginAsAdmin();
    }

    @AfterMethod
    public void afterTest() {
        AuthEndPointsHandler.logout();
    }

    @Test
    public void shouldGetItems() {
        final List<PointsCalculationStrategyEditDTO> items = getItems();
        assertThat(items, notNullValue());
        // TODO: check ordering
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
        AdminPointsStrategyEndPointsHandler.get(10002, ResponseStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldNotCreateIfNameAlreadyExists() {
        PointsCalculationStrategyEditDTO dto = createPointsStrategy();

        PointsCalculationStrategyEditDTO anotherDto = construct();
        anotherDto.setStrategyName(dto.getStrategyName());

        AdminPointsStrategyEndPointsHandler.create(anotherDto, HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldCreateIfMandatoryDataProvided() {
        PointsCalculationStrategyEditDTO dto = construct();

        PointsCalculationStrategyEditDTO result = AdminPointsStrategyEndPointsHandler.create(dto);

        assertThat(result, notNullValue());
        assertThat(result.getPcsId() > 0, is(true));
        assertThat(result.getStrategyName(), is(dto.getStrategyName()));
        assertThat(result.getPointsDelta(), is(dto.getPointsDelta()));
        assertThat(result.getPointsForBetWithinDelta(), is(dto.getPointsForBetWithinDelta()));
        assertThat(result.getPointsForMatchScore(), is(dto.getPointsForMatchScore()));
        assertThat(result.getPointsForMatchWinner(), is(dto.getPointsForMatchWinner()));
    }

    @Test
    public void shouldNotUpdateIfNameAlreadyExists() {
        PointsCalculationStrategyEditDTO dto = createPointsStrategy();

        PointsCalculationStrategyEditDTO anotherDto = createPointsStrategy();
        anotherDto.setStrategyName(dto.getStrategyName());
        AdminPointsStrategyEndPointsHandler.update(anotherDto, HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldUpdateIfNameProvided() {
        PointsCalculationStrategyEditDTO result1 = createPointsStrategy();

        String strategyName = RandomUtils.pointsStrategyName();
        PointsCalculationStrategyEditDTO anotherDto = construct();
        anotherDto.setPcsId(result1.getPcsId());
        anotherDto.setStrategyName(strategyName);
        anotherDto.setPointsDelta(1);
        anotherDto.setPointsForBetWithinDelta(2);
        anotherDto.setPointsForMatchScore(3);
        anotherDto.setPointsForMatchWinner(4);

        PointsCalculationStrategyEditDTO result = AdminPointsStrategyEndPointsHandler.update(anotherDto);

        assertThat(result, notNullValue());
        assertThat(result.getStrategyName(), is(strategyName));
        assertThat(result.getPointsDelta(), is(1));
        assertThat(result.getPointsForBetWithinDelta(), is(2));
        assertThat(result.getPointsForMatchScore(), is(3));
        assertThat(result.getPointsForMatchWinner(), is(4));
    }

    @Test
    public void deleteShouldReturnTrueIfIdIsZero() {
        assertThat(AdminPointsStrategyEndPointsHandler.delete(0), is(Boolean.TRUE));
    }

    @Test
    public void shouldNotDeleteIfAssignedToCups() {
        CupEditDTO cup = AdminTestDataGenerator.createRandomCup();
        AdminPointsStrategyEndPointsHandler.delete(cup.getCupPointsCalculationStrategyId(), HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldDeleteIfNotAssignedToCups() {
        PointsCalculationStrategyEditDTO result1 = createPointsStrategy();
        assertThat(AdminPointsStrategyEndPointsHandler.delete(result1.getPcsId()), is(Boolean.TRUE));
    }

    @Test
    public void shouldEvictCacheAfterDeletion() {
        DataCleanUpUtils.cleanupAll();
        AuthEndPointsHandler.loginAsAdmin();
        PointsCalculationStrategyEditDTO dto = createPointsStrategy();
        assertThat(AdminPointsStrategyEndPointsHandler.delete(dto.getPcsId()), is(Boolean.TRUE));
        AdminPointsStrategyEndPointsHandler.get(dto.getPcsId(), ResponseStatus.UNPROCESSABLE_ENTITY);
        List<PointsCalculationStrategyEditDTO> items = AdminPointsStrategyEndPointsHandler.getItems();
        assertThat(items, hasSize(0));
    }

    @Test
    public void shouldNotDeleteIfWrongId() {
        AdminPointsStrategyEndPointsHandler.delete(9999, HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    private void assertNotSaved(final String name) {
        int sizeBefore = getItems().size();

        PointsCalculationStrategyEditDTO dto = new PointsCalculationStrategyEditDTO();
        dto.setStrategyName(name);

        Response response = AdminPointsStrategyEndPointsHandler.create(dto, HttpServletResponse.SC_BAD_REQUEST);
        FieldErrorsResponse errors = response.as(FieldErrorsResponse.class);
        assertThat(errors.containsError("strategyName", "errors.name_should_not_be_blank"), is(Boolean.TRUE));
        assertThat(errors.containsError("strategyName", "errors.name_has_wrong_length"), is(Boolean.TRUE));
        assertThat(errors.containsError("pointsForMatchScore", "must be greater than or equal to 1"), is(Boolean.TRUE));
        assertThat(errors.containsError("pointsForMatchWinner", "must be greater than or equal to 1"), is(Boolean.TRUE));

        assertThat(getItems().size(), is(sizeBefore));
    }

    private List<PointsCalculationStrategyEditDTO> getItems() {
        return AdminPointsStrategyEndPointsHandler.getItems();
    }

    private PointsCalculationStrategyEditDTO createPointsStrategy() {
        return AdminTestDataGenerator.createPointsStrategy();
    }

    private PointsCalculationStrategyEditDTO construct() {
        return PointsCalculationStrategyEditDtoBuilder.construct();
    }
}

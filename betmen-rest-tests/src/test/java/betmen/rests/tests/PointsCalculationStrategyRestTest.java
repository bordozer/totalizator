package betmen.rests.tests;

import betmen.dto.dto.CupDTO;
import betmen.dto.dto.PointsCalculationStrategyDTO;
import betmen.dto.dto.admin.CategoryEditDTO;
import betmen.dto.dto.admin.CupEditDTO;
import betmen.dto.dto.admin.PointsCalculationStrategyEditDTO;
import betmen.rests.common.ResponseStatus;
import betmen.rests.utils.RandomUtils;
import betmen.rests.utils.data.DataCleanUpUtils;
import betmen.rests.utils.data.builders.PointsCalculationStrategyEditDtoBuilder;
import betmen.rests.utils.data.generator.AdminTestDataGenerator;
import betmen.rests.utils.data.templater.CupTemplater;
import betmen.rests.utils.helpers.AuthEndPointsHandler;
import betmen.rests.utils.helpers.PointsCalculationStrategyEndpointHandler;
import betmen.rests.utils.helpers.admin.AdminCupEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminPointsStrategyEndPointsHandler;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;

public class PointsCalculationStrategyRestTest {

    @BeforeMethod
    public void testcaseInit() {
        AuthEndPointsHandler.logout();
    }

    @Test
    public void shouldNotAllowToGetAccessToListForAnonymousUser() {
        PointsCalculationStrategyEndpointHandler.getPointsCalculationStrategies(ResponseStatus.UNAUTHORIZED);
    }

    @Test
    public void shouldNotAllowToGetAccessToItemForAnonymousUser() {
        PointsCalculationStrategyEndpointHandler.getCupsOf(0, ResponseStatus.UNAUTHORIZED);
    }

    @Test
    public void shouldReturn422IfWrongItemIdProvided() {
        AuthEndPointsHandler.loginAsAdmin();
        PointsCalculationStrategyEndpointHandler.getCupsOf(-12, ResponseStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldGetListSortedByName() {
        // given
        DataCleanUpUtils.cleanupAll();

        AuthEndPointsHandler.loginAsAdmin();

        PointsCalculationStrategyEditDTO edit1 = AdminPointsStrategyEndPointsHandler.create(new PointsCalculationStrategyEditDtoBuilder()
                .withName(String.format("3-%s", RandomUtils.pointsStrategyName()))
                .withPointsDelta(1)
                .withPointsForBetWithinDelta(2)
                .withPointsForMatchScore(3)
                .withPointsForMatchWinner(4)
                .build());
        PointsCalculationStrategyEditDTO edit2 = AdminPointsStrategyEndPointsHandler.create(new PointsCalculationStrategyEditDtoBuilder()
                .withName(String.format("1-%s", RandomUtils.pointsStrategyName()))
                .withPointsDelta(1)
                .withPointsForBetWithinDelta(2)
                .withPointsForMatchScore(3)
                .withPointsForMatchWinner(4)
                .build());
        PointsCalculationStrategyEditDTO edit3 = AdminPointsStrategyEndPointsHandler.create(new PointsCalculationStrategyEditDtoBuilder()
                .withName(String.format("2-%s", RandomUtils.pointsStrategyName()))
                .withPointsDelta(1)
                .withPointsForBetWithinDelta(2)
                .withPointsForMatchScore(3)
                .withPointsForMatchWinner(4)
                .build());

        // when
        List<PointsCalculationStrategyDTO> list = PointsCalculationStrategyEndpointHandler.getPointsCalculationStrategies();

        // then
        assertThat(list, notNullValue());
        assertThat(list, hasSize(3));
        assertThat(list.get(0).getPcsId(), is(edit2.getPcsId()));
        assertThat(list.get(1).getPcsId(), is(edit3.getPcsId()));
        assertThat(list.get(2).getPcsId(), is(edit1.getPcsId()));

        PointsCalculationStrategyDTO created = find(list, edit1.getPcsId());
        assertThat(created, notNullValue());
        assertThat(created.getStrategyName(), is(edit1.getStrategyName()));

        // given
        String newName = RandomUtils.UUID();
        edit1.setStrategyName(newName);

        AdminPointsStrategyEndPointsHandler.update(edit1);

        // when
        List<PointsCalculationStrategyDTO> list1 = PointsCalculationStrategyEndpointHandler.getPointsCalculationStrategies();

        // then
        assertThat(list1, notNullValue());
        PointsCalculationStrategyDTO userDTO1 = find(list1, edit1.getPcsId());
        assertThat(userDTO1, notNullValue());
        assertThat(userDTO1.getStrategyName(), is(newName));
    }

    @Test
    public void shouldGetPointsStrategyCupsSortedByStartTime() {
        // given
        DataCleanUpUtils.cleanupAll();
        AuthEndPointsHandler.loginAsAdmin();

        PointsCalculationStrategyEditDTO edit = AdminPointsStrategyEndPointsHandler.create(new PointsCalculationStrategyEditDtoBuilder()
                .withName(RandomUtils.pointsStrategyName())
                .withPointsDelta(1)
                .withPointsForBetWithinDelta(2)
                .withPointsForMatchScore(3)
                .withPointsForMatchWinner(4)
                .build());

        // when
        List<CupDTO> cups = PointsCalculationStrategyEndpointHandler.getCupsOf(edit.getPcsId());

        // then
        assertThat(cups, notNullValue());
        assertThat(cups, hasSize(0));

        CategoryEditDTO category = AdminTestDataGenerator.createCategory();
        CupEditDTO cupEdit1 = AdminCupEndPointsHandler.create(CupTemplater.random(category.getCategoryId(), edit.getPcsId()).inDays(3).privateCup().build());
        CupEditDTO cupEdit2 = AdminCupEndPointsHandler.create(CupTemplater.random(category.getCategoryId(), edit.getPcsId()).inDays(1).build());
        CupEditDTO cupEdit3 = AdminCupEndPointsHandler.create(CupTemplater.random(category.getCategoryId(), AdminTestDataGenerator.createPointsStrategy().getPcsId()).build());
        CupEditDTO cupEdit4 = AdminCupEndPointsHandler.create(CupTemplater.random(category.getCategoryId(), edit.getPcsId()).inDays(2).build());

        List<CupDTO> cups1 = PointsCalculationStrategyEndpointHandler.getCupsOf(edit.getPcsId());
        assertThat(cups1, notNullValue());
        assertThat(cups1, hasSize(3));
        assertThat(cups1.get(0).getCupId(), is(cupEdit1.getCupId()));
        assertThat(cups1.get(1).getCupId(), is(cupEdit4.getCupId()));
        assertThat(cups1.get(2).getCupId(), is(cupEdit2.getCupId()));
    }

    private PointsCalculationStrategyDTO find(final List<PointsCalculationStrategyDTO> userList, final int pcsId) {
        return userList.stream().filter(userDto -> userDto.getPcsId() == pcsId).findFirst().orElse(null);
    }
}

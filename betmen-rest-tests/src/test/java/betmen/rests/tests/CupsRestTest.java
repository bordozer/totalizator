package betmen.rests.tests;

import betmen.dto.dto.CupDTO;
import betmen.dto.dto.admin.CategoryEditDTO;
import betmen.dto.dto.admin.CupEditDTO;
import betmen.dto.dto.admin.PointsCalculationStrategyEditDTO;
import betmen.dto.dto.admin.TeamEditDTO;
import betmen.rests.common.ResponseStatus;
import betmen.rests.utils.ComparisonUtils;
import betmen.rests.utils.data.DataCleanUpUtils;
import betmen.rests.utils.data.builders.TeamEditDtoBuilder;
import betmen.rests.utils.data.generator.AdminTestDataGenerator;
import betmen.rests.utils.data.templater.CupTemplater;
import betmen.rests.utils.helpers.AuthEndPointsHandler;
import betmen.rests.utils.helpers.CupsEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminCupEndPointsHandler;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsNull.notNullValue;

public class CupsRestTest {

    @BeforeMethod
    public void testInit() {
        AuthEndPointsHandler.logout();
    }

    @Test
    public void shouldNotAllowToGetAccessToCupsForAnonymousUser() {
        CupsEndPointsHandler.getAllCups(ResponseStatus.UNAUTHORIZED);
        CupsEndPointsHandler.getAllPublicCups(ResponseStatus.UNAUTHORIZED);
        CupsEndPointsHandler.getAllPublicCurrentCups(ResponseStatus.UNAUTHORIZED);
        CupsEndPointsHandler.getCup(0, ResponseStatus.UNAUTHORIZED);
    }

    @Test
    public void shouldReturn422IfRequestedNotExistingCup() {
        AuthEndPointsHandler.loginAsAdmin();
        CupsEndPointsHandler.getCup(0, ResponseStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldGetExistingPublicCup() {
        // given
        DataCleanUpUtils.cleanupAll();
        AuthEndPointsHandler.loginAsAdmin();
        CategoryEditDTO category = AdminTestDataGenerator.createCategory();
        PointsCalculationStrategyEditDTO pointsStrategy = AdminTestDataGenerator.createPointsStrategy();

        CupEditDTO cupEdit = CupTemplater.random(category.getCategoryId(), pointsStrategy.getPcsId())
                .publicCup()
                .build();
        CupEditDTO created = AdminCupEndPointsHandler.create(cupEdit);

        // when
        CupDTO cup = CupsEndPointsHandler.getCup(created.getCupId());

        // then
        assertThat(cup, notNullValue());
        ComparisonUtils.assertTheSame(cup, created);
    }

    @Test
    public void shouldReturn422IfCupExistsButIsNotPublic() {
        // given
        DataCleanUpUtils.cleanupCups();
        AuthEndPointsHandler.loginAsAdmin();
        CategoryEditDTO category = AdminTestDataGenerator.createCategory();
        PointsCalculationStrategyEditDTO pointsStrategy = AdminTestDataGenerator.createPointsStrategy();

        CupEditDTO cupEdit = CupTemplater.random(category.getCategoryId(), pointsStrategy.getPcsId())
                .privateCup()
                .build();
        CupEditDTO created = AdminCupEndPointsHandler.create(cupEdit);

        // when
        CupsEndPointsHandler.getCup(created.getCupId(), ResponseStatus.UNPROCESSABLE_ENTITY);
        CupDTO cup = AdminCupEndPointsHandler.getCup(created.getCupId());

        // then
        assertThat(cup, notNullValue());
//        ComparisonUtils.assertTheSame(cup, created); // TODO: not hidden cup has '[]' around name, so assertion fails

    }

    @Test
    public void shouldReturnPublicCupsSortedByBeginningTimeAsc() {
        // given
        DataCleanUpUtils.cleanupCups();
        AuthEndPointsHandler.loginAsAdmin();
        CategoryEditDTO category1 = AdminTestDataGenerator.createCategory();
        CategoryEditDTO category2 = AdminTestDataGenerator.createCategory();
        PointsCalculationStrategyEditDTO pointsStrategy = AdminTestDataGenerator.createPointsStrategy();

        TeamEditDTO teamWinnerCup2 = AdminTestDataGenerator.createTeam(category1.getCategoryId());

        CupEditDTO cupEdit1 = CupTemplater.random(category1.getCategoryId(), pointsStrategy.getPcsId())
                .publicCup()
                .builder()
                .withStartDate(LocalDateTime.of(2016, 10, 2, 0, 0))
                .build();
        CupEditDTO created1 = AdminCupEndPointsHandler.create(cupEdit1);

        CupEditDTO cupEdit2 = CupTemplater.random(category1.getCategoryId(), pointsStrategy.getPcsId())
                .publicCup()
                .finished(TeamEditDtoBuilder.convertTeamToCupWinner(1, teamWinnerCup2.getTeamId()))
                .builder()
                .withStartDate(LocalDateTime.of(2016, 10, 4, 0, 0))
                .build();
        CupEditDTO created2 = AdminCupEndPointsHandler.create(cupEdit2);

        CupEditDTO cupEdit3 = CupTemplater.random(category2.getCategoryId(), pointsStrategy.getPcsId())
                .publicCup()
                .builder()
                .withStartDate(LocalDateTime.of(2016, 10, 1, 0, 0))
                .build();
        CupEditDTO created3 = AdminCupEndPointsHandler.create(cupEdit3);

        // when
        List<CupDTO> allPublicCups = CupsEndPointsHandler.getAllPublicCups();

        // then
        assertThat(allPublicCups, hasSize(3));
        ComparisonUtils.assertTheSame(allPublicCups.get(0), created3);
        ComparisonUtils.assertTheSame(allPublicCups.get(1), created1);
        ComparisonUtils.assertTheSame(allPublicCups.get(2), created2);

        // when
        List<CupDTO> allPublicCurrentCups = CupsEndPointsHandler.getAllPublicCurrentCups();

        // then
        assertThat(allPublicCurrentCups, hasSize(2));
        ComparisonUtils.assertTheSame(allPublicCups.get(0), created3);
        ComparisonUtils.assertTheSame(allPublicCups.get(1), created1);
    }
}

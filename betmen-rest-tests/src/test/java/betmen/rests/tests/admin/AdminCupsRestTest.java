package betmen.rests.tests.admin;

import betmen.dto.dto.CupDTO;
import betmen.dto.dto.admin.CategoryEditDTO;
import betmen.dto.dto.admin.CupEditDTO;
import betmen.dto.dto.admin.CupForGameImportDTO;
import betmen.dto.dto.admin.CupWinnerEditDTO;
import betmen.dto.dto.admin.PointsCalculationStrategyEditDTO;
import betmen.dto.dto.admin.SportKindEditDTO;
import betmen.dto.dto.admin.TeamEditDTO;
import betmen.rests.model.GameImportStrategyType;
import betmen.rests.utils.data.DataCleanUpUtils;
import betmen.rests.utils.data.builders.CategoryEditDtoBuilder;
import betmen.rests.utils.data.builders.TeamEditDtoBuilder;
import betmen.rests.utils.data.generator.AdminTestDataGenerator;
import betmen.rests.utils.data.templater.CupTemplater;
import betmen.rests.utils.helpers.AuthEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminCategoryEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminCupEndPointsHandler;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

public class AdminCupsRestTest {

    private SportKindEditDTO sport1;
    private SportKindEditDTO sport2;

    private CategoryEditDTO nba;
    private CategoryEditDTO ncaa;

    // private
    private CupEditDTO privateFinishedCup;
    private CupEditDTO privateCurrentCup;
    private CupEditDTO privateFutureCup;

    // public
    private CupEditDTO publicFinishedCup;
    private CupEditDTO publicCurrentCup;
    private CupEditDTO publicCurrentImportableCup;
    private CupEditDTO publicFutureCup;
    private CupEditDTO publicFutureImportableCup;
    private CupEditDTO publicFutureImportableCup2;

    @BeforeClass
    public void initClass() {
        DataCleanUpUtils.cleanupAll();

        AuthEndPointsHandler.loginAsAdmin();

        PointsCalculationStrategyEditDTO sp = AdminTestDataGenerator.createPointsStrategy();

        sport1 = AdminTestDataGenerator.createSport();
        sport2 = AdminTestDataGenerator.createSport();

        nba = AdminTestDataGenerator.createCategory(sport1.getSportKindId());

        CategoryEditDTO categoryEditDTO = CategoryEditDtoBuilder.construct(sport2.getSportKindId());
        categoryEditDTO.setRemoteGameImportStrategyTypeId(GameImportStrategyType.NBA.getId());
        ncaa = AdminCategoryEndPointsHandler.create(categoryEditDTO);

        TeamEditDTO nbaTeam1 = AdminTestDataGenerator.createTeam(nba.getCategoryId());
        CupWinnerEditDTO nbaWinner1 = TeamEditDtoBuilder.convertTeamToCupWinner(1, nbaTeam1.getTeamId());

        TeamEditDTO nbaTeam2 = AdminTestDataGenerator.createTeam(nba.getCategoryId());
        CupWinnerEditDTO nbaWinner2 = TeamEditDtoBuilder.convertTeamToCupWinner(1, nbaTeam2.getTeamId());

        privateFinishedCup = createCup(cupTemplater(nba, sp).privateCup().finished(nbaWinner1).builder().withImportId("IMPORT_ID_3").build());
        privateCurrentCup = createCup(cupTemplater(nba, sp).privateCup().current().build());
        privateFutureCup = createCup(cupTemplater(nba, sp).privateCup().inHour(24).build());

        publicFinishedCup = createCup(cupTemplater(nba, sp).publicCup().finished(nbaWinner2).build());
        publicCurrentCup = createCup(cupTemplater(nba, sp).publicCup().current().build());
        publicCurrentImportableCup = createCup(cupTemplater(ncaa, sp).publicCup().inHour(2).builder().withImportId("IMPORT_ID_1").build());
        publicFutureCup = createCup(cupTemplater(nba, sp).publicCup().inHour(12).build());
        publicFutureImportableCup = createCup(cupTemplater(ncaa, sp).publicCup().inDays(4).builder().withImportId("IMPORT_ID_2").build());
        publicFutureImportableCup2 = createCup(cupTemplater(ncaa, sp).publicCup().inDays(30).builder().build());

        AuthEndPointsHandler.logout();
    }

    @BeforeMethod
    public void beforeTest() {
        AuthEndPointsHandler.loginAsAdmin();
    }

    @AfterMethod
    public void afterTest() {
        AuthEndPointsHandler.logout();
    }

    @Test
    public void shouldGetItem() {
        CupDTO cup = AdminCupEndPointsHandler.getCup(publicFinishedCup.getCupId());
        assertThat(cup, notNullValue());
        assertThat(cup.getCupId(), is(publicFinishedCup.getCupId()));
    }

    @Test
    public void shouldGetAllCups() {
        List<CupDTO> cups = AdminCupEndPointsHandler.getAllCups();
        assertThat(cups, notNullValue());
        assertThat(cups, hasSize(9));
        assertThat(cups.get(0).getCupId(), is(publicFutureImportableCup2.getCupId()));
        assertThat(cups.get(1).getCupId(), is(publicFutureImportableCup.getCupId()));
        assertThat(cups.get(2).getCupId(), is(publicFutureCup.getCupId()));
        assertThat(cups.get(3).getCupId(), is(publicCurrentImportableCup.getCupId()));
        assertThat(cups.get(4).getCupId(), is(publicCurrentCup.getCupId()));
        assertThat(cups.get(5).getCupId(), is(publicFinishedCup.getCupId()));

        assertThat(cups.get(6).getCupId(), is(privateFutureCup.getCupId()));
        assertThat(cups.get(7).getCupId(), is(privateCurrentCup.getCupId()));
        assertThat(cups.get(8).getCupId(), is(privateFinishedCup.getCupId()));
    }

    @Test
    public void shouldGetCurrentCups() {
        List<CupDTO> cups = AdminCupEndPointsHandler.getAllCurrentCups();
        assertThat(cups, notNullValue());
        assertThat(cups, hasSize(7));

        assertThat(cups.get(0).getCupId(), is(publicFutureImportableCup2.getCupId()));
        assertThat(cups.get(1).getCupId(), is(publicFutureImportableCup.getCupId()));
        assertThat(cups.get(2).getCupId(), is(publicFutureCup.getCupId()));
        assertThat(cups.get(3).getCupId(), is(publicCurrentImportableCup.getCupId()));
        assertThat(cups.get(4).getCupId(), is(publicCurrentCup.getCupId()));

        assertThat(cups.get(5).getCupId(), is(privateFutureCup.getCupId()));
        assertThat(cups.get(6).getCupId(), is(privateCurrentCup.getCupId()));
    }

    @Test
    public void shouldGetEmptyListIfNoImportableCupsForSportKind() {
        List<CupForGameImportDTO> cups = AdminCupEndPointsHandler.getAllImportableCups(sport1.getSportKindId());
        assertThat(cups, notNullValue());
        assertThat(cups, hasSize(0));
    }

    @Test
    public void shouldNotGetCupAsImportableIfCategoryDoesNotHaveImportStrategyEvenIfCupHasImportId() {
        List<CupForGameImportDTO> cups = AdminCupEndPointsHandler.getAllImportableCups(sport1.getSportKindId());
        assertThat(cups, notNullValue());
        assertThat(cups, hasSize(0));
    }

    @Test
    public void shouldGetCupAsImportableIfCategoryHaveImportStrategyAndCupHasImportId() {
        List<CupForGameImportDTO> cups = AdminCupEndPointsHandler.getAllImportableCups(sport2.getSportKindId());
        assertThat(cups, notNullValue());
        assertThat(cups, hasSize(3));
        assertThat(cups.get(0).getCup().getCupId(), is(publicFutureImportableCup2.getCupId()));
        assertThat(cups.get(1).getCup().getCupId(), is(publicFutureImportableCup.getCupId()));
        assertThat(cups.get(2).getCup().getCupId(), is(publicCurrentImportableCup.getCupId()));
    }

    @Test
    public void shouldGetCategoryCups() {
        List<CupDTO> cups = AdminCupEndPointsHandler.getAllCategoryCups(ncaa.getCategoryId());
        assertThat(cups, notNullValue());
        assertThat(cups, hasSize(3));
        assertThat(cups.get(0).getCupId(), is(publicFutureImportableCup2.getCupId()));
        assertThat(cups.get(1).getCupId(), is(publicFutureImportableCup.getCupId()));
        assertThat(cups.get(2).getCupId(), is(publicCurrentImportableCup.getCupId()));
    }

    private CupTemplater cupTemplater(final CategoryEditDTO category, final PointsCalculationStrategyEditDTO ps) {
        return CupTemplater.random(category.getCategoryId(), ps.getPcsId());
    }

    private CupEditDTO createCup(final CupEditDTO cup) {
        return AdminCupEndPointsHandler.create(cup);
    }
}

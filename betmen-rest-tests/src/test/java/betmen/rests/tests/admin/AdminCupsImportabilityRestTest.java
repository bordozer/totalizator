package betmen.rests.tests.admin;

import betmen.dto.dto.admin.CategoryEditDTO;
import betmen.dto.dto.admin.CupEditDTO;
import betmen.dto.dto.admin.CupForGameImportDTO;
import betmen.dto.dto.admin.PointsCalculationStrategyEditDTO;
import betmen.dto.dto.admin.SportKindEditDTO;
import betmen.rests.utils.data.DataCleanUpUtils;
import betmen.rests.utils.data.builders.CategoryEditDtoBuilder;
import betmen.rests.utils.data.generator.AdminTestDataGenerator;
import betmen.rests.utils.data.templater.CupTemplater;
import betmen.rests.utils.helpers.AuthEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminCategoryEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminCupEndPointsHandler;
import org.apache.commons.lang.StringUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

public class AdminCupsImportabilityRestTest {

    @BeforeMethod
    public void init() {
        DataCleanUpUtils.cleanupAll();
        AuthEndPointsHandler.loginAsAdmin();
    }

    @AfterMethod
    public void teardown() {
        AuthEndPointsHandler.logout();
    }

    @Test
    public void shouldGetEmptyCupListIfGameImportStrategyTypeIsZero() {
        SportKindEditDTO sport = AdminTestDataGenerator.createSport();

        PointsCalculationStrategyEditDTO sp = AdminTestDataGenerator.createPointsStrategy();

        CategoryEditDTO construct = CategoryEditDtoBuilder.construct(sport.getSportKindId());
        construct.setCategoryImportId("IMPORT_ID_4");
        construct.setRemoteGameImportStrategyTypeId(0); // GameImportStrategyType.NO_IMPORT
        CategoryEditDTO category = AdminCategoryEndPointsHandler.create(construct);

        CupEditDTO cup = createCup(cupTemplater(category, sp).privateCup().builder().withImportId("IMPORT_ID_3").build());

        List<CupForGameImportDTO> cups = AdminCupEndPointsHandler.getAllImportableCups(sport.getSportKindId());

        assertThat(cups, notNullValue());
        assertThat(cups.size(), is(0));
    }

    @Test
    public void shouldGetCupListIfGameImportStrategyTypeIsNBAAndCupHasImportId() {
        List<CupForGameImportDTO> cups = testStrategy(1, "IMPORT_ID_3");
        assertThat(cups, notNullValue());
        assertThat(cups.size(), is(1));
    }

    @Test
    public void shouldGetCupListIfGameImportStrategyTypeIsNBAAndCupHasNoImportId() {
        List<CupForGameImportDTO> cups = testStrategy(1, StringUtils.EMPTY);
        assertThat(cups, notNullValue());
        assertThat(cups.size(), is(1));
    }

    @Test
    public void shouldGetCupListIfGameImportStrategyTypeIsUEFAAndCupHasImportId() {
        List<CupForGameImportDTO> cups = testStrategy(2, "IMPORT_ID_3");
        assertThat(cups, notNullValue());
        assertThat(cups.size(), is(1));
    }

    @Test
    public void shouldGetEmptyCupListIfGameImportStrategyTypeIsUEFAAndCupHasNoImportId() {
        List<CupForGameImportDTO> cups = testStrategy(2, StringUtils.EMPTY);
        assertThat(cups, notNullValue());
        assertThat(cups.size(), is(0)); // UEFA demand ImportId for a cup to be able be imported
    }

    @Test
    public void shouldGetCupListIfGameImportStrategyTypeIsNHLAndCupHasImportId() {
        List<CupForGameImportDTO> cups = testStrategy(3, "IMPORT_ID_3");
        assertThat(cups, notNullValue());
        assertThat(cups.size(), is(1));
    }

    @Test
    public void shouldGetCupListIfGameImportStrategyTypeIsNHLAndCupHasNoImportId() {
        List<CupForGameImportDTO> cups = testStrategy(3, StringUtils.EMPTY);
        assertThat(cups, notNullValue());
        assertThat(cups.size(), is(1));
    }

    private List<CupForGameImportDTO> testStrategy(final int remoteGameImportStrategyTypeId, final String importId) {
        SportKindEditDTO sport = AdminTestDataGenerator.createSport();

        PointsCalculationStrategyEditDTO sp = AdminTestDataGenerator.createPointsStrategy();

        CategoryEditDTO construct = CategoryEditDtoBuilder.construct(sport.getSportKindId());
        construct.setCategoryImportId("IMPORT_ID_4");
        construct.setRemoteGameImportStrategyTypeId(remoteGameImportStrategyTypeId);
        CategoryEditDTO category = AdminCategoryEndPointsHandler.create(construct);

        CupEditDTO cup = createCup(cupTemplater(category, sp).privateCup().builder().withImportId(importId).build());

        return AdminCupEndPointsHandler.getAllImportableCups(sport.getSportKindId());
    }

    private CupTemplater cupTemplater(final CategoryEditDTO category1, final PointsCalculationStrategyEditDTO pointsStrategy) {
        return CupTemplater.random(category1.getCategoryId(), pointsStrategy.getPcsId());
    }

    private CupEditDTO createCup(final CupEditDTO cupEdit) {
        return AdminCupEndPointsHandler.create(cupEdit);
    }
}

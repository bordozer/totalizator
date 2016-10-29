package betmen.rests.tests.admin;

import betmen.dto.dto.error.FieldErrorsResponse;
import betmen.dto.dto.admin.CategoryEditDTO;
import betmen.dto.dto.admin.CupEditDTO;
import betmen.dto.dto.admin.CupWinnerEditDTO;
import betmen.dto.dto.admin.MatchEditDTO;
import betmen.dto.dto.admin.PointsCalculationStrategyEditDTO;
import betmen.dto.dto.admin.TeamEditDTO;
import betmen.rests.common.ResponseStatus;
import betmen.rests.utils.RandomUtils;
import betmen.rests.utils.data.builders.TeamEditDtoBuilder;
import betmen.rests.utils.data.generator.AdminTestDataGenerator;
import betmen.rests.utils.data.templater.CupTemplater;
import betmen.rests.utils.data.templater.MatchTemplater;
import betmen.rests.utils.helpers.AuthEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminCupEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminMatchEndPointsHandler;
import com.google.common.collect.Lists;
import com.jayway.restassured.response.Response;
import org.apache.commons.lang.StringUtils;
import org.hamcrest.Matchers;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class AdminCupEditRestTest {

    private CategoryEditDTO category;
    private PointsCalculationStrategyEditDTO pointsCalculationStrategy;

    @BeforeMethod
    public void setup() {
        AuthEndPointsHandler.loginAsAdmin();
        category = AdminTestDataGenerator.createCategory();
        pointsCalculationStrategy = AdminTestDataGenerator.createPointsStrategy();
    }

    @AfterMethod
    public void afterTest() {
        AuthEndPointsHandler.logout();
    }

    @Test
    public void shouldGetItems() {
        final List<CupEditDTO> items = getItems();
        assertThat(items, notNullValue());
        // TODO: test for ordering
    }

    @Test
    public void shouldCreateIfMandatoryDataProvided() {
        AdminTestDataGenerator.createRandomCup();
    }

    @Test
    public void shouldGetItemIfExists() {
        CupEditDTO cup = AdminTestDataGenerator.createRandomCup();
        CupEditDTO response = AdminCupEndPointsHandler.getEdit(cup.getCupId());
        assertThat(response, notNullValue());
        assertThat(response.getCupId(), is(cup.getCupId()));
    }

    @Test
    public void shouldResponseWithBadRequestIfItemDoesNotExist() {
        AdminCupEndPointsHandler.getEdit(10002, ResponseStatus.UNPROCESSABLE_ENTITY);
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
    public void shouldNotCreateIfNameAlreadyExists() {
        CupEditDTO cup = AdminTestDataGenerator.createRandomCup();
        CupEditDTO anotherCup = AdminTestDataGenerator.createRandomCup();
        anotherCup.setCupName(cup.getCupName());
        CupEditDTO update = AdminCupEndPointsHandler.create(anotherCup);
        assertThat(update, notNullValue());
        assertThat(update.getCupId() > 0, is(true));
    }

    @Test
    public void shouldNotCreateIfCategoryDoesNotExist() {
        CupEditDTO dto = construct();
        dto.setCategoryId(99999);
        AdminCupEndPointsHandler.create(dto, ResponseStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldNotCreateIfPointStrategyIsWrong() {
        CupEditDTO dto = construct();
        dto.setCupPointsCalculationStrategyId(99999);
        AdminCupEndPointsHandler.create(dto, ResponseStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldNotUpdateIfCategoryIsWrong() {
        CupEditDTO dto = AdminTestDataGenerator.createRandomCup();
        dto.setCategoryId(99999);
        AdminCupEndPointsHandler.update(dto, ResponseStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldUpdateIfNameAlreadyExists() {
        CupEditDTO cup = AdminTestDataGenerator.createRandomCup();
        CupEditDTO anotherCup = AdminTestDataGenerator.createRandomCup();
        anotherCup.setCupName(cup.getCupName());
        CupEditDTO update = AdminCupEndPointsHandler.update(anotherCup);
        assertThat(update, notNullValue());
        assertThat(update.getCupId() > 0, is(true));
    }

    @Test
    public void shouldNotUpdateIfPointStrategyIsWrong() {
        CupEditDTO dto = AdminTestDataGenerator.createRandomCup();
        dto.setCupPointsCalculationStrategyId(99999);
        AdminCupEndPointsHandler.update(dto, ResponseStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldNotCreateIfWinnersCountIsZero() {
        CupEditDTO dto = construct();
        dto.setWinnersCount(0);
        AdminCupEndPointsHandler.create(dto, ResponseStatus.BAD_REQUEST);
    }

    @Test
    public void shouldCreateAndThenUpdateWithWinners() {
        TeamEditDTO team1 = AdminTestDataGenerator.createTeam(category.getCategoryId());
        TeamEditDTO team2 = AdminTestDataGenerator.createTeam(category.getCategoryId());
        TeamEditDTO team3 = AdminTestDataGenerator.createTeam(category.getCategoryId());
        TeamEditDTO team4 = AdminTestDataGenerator.createTeam(category.getCategoryId());

        List<CupWinnerEditDTO> cupWinners = Lists.newArrayList();
        cupWinners.add(TeamEditDtoBuilder.convertTeamToCupWinner(1, team1.getTeamId()));
        cupWinners.add(TeamEditDtoBuilder.convertTeamToCupWinner(2, team2.getTeamId()));

        CupEditDTO dto = construct();
        dto.setWinnersCount(cupWinners.size());
        dto.setCupWinners(cupWinners);

        CupEditDTO created = AdminCupEndPointsHandler.create(dto);

        assertThat(created, Matchers.notNullValue());
        assertThat(created.getCupWinners(), Matchers.notNullValue());
        assertThat(created.getCupWinners().size(), is(2));
        assertThat(created.getCupWinners().get(0).getTeamId(), is(cupWinners.get(0).getTeamId()));
        assertThat(created.getCupWinners().get(1).getTeamId(), is(cupWinners.get(1).getTeamId()));

        // update the same cup
        List<CupWinnerEditDTO> updatedWinners = Lists.newArrayList();
        updatedWinners.add(TeamEditDtoBuilder.convertTeamToCupWinner(1, team3.getTeamId()));
        updatedWinners.add(TeamEditDtoBuilder.convertTeamToCupWinner(2, team2.getTeamId()));
        updatedWinners.add(TeamEditDtoBuilder.convertTeamToCupWinner(3, team1.getTeamId()));
        updatedWinners.add(TeamEditDtoBuilder.convertTeamToCupWinner(4, team4.getTeamId()));
        created.setCupWinners(updatedWinners);
        created.setWinnersCount(updatedWinners.size());

        CupEditDTO updated = AdminCupEndPointsHandler.update(created);

        assertThat(updated, Matchers.notNullValue());
        assertThat(updated.getCupWinners(), Matchers.notNullValue());
        assertThat(updated.getCupWinners().size(), is(updatedWinners.size()));
        assertThat(updated.getCupWinners().get(0).getTeamId(), is(updatedWinners.get(0).getTeamId()));
        assertThat(updated.getCupWinners().get(1).getTeamId(), is(updatedWinners.get(1).getTeamId()));
        assertThat(updated.getCupWinners().get(2).getTeamId(), is(updatedWinners.get(2).getTeamId()));
        assertThat(updated.getCupWinners().get(3).getTeamId(), is(updatedWinners.get(3).getTeamId()));

        CupEditDTO read = AdminCupEndPointsHandler.getEdit(created.getCupId());
        assertThat(read.getCupWinners(), Matchers.notNullValue());
        assertThat(read.getCupWinners().size(), is(updatedWinners.size()));
        assertThat(read.getCupWinners().get(0).getTeamId(), is(updatedWinners.get(0).getTeamId()));
        assertThat(read.getCupWinners().get(1).getTeamId(), is(updatedWinners.get(1).getTeamId()));
        assertThat(read.getCupWinners().get(2).getTeamId(), is(updatedWinners.get(2).getTeamId()));
        assertThat(read.getCupWinners().get(3).getTeamId(), is(updatedWinners.get(3).getTeamId()));
    }

    @Test
    public void shouldNotCreateIfWinnersCountDoesNotMatch() {
        TeamEditDTO team1 = AdminTestDataGenerator.createTeam(category.getCategoryId());
        TeamEditDTO team2 = AdminTestDataGenerator.createTeam(category.getCategoryId());

        List<CupWinnerEditDTO> cupWinners = Lists.newArrayList();
        cupWinners.add(TeamEditDtoBuilder.convertTeamToCupWinner(1, team1.getTeamId()));
        cupWinners.add(TeamEditDtoBuilder.convertTeamToCupWinner(2, team2.getTeamId()));

        CupEditDTO dto = construct();
        dto.setWinnersCount(1); // less then actual winners (2)
        dto.setCupWinners(cupWinners);
        AdminCupEndPointsHandler.create(dto, ResponseStatus.BAD_REQUEST);
    }

    @Test
    public void shouldNotCreateIfWinnerHasNoTeamId() {
        List<CupWinnerEditDTO> cupWinners = Lists.newArrayList();
        CupWinnerEditDTO cupWinner = new CupWinnerEditDTO();
        cupWinner.setCupPosition(1);
        cupWinner.setTeamId(0);
        cupWinners.add(cupWinner);

        CupEditDTO dto = construct();
        dto.setWinnersCount(cupWinners.size());
        dto.setCupWinners(cupWinners);
        AdminCupEndPointsHandler.create(dto, ResponseStatus.BAD_REQUEST);
    }

    @Test
    public void shouldNotCreateIfWinnerHasNoPosition() {
        List<CupWinnerEditDTO> cupWinners = Lists.newArrayList();
        CupWinnerEditDTO cupWinner = new CupWinnerEditDTO();
        cupWinner.setCupPosition(0);
        cupWinner.setTeamId(1);
        cupWinners.add(cupWinner);

        CupEditDTO dto = construct();
        dto.setWinnersCount(cupWinners.size());
        dto.setCupWinners(cupWinners);
        AdminCupEndPointsHandler.create(dto, ResponseStatus.BAD_REQUEST);
    }

    @Test
    public void shouldNotCreateIfWinnerHasWrongTeamId() {
        List<CupWinnerEditDTO> cupWinners = Lists.newArrayList();
        CupWinnerEditDTO cupWinner = new CupWinnerEditDTO();
        cupWinner.setCupPosition(1);
        cupWinner.setTeamId(98745);
        cupWinners.add(cupWinner);

        CupEditDTO dto = construct();
        dto.setWinnersCount(cupWinners.size());
        dto.setCupWinners(cupWinners);
        AdminCupEndPointsHandler.create(dto, ResponseStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldNotCreateIfTwoWinnersHaveTheSamePositions() {
        TeamEditDTO team1 = AdminTestDataGenerator.createTeam(category.getCategoryId());
        TeamEditDTO team2 = AdminTestDataGenerator.createTeam(category.getCategoryId());

        List<CupWinnerEditDTO> cupWinners = Lists.newArrayList();
        cupWinners.add(TeamEditDtoBuilder.convertTeamToCupWinner(1, team1.getTeamId()));
        cupWinners.add(TeamEditDtoBuilder.convertTeamToCupWinner(1, team2.getTeamId()));

        CupEditDTO dto = construct();
        dto.setWinnersCount(cupWinners.size());
        dto.setCupWinners(cupWinners);
        AdminCupEndPointsHandler.create(dto, ResponseStatus.BAD_REQUEST);
    }

    @Test
    public void shouldNotCreateIfWinnerTeamsAreNotUnique() {
        TeamEditDTO team1 = AdminTestDataGenerator.createTeam(category.getCategoryId());

        List<CupWinnerEditDTO> cupWinners = Lists.newArrayList();
        cupWinners.add(TeamEditDtoBuilder.convertTeamToCupWinner(1, team1.getTeamId()));
        cupWinners.add(TeamEditDtoBuilder.convertTeamToCupWinner(2, team1.getTeamId()));

        CupEditDTO dto = construct();
        dto.setWinnersCount(cupWinners.size());
        dto.setCupWinners(cupWinners);
        AdminCupEndPointsHandler.create(dto, ResponseStatus.BAD_REQUEST);
    }

    @Test
    public void shouldUpdate() {
        CupEditDTO cup = AdminTestDataGenerator.createRandomCup();
        CategoryEditDTO anotherCategory = AdminTestDataGenerator.createCategory();
        PointsCalculationStrategyEditDTO anotherPointsStrategy = AdminTestDataGenerator.createPointsStrategy();

        String newName = RandomUtils.cupName();
        LocalDateTime cupStartDate = LocalDateTime.of(2016, 4, 25, 15, 0);

        cup.setCupName(newName);
        cup.setCategoryId(anotherCategory.getCategoryId());
        cup.setCupPointsCalculationStrategyId(anotherPointsStrategy.getPcsId());
        cup.setWinnersCount(2);
        cup.setCupWinners(Lists.newArrayList());
        cup.setCupStartDate(cupStartDate);
        cup.setPublicCup(true);
        cup.setLogoUrl("/logo/url/"); // does not matte what has come from FE
        cup.setCupImportId("IMPORT_ID");

        cup.setReadyForMatchBets(true); // calculated. does not matte what has come from FE
        cup.setReadyForCupBets(true); // calculated. does not matte what has come from FE
        cup.setFinished(true); // calculated. does not matte what has come from FE

        CupEditDTO updated = AdminCupEndPointsHandler.update(cup);

        assertThat(updated.getCupId() > 0, is(true));
        assertThat(updated.getCupName(), is(newName));
        assertThat(updated.getCategoryId(), is(anotherCategory.getCategoryId()));
        assertThat(updated.getCupPointsCalculationStrategyId(), is(anotherPointsStrategy.getPcsId()));
        assertThat(updated.getCupStartDate(), is(cupStartDate));
        assertThat(updated.getWinnersCount(), is(2));
        assertThat(updated.getCupWinners(), Matchers.notNullValue());
        assertThat(updated.isPublicCup(), is(true));
        assertThat(updated.getLogoUrl(), is(String.format("/resources/cups/%s/logo/", updated.getCupId())));
        assertThat(updated.getCupImportId(), is("IMPORT_ID"));
        assertThat(updated.isReadyForMatchBets(), is(true));

        assertThat(updated.isReadyForCupBets(), is(false)); // calculated
        assertThat(updated.isFinished(), is(false)); // finished if the cup has champion(s)
    }

    @Test
    public void shouldNotDeleteIfWrongEntityId() {
        AdminCupEndPointsHandler.delete(9999, ResponseStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldNotDeleteIfCupAssignedToMatch() {
        CupEditDTO cup = AdminTestDataGenerator.createRandomCup();

        TeamEditDTO team1 = AdminTestDataGenerator.createTeam(cup.getCategoryId());
        TeamEditDTO team2 = AdminTestDataGenerator.createTeam(cup.getCategoryId());

        MatchEditDTO dto = MatchTemplater.random(cup.getCupId(), team1.getTeamId(), team2.getTeamId()).build();
        MatchEditDTO created = AdminMatchEndPointsHandler.create(dto);
        AdminCupEndPointsHandler.delete(created.getCupId(), ResponseStatus.UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldDeleteIfCupHasActiveTeams() {
        PointsCalculationStrategyEditDTO pointsStrategy = AdminTestDataGenerator.createPointsStrategy();
        CategoryEditDTO category = AdminTestDataGenerator.createCategory();
        TeamEditDTO team = AdminTestDataGenerator.createTeam(category.getCategoryId());
        CupEditDTO cup = AdminTestDataGenerator.createRandomCup(category.getCategoryId(), pointsStrategy.getPcsId());
        TeamEditDTO team2 = AdminTestDataGenerator.createTeamAndActivateForCup(category.getCategoryId(), cup.getCupId());
        AdminCupEndPointsHandler.delete(cup.getCupId());
    }

    @Test
    public void shouldDelete() {
        AdminCupEndPointsHandler.delete(AdminTestDataGenerator.createRandomCup().getCupId());
    }

    @Test
    public void shouldCreateWithWinnersAndThenDelete() {
        TeamEditDTO team1 = AdminTestDataGenerator.createTeam(category.getCategoryId());

        List<CupWinnerEditDTO> cupWinners = Lists.newArrayList();
        cupWinners.add(TeamEditDtoBuilder.convertTeamToCupWinner(1, team1.getTeamId()));

        CupEditDTO dto = construct();
        dto.setWinnersCount(cupWinners.size());
        dto.setCupWinners(cupWinners);

        CupEditDTO created = AdminCupEndPointsHandler.create(dto);
        AdminCupEndPointsHandler.delete(created.getCupId());
    }

    private void assertNotSaved(final String name) {
        int sizeBefore = getItems().size();

        CupEditDTO dto = construct();
        dto.setCupName(name);

        Response response = AdminCupEndPointsHandler.create(dto, ResponseStatus.BAD_REQUEST);
        FieldErrorsResponse errors = response.as(FieldErrorsResponse.class);
        assertThat(errors.containsError("cupName", "errors.name_should_not_be_blank"), is(Boolean.TRUE));
        assertThat(errors.containsError("cupName", "errors.name_has_wrong_length"), is(Boolean.TRUE));

        assertThat(getItems().size(), is(sizeBefore));
    }

    private List<CupEditDTO> getItems() {
        return AdminCupEndPointsHandler.getCups();
    }

    private CupEditDTO construct() {
        return CupTemplater.random(category.getCategoryId(), pointsCalculationStrategy.getPcsId()).build();
    }
}

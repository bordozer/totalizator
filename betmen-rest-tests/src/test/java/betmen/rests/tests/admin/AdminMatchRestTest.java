package betmen.rests.tests.admin;

import betmen.dto.dto.MatchSearchModelDto;
import betmen.dto.dto.admin.CupEditDTO;
import betmen.dto.dto.admin.CupWinnerEditDTO;
import betmen.dto.dto.admin.MatchEditDTO;
import betmen.dto.dto.admin.TeamEditDTO;
import betmen.dto.dto.error.FieldErrorsResponse;
import betmen.rests.utils.ComparisonUtils;
import betmen.rests.utils.data.builders.MatchEditDtoBuilder;
import betmen.rests.utils.data.builders.TeamEditDtoBuilder;
import betmen.rests.utils.data.generator.AdminTestDataGenerator;
import betmen.rests.utils.data.templater.CupTemplater;
import betmen.rests.utils.data.templater.MatchTemplater;
import betmen.rests.utils.helpers.AuthEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminCupEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminMatchEndPointsHandler;
import com.jayway.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class AdminMatchRestTest {

    private CupEditDTO cup;
    TeamEditDTO team1;
    TeamEditDTO team2;

    @BeforeMethod
    public void setup() {
        AuthEndPointsHandler.loginAsAdmin();
        cup = AdminTestDataGenerator.createRandomCup();
        team1 = AdminTestDataGenerator.createTeam(cup.getCategoryId());
        team2 = AdminTestDataGenerator.createTeam(cup.getCategoryId());
    }

    @AfterMethod
    public void afterTest() {
        AuthEndPointsHandler.logout();
    }

    @Test
    public void shouldCreateItem() {
        final MatchEditDTO item = createMatch();
        assertThat(item, notNullValue());
        assertThat(item.getCupId() > 0, is(true));
        assertThat(item.getTeam1Id() > 0, is(true));
        assertThat(item.getTeam2Id() > 0, is(true));
    }

    @Test
    public void shouldGetItemIfExists() {
        final MatchEditDTO created = createMatch();
        final MatchEditDTO item = AdminMatchEndPointsHandler.get(created.getMatchId());
        ComparisonUtils.assertEqual(created, item);
    }

    @Test
    public void shouldGetAllMatches() {
        MatchSearchModelDto searchModel = new MatchSearchModelDto();
        searchModel.setCupId(cup.getCupId());
        searchModel.setShowFutureMatches(true);
        searchModel.setShowFinished(true);

        final List<MatchEditDTO> items = AdminMatchEndPointsHandler.getItems(searchModel);

        assertThat(items, notNullValue());
        // TODO: check ordering
    }

    @Test
    public void shouldResponseWithBadRequestIfRequestedItemDoesNotExist() {
        AdminMatchEndPointsHandler.get(99999, HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldResponseWithBadRequestIfDtoValidationOnCreateFailed() {
        MatchEditDTO dto = new MatchEditDTO();
        dto.setBeginningTime(LocalDateTime.now());
        Response response = AdminMatchEndPointsHandler.create(dto, HttpStatus.SC_BAD_REQUEST);
        FieldErrorsResponse errors = response.as(FieldErrorsResponse.class);
        assertThat(errors.containsError("cupId", "errors.cup_should_be_provided"), Matchers.is(Boolean.TRUE));
        assertThat(errors.containsError("team1Id", "errors.team1_should_be_provided"), Matchers.is(Boolean.TRUE));
        assertThat(errors.containsError("team2Id", "errors.team2_should_be_provided"), Matchers.is(Boolean.TRUE));
    }

    @Test
    public void shouldResponseWithBadRequestIfCupDoesNotExistWhenCreate() {
        AdminMatchEndPointsHandler.create(construct().withCupId(9999).build(), HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldResponseWithBadRequestIfTeam1DoesNotExistWhenCreate() {
        AdminMatchEndPointsHandler.create(construct().withTeam1Id(88888).build(), HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldResponseWithBadRequestIfTeam2DoesNotExistWhenCreate() {
        AdminMatchEndPointsHandler.create(construct().withTeam2Id(101010).build(), HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldResponseWithBadRequestIfCupDoesNotExistWhenUpdate() {
        MatchEditDTO dto = createMatch();
        dto.setCupId(9999);
        AdminMatchEndPointsHandler.update(dto, HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldResponseWithBadRequestIfTeam1DoesNotExistWhenUpdate() {
        MatchEditDTO dto = createMatch();
        dto.setTeam1Id(88888);
        AdminMatchEndPointsHandler.update(dto, HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldResponseWithBadRequestIfTeam2DoesNotExistWhenUpdate() {
        MatchEditDTO dto = createMatch();
        dto.setTeam2Id(101010);
        AdminMatchEndPointsHandler.update(dto, HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldNotDeleteIfWrongEntityId() {
        AdminMatchEndPointsHandler.delete(9999, HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test
    public void shouldDelete() {
        AdminMatchEndPointsHandler.delete(createMatch().getMatchId());
    }

    @Test(enabled = false) // TODO: come back after bet rest-test functionality is implemented
    public void shouldDeleteEvenIfBetsOnTheMatchExist() {
//        AdminMatchEndPointsHandler.delete(createFutureMatch().getMatchId());
    }

    @Test
    public void shouldCreateMatchIfCupIsFinished() {
        TeamEditDTO team = AdminTestDataGenerator.createTeam(cup.getCategoryId());
        CupWinnerEditDTO cupWinner = TeamEditDtoBuilder.convertTeamToCupWinner(1, team.getTeamId());
        CupEditDTO cupEdit = CupTemplater.random(cup.getCategoryId(), cup.getCupPointsCalculationStrategyId()).finished(cupWinner).build();
        CupEditDTO cup = AdminCupEndPointsHandler.create(cupEdit);

        TeamEditDTO team1 = AdminTestDataGenerator.createTeam(cup.getCategoryId());
        TeamEditDTO team2 = AdminTestDataGenerator.createTeam(cup.getCategoryId());

        MatchEditDTO matchEdit = MatchTemplater.random(cup.getCupId(), team1.getTeamId(), team2.getTeamId()).future().build();
        MatchEditDTO match = AdminMatchEndPointsHandler.create(matchEdit);
        MatchEditDTO update = AdminMatchEndPointsHandler.update(match);
        assertThat(update, notNullValue());
        assertThat(update.getMatchId(), is(match.getMatchId()));
    }

    private MatchEditDTO createMatch() {
        return AdminTestDataGenerator.createRandomMatch();
    }

    private MatchEditDtoBuilder construct() {
        return MatchTemplater.random(cup.getCupId(), team1.getTeamId(), team2.getTeamId()).builder();
    }
}

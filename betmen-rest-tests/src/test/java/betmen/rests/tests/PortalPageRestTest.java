package betmen.rests.tests;

import betmen.dto.dto.CupDTO;
import betmen.dto.dto.UserDTO;
import betmen.dto.dto.admin.CategoryEditDTO;
import betmen.dto.dto.admin.CupEditDTO;
import betmen.dto.dto.admin.MatchEditDTO;
import betmen.dto.dto.admin.PointsCalculationStrategyEditDTO;
import betmen.dto.dto.admin.SportKindEditDTO;
import betmen.dto.dto.admin.TeamEditDTO;
import betmen.dto.dto.PortalPageDTO;
import betmen.rests.common.UserRegData;
import betmen.rests.utils.DateTimeUtils;
import betmen.rests.utils.RandomUtils;
import betmen.rests.utils.data.DataCleanUpUtils;
import betmen.rests.utils.data.builders.TeamEditDtoBuilder;
import betmen.rests.utils.data.generator.AdminTestDataGenerator;
import betmen.rests.utils.data.templater.CupTemplater;
import betmen.rests.utils.data.templater.MatchTemplater;
import betmen.rests.utils.helpers.AuthEndPointsHandler;
import betmen.rests.utils.helpers.PortalPageEndPointHandler;
import betmen.rests.utils.helpers.admin.AdminCupEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminMatchEndPointsHandler;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

public class PortalPageRestTest {

    private static final LocalDateTime NOW = LocalDateTime.now();
    public static final LocalDateTime TWO_DAYS_AGO = NOW.minusDays(2);

    private static final String TODAY = DateTimeUtils.formatDate(NOW.toLocalDate());

    private UserRegData userData1;
    private UserDTO user1;

    private CupEditDTO finishedPublicCup;
    private CupEditDTO finishedPrivateCup;
    private CupEditDTO currentPublicCup;
    private CupEditDTO currentPrivateCup;
    private CupEditDTO futurePublicCup;
    private CupEditDTO futurePrivateCup;

    @BeforeClass
    public void initClass() {
        DataCleanUpUtils.cleanupAll();

        AuthEndPointsHandler.loginAsAdmin();

        PointsCalculationStrategyEditDTO sp = AdminTestDataGenerator.createPointsStrategy();
        SportKindEditDTO sport = AdminTestDataGenerator.createSport();

        CategoryEditDTO category1 = AdminTestDataGenerator.createCategory(sport.getSportKindId());
        TeamEditDTO team11 = AdminTestDataGenerator.createTeam(category1.getCategoryId());
        TeamEditDTO team12 = AdminTestDataGenerator.createTeam(category1.getCategoryId());

        finishedPublicCup = createCup((cupTemplater(category1, sp).publicCup().finished(TeamEditDtoBuilder.convertTeamToCupWinner(1, team11.getTeamId())).build()));
        finishedPrivateCup = createCup((cupTemplater(category1, sp).privateCup().finished(TeamEditDtoBuilder.convertTeamToCupWinner(1, team12.getTeamId())).build()));

        currentPublicCup = createCup((cupTemplater(category1, sp).publicCup().current().build()));
        currentPrivateCup = createCup((cupTemplater(category1, sp).privateCup().current().build()));

        futurePublicCup = createCup((cupTemplater(category1, sp).publicCup().future().build()));
        futurePrivateCup = createCup((cupTemplater(category1, sp).privateCup().future().build()));

        List<MatchEditDTO> matches = new ArrayList<>();
        matches.add(matchTemplater(finishedPrivateCup.getCupId(), team11.getTeamId(), team12.getTeamId()).builder().withBeginningTime(TWO_DAYS_AGO).build());
        matches.add(matchTemplater(currentPrivateCup.getCupId(), team11.getTeamId(), team12.getTeamId()).builder().withBeginningTime(TWO_DAYS_AGO).build());
        matches.add(matchTemplater(futurePrivateCup.getCupId(), team11.getTeamId(), team12.getTeamId()).builder().withBeginningTime(TWO_DAYS_AGO).build());
        matches.add(matchTemplater(finishedPrivateCup.getCupId(), team11.getTeamId(), team12.getTeamId()).builder().withBeginningTime(NOW).build());
        matches.add(matchTemplater(currentPrivateCup.getCupId(), team11.getTeamId(), team12.getTeamId()).builder().withBeginningTime(NOW).build());

        matches.add(matchTemplater(currentPublicCup.getCupId(), team11.getTeamId(), team12.getTeamId()).builder().withBeginningTime(TWO_DAYS_AGO).build());
        matches.add(matchTemplater(finishedPublicCup.getCupId(), team11.getTeamId(), team12.getTeamId()).builder().withBeginningTime(NOW).finished(true).build());
        matches.add(matchTemplater(currentPublicCup.getCupId(), team11.getTeamId(), team12.getTeamId()).builder().withBeginningTime(NOW).build());
        matches.add(matchTemplater(futurePublicCup.getCupId(), team11.getTeamId(), team12.getTeamId()).builder().withBeginningTime(NOW).build());

        matches.stream().forEach(PortalPageRestTest::createMatch);

        userData1 = RandomUtils.randomUser();
        user1 = AuthEndPointsHandler.registerNewUser(userData1);
    }

    @BeforeMethod
    public void beforeTest() {
        AuthEndPointsHandler.login(userData1);
    }

    @AfterMethod
    public void afterTest() {
        AuthEndPointsHandler.logout();
    }

    @Test
    public void shouldGetPortalPageCupsOnToday() {
        PortalPageDTO dto = new PortalPageDTO();
        dto.setPortalPageDate(TODAY);
        PortalPageDTO responseDto = PortalPageEndPointHandler.getPortalPageCups(dto);

        assertThat(responseDto, notNullValue());

        List<CupDTO> cupsTodayToShow = responseDto.getCupsTodayToShow();
        assertThat(cupsTodayToShow, notNullValue());
        assertThat(cupsTodayToShow, hasSize(3));
        assertThat(cupsTodayToShow.get(0).getCupId(), is(finishedPublicCup.getCupId()));
        assertThat(cupsTodayToShow.get(1).getCupId(), is(currentPublicCup.getCupId()));
        assertThat(cupsTodayToShow.get(2).getCupId(), is(futurePublicCup.getCupId()));

        List<CupDTO> cupsCupsToShow = responseDto.getCupsToShow();
        assertThat(cupsCupsToShow, notNullValue());
        assertThat(cupsCupsToShow, hasSize(2));
        assertThat(cupsCupsToShow.get(0).getCupId(), is(futurePublicCup.getCupId()));
        assertThat(cupsCupsToShow.get(1).getCupId(), is(currentPublicCup.getCupId()));

        assertThat(responseDto.getPortalPageDate(), is(TODAY));
    }

    @Test
    public void shouldGetPortalPageCupsOnDate() {
        String pageDate = DateTimeUtils.formatDate(TWO_DAYS_AGO);
        PortalPageDTO dto = new PortalPageDTO();
        dto.setPortalPageDate(pageDate);
        PortalPageDTO responseDto = PortalPageEndPointHandler.getPortalPageCups(dto);

        assertThat(responseDto, notNullValue());

        List<CupDTO> cupsTodayToShow = responseDto.getCupsTodayToShow();
        assertThat(cupsTodayToShow, notNullValue());
        assertThat(cupsTodayToShow, hasSize(1));
        assertThat(cupsTodayToShow.get(0).getCupId(), is(currentPublicCup.getCupId()));

        List<CupDTO> cupsCupsToShow = responseDto.getCupsToShow();
        assertThat(cupsCupsToShow, notNullValue());
        assertThat(cupsCupsToShow, hasSize(2));
        assertThat(cupsCupsToShow.get(0).getCupId(), is(futurePublicCup.getCupId()));
        assertThat(cupsCupsToShow.get(1).getCupId(), is(currentPublicCup.getCupId()));

        assertThat(responseDto.getPortalPageDate(), is(pageDate));
    }

    @Test
    public void shouldGetPortalPageCupsOnDateWithoutGames() {
        String pageDate = DateTimeUtils.formatDate(TWO_DAYS_AGO.minusDays(1));
        PortalPageDTO dto = new PortalPageDTO();
        dto.setPortalPageDate(pageDate);
        PortalPageDTO responseDto = PortalPageEndPointHandler.getPortalPageCups(dto);

        assertThat(responseDto, notNullValue());

        List<CupDTO> cupsTodayToShow = responseDto.getCupsTodayToShow();
        assertThat(cupsTodayToShow, notNullValue());
        assertThat(cupsTodayToShow, hasSize(0));

        List<CupDTO> cupsCupsToShow = responseDto.getCupsToShow();
        assertThat(cupsCupsToShow, notNullValue());
        assertThat(cupsCupsToShow, hasSize(2));
        assertThat(cupsCupsToShow.get(0).getCupId(), is(futurePublicCup.getCupId()));
        assertThat(cupsCupsToShow.get(1).getCupId(), is(currentPublicCup.getCupId()));

        assertThat(responseDto.getPortalPageDate(), is(pageDate));
    }

    private CupTemplater cupTemplater(final CategoryEditDTO category, final PointsCalculationStrategyEditDTO ps) {
        return CupTemplater.random(category.getCategoryId(), ps.getPcsId());
    }

    private CupEditDTO createCup(final CupEditDTO cup) {
        return AdminCupEndPointsHandler.create(cup);
    }

    private static MatchTemplater matchTemplater(final int cupId, final int team1Id, final int team2Id) {
        return MatchTemplater.random(cupId, team1Id, team2Id);
    }

    public static MatchEditDTO createMatch(final MatchEditDTO matchEditDTO) {
        return AdminMatchEndPointsHandler.create(matchEditDTO);
    }
}

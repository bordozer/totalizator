package betmen.rests.tests;

import betmen.dto.dto.TeamCardCupData;
import betmen.dto.dto.TeamCardDTO;
import betmen.dto.dto.admin.CategoryEditDTO;
import betmen.dto.dto.admin.CupEditDTO;
import betmen.dto.dto.admin.PointsCalculationStrategyEditDTO;
import betmen.dto.dto.admin.SportKindEditDTO;
import betmen.dto.dto.admin.TeamEditDTO;
import betmen.dto.dto.error.CommonErrorResponse;
import betmen.rests.common.ErrorCodes;
import betmen.rests.common.ResponseStatus;
import betmen.rests.common.UserRegData;
import betmen.rests.utils.ComparisonUtils;
import betmen.rests.utils.RandomUtils;
import betmen.rests.utils.data.DataCleanUpUtils;
import betmen.rests.utils.data.generator.AdminTestDataGenerator;
import betmen.rests.utils.data.templater.CupTemplater;
import betmen.rests.utils.data.templater.MatchTemplater;
import betmen.rests.utils.helpers.AuthEndPointsHandler;
import betmen.rests.utils.helpers.TeamEndPointHandler;
import betmen.rests.utils.helpers.admin.AdminCupEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminMatchEndPointsHandler;
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsNull.notNullValue;

public class TeamCardRestTest {

    private static final Logger LOGGER = Logger.getLogger(TeamCardRestTest.class);
    private static final int WRONG_TEAM_ID = 125690;

    private UserRegData userData;

    private CategoryEditDTO category;
    private CupEditDTO privateCup;
    private CupEditDTO publicCup;
    private CupEditDTO anotherPublicCup;

    private TeamEditDTO team1;
    private TeamEditDTO team2;

    @BeforeClass
    public void initClass() {
        LOGGER.debug(this.getClass().getName());
        DataCleanUpUtils.cleanupAll();
        loginAsAdmin();

        PointsCalculationStrategyEditDTO sp = AdminTestDataGenerator.createPointsStrategy();
        SportKindEditDTO sport = AdminTestDataGenerator.createSport();
        category = AdminTestDataGenerator.createCategory(sport.getSportKindId());

        privateCup = AdminCupEndPointsHandler.create(CupTemplater.random(category.getCategoryId(), sp.getPcsId()).privateCup().build());
        publicCup = AdminCupEndPointsHandler.create(
                CupTemplater.random(category.getCategoryId(), sp.getPcsId())
                        .publicCup()
                        .builder()
                        .withStartDate(LocalDateTime.now().plusDays(7))
                        .build());
        anotherPublicCup = AdminCupEndPointsHandler.create(
                CupTemplater.random(category.getCategoryId(), sp.getPcsId())
                        .publicCup()
                        .builder()
                        .withStartDate(LocalDateTime.now().plusDays(20))
                        .build());

        team1 = AdminTestDataGenerator.createTeam(category.getCategoryId());
        team2 = AdminTestDataGenerator.createTeam(category.getCategoryId());

        AuthEndPointsHandler.logout();

        userData = RandomUtils.randomUser();
        AuthEndPointsHandler.registerNewUser(userData);
    }

    @Test
    public void shouldReturn422IfTeamDoesNotExist() {
        loginAsUser();
        CommonErrorResponse errorsResponse = TeamEndPointHandler.getTeamCard(WRONG_TEAM_ID, ResponseStatus.UNPROCESSABLE_ENTITY);
        assertThat(errorsResponse.containsError(ErrorCodes.TEAM_DOES_NOT_EXIST), is(true));
    }

    @Test(priority = 10)
    public void shouldGetTeamCardDataWithoutCupsIfTeamHasNoMatches() {
        ExpectedCardData emptyData = new ExpectedCardData();

        loginAsUser();
        checkCupTeamData(team1, publicCup, emptyData);
        checkCupTeamData(team2, publicCup, emptyData);

        checkCupData(publicCup, TeamEndPointHandler.getTeamCupStatistics(team1.getTeamId(), publicCup.getCupId()), emptyData);
        checkCupData(publicCup, TeamEndPointHandler.getTeamCupStatistics(team2.getTeamId(), publicCup.getCupId()), emptyData);
    }

    @Test(priority = 20)
    public void privateCupMatchIsAddedButCupIsNotShownInCard() {
        ExpectedCardData emptyData = new ExpectedCardData();

        loginAsAdmin();
        AdminMatchEndPointsHandler.create(MatchTemplater.random(privateCup.getCupId(), team1.getTeamId(), team2.getTeamId()).future().finished(false).build());

        loginAsUser();
        checkCupTeamData(team1, publicCup, emptyData);
        checkCupTeamData(team2, publicCup, emptyData);

        checkCupData(publicCup, TeamEndPointHandler.getTeamCupStatistics(team1.getTeamId(), publicCup.getCupId()), emptyData);
        checkCupData(publicCup, TeamEndPointHandler.getTeamCupStatistics(team2.getTeamId(), publicCup.getCupId()), emptyData);
    }

    @Test(priority = 30)
    public void publicCupMatchIsAddedAndCupAppearedInCard() {
        ExpectedCardData expectedCardData = new ExpectedCardData().cupsCount(1).cupIndex(0).finishedMatchCount(0).wonMatchCount(0).futureMatchesCount(1);

        loginAsAdmin();
        AdminMatchEndPointsHandler.create(MatchTemplater.random(publicCup.getCupId(), team1.getTeamId(), team2.getTeamId()).future().build());

        loginAsUser();
        checkCupTeamData(team1, publicCup, expectedCardData);
        checkCupTeamData(team2, publicCup, expectedCardData);

        checkCupData(publicCup, TeamEndPointHandler.getTeamCupStatistics(team1.getTeamId(), publicCup.getCupId()), expectedCardData);
        checkCupData(publicCup, TeamEndPointHandler.getTeamCupStatistics(team2.getTeamId(), publicCup.getCupId()), expectedCardData);
    }

    @Test(priority = 40)
    public void publicCupAnotherMatchIsAddedAndStillOnlyOnePublicCupInCard() {
        ExpectedCardData team1ExpectedData = new ExpectedCardData().cupsCount(1).cupIndex(0).finishedMatchCount(1).wonMatchCount(0).futureMatchesCount(1);
        ExpectedCardData team2ExpectedData = new ExpectedCardData().cupsCount(1).cupIndex(0).finishedMatchCount(1).wonMatchCount(1).futureMatchesCount(1);

        loginAsAdmin();
        AdminMatchEndPointsHandler.create(MatchTemplater.random(publicCup.getCupId(), team1.getTeamId(), team2.getTeamId()).finished(1, 2).build());

        loginAsUser();
        checkCupTeamData(team1, publicCup, team1ExpectedData);
        checkCupTeamData(team2, publicCup, team2ExpectedData);

        checkCupData(publicCup, TeamEndPointHandler.getTeamCupStatistics(team1.getTeamId(), publicCup.getCupId()), team1ExpectedData);
        checkCupData(publicCup, TeamEndPointHandler.getTeamCupStatistics(team2.getTeamId(), publicCup.getCupId()), team2ExpectedData);
    }

    @Test(priority = 50)
    public void anotherPublicCupMatchIsAddedAndItAppearedInCard() {
        ExpectedCardData team1Cup1Expected = new ExpectedCardData().cupsCount(2).cupIndex(1).finishedMatchCount(1).wonMatchCount(0).futureMatchesCount(1);
        ExpectedCardData team1Cup2Expected = new ExpectedCardData().cupsCount(2).cupIndex(0).finishedMatchCount(1).wonMatchCount(1).futureMatchesCount(0);
        ExpectedCardData team2Cup1Expected = new ExpectedCardData().cupsCount(2).cupIndex(1).finishedMatchCount(1).wonMatchCount(1).futureMatchesCount(1);
        ExpectedCardData team2Cup2Expected = new ExpectedCardData().cupsCount(2).cupIndex(0).finishedMatchCount(1).wonMatchCount(0).futureMatchesCount(0);

        loginAsAdmin();
        AdminMatchEndPointsHandler.create(MatchTemplater.random(anotherPublicCup.getCupId(), team1.getTeamId(), team2.getTeamId()).finished(3, 2).build());
        loginAsUser();
        checkCupTeamData(team1, publicCup, team1Cup1Expected);
        checkCupTeamData(team1, anotherPublicCup, team1Cup2Expected);
        checkCupTeamData(team2, publicCup, team2Cup1Expected);
        checkCupTeamData(team2, anotherPublicCup, team2Cup2Expected);

        checkCupData(publicCup, TeamEndPointHandler.getTeamCupStatistics(team1.getTeamId(), publicCup.getCupId()), team1Cup1Expected);
        checkCupData(anotherPublicCup, TeamEndPointHandler.getTeamCupStatistics(team1.getTeamId(), anotherPublicCup.getCupId()), team1Cup2Expected);
        checkCupData(publicCup, TeamEndPointHandler.getTeamCupStatistics(team2.getTeamId(), publicCup.getCupId()), team2Cup1Expected);
        checkCupData(anotherPublicCup, TeamEndPointHandler.getTeamCupStatistics(team2.getTeamId(), anotherPublicCup.getCupId()), team2Cup2Expected);
    }

    private void checkCupTeamData(final TeamEditDTO team, final CupEditDTO cup, final ExpectedCardData expected) {
        TeamCardDTO teamCard1 = TeamEndPointHandler.getTeamCard(team.getTeamId());
        assertThat(teamCard1, notNullValue());

        assertThat(teamCard1.getTeam(), notNullValue());
        ComparisonUtils.assertTheSame(teamCard1.getTeam(), team);

        List<TeamCardCupData> cardCupData1 = teamCard1.getCardCupData();
        assertThat(cardCupData1, notNullValue());
        assertThat(cardCupData1, hasSize(expected.getCupsCount()));

        if (expected.getCupsCount() == 0) {
            return;
        }

        checkCupData(cup, cardCupData1.get(expected.getCupIndex()), expected);
    }

    private void checkCupData(final CupEditDTO cup, final TeamCardCupData teamCardCupData, final ExpectedCardData expected) {
        ComparisonUtils.assertTheSame(teamCardCupData.getCup(), cup);
        assertThat(teamCardCupData.getCupWinner(), nullValue());
        assertThat(teamCardCupData.getFinishedMatchCount(), is(expected.getFinishedMatchCount()));
        assertThat(teamCardCupData.getWonMatchCount(), is(expected.getWonMatchCount()));
        assertThat(teamCardCupData.getFutureMatchesCount(), is(expected.getFutureMatchesCount()));
    }

    private void loginAsAdmin() {
        AuthEndPointsHandler.loginAsAdmin();
    }

    private void loginAsUser() {
        AuthEndPointsHandler.login(userData);
    }

    private static class ExpectedCardData {
        private int cupsCount;
        private int cupIndex;
        private int finishedMatchCount;
        private int wonMatchCount;
        private int futureMatchesCount;

        public int getCupsCount() {
            return cupsCount;
        }

        public ExpectedCardData cupsCount(final int cupsCount) {
            this.cupsCount = cupsCount;
            return this;
        }

        public int getCupIndex() {
            return cupIndex;
        }

        public ExpectedCardData cupIndex(final int cupIndex) {
            this.cupIndex = cupIndex;
            return this;
        }

        public int getFinishedMatchCount() {
            return finishedMatchCount;
        }

        public ExpectedCardData finishedMatchCount(final int finishedMatchCount) {
            this.finishedMatchCount = finishedMatchCount;
            return this;
        }

        public int getWonMatchCount() {
            return wonMatchCount;
        }

        public ExpectedCardData wonMatchCount(final int wonMatchCount) {
            this.wonMatchCount = wonMatchCount;
            return this;
        }

        public int getFutureMatchesCount() {
            return futureMatchesCount;
        }

        public ExpectedCardData futureMatchesCount(final int futureMatchesCount) {
            this.futureMatchesCount = futureMatchesCount;
            return this;
        }
    }
}

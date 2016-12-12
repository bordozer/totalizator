package betmen.rests.stories;

import betmen.dto.dto.ActivityStreamDTO;
import betmen.dto.dto.UserDTO;
import betmen.dto.dto.UsersRatingPositionDTO;
import betmen.dto.dto.admin.CategoryEditDTO;
import betmen.dto.dto.admin.CupEditDTO;
import betmen.dto.dto.admin.MatchEditDTO;
import betmen.dto.dto.admin.PointsCalculationStrategyEditDTO;
import betmen.dto.dto.admin.TeamEditDTO;
import betmen.rests.common.UserRegData;
import betmen.rests.utils.ComparisonUtils;
import betmen.rests.utils.RandomUtils;
import betmen.rests.utils.data.DataCleanUpUtils;
import betmen.rests.utils.data.generator.AdminTestDataGenerator;
import betmen.rests.utils.data.templater.CupTemplater;
import betmen.rests.utils.data.templater.MatchTemplater;
import betmen.rests.utils.helpers.ActivityEndPointsHandler;
import betmen.rests.utils.helpers.AuthEndPointsHandler;
import betmen.rests.utils.helpers.BetEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminCupEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminMatchEndPointsHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Builder;
import org.jbehave.core.annotations.Aliases;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Named;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.IsNull.notNullValue;

public class ActivityStreamFlowStory extends AbstractCommonStepsStory {

    private static final int USER1_BET1_SCORE_1 = 2;
    private static final int USER1_BET1_SCORE_2 = 3;

    private static final int USER2_BET1_SCORE_1 = 4;
    private static final int USER2_BET1_SCORE_2 = 1;
    private static final int USER2_BET2_SCORE_1 = 5;
    private static final int USER2_BET2_SCORE_2 = 0;

    private UserRegData userData1;
    private UserDTO user1;

    private UserRegData userData2;
    private UserDTO user2;

    private static final DataHolder DATA_HOLDER = new DataHolder();

    @BeforeScenario
    public void beforeScenario() {
        userData1 = RandomUtils.randomUser();
        user1 = AuthEndPointsHandler.registerNewUser(userData1);

        userData2 = RandomUtils.randomUser();
        user2 = AuthEndPointsHandler.registerNewUser(userData2);
    }

    @Given("$userName cleanup system and creates a match")
    public void cleanupAll(@Named("userName") final String userName) {
        DATA_HOLDER.cleanupAndCreateData();
    }

    @When("User $userName logged in the system")
    public void user1LoggedIn(@Named("userName") final String userName) {
        AuthEndPointsHandler.login(userData1);
    }

    @When("Another user $userName logged in the system")
    public void user2LoggedIn(@Named("userName") final String userName) {
        AuthEndPointsHandler.login(userData2);
    }

    @When("User $userName makes a bet")
    public void user1MakeABet(@Named("userName") final String userName) {
        BetEndPointsHandler.make(DATA_HOLDER.getMatchId(), USER1_BET1_SCORE_1, USER1_BET1_SCORE_2);
    }

    @When("Another user $userName makes a bet")
    public void user2MakeABet(@Named("userName") final String userName) {
        BetEndPointsHandler.make(DATA_HOLDER.getMatchId(), USER2_BET1_SCORE_1, USER2_BET1_SCORE_2);
    }

    @When("Another user $userName change his bet")
    public void user2ChangedHisBet(@Named("userName") final String userName) {
        BetEndPointsHandler.make(DATA_HOLDER.getMatchId(), USER2_BET2_SCORE_1, USER2_BET2_SCORE_2);
    }

    @When("$userName was thinking a couple of seconds")
    public void sleep(@Named("userName") final String userName) throws InterruptedException {
        Thread.sleep(2000);
    }

    @Then("User $userName is seeing the bet activity stream item with his bet details")
    public void user1SeeOwnBetActivityWithBetDetails(@Named("userName") final String userName) {
        ActivityParams expectedParams = ActivityParams.builder()
                .user(user1)
                .match(DATA_HOLDER.getMatch())
                .score1(USER1_BET1_SCORE_1)
                .score2(USER1_BET1_SCORE_2)
                .oldScore1(0)
                .oldScore2(0)
                .betPoints(null)
                .showBetData(true)
                .showOldScores(false)
                .build();

        List<ActivityStreamDTO> ppActivities = assertPortalPageActivitiesCount(1);
        assertActivity(ppActivities.get(0), expectedParams);

        List<ActivityStreamDTO> userActivities = assertUserActivitiesCount(user1.getUserId(), 1);
        assertActivity(userActivities.get(0), expectedParams);

        List<ActivityStreamDTO> matchActivities = assertMatchActivitiesCount(DATA_HOLDER.getMatchId(), 1);
        assertActivity(matchActivities.get(0), expectedParams);
    }

    @Then("Another user $userName is seeing the bet activity stream item without bet details")
    public void user2SeeBetOfUser1ActivityWithoutBetDetails(@Named("userName") final String userName) {
        ActivityParams expectedParams = ActivityParams.builder()
                .user(user1)
                .match(DATA_HOLDER.getMatch())
                .score1(0)
                .score2(0)
                .oldScore1(0)
                .oldScore2(0)
                .betPoints(null)
                .showBetData(false)
                .showOldScores(false)
                .build();

        List<ActivityStreamDTO> ppActivities = assertPortalPageActivitiesCount(1);
        assertActivity(ppActivities.get(0), expectedParams);

        assertUserActivitiesCount(user2.getUserId(), 0);

        List<ActivityStreamDTO> matchActivities = assertMatchActivitiesCount(DATA_HOLDER.getMatchId(), 1);
        assertActivity(matchActivities.get(0), expectedParams);
    }

    @Then("Another user $userName is seeing the bet activity stream item with his bet details")
    public void user2SeeOwnBetActivityWithBetDetails(@Named("userName") final String userName) {
        ActivityParams expectedParams = ActivityParams.builder()
                .user(user2)
                .match(DATA_HOLDER.getMatch())
                .score1(USER2_BET1_SCORE_1)
                .score2(USER2_BET1_SCORE_2)
                .oldScore1(0)
                .oldScore2(0)
                .betPoints(null)
                .showBetData(true)
                .showOldScores(false)
                .build();

        List<ActivityStreamDTO> ppActivities = assertPortalPageActivitiesCount(2);
        assertActivity(ppActivities.get(0), expectedParams);

        List<ActivityStreamDTO> userActivities = assertUserActivitiesCount(user2.getUserId(), 1);
        assertActivity(userActivities.get(0), expectedParams);

        List<ActivityStreamDTO> matchActivities = assertMatchActivitiesCount(DATA_HOLDER.getMatchId(), 2);
        assertActivity(matchActivities.get(0), expectedParams);
    }

    @Then("Another user $userName is seeing the a newly created bet activity with his bet details")
    public void user2SeeNewOwnBetActivityWithBetDetails(@Named("userName") final String userName) {
        ActivityParams expectedParams = ActivityParams.builder()
                .user(user2)
                .match(DATA_HOLDER.getMatch())
                .score1(USER2_BET2_SCORE_1)
                .score2(USER2_BET2_SCORE_2)
                .oldScore1(USER2_BET1_SCORE_1)
                .oldScore2(USER2_BET1_SCORE_2)
                .betPoints(null)
                .showBetData(true)
                .showOldScores(true)
                .build();

        List<ActivityStreamDTO> ppActivities = assertPortalPageActivitiesCount(3);
        assertActivity(ppActivities.get(0), expectedParams);

        List<ActivityStreamDTO> userActivities = assertUserActivitiesCount(user2.getUserId(), 2);
        assertActivity(userActivities.get(0), expectedParams);

        List<ActivityStreamDTO> matchActivities = assertMatchActivitiesCount(DATA_HOLDER.getMatchId(), 3);
        assertActivity(matchActivities.get(0), expectedParams);
    }

    @Then("User $userName is seeing the change bet activity of User2 still without bet details")
    public void user1SeeChangedBetActivityOfUser2WithoutBetDetails(@Named("userName") final String userName) {
        ActivityParams expectedParams = ActivityParams.builder()
                .user(user2)
                .match(DATA_HOLDER.getMatch())
                .score1(0)
                .score2(0)
                .oldScore1(0)
                .oldScore2(0)
                .betPoints(null)
                .showBetData(false)
                .showOldScores(false)
                .build();

        List<ActivityStreamDTO> ppActivities = assertPortalPageActivitiesCount(3);
        assertActivity(ppActivities.get(0), expectedParams);

        List<ActivityStreamDTO> userActivities = assertUserActivitiesCount(user2.getUserId(), 2);
        assertActivity(userActivities.get(0), expectedParams);

        List<ActivityStreamDTO> matchActivities = assertMatchActivitiesCount(DATA_HOLDER.getMatchId(), 3);
        assertActivity(matchActivities.get(0), expectedParams);
    }

    @Then("User $userName is seeing his bet activity with bet details and match points")
    public void user1SeeHisBetActivityWithBetDetailsAndMatchPoints(@Named("userName") final String userName) {
        UsersRatingPositionDTO betPoints = new UsersRatingPositionDTO(user1, 6, 1.00F);
        ActivityParams expectedParams = ActivityParams.builder()
                .user(user1)
                .match(DATA_HOLDER.getMatch())
                .score1(USER1_BET1_SCORE_1)
                .score2(USER1_BET1_SCORE_2)
                .oldScore1(0)
                .oldScore2(0)
                .betPoints(betPoints)
                .showBetData(true)
                .showOldScores(false)
                .build();

        List<ActivityStreamDTO> ppActivities = assertPortalPageActivitiesCount(3);
        assertActivity(ppActivities.get(2), expectedParams);

        List<ActivityStreamDTO> userActivities = assertUserActivitiesCount(user1.getUserId(), 1);
        assertActivity(userActivities.get(0), expectedParams);

        List<ActivityStreamDTO> matchActivities = assertMatchActivitiesCount(DATA_HOLDER.getMatchId(), 4);
        assertActivity(matchActivities.get(3), expectedParams);
    }

    @Then("User $userName is seeing User2 original bet activity with bet details and match points")
    public void user1SeeUser2OriginalBetActivityWithBetDetailsAndMatchPoints(@Named("userName") final String userName) {
        UsersRatingPositionDTO betPoints = new UsersRatingPositionDTO(user2, -1, 0.00F);
        ActivityParams expectedParams = ActivityParams.builder()
                .user(user2)
                .match(DATA_HOLDER.getMatch())
                .score1(USER2_BET1_SCORE_1)
                .score2(USER2_BET1_SCORE_2)
                .oldScore1(0)
                .oldScore2(0)
                .betPoints(betPoints)
                .showBetData(true)
                .showOldScores(false)
                .build();

        List<ActivityStreamDTO> ppActivities = assertPortalPageActivitiesCount(3);
        assertActivity(ppActivities.get(1), expectedParams);

        List<ActivityStreamDTO> userActivities = assertUserActivitiesCount(user2.getUserId(), 2);
        assertActivity(userActivities.get(1), expectedParams);

        List<ActivityStreamDTO> matchActivities = assertMatchActivitiesCount(DATA_HOLDER.getMatchId(), 4);
        assertActivity(matchActivities.get(2), expectedParams);
    }

    @Then("User $userName is seeing User2 changed bet activity with bet details and match points")
    public void user1SeeUser2ChangedBetActivityWithBetDetailsAndMatchPoints(@Named("userName") final String userName) {
        UsersRatingPositionDTO betPoints = new UsersRatingPositionDTO(user2, -1, 0.00F);
        ActivityParams expectedParams = ActivityParams.builder()
                .user(user2)
                .match(DATA_HOLDER.getMatch())
                .score1(USER2_BET2_SCORE_1)
                .score2(USER2_BET2_SCORE_2)
                .oldScore1(USER2_BET1_SCORE_1)
                .oldScore2(USER2_BET1_SCORE_2)
                .betPoints(betPoints)
                .showBetData(true)
                .showOldScores(true)
                .build();

        List<ActivityStreamDTO> ppActivities = assertPortalPageActivitiesCount(3);
        assertActivity(ppActivities.get(0), expectedParams);

        List<ActivityStreamDTO> userActivities = assertUserActivitiesCount(user2.getUserId(), 2);
        assertActivity(userActivities.get(0), expectedParams);

        List<ActivityStreamDTO> matchActivities = assertMatchActivitiesCount(DATA_HOLDER.getMatchId(), 4);
        assertActivity(matchActivities.get(1), expectedParams);
    }

    @Then("Match finish activity is not shown on portal page and in user cards")
    public void noMatchActivityOnPPAndUserCards() {
        assertPortalPageActivitiesCount(3);// there is no match finished activity on PP
        assertUserActivitiesCount(user1.getUserId(), 1);// only user bet
        assertUserActivitiesCount(user2.getUserId(), 2);// user bet and change of bet
    }

    @Then("$userName is seeing match finished activity in match card")
    public void currentUserSeeMatchFinishedActivity(@Named("userName") final String userName) {
        ActivityParams expectedParams = ActivityParams.builder()
                .user(null)
                .match(DATA_HOLDER.getMatch())
                .score1(0)
                .score2(0)
                .oldScore1(0)
                .oldScore2(0)
                .betPoints(null)
                .showBetData(false)
                .showOldScores(false)
                .build();

        List<ActivityStreamDTO> matchActivities = assertMatchActivitiesCount(DATA_HOLDER.getMatchId(), 4);
        assertActivity(matchActivities.get(0), expectedParams);
    }

    @When("$userName finished the match with User1 score")
    public void adminFinishedMatch(@Named("userName") final String userName) {
        MatchEditDTO match = DATA_HOLDER.getMatch();
        match.setScore1(USER1_BET1_SCORE_1);
        match.setScore2(USER1_BET1_SCORE_2);
        match.setMatchFinished(true);
        DATA_HOLDER.setMatch(AdminMatchEndPointsHandler.update(match));
    }

    @Given("$userName is registered user")
    @Aliases(values = {"$userName is another registered user"})
    public void ignore(@Named("userName") final String userName) {

    }

    private void assertActivity(final ActivityStreamDTO ppActivity, final ActivityParams expectedParams) {
        assertThat("Activity ID should not be null", ppActivity.getActivityStreamEntryTypeId(), notNullValue());
        assertThat("Activity time should not be null", ppActivity.getActivityTime(), notNullValue());
        assertThat("Activity has wrong userId", ppActivity.getActivityOfUser(), is(expectedParams.getUser()));

        ComparisonUtils.assertTheSame(ppActivity.getMatch(), expectedParams.getMatch());
        assertThat("Activity score1 is wrong", ppActivity.getScore1(), is(expectedParams.getScore1()));
        assertThat("Activity score2 is wrong", ppActivity.getScore2(), is(expectedParams.getScore2()));
        assertThat("Activity old score1 is wrong", ppActivity.getOldScore1(), is(expectedParams.getOldScore1()));
        assertThat("Activity old score2 is wrong", ppActivity.getOldScore2(), is(expectedParams.getOldScore2()));
        assertThat("Activity has wrong bet points", ppActivity.getActivityBetPoints(), is(expectedParams.getBetPoints()));
        assertThat("Activity bet visibility assertion error", ppActivity.isShowBetData(), is(expectedParams.isShowBetData()));
        assertThat("Activity bet old score visibility assertion error", ppActivity.isShowOldScores(), is(expectedParams.isShowOldScores()));
    }

    private List<ActivityStreamDTO> assertPortalPageActivitiesCount(final int count) {
        List<ActivityStreamDTO> portalPageActivities = ActivityEndPointsHandler.getPortalPageActivities();
        assertThat("Portal page activity list should not be null", portalPageActivities, notNullValue());
        assertThat("Wrong activities count on Portal page", portalPageActivities, hasSize(count));
        return portalPageActivities;
    }

    private List<ActivityStreamDTO> assertMatchActivitiesCount(final int matchId, final int count) {
        List<ActivityStreamDTO> matchActivities = ActivityEndPointsHandler.getMatchPageActivities(matchId);
        assertThat("Match activity list should not be null", matchActivities, notNullValue());
        assertThat("Wrong match's activities count", matchActivities, hasSize(count));
        return matchActivities;
    }

    private List<ActivityStreamDTO> assertUserActivitiesCount(final int userId, final int count) {
        List<ActivityStreamDTO> userActivities = ActivityEndPointsHandler.getUserPageActivities(userId);
        assertThat("User activity list should not be null", userActivities, notNullValue());
        assertThat("Wrong user's activities count", userActivities, hasSize(count));
        return userActivities;
    }

    @Getter
    @Setter
    private static class DataHolder {

        private CategoryEditDTO category;
        private TeamEditDTO team1;
        private TeamEditDTO team2;
        private CupEditDTO cup;
        private MatchEditDTO match;

        public void cleanupAndCreateData() {
            DataCleanUpUtils.cleanupAll();

            AuthEndPointsHandler.loginAsAdmin();

            PointsCalculationStrategyEditDTO pointsStrategy = AdminTestDataGenerator.createPointsStrategy();
            category = AdminTestDataGenerator.createCategory();
            team1 = AdminTestDataGenerator.createTeam(category.getCategoryId());
            team2 = AdminTestDataGenerator.createTeam(category.getCategoryId());
            cup = AdminCupEndPointsHandler.create(CupTemplater.random(category.getCategoryId(), pointsStrategy.getPcsId()).inDays(7).build());
            match = AdminMatchEndPointsHandler.create(MatchTemplater.random(cup.getCupId(), team1.getTeamId(), team2.getTeamId()).future(1).build());
        }

        public int getMatchId() {
            return match.getMatchId();
        }
    }

    @Builder
    @Getter
    private static class ActivityParams {
        private UserDTO user;
        private MatchEditDTO match;
        private int score1;
        private int score2;
        private int oldScore1;
        private int oldScore2;
        private UsersRatingPositionDTO betPoints;
        private boolean showBetData;
        private boolean showOldScores;
    }
}

package betmen.rests.tests;

import betmen.dto.dto.ActivityStreamDTO;
import betmen.dto.dto.BetDTO;
import betmen.dto.dto.UserDTO;
import betmen.dto.dto.admin.CategoryEditDTO;
import betmen.dto.dto.admin.CupEditDTO;
import betmen.dto.dto.admin.MatchEditDTO;
import betmen.dto.dto.admin.PointsCalculationStrategyEditDTO;
import betmen.dto.dto.admin.TeamEditDTO;
import betmen.rests.common.ResponseStatus;
import betmen.rests.common.UserRegData;
import betmen.rests.utils.RandomUtils;
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
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class ActivityStreamRestControllerRestTest extends AbstractCleanableRestTest {

    private UserRegData userData1;
    private UserDTO user1;

    private UserRegData userData2;
    private UserDTO user2;

    @Override
    protected void beforeTest() {
        userData1 = RandomUtils.randomUser();
        user1 = AuthEndPointsHandler.registerNewUser(userData1);

        userData2 = RandomUtils.randomUser();
        user2 = AuthEndPointsHandler.registerNewUser(userData2);
    }

    @BeforeMethod
    public void beforeEachMethod() {
        AuthEndPointsHandler.logout();
    }

    @Test
    public void shouldNotAllowToGetAccessToMatchForAnonymousUser() {
        ActivityEndPointsHandler.getPortalPageActivities(ResponseStatus.UNAUTHORIZED);
        ActivityEndPointsHandler.getMatchPageActivities(0, ResponseStatus.UNAUTHORIZED);
        ActivityEndPointsHandler.getUserPageActivities(0, ResponseStatus.UNAUTHORIZED);
    }

    @Test
    public void shouldGetEmptyListsIfNoActivities() {
        cleanupAll();

        AuthEndPointsHandler.login(userData1);

        assertPortalPageActivities(0);
        assertUserActivities(user1.getUserId(), 0);
        assertMatchActivities(0, 0);
    }

    @Test
    public void shouldNotCreateActivityIfMatchCreated() {
        DataHolder dataHolder = new DataHolder();

        AuthEndPointsHandler.login(userData1);

        assertPortalPageActivities(0);
        assertUserActivities(user1.getUserId(), 0);
        assertMatchActivities(dataHolder.getMatchId(), 0);
    }

    @Test
    public void shouldCreateActivityIfFutureMatchBetCreated() {
        DataHolder dataHolder = new DataHolder();

        AuthEndPointsHandler.login(userData1);

        BetDTO bet = BetEndPointsHandler.make(dataHolder.getMatchId(), 1, 2);

        assertPortalPageActivities(1);
        assertUserActivities(user1.getUserId(), 1);
        assertMatchActivities(dataHolder.getMatch().getMatchId(), 1);
    }

    private List<ActivityStreamDTO> assertPortalPageActivities(final int count) {
        List<ActivityStreamDTO> portalPageActivities = ActivityEndPointsHandler.getPortalPageActivities();
        assertThat(portalPageActivities, hasSize(count));
        return portalPageActivities;
    }

    private List<ActivityStreamDTO> assertMatchActivities(final int matchId, final int count) {
        List<ActivityStreamDTO> matchActivities = ActivityEndPointsHandler.getMatchPageActivities(matchId);
        assertThat(matchActivities, hasSize(count));
        return matchActivities;
    }

    private List<ActivityStreamDTO> assertUserActivities(final int userId, final int count) {
        List<ActivityStreamDTO> userActivities = ActivityEndPointsHandler.getUserPageActivities(userId);
        assertThat(userActivities, hasSize(count));
        return userActivities;
    }

    @Getter
    @Setter
    private class DataHolder {

        private CategoryEditDTO category;
        private TeamEditDTO team1;
        private TeamEditDTO team2;
        private CupEditDTO cup;
        private MatchEditDTO match;

        public DataHolder() {
            cleanupAll();

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
}

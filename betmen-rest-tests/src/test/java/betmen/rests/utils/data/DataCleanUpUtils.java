package betmen.rests.utils.data;

import betmen.dto.dto.MatchSearchModelDto;
import betmen.dto.dto.admin.CategoryEditDTO;
import betmen.dto.dto.admin.MatchEditDTO;
import betmen.dto.dto.admin.PointsCalculationStrategyEditDTO;
import betmen.dto.dto.admin.SportKindEditDTO;
import betmen.rests.utils.helpers.AuthEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminCategoryEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminCupEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminMatchEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminPointsStrategyEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminSportEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminTeamEndPointsHandler;
import org.apache.log4j.Logger;
import org.springframework.util.Assert;

import java.util.List;

public class DataCleanUpUtils {

    private static final Logger LOGGER = Logger.getLogger(DataCleanUpUtils.class);

    public static final MatchSearchModelDto SEARCH_MODEL = new MatchSearchModelDto();

    static {
        SEARCH_MODEL.setShowFinished(true);
        SEARCH_MODEL.setShowFutureMatches(true);
    }

    public static void cleanupAll() {
        cleanupMatches();
        cleanupCups();
        cleanupTeams();
        cleanupCategories();
        cleanupSports();
        cleanupPointsCalculationStrategies();
    }

    public static void cleanupMatches() {
        Counter counter = new Counter();
        AuthEndPointsHandler.loginAsAdmin();
        AdminCupEndPointsHandler.getAllCups().stream()
                .forEach(cup -> {
                    SEARCH_MODEL.setCupId(cup.getCupId());
                    List<MatchEditDTO> items = AdminMatchEndPointsHandler.getItems(SEARCH_MODEL);
                    counter.setTotal(items.size());
                    items.stream()
                            .forEach(match -> {
                                counter.increase();
                                AdminMatchEndPointsHandler.delete(match.getMatchId());
                            });
                });
        AuthEndPointsHandler.logout();
    }

    public static void cleanupTeams() {
        AuthEndPointsHandler.loginAsAdmin();
        AdminCategoryEndPointsHandler.getItems().stream()
                .forEach(DataCleanUpUtils::cleanupTeams);
        AuthEndPointsHandler.logout();
    }

    public static void cleanupTeams(final CategoryEditDTO category) {
        AdminTeamEndPointsHandler.getTeamOfCategory(category.getCategoryId()).stream().forEach(team -> AdminTeamEndPointsHandler.delete(team.getTeamId()));
    }

    public static void cleanupCups() {
        AuthEndPointsHandler.loginAsAdmin();
        AdminCupEndPointsHandler.getCups().stream()
                .forEach(cup -> {
                    SEARCH_MODEL.setCupId(cup.getCupId());
                    List<MatchEditDTO> cupMatches = AdminMatchEndPointsHandler.getItems(SEARCH_MODEL);
                    Assert.isTrue(cupMatches.size() == 0, String.format("Cup %s has matches, can not delete the cup", cup));
                    AdminCupEndPointsHandler.delete(cup.getCupId());
                });
        AuthEndPointsHandler.logout();
    }

    public static void cleanupCategories() {
        AuthEndPointsHandler.loginAsAdmin();
        AdminCategoryEndPointsHandler.getItems().stream().forEach(category -> AdminCategoryEndPointsHandler.delete(category.getCategoryId()));
        AuthEndPointsHandler.logout();
    }

    public static void cleanupSports() {
        Counter counter = new Counter();
        AuthEndPointsHandler.loginAsAdmin();
        List<SportKindEditDTO> items = AdminSportEndPointsHandler.getItems();
        counter.setTotal(items.size());
        items.stream().forEach(sport -> {
            counter.increase();
            AdminSportEndPointsHandler.delete(sport.getSportKindId());
        });
        AuthEndPointsHandler.logout();
    }

    public static void cleanupPointsCalculationStrategies() {
        Counter counter = new Counter();
        AuthEndPointsHandler.loginAsAdmin();
        List<PointsCalculationStrategyEditDTO> items = AdminPointsStrategyEndPointsHandler.getItems();
        counter.setTotal(items.size());
        items.stream().forEach(sport -> {
            counter.increase();
            AdminPointsStrategyEndPointsHandler.delete(sport.getPcsId());
        });
        AuthEndPointsHandler.logout();
    }

    private static void log(final String x) {
        LOGGER.debug(x);
    }

    private static class Counter {
        private int count = 0;
        private int total = 0;

        public int getCount() {
            return count;
        }

        public void increase() {
            this.count++;
        }

        public void setCount(final int count) {
            this.count = count;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(final int total) {
            this.total = total;
        }
    }
}

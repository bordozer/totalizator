package betmen.rests.tests;

import betmen.dto.dto.UserDTO;
import betmen.dto.dto.admin.CategoryEditDTO;
import betmen.dto.dto.admin.CupEditDTO;
import betmen.dto.dto.admin.MatchEditDTO;
import betmen.dto.dto.admin.PointsCalculationStrategyEditDTO;
import betmen.dto.dto.admin.SportKindEditDTO;
import betmen.dto.dto.admin.TeamEditDTO;
import betmen.rests.utils.RandomUtils;
import betmen.rests.utils.data.DataCleanUpUtils;
import betmen.rests.utils.data.generator.AdminTestDataGenerator;
import betmen.rests.utils.data.templater.CupTemplater;
import betmen.rests.utils.data.templater.MatchTemplater;
import betmen.rests.utils.helpers.AuthEndPointsHandler;
import betmen.rests.utils.helpers.BetEndPointsHandler;
import betmen.rests.utils.helpers.UserFavoritesEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminCupEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminMatchEndPointsHandler;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AdminMatchesGenerator {

    private static final LocalDateTime USER_CARD_TIME_DATE = LocalDateTime.now();
    private static final LocalDate ON_DATE = USER_CARD_TIME_DATE.toLocalDate();

    @BeforeTest
    public void initTest() {
        DataCleanUpUtils.cleanupAll();
    }

    @Test
    public void shouldGetAnotherUserCardData() {
        AuthEndPointsHandler.loginAsAdmin();

        PointsCalculationStrategyEditDTO pcs = AdminTestDataGenerator.createPointsStrategy();
        SportKindEditDTO sport1 = AdminTestDataGenerator.createSport();
        CategoryEditDTO category1 = AdminTestDataGenerator.createCategory(sport1.getSportKindId());
        UserFavoritesEndPointsHandler.addCategoryToFavorites(category1.getCategoryId());
        TeamEditDTO team11 = AdminTestDataGenerator.createTeam(category1.getCategoryId());
        TeamEditDTO team12 = AdminTestDataGenerator.createTeam(category1.getCategoryId());
        TeamEditDTO team13 = AdminTestDataGenerator.createTeam(category1.getCategoryId());
        TeamEditDTO team14 = AdminTestDataGenerator.createTeam(category1.getCategoryId());
        TeamEditDTO team15 = AdminTestDataGenerator.createTeam(category1.getCategoryId());
        TeamEditDTO team16 = AdminTestDataGenerator.createTeam(category1.getCategoryId());
        CupEditDTO cup11 = AdminCupEndPointsHandler.create(CupTemplater.random(category1.getCategoryId(), pcs.getPcsId()).future().publicCup().build());
        MatchEditDTO match11 = AdminMatchEndPointsHandler.create(MatchTemplater.random(cup11.getCupId(), team11.getTeamId(), team12.getTeamId()).future().build());
        MatchEditDTO match12 = AdminMatchEndPointsHandler.create(MatchTemplater.random(cup11.getCupId(), team13.getTeamId(), team14.getTeamId()).future().build());
        MatchEditDTO match13 = AdminMatchEndPointsHandler.create(MatchTemplater.random(cup11.getCupId(), team15.getTeamId(), team16.getTeamId()).future().build());

        SportKindEditDTO sport2 = AdminTestDataGenerator.createSport();
        CategoryEditDTO category2 = AdminTestDataGenerator.createCategory(sport2.getSportKindId());
        TeamEditDTO team21 = AdminTestDataGenerator.createTeam(category2.getCategoryId());
        TeamEditDTO team22 = AdminTestDataGenerator.createTeam(category2.getCategoryId());
        TeamEditDTO team23 = AdminTestDataGenerator.createTeam(category2.getCategoryId());
        TeamEditDTO team24 = AdminTestDataGenerator.createTeam(category2.getCategoryId());
        TeamEditDTO team25 = AdminTestDataGenerator.createTeam(category2.getCategoryId());
        TeamEditDTO team26 = AdminTestDataGenerator.createTeam(category2.getCategoryId());
        CupEditDTO cup21 = AdminCupEndPointsHandler.create(CupTemplater.random(category2.getCategoryId(), pcs.getPcsId()).future().publicCup().build());
        MatchEditDTO match21 = AdminMatchEndPointsHandler.create(MatchTemplater.random(cup21.getCupId(), team21.getTeamId(), team22.getTeamId()).future().build());
        MatchEditDTO match22 = AdminMatchEndPointsHandler.create(MatchTemplater.random(cup21.getCupId(), team23.getTeamId(), team24.getTeamId()).future().build());
        MatchEditDTO match23 = AdminMatchEndPointsHandler.create(MatchTemplater.random(cup21.getCupId(), team25.getTeamId(), team26.getTeamId()).future().build());

        BetEndPointsHandler.make(match11.getMatchId(), score(), score());
        BetEndPointsHandler.make(match12.getMatchId(), score(), score());
        BetEndPointsHandler.make(match13.getMatchId(), score(), score());
        BetEndPointsHandler.make(match21.getMatchId(), score(), score());
        BetEndPointsHandler.make(match22.getMatchId(), score(), score());
        BetEndPointsHandler.make(match23.getMatchId(), score(), score());

        registerUserAndMakeBets(match11, match12, match13, match21, match22, match23);
        registerUserAndMakeBets(match11, match12, match13, match21, match22, match23);
        registerUserAndMakeBets(match11, match12, match13, match21, match22, match23);
        registerUserAndMakeBets(match11, match12, match13, match21, match22, match23);

        AuthEndPointsHandler.loginAsAdmin();

        finishMatch(match11);
        finishMatch(match12);
        finishMatch(match13);
        finishMatch(match21);
        finishMatch(match22);
        finishMatch(match23);
    }

    private void registerUserAndMakeBets(final MatchEditDTO match11, final MatchEditDTO match12, final MatchEditDTO match13, final MatchEditDTO match21, final MatchEditDTO match22, final MatchEditDTO match23) {
        UserDTO anotherUser = AuthEndPointsHandler.registerNewUserAndLogin();
        BetEndPointsHandler.make(match11.getMatchId(), score(), score());
        BetEndPointsHandler.make(match12.getMatchId(), score(), score());
        BetEndPointsHandler.make(match13.getMatchId(), score(), score());
        BetEndPointsHandler.make(match21.getMatchId(), score(), score());
        BetEndPointsHandler.make(match22.getMatchId(), score(), score());
        BetEndPointsHandler.make(match23.getMatchId(), score(), score());
    }

    private void finishMatch(final MatchEditDTO match) {
        match.setScore1(score());
        match.setScore1(score());
        match.setMatchFinished(true);
        AdminMatchEndPointsHandler.update(match);
    }

    private int score() {
        return RandomUtils.randomInt(0, 8);
    }
}

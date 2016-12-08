package betmen.rests.utils.data.generator;

import betmen.dto.dto.admin.CategoryEditDTO;
import betmen.dto.dto.admin.CupEditDTO;
import betmen.dto.dto.admin.MatchEditDTO;
import betmen.dto.dto.admin.PointsCalculationStrategyEditDTO;
import betmen.dto.dto.admin.SportKindEditDTO;
import betmen.dto.dto.admin.TeamEditDTO;
import betmen.rests.utils.RandomUtils;
import betmen.rests.utils.data.builders.CategoryEditDtoBuilder;
import betmen.rests.utils.data.builders.PointsCalculationStrategyEditDtoBuilder;
import betmen.rests.utils.data.builders.SportKindEditDtoBuilder;
import betmen.rests.utils.data.builders.TeamEditDtoBuilder;
import betmen.rests.utils.data.templater.CupTemplater;
import betmen.rests.utils.data.templater.MatchTemplater;
import betmen.rests.utils.helpers.admin.AdminCategoryEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminCupEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminMatchEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminPointsStrategyEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminSportEndPointsHandler;
import betmen.rests.utils.helpers.admin.AdminTeamEndPointsHandler;

public class AdminTestDataGenerator {

    public static SportKindEditDTO createSport() {
        return AdminSportEndPointsHandler.create(SportKindEditDtoBuilder.construct());
    }

    public static PointsCalculationStrategyEditDTO createPointsStrategy() {
        return AdminPointsStrategyEndPointsHandler.create(PointsCalculationStrategyEditDtoBuilder.construct());
    }

    public static CategoryEditDTO createCategory() {
        return createCategory(createSport().getSportKindId());
    }

    public static CategoryEditDTO createCategory(final int sportKindId) {
        return AdminCategoryEndPointsHandler.create(CategoryEditDtoBuilder.construct(sportKindId));
    }

    public static CupEditDTO createRandomCup() {
        return createRandomCup(createCategory().getCategoryId(), createPointsStrategy().getPcsId());
    }

    public static CupEditDTO createRandomCup(final int categoryId, final int pointsStrategyId) {
        return AdminCupEndPointsHandler.create(CupTemplater.random(categoryId, pointsStrategyId).build());
    }

    public static TeamEditDTO createTeam(final int categoryId) {
        TeamEditDTO dto = TeamEditDtoBuilder.construct(RandomUtils.teamName(), categoryId);
        return AdminTeamEndPointsHandler.create(dto);
    }

    public static TeamEditDTO createTeamAndActivateForCup(final int categoryId, final int cupId) {
        TeamEditDTO dto = TeamEditDtoBuilder.construct(RandomUtils.teamName(), categoryId);
        return AdminTeamEndPointsHandler.create(cupId, dto);
    }

    public static MatchEditDTO createRandomMatch() {
        CupEditDTO cup = createRandomCup();
        TeamEditDTO team1 = createTeam(cup.getCategoryId());
        TeamEditDTO team2 = createTeam(cup.getCategoryId());
        return createRandomMatch(cup.getCupId(), team1.getTeamId(), team2.getTeamId());
    }

    public static MatchEditDTO createRandomMatch(final int cupId, final int team1Id, final int team2Id) {
        MatchEditDTO dto = MatchTemplater.random(cupId, team1Id, team2Id).build();
        return AdminMatchEndPointsHandler.create(dto);
    }
}

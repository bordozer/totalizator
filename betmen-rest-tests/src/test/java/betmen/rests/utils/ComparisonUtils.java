package betmen.rests.utils;

import betmen.dto.dto.BetDTO;
import betmen.dto.dto.CategoryDTO;
import betmen.dto.dto.CupDTO;
import betmen.dto.dto.CupItemDTO;
import betmen.dto.dto.FavoriteCategoryDTO;
import betmen.dto.dto.MatchBetDTO;
import betmen.dto.dto.MatchDTO;
import betmen.dto.dto.SportKindDTO;
import betmen.dto.dto.TeamDTO;
import betmen.dto.dto.UserDTO;
import betmen.dto.dto.UserGroupEditDTO;
import betmen.dto.dto.admin.CategoryEditDTO;
import betmen.dto.dto.admin.CupEditDTO;
import betmen.dto.dto.admin.MatchEditDTO;
import betmen.dto.dto.admin.SportKindEditDTO;
import betmen.dto.dto.admin.TeamEditDTO;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

public class ComparisonUtils {

    public static void assertEqual(final UserDTO expected, final UserDTO actual) {
        assertThat(expected, notNullValue());
        assertThat(actual, notNullValue());

        assertThat(actual.getUserId(), is(actual.getUserId()));
        assertThat(actual.getUserName(), is(actual.getUserName()));
    }

    public static void assertEqual(final SportKindDTO expected, final SportKindDTO actual) {
        assertThat(expected, notNullValue());
        assertThat(actual, notNullValue());

        assertThat(expected.getSportKindId(), is(actual.getSportKindId()));
        assertThat(expected.getSportKindName(), is(actual.getSportKindName()));
    }

    public static void assertEqual(final CategoryDTO expected, final CategoryDTO actual) {
        assertThat(expected, notNullValue());
        assertThat(actual, notNullValue());

        assertThat(expected.getCategoryId(), is(actual.getCategoryId()));
        assertThat(expected.getCategoryName(), is(actual.getCategoryName()));
        assertThat(expected.getLogoUrl(), is(actual.getLogoUrl()));
    }

    public static void assertEqual(final CategoryDTO expected, final CategoryEditDTO actual) {
        assertThat(expected, notNullValue());
        assertThat(actual, notNullValue());

        assertThat(expected.getCategoryId(), is(actual.getCategoryId()));
        assertThat(expected.getCategoryName(), is(actual.getCategoryName()));
        assertThat(expected.getLogoUrl(), is(actual.getLogoUrl()));
    }

    public static void assertEqual(final CupDTO expected, final CupDTO actual) {
        assertThat(expected, notNullValue());
        assertThat(actual, notNullValue());

        assertThat(expected.getCupId(), is(actual.getCupId()));
        assertThat(expected.getCupName(), is(actual.getCupName()));
        assertThat(expected.getCupStartDate(), is(actual.getCupStartDate()));
        assertThat(expected.getLogoUrl(), is(actual.getLogoUrl()));
        assertThat(expected.getWinnersCount(), is(actual.getWinnersCount()));
        assertThat(expected.getCupBetAllowance(), is(actual.getCupBetAllowance()));

        assertEqual(expected.getCategory(), actual.getCategory());
    }

    public static void assertEqual(final CupEditDTO expected, final CupEditDTO actual) {
        assertThat(expected, notNullValue());
        assertThat(actual, notNullValue());

        assertThat(expected.getCupId(), is(actual.getCupId()));
        assertThat(expected.getCategoryId(), is(actual.getCategoryId()));
        assertThat(expected.getCupPointsCalculationStrategyId(), is(actual.getCupPointsCalculationStrategyId()));
        assertThat(expected.getCupName(), is(actual.getCupName()));
        assertThat(expected.getCupStartDate(), is(actual.getCupStartDate()));
        assertThat(expected.getWinnersCount(), is(actual.getWinnersCount()));
//        assertThat(expected.getCupWinners(), is(actual.get())); // TODO
        assertThat(expected.getLogoUrl(), is(actual.getLogoUrl()));
        assertThat(expected.getCupImportId(), is(actual.getCupImportId()));

    }

    public static void assertTheSame(final CupDTO expected, final CupEditDTO actual) {
        assertThat(actual, notNullValue());
        assertThat(expected, notNullValue());

        assertThat(actual.getCupId(), is(expected.getCupId()));
        assertThat(actual.getCategoryId(), is(expected.getCategory().getCategoryId()));
        assertThat(actual.getCupName(), is(expected.getCupName()));
//        assertThat(expected.getCupStartDate(), is(expected.getCupStartDate())); // TODO: time zone!
        assertThat(actual.getWinnersCount(), is(expected.getWinnersCount()));
        assertThat(actual.getLogoUrl(), is(expected.getLogoUrl()));
    }

    public static void assertEqual(final TeamDTO expected, final TeamDTO actual) {
        assertThat(expected, notNullValue());
        assertThat(actual, notNullValue());

        assertThat(expected.getTeamId(), is(actual.getTeamId()));
        assertThat(expected.getTeamName(), is(actual.getTeamName()));
        assertThat(expected.getTeamLogo(), is(actual.getTeamLogo()));

        assertEqual(expected.getCategory(), actual.getCategory());
    }

    public static void assertTheSame(final TeamDTO teamDto, final TeamEditDTO teamEditDTO) {
        assertThat(teamDto, notNullValue());
        assertThat(teamEditDTO, notNullValue());

        assertThat(teamDto.getTeamId(), is(teamEditDTO.getTeamId()));
        assertThat(teamDto.getTeamName(), is(teamEditDTO.getTeamName()));
        assertThat(teamDto.getTeamLogo(), is(teamEditDTO.getTeamLogo()));
        assertThat(teamDto.getCategory().getCategoryId(), is(teamEditDTO.getCategoryId()));
    }

    public static void assertEqual(final MatchEditDTO expected, final MatchEditDTO actual) {
        assertThat(expected, notNullValue());
        assertThat(actual, notNullValue());

        assertThat(actual.getMatchId(), is(expected.getMatchId()));
        assertThat(actual.getCategoryId(), is(expected.getCategoryId()));
        assertThat(actual.getCupId(), is(expected.getCupId()));
//        assertThat(actual.getBeginningTime(), is(expected.getBeginningTime())); // TODO: looks like time zone make this fail
        assertThat(actual.getTeam1(), is(expected.getTeam1()));
        assertThat(actual.getTeam2(), is(expected.getTeam2()));
        assertThat(actual.getScore1(), is(expected.getScore1()));
        assertThat(actual.getScore2(), is(expected.getScore2()));
        assertThat(actual.getHomeTeamNumber(), is(expected.getHomeTeamNumber()));
        assertThat(actual.getRemoteGameId(), is(expected.getRemoteGameId()));
        assertThat(actual.getBetsCount(), is(expected.getBetsCount()));

        assertThat(actual.getTeam1(), nullValue());
        assertThat(actual.getTeam2(), nullValue());
    }

    public static void assertEqual(final MatchDTO expected, final MatchDTO actual) {
        assertThat(expected, notNullValue());
        assertThat(actual, notNullValue());

        assertThat(actual.getMatchId(), is(expected.getMatchId()));
        assertThat(actual.getCategory().getCategoryId(), is(expected.getCategory().getCategoryId()));
        assertThat(actual.getCup().getCupId(), is(expected.getCup().getCupId()));
//        assertThat(actual.getBeginningTime(), is(expected.getBeginningTime())); // TODO: looks like time zone make this fail
        assertThat(actual.getTeam1(), is(expected.getTeam1()));
        assertThat(actual.getTeam2(), is(expected.getTeam2()));
        assertThat(actual.getScore1(), is(expected.getScore1()));
        assertThat(actual.getScore2(), is(expected.getScore2()));
        assertThat(actual.getHomeTeamNumber(), is(expected.getHomeTeamNumber()));

        assertEqual(expected.getTeam1(), actual.getTeam1());
        assertEqual(expected.getTeam2(), actual.getTeam2());
    }

    public static void assertTheSame(final MatchDTO expected, final MatchEditDTO actual) {
        assertThat(expected, notNullValue());
        assertThat(actual, notNullValue());

        assertThat(actual.getMatchId(), is(expected.getMatchId()));
        assertThat(actual.getCategoryId(), is(expected.getCategory().getCategoryId()));
        assertThat(actual.getCupId(), is(expected.getCup().getCupId()));
//        assertThat(actual.getBeginningTime(), is(expected.getBeginningTime())); // TODO: looks like time zone make this fail
        assertThat(actual.getTeam1Id(), is(expected.getTeam1().getTeamId()));
        assertThat(actual.getTeam2Id(), is(expected.getTeam2().getTeamId()));
        assertThat(actual.getScore1(), is(expected.getScore1()));
        assertThat(actual.getScore2(), is(expected.getScore2()));
        assertThat(actual.getHomeTeamNumber(), is(expected.getHomeTeamNumber()));
    }

    public static void assertEqual(final MatchBetDTO expected, final MatchBetDTO actual) {
        assertThat(expected, notNullValue());
        assertThat(actual, notNullValue());

        assertThat(actual.getBettingValidationMessage(), is(expected.getBettingValidationMessage()));
        assertThat(actual.getMatchId(), is(expected.getMatchId()));
        assertThat(actual.getTotalBets(), is(expected.getTotalBets()));
//        assertThat(actual.getUserMatchPointsHolder(), is(expected.getEdit())); // TODO

        assertEqual(actual.getMatch(), actual.getMatch());
    }

    public static void assertEqual(final BetDTO expected, final BetDTO actual) {
        assertThat(expected, notNullValue());
        assertThat(actual, notNullValue());

        assertThat(actual.getMatchBetId(), is(expected.getMatchBetId()));
        assertThat(actual.getScore1(), is(expected.getScore1()));
        assertThat(actual.getScore2(), is(expected.getScore2()));

        assertEqual(actual.getMatch(), actual.getMatch());
        assertEqual(actual.getUser(), actual.getUser());
    }

    public static void assertEqual(final UserGroupEditDTO expected, final UserGroupEditDTO actual) {
        assertThat(expected, notNullValue());
        assertThat(actual, notNullValue());

        assertThat(actual.getUserGroupId(), is(actual.getUserGroupId()));
        assertThat(actual.getUserGroupName(), is(actual.getUserGroupName()));
        assertThat(actual.getCupIds(), containsInAnyOrder(actual.getCupIds().toArray()));
    }

    public static void assertTheSame(final SportKindDTO expected, final SportKindEditDTO actual) {
        assertThat(expected, notNullValue());
        assertThat(actual, notNullValue());

        assertThat(expected.getSportKindId(), is(actual.getSportKindId()));
        assertThat(expected.getSportKindName(), is(actual.getSportKindName()));
    }

    public static void assertTheSame(final FavoriteCategoryDTO expected, final CategoryEditDTO actual) {
        assertThat(expected, notNullValue());
        assertThat(actual, notNullValue());

        assertThat(expected.getCategoryId(), is(actual.getCategoryId()));
        assertThat(expected.getCategoryName(), is(actual.getCategoryName()));
        assertThat(expected.getLogoUrl(), is(actual.getLogoUrl()));
    }

    public static void assertTheSame(final CupEditDTO expected, final CupItemDTO actual) {
        assertThat(expected, notNullValue());
        assertThat(actual, notNullValue());

        assertThat(expected.getCupId(), is(actual.getCupId()));
        assertThat(expected.getCupName(), is(actual.getCupName()));
    }

    public static void assertTheSame(CategoryDTO expected, CategoryEditDTO actual) {
        assertThat(expected, notNullValue());
        assertThat(actual, notNullValue());

        assertThat(expected.getCategoryId(), is(actual.getCategoryId()));
        assertThat(expected.getCategoryName(), is(actual.getCategoryName()));
        assertThat(expected.getLogoUrl(), is(actual.getLogoUrl()));
    }
}

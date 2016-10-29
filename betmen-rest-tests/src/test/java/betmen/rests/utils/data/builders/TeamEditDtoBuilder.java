package betmen.rests.utils.data.builders;

import betmen.dto.dto.admin.CupWinnerEditDTO;
import betmen.dto.dto.admin.TeamEditDTO;
import betmen.rests.utils.RandomUtils;

public class TeamEditDtoBuilder {

    private int teamId;
    private String teamName;
    private int categoryId;
    private String teamLogo;
    private String teamImportId;
    private boolean teamChecked;
    private int matchCount;

    public static CupWinnerEditDTO convertTeamToCupWinner(final int position, final int teamId) {
        CupWinnerEditDTO cupWinner = new CupWinnerEditDTO();
        cupWinner.setCupPosition(position);
        cupWinner.setTeamId(teamId);

        return cupWinner;
    }

    public TeamEditDtoBuilder withId(final int teamId) {
        this.teamId = teamId;
        return this;
    }

    public TeamEditDtoBuilder withName(final String teamName) {
        this.teamName = teamName;
        return this;
    }

    public TeamEditDtoBuilder withCategory(final int categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public TeamEditDtoBuilder withTeamLogo(final String teamLogo) {
        this.teamLogo = teamLogo;
        return this;
    }

    public TeamEditDtoBuilder withImportId(final String teamImportId) {
        this.teamImportId = teamImportId;
        return this;
    }

    public TeamEditDtoBuilder withChecked(final boolean teamChecked) {
        this.teamChecked = teamChecked;
        return this;
    }

    public TeamEditDtoBuilder withMatchCount(final int matchCount) {
        this.matchCount = matchCount;
        return this;
    }

    public TeamEditDTO build() {
        TeamEditDTO team = new TeamEditDTO();
        team.setTeamId(teamId);
        team.setTeamName(teamName);
        team.setCategoryId(categoryId);
        team.setTeamLogo(teamLogo);
        team.setTeamChecked(teamChecked);
        team.setMatchCount(matchCount);
        team.setTeamImportId(teamImportId);

        return team;
    }

    public static TeamEditDTO construct(final int categoryId) {
        return construct(RandomUtils.teamName(), categoryId);
    }

    public static TeamEditDTO construct(final String teamName, final int categoryId) {
        return new TeamEditDtoBuilder()
                .withName(teamName)
                .withCategory(categoryId)
                .build();
    }
}

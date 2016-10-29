package betmen.rests.utils.data.templater;

import betmen.dto.dto.admin.MatchEditDTO;
import betmen.rests.utils.RandomUtils;
import betmen.rests.utils.data.builders.MatchEditDtoBuilder;

import java.time.LocalDateTime;

public class MatchTemplater {

    private final MatchEditDtoBuilder builder = new MatchEditDtoBuilder();

    private MatchTemplater() {
    }

    public static MatchTemplater random(final int cupId, final int team1Id, final int team2Id) {
        MatchTemplater templater = new MatchTemplater();
        boolean finished = RandomUtils.randomBoolean();
        templater.builder().withCupId(cupId)
                .startingAt(RandomUtils.randomTime())
                .withTeam1Id(team1Id)
                .withTeam2Id(team2Id)
                .finished(finished)
                .withScore1(finished ? RandomUtils.randomInt(4) : 0)
                .withScore2(finished ? RandomUtils.randomInt(4) : 0)
                .withHomeTeamNumber(RandomUtils.randomInt(1, 2))
                .withDescription("Match description")
                .withImportId("MATCH_IMPORT_ID");
        return templater;
    }

    public MatchEditDtoBuilder future() {
        return builder.withBeginningTime(LocalDateTime.now().plusHours(1).plusMinutes(31)).withScore1(0).withScore2(0).finished(false);
    }

    public MatchEditDtoBuilder current() {
        return builder.withBeginningTime(LocalDateTime.now().minusMinutes(19)).finished(false);
    }

    public MatchEditDtoBuilder finished(final int score1, final int score2) {
        return builder.withBeginningTime(LocalDateTime.now().minusDays(1).minusMinutes(17)).finished(true).withScore1(score1).withScore2(score2);
    }

    public MatchEditDtoBuilder team1Win() {
        return builder.withScore1(4).withScore2(1);
    }

    public MatchEditDtoBuilder team2Win() {
        return builder.withScore1(2).withScore2(3);
    }

    public MatchEditDtoBuilder team1Home() {
        return builder.withHomeTeamNumber(1);
    }

    public MatchEditDtoBuilder team2Home() {
        return builder.withHomeTeamNumber(2);
    }

    public MatchEditDTO build() {
        return builder.build();
    }

    public MatchEditDtoBuilder builder() {
        return builder;
    }
}

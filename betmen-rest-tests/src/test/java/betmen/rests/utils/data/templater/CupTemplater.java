package betmen.rests.utils.data.templater;

import betmen.dto.dto.admin.CupEditDTO;
import betmen.dto.dto.admin.CupWinnerEditDTO;
import betmen.rests.utils.RandomUtils;
import betmen.rests.utils.data.builders.CupEditDtoBuilder;
import com.google.common.collect.Lists;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

public class CupTemplater {

    private static final int DEFAULT_WINNERS_COUNT = 2;

    private final CupEditDtoBuilder builder = new CupEditDtoBuilder();

    public CupTemplater() {
    }

    public static CupTemplater random(final int categoryId, final int pointsStrategyId) {
        CupTemplater templater = new CupTemplater();
        templater.builder().withCategory(categoryId)
                .withPointsStrategyId(pointsStrategyId)
                .withName(RandomUtils.cupName())
                .withStartDate(RandomUtils.randomTime())
                .withWinnersCount(DEFAULT_WINNERS_COUNT)
                .withWinners(Collections.emptyList())
                .publicCup(RandomUtils.randomBoolean())
                .withLogo(RandomUtils.UUID())
                .withImportId(RandomUtils.UUID());
        return templater;
    }

    public CupTemplater name(final String name) {
        builder.withName(name);
        return this;
    }

    public CupTemplater future() {
        builder.withStartDate(LocalDateTime.now().plusMonths(1)).withWinners(Collections.emptyList());
        return this;
    }

    public CupTemplater inHour(final int hours) {
        builder.withStartDate(LocalDateTime.now().plusHours(hours)).withWinners(Collections.emptyList());
        return this;
    }

    public CupTemplater inDays(final int days) {
        builder.withStartDate(LocalDateTime.now().plusDays(days)).withWinners(Collections.emptyList());
        return this;
    }

    public CupTemplater current() {
        builder.withStartDate(LocalDateTime.now().minusMonths(1)).withWinners(Collections.emptyList());
        return this;
    }

    public CupTemplater finished(final List<CupWinnerEditDTO> cupWinners) {
        Assert.notEmpty(cupWinners, "Cup winners were not provided (existing teams needed)");
        builder.withStartDate(LocalDateTime.now().minusMonths(11)).withWinnersCount(1).withWinners(cupWinners);
        return this;
    }

    public CupTemplater finished(final CupWinnerEditDTO cupWinners) {
        return finished(Lists.newArrayList(cupWinners));
    }

    public CupTemplater publicCup() {
        builder.publicCup(true);
        return this;
    }

    public CupTemplater privateCup() {
        builder.publicCup(false);
        return this;
    }

    public CupEditDTO build() {
        return builder.build();
    }

    public CupEditDtoBuilder builder() {
        return builder;
    }
}

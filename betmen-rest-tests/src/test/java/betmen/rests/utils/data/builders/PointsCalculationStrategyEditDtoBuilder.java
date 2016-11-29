package betmen.rests.utils.data.builders;

import betmen.dto.dto.admin.PointsCalculationStrategyEditDTO;
import betmen.rests.utils.RandomUtils;

public class PointsCalculationStrategyEditDtoBuilder {
    private int pcsId;
    private String name;
    private int pointsForMatchScore;
    private int pointsForMatchWinner;
    private int pointsDelta;
    private int pointsForBetWithinDelta;

    public PointsCalculationStrategyEditDtoBuilder withId(final int pcsId) {
        this.pcsId = pcsId;
        return this;
    }

    public PointsCalculationStrategyEditDtoBuilder withName(final String strategyName) {
        this.name = strategyName;
        return this;
    }

    public PointsCalculationStrategyEditDtoBuilder withPointsForMatchScore(final int pointsForMatchScore) {
        this.pointsForMatchScore = pointsForMatchScore;
        return this;
    }

    public PointsCalculationStrategyEditDtoBuilder withPointsForMatchWinner(final int pointsForMatchWinner) {
        this.pointsForMatchWinner = pointsForMatchWinner;
        return this;
    }

    public PointsCalculationStrategyEditDtoBuilder withPointsDelta(final int pointsDelta) {
        this.pointsDelta = pointsDelta;
        return this;
    }

    public PointsCalculationStrategyEditDtoBuilder withPointsForBetWithinDelta(final int pointsForBetWithinDelta) {
        this.pointsForBetWithinDelta = pointsForBetWithinDelta;
        return this;
    }

    public PointsCalculationStrategyEditDTO build() {
        PointsCalculationStrategyEditDTO dto = new PointsCalculationStrategyEditDTO();
        dto.setPcsId(pcsId);
        dto.setStrategyName(name);
        dto.setPointsDelta(pointsDelta);
        dto.setPointsForBetWithinDelta(pointsForBetWithinDelta);
        dto.setPointsForMatchScore(pointsForMatchScore);
        dto.setPointsForMatchWinner(pointsForMatchWinner);
        return dto;
    }

    public static PointsCalculationStrategyEditDTO construct() {
        return new PointsCalculationStrategyEditDtoBuilder()
                .withName(RandomUtils.pointsStrategyName())
                .withPointsForMatchScore(6)
                .withPointsForMatchWinner(1)
                .withPointsDelta(3)
                .withPointsForBetWithinDelta(2)
                .build();
    }
}

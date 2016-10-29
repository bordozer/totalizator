package betmen.rests.utils.data.builders;

import betmen.dto.dto.admin.CupEditDTO;
import betmen.dto.dto.admin.CupWinnerEditDTO;

import java.time.LocalDateTime;
import java.util.List;

public class CupEditDtoBuilder {

    private int cupId;
    private int categoryId;
    private int pointsStrategyId;
    private String name;
    private LocalDateTime cupStartDate;
    private int winnersCount;
    private List<CupWinnerEditDTO> cupWinners;
    private String logoUrl;
    private boolean publicCup;
    private String cupImportId;

    public CupEditDtoBuilder withId(final int id) {
        this.cupId = id;
        return this;
    }

    public CupEditDtoBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public CupEditDtoBuilder withCategory(final int categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public CupEditDtoBuilder withStartDate(final LocalDateTime cupStartDate) {
        this.cupStartDate = cupStartDate;
        return this;
    }

    public CupEditDtoBuilder started() {
        this.cupStartDate = LocalDateTime.now().minusMonths(1);
        return this;
    }

    public CupEditDtoBuilder withWinnersCount(final int winnersCount) {
        this.winnersCount = winnersCount;
        return this;
    }

    public CupEditDtoBuilder withWinners(final List<CupWinnerEditDTO> cupWinners) {
        this.cupWinners = cupWinners;
        return this;
    }

    public CupEditDtoBuilder withPointsStrategyId(final int pointsCalculationStrategyId) {
        this.pointsStrategyId = pointsCalculationStrategyId;
        return this;
    }

    public CupEditDtoBuilder publicCup(final boolean publicCup) {
        this.publicCup = publicCup;
        return this;
    }

    public CupEditDtoBuilder withImportId(final String cupImportId) {
        this.cupImportId = cupImportId;
        return this;
    }

    public CupEditDtoBuilder withLogo(final String logoUrl) {
        this.logoUrl = logoUrl;
        return this;
    }

    public CupEditDTO build() {
        CupEditDTO dto = new CupEditDTO();
        dto.setCupId(cupId);
        dto.setCupName(name);
        dto.setCategoryId(categoryId);
        dto.setCupStartDate(cupStartDate);
        dto.setWinnersCount(winnersCount);
        dto.setCupWinners(cupWinners);
        dto.setPublicCup(publicCup);
        dto.setLogoUrl(logoUrl);
        dto.setCupImportId(cupImportId);
        dto.setCupPointsCalculationStrategyId(pointsStrategyId);
        return dto;
    }
}

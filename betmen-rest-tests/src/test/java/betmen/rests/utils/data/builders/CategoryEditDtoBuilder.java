package betmen.rests.utils.data.builders;

import betmen.dto.dto.admin.CategoryEditDTO;
import betmen.rests.utils.RandomUtils;

public class CategoryEditDtoBuilder {
    private int categoryId;
    private String name;
    private int sportKindId;

    private String logoUrl;
    private String categoryImportId;
    private int remoteGameImportStrategyTypeId;

    public CategoryEditDtoBuilder withId(final int id) {
        this.categoryId = id;
        return this;
    }

    public CategoryEditDtoBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public CategoryEditDtoBuilder withSport(final int sportKindId) {
        this.sportKindId = sportKindId;
        return this;
    }

    public CategoryEditDtoBuilder withLogo(final String logoUrl) {
        this.logoUrl = logoUrl;
        return this;
    }

    public CategoryEditDtoBuilder withImportId(final String categoryImportId) {
        this.categoryImportId = categoryImportId;
        return this;
    }

    public CategoryEditDtoBuilder withImportStrategy(final int remoteGameImportStrategyTypeId) {
        this.remoteGameImportStrategyTypeId = remoteGameImportStrategyTypeId;
        return this;
    }

    public CategoryEditDTO build() {
        CategoryEditDTO dto = new CategoryEditDTO(categoryId, name);
        dto.setSportKindId(sportKindId);
        return dto;
    }

    public static CategoryEditDTO construct(final int sportKindId) {
        return new CategoryEditDtoBuilder()
                .withSport(sportKindId)
                .withName(RandomUtils.categoryName())
                .build();
    }
}

package betmen.rests.utils.data.builders;

import betmen.dto.dto.admin.SportKindEditDTO;
import betmen.rests.utils.RandomUtils;

public class SportKindEditDtoBuilder {
    private int sportKindId;
    private String name;

    public SportKindEditDtoBuilder withId(final int id) {
        this.sportKindId = id;
        return this;
    }

    public SportKindEditDtoBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public SportKindEditDTO build() {
        return new SportKindEditDTO(sportKindId, name);
    }

    public static SportKindEditDTO construct() {
        return new SportKindEditDtoBuilder()
                .withName(RandomUtils.sportName())
                .build();
    }
}

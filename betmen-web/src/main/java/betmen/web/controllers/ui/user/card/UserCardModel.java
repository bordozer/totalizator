package betmen.web.controllers.ui.user.card;

import betmen.core.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserCardModel {
    private final User user;
    private int filterByCupId;
    private String onDate;

    public UserCardModel(final User user) {
        this.user = user;
    }
}

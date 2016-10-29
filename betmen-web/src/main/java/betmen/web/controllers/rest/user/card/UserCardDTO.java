package betmen.web.controllers.rest.user.card;

import betmen.dto.dto.CupDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class UserCardDTO {
    private List<CupDTO> cupsToShow;
}

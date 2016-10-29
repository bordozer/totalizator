package betmen.web.controllers.rest.sportKindCups;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SportKindsCupsDTO {
    private final List<SportKindCupsDTO> sportKindCups;
}

package betmen.web.controllers.ui.cups.matches;

import betmen.core.entity.Cup;
import betmen.core.entity.Team;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CupMatchesModel {
    private Cup cup;
    private Team team1;
    private Team team2;
}

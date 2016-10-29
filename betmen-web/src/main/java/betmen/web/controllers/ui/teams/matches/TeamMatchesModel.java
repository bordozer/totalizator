package betmen.web.controllers.ui.teams.matches;

import betmen.core.entity.Cup;
import betmen.core.entity.Team;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamMatchesModel {
    private Cup cup;
    private Team team;
}

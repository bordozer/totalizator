package betmen.web.controllers.ui.matches.bets;

import betmen.core.entity.Cup;
import betmen.core.entity.Match;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchBetsModel {
    private Match match;
    private String matchDate;
    private String matchTime;
    private Cup cup;
}

package betmen.web.controllers.ui.matches.bets;

import betmen.core.entity.Match;
import betmen.core.service.matches.MatchService;
import betmen.core.service.utils.DateTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/betmen/matches")
public class MatchBetsController {

    public static final String MODEL_NAME = "matchBetsModel";

    private static final String VIEW = "/MatchBets";

    @Autowired
    private MatchService matchService;

    @Autowired
    private DateTimeService dateTimeService;

    @ModelAttribute(MODEL_NAME)
    public MatchBetsModel preparePagingModel() {
        return new MatchBetsModel();
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{matchId}/bets/")
    public String portalPage(final @PathVariable("matchId") int matchId, final @ModelAttribute(MODEL_NAME) MatchBetsModel model) {

        final Match match = matchService.load(matchId);

        model.setMatch(match);
        model.setMatchDate(dateTimeService.formatDateUI(match.getBeginningTime()));
        model.setMatchTime(dateTimeService.formatTime(match.getBeginningTime()));

        model.setCup(match.getCup());

        return VIEW;
    }
}

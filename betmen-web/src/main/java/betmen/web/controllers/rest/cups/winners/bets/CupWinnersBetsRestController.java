package betmen.web.controllers.rest.cups.winners.bets;

import betmen.core.entity.Cup;
import betmen.core.entity.CupTeamBet;
import betmen.core.entity.CupWinner;
import betmen.core.entity.Team;
import betmen.core.entity.User;
import betmen.core.model.AppContext;
import betmen.core.service.CupBetsService;
import betmen.core.service.CupService;
import betmen.core.service.CupWinnerService;
import betmen.core.service.UserService;
import betmen.core.service.utils.DateTimeService;
import betmen.core.translator.Language;
import betmen.core.translator.TranslatorService;
import betmen.dto.dto.CupTeamBetDTO;
import betmen.dto.dto.TeamDTO;
import betmen.web.converters.DTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.newHashSet;

@RestController
@RequestMapping("/rest/cups/{cupId}")
public class CupWinnersBetsRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private CupService cupService;
    @Autowired
    private CupBetsService cupBetsService;
    @Autowired
    private DTOService dtoService;
    @Autowired
    private TranslatorService translatorService;
    @Autowired
    private DateTimeService dateTimeService;
    @Autowired
    private CupWinnerService cupWinnerService;

    @RequestMapping(method = RequestMethod.GET, value = "/winners/")
    public List<TeamDTO> winners(@PathVariable("cupId") final int cupId, final Principal principal) {
        final User currentUser = userService.findByLogin(principal.getName());
        final Cup cup = cupService.load(cupId);
        return dtoService.transformTeams(cup.getCategory(), toTeams(cup), currentUser);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/winners/bets/")
    public CupWinnersBetsDTO all(@PathVariable("cupId") final int cupId, final HttpServletRequest request, final Principal principal) {
        final User currentUser = userService.findByLogin(principal.getName());
        final Cup cup = cupService.load(cupId);
        final Language language = AppContext.read(request.getSession()).getLanguage();

        final CupWinnersBetsDTO result = new CupWinnersBetsDTO();
        result.setWinnersCount(cup.getWinnersCount());
        result.setWinners(dtoService.transformTeams(cup.getCategory(), toTeams(cup), currentUser));

        final boolean isCupBetsAreHiddenYet = !cupBetsService.isCupBettingFinished(cup);
        final List<User> users = getUsers(cup);
        final List<UserCupBetsDTO> usersCupBets = newArrayList();
        for (final User user : users) {
            final List<CupTeamBet> cupTeamBets = cupBetsService.load(cup, user);
            final UserCupBetsDTO userCupBetsDTO = new UserCupBetsDTO();
            userCupBetsDTO.setUser(dtoService.transformUser(user));
            final List<CupTeamBetDTO> userCupBets = dtoService.transformCupTeamBets(cupTeamBets, user);
            if (isCupBetsAreHiddenYet) {
                replaceTeamsWithFakeData(cup, userCupBets, language);
            }
            userCupBetsDTO.setUserCupBets(userCupBets);
            usersCupBets.add(userCupBetsDTO);
        }
        result.setUsersCupBets(usersCupBets);
        return result;
    }

    private void replaceTeamsWithFakeData(final Cup cup, final List<CupTeamBetDTO> userCupBets, final Language language) {
        for (final CupTeamBetDTO userCupBet : userCupBets) {
            final TeamDTO team = userCupBet.getTeam();

            final TeamDTO fakeTeam = new TeamDTO();
            fakeTeam.setCategory(team.getCategory());
            fakeTeam.setTeamId(0);
            fakeTeam.setTeamLogo("/resources/img/team-logo-not-found.png");
            fakeTeam.setTeamName(translatorService.translate("Team name is hidden till $1"
                    , language
                    , dateTimeService.formatDateTimeUI(cup.getCupStartTime())
            ));
            userCupBet.setTeam(fakeTeam);
        }
    }

    private List<User> getUsers(final Cup cup) {
        final Set<User> usersSet = newHashSet();
        final List<CupTeamBet> cupBets = cupBetsService.load(cup);
        for (final CupTeamBet bet : cupBets) {
            usersSet.add(bet.getUser());
        }
        final List<User> users = newArrayList(usersSet);
        Collections.sort(users, (o1, o2) -> o1.getUsername().compareToIgnoreCase(o2.getUsername()));
        return users;
    }

    private List<Team> toTeams(final Cup cup) {
        return cupWinnerService.loadAll(cup).stream().map(CupWinner::getTeam).collect(Collectors.toList());
    }
}

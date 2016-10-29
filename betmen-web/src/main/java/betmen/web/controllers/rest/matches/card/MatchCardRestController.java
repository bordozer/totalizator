package betmen.web.controllers.rest.matches.card;

import betmen.core.entity.Match;
import betmen.core.entity.MatchBet;
import betmen.core.entity.User;
import betmen.core.entity.UserGroup;
import betmen.core.service.UserGroupService;
import betmen.core.service.UserService;
import betmen.core.service.matches.MatchBetsService;
import betmen.core.service.matches.MatchService;
import betmen.dto.dto.MatchBetDTO;
import betmen.dto.dto.MatchBetsDTO;
import betmen.dto.dto.TeamDTO;
import betmen.dto.dto.points.UserMatchPointsHolderDTO;
import betmen.web.converters.DTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

@RestController
@RequestMapping("/rest/matches")
public class MatchCardRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private MatchService matchService;
    @Autowired
    private MatchBetsService matchBetsService;
    @Autowired
    private DTOService dtoService;
    @Autowired
    private UserGroupService userGroupService;

    @RequestMapping(method = RequestMethod.GET, value = "/{matchId}/bets/")
    public MatchBetsDTO matchBets(@PathVariable("matchId") final int matchId, @RequestParam(value = "userGroupId", required = false) final int userGroupId,
                                  final Principal principal) {
        final User currentUser = userService.findByLogin(principal.getName());
        final Match match = matchService.load(matchId);
        final TeamDTO team1DTO = dtoService.transformTeam(match.getTeam1(), currentUser);
        final TeamDTO team2DTO = dtoService.transformTeam(match.getTeam2(), currentUser);

        final MatchBetsDTO result = new MatchBetsDTO();
        result.setMatchId(matchId);
        result.setMatch(dtoService.transformMatch(match, currentUser));
        result.setTeam1(team1DTO);
        result.setTeam2(team2DTO);
        result.setMatchBets(getMatchBetDTOs(match, currentUser, userGroupId));
        return result;
    }

    private List<MatchBetDTO> getMatchBetDTOs(final Match match, final User currentUser, final int userGroupId) {
        final UserGroup userGroup = userGroupId > 0 ? userGroupService.load(userGroupId) : null;
        final List<MatchBet> matchBets = matchBetsService.loadAll(match, userGroup);
        final List<MatchBetDTO> matchBetsDTOs = newArrayList();
        for (final MatchBet matchBet : matchBets) {
            final MatchBetDTO matchBetDTO = userGroup != null ? dtoService.getMatchBetForMatch(match, matchBet.getUser(), currentUser, userGroup) : dtoService.getMatchBetForMatch(match, matchBet.getUser(), currentUser);
            if (matchBetDTO.getBet().isSecuredBet()) {
                matchBetDTO.getBet().setScore1(0);
                matchBetDTO.getBet().setScore2(0);
            }
            matchBetsDTOs.add(matchBetDTO);
        }

        return matchBetsDTOs.stream().sorted((o1, o2) -> {
            final UserMatchPointsHolderDTO pointsHolder1 = o2.getUserMatchPointsHolder();
            final UserMatchPointsHolderDTO pointsHolder2 = o1.getUserMatchPointsHolder();

            boolean hasNonZeroPoints = pointsHolder1.getSummary() - pointsHolder2.getSummary() != 0F;
            if (hasNonZeroPoints) {
                return ((Float) pointsHolder1.getSummary()).compareTo(pointsHolder2.getSummary());
            }
            return o1.getBet().getUser().getUserName().compareToIgnoreCase(o2.getBet().getUser().getUserName());
        }).collect(Collectors.toList());
    }
}

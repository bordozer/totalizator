package betmen.web.controllers.rest.cupTeamBets;

import betmen.core.entity.Cup;
import betmen.core.entity.CupTeamBet;
import betmen.core.entity.Team;
import betmen.core.entity.User;
import betmen.core.service.CupBetsService;
import betmen.core.service.CupService;
import betmen.core.service.TeamService;
import betmen.core.service.UserService;
import betmen.core.service.utils.DateTimeService;
import betmen.web.converters.DTOService;
import betmen.dto.dto.CupDTO;
import betmen.dto.dto.CupTeamBetDTO;
import betmen.dto.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/rest/cups/{cupId}/bets")
public class CupBetsRestController {

    @Autowired
    private UserService userService;
    @Autowired
    private CupService cupService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private CupBetsService cupBetsService;
    @Autowired
    private DTOService dtoService;

    @RequestMapping(method = RequestMethod.GET, value = "/{userId}/")
    public CupTeamBetsDTO show(final @PathVariable("cupId") int cupId, final @PathVariable("userId") int userId) {
        return getCupTeamBetsDTO(cupId, userId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{position}/{teamId}/")
    public void save(@PathVariable("cupId") final int cupId, @PathVariable("position") final int position, @PathVariable("teamId") final int teamId,
                     final Principal principal) {
        // TODO: server validation

        final Cup cup = cupService.load(cupId);
        if (cupBetsService.isCupBettingFinished(cup)) {
            throw new IllegalArgumentException(String.format("Match betting for cup %s is finished", cup));
        }

        final User user = userService.findByLogin(principal.getName());

        final CupTeamBet existingTupTeamBet = cupBetsService.load(cup, user, position);
        if (cupId > 0 && position > 0 && teamId == 0 && existingTupTeamBet != null) {
            cupBetsService.delete(existingTupTeamBet.getId());
            return;
        }

        if (cupId == 0 || teamId == 0 || position <= 0) {
            return; // no error, just return
        }

        final Team team = teamService.load(teamId);

        if (existingTupTeamBet != null) {
            existingTupTeamBet.setTeam(team);
            cupBetsService.save(existingTupTeamBet);

            return;
        }

        final CupTeamBet entry = new CupTeamBet();

        entry.setCup(cup);
        entry.setUser(user);
        entry.setTeam(team);
        entry.setCupPosition(position);

        cupBetsService.save(entry);
    }

    private CupTeamBetsDTO getCupTeamBetsDTO(final int cupId, final int userId) {

        final User user = userService.load(userId);
        final Cup cup = cupService.load(cupId);

        final UserDTO userDTO = dtoService.transformUser(user);
        final CupDTO cupDTO = dtoService.transformCup(cup, user);

        final List<CupTeamBetDTO> result = newArrayList();

        for (int i = 1; i <= cup.getWinnersCount(); i++) {
            final CupTeamBet cupTeamBet = cupBetsService.load(cup, user, i);
            if (cupTeamBet != null) {
                result.add(dtoService.transformCupTeamBet(cupTeamBet, user));
            } else {
                final CupTeamBetDTO emptyCupTeamBetDTO = new CupTeamBetDTO();
                emptyCupTeamBetDTO.setCup(cupDTO);
                emptyCupTeamBetDTO.setUser(userDTO);
                emptyCupTeamBetDTO.setCupPosition(i);
                result.add(emptyCupTeamBetDTO);
            }
        }

        return new CupTeamBetsDTO(result);
    }
}

package betmen.web.controllers.rest.cupTeamBets;

import betmen.core.entity.Cup;
import betmen.core.entity.CupTeamBet;
import betmen.core.entity.Team;
import betmen.core.entity.User;
import betmen.core.exception.UnprocessableEntityException;
import betmen.core.model.ErrorCodes;
import betmen.core.service.CupBetsService;
import betmen.core.service.CupService;
import betmen.core.service.TeamService;
import betmen.core.service.UserService;
import betmen.dto.dto.CupDTO;
import betmen.dto.dto.CupTeamBetDTO;
import betmen.dto.dto.CupTeamBetsDTO;
import betmen.dto.dto.UserDTO;
import betmen.web.converters.DTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

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
    public CupTeamBetsDTO show(@PathVariable("cupId") final int cupId, @PathVariable("userId") final int userId) {
        return getCupTeamBetsDTO(cupId, userId);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{position}/{teamId}/")
    public void saveCupResultBet(@PathVariable("cupId") final int cupId, @PathVariable("teamId") final int teamId, @PathVariable("position") final int position, final Principal principal) {
        Assert.isTrue(position >= 0, ErrorCodes.CUP_WINNER_POSITION_SHOULD_NOT_BE_NEGATIVE);
        final Cup cup = cupService.loadAndAssertExists(cupId);
        if (cupBetsService.isCupBettingFinished(cup)) {
            throw new UnprocessableEntityException(ErrorCodes.BETTING_ON_CUP_RESULTS_IS_NOT_ACCESSIBLE);
        }

        final User user = userService.findByLogin(principal.getName());

        final CupTeamBet existingCupTeamBet = cupBetsService.load(cup, user, position);
        if (cupId > 0 && position > 0 && teamId == 0 && existingCupTeamBet != null) {
            cupBetsService.delete(existingCupTeamBet.getId());
            return;
        }

        if (cupId == 0 || teamId == 0 || position <= 0) {
            return; // no error, just return
        }

        final Team team = teamService.loadAndAssertExists(teamId);

        if (existingCupTeamBet != null) {
            existingCupTeamBet.setTeam(team);
            cupBetsService.save(existingCupTeamBet);

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

        final User user = userService.loadAndAssertExists(userId);
        final Cup cup = cupService.loadAndAssertExists(cupId);

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

package betmen.web.controllers.rest.matches.bets;

import betmen.core.entity.Match;
import betmen.core.entity.MatchBet;
import betmen.core.entity.User;
import betmen.core.exception.BusinessException;
import betmen.core.exception.UnprocessableEntityException;
import betmen.core.model.ValidationResult;
import betmen.core.service.UserService;
import betmen.core.service.matches.MatchBetsService;
import betmen.core.service.matches.MatchService;
import betmen.core.service.matches.MatchesAndBetsWidgetService;
import betmen.core.service.utils.DateTimeService;
import betmen.dto.dto.BetDTO;
import betmen.dto.dto.MatchBetDTO;
import betmen.dto.dto.MatchBetsOnDateDTO;
import betmen.dto.dto.MatchSearchModelDto;
import betmen.web.converters.DTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

@RestController
@RequestMapping("/rest/matches")
public class MatchesAndBetsRestController {

    @Autowired
    private MatchService matchService;
    @Autowired
    private MatchesAndBetsWidgetService matchesAndBetsWidgetService;
    @Autowired
    private MatchBetsService matchBetsService;
    @Autowired
    private UserService userService;
    @Autowired
    private DTOService dtoService;
    @Autowired
    private DateTimeService dateTimeService;

    @RequestMapping(method = RequestMethod.GET, value = "/bets/")
    public List<MatchBetsOnDateDTO> matchesAndBets(@Validated final MatchSearchModelDto dto, final Principal principal) {
        final List<Match> matches = matchesAndBetsWidgetService.loadAll(dtoService.transformMatchSearchModel(dto));

        final int userId = dto.getUserId();

        final User currentUser = userService.findByLogin(principal.getName());
        final User showBetsOfUser = userId > 0 ? userService.loadAndAssertExists(userId) : currentUser;

        final List<MatchBetDTO> matchBetDTOs = dtoService.getMatchBetForMatches(matches, showBetsOfUser, currentUser);

		/*if ( userId > 0 ) {

			CollectionUtils.filter( matchBetDTOs, new Predicate<MatchBetDTO>() {

				@Override
				public boolean evaluate( final MatchBetDTO matchBetDTO ) {
					final BetDTO bet = matchBetDTO.getBet();

					if ( bet == null ) {
						return false;
					}

					return bet.getUser().getUserId() == userId;
				}
			} );
		}*/

        final List<LocalDate> matchDates = matchBetDTOs
                .stream()
                .map(matchBetDTO -> matchBetDTO.getMatch().getBeginningTime().toLocalDate())
                .distinct()
                .collect(Collectors.toList());

        final List<MatchBetsOnDateDTO> result = newArrayList();

        matchDates.stream().forEach(date -> {
            final List<MatchBetDTO> matchIds = matchBetDTOs
                    .stream()
                    .filter(matchBetDTO -> dateTimeService.hasTheSameDate(matchBetDTO.getMatch().getBeginningTime(), date))
                    .collect(Collectors.toList());
            MatchBetsOnDateDTO dateDTO = new MatchBetsOnDateDTO();
            dateDTO.setDate(date);
            dateDTO.setMatchBets(matchIds);
            result.add(dateDTO);
        });

        return result;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{matchId}/bet-of-user/{userId}/")
    public MatchBetDTO getBet(@PathVariable("matchId") final int matchId, @PathVariable("userId") final int userId, final Principal principal) {
        final User currentUser = userService.findByLogin(principal.getName());
        final User showBetOfUser = userId > 0 ? userService.loadAndAssertExists(userId) : currentUser;
        final Match match = matchService.loadAndAssertExists(matchId);

        return dtoService.getMatchBetForMatch(match, showBetOfUser, currentUser);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{matchId}/bets/{score1}/{score2}/")
    public BetDTO makeBet(final Principal principal, @PathVariable("matchId") final int matchId, @PathVariable("score1") final int score1,
                          @PathVariable("score2") final int score2) {

        final User currentUser = userService.findByLogin(principal.getName());
        final Match match = matchService.loadAndAssertExists(matchId);

        assertBetIsModifiable(currentUser, match);

        final MatchBet existingBet = matchBetsService.load(currentUser, match);

        if (existingBet != null) {
            populateEntity(existingBet, score2, score1);
            matchBetsService.save(existingBet);
            return dtoService.transformMatchBet(existingBet, currentUser, currentUser);
        }

        MatchBet constructed = new MatchBet();
        constructed.setUser(currentUser);
        constructed.setMatch(match);
        populateEntity(constructed, score2, score1);
        return dtoService.transformMatchBet(matchBetsService.save(constructed), currentUser, currentUser);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "{matchId}/bets/")
    public boolean deleteBet(final Principal principal, @PathVariable("matchId") final int matchId) {
        final User currentUser = userService.findByLogin(principal.getName());
        Match match = matchService.loadAndAssertExists(matchId);

        assertBetIsModifiable(currentUser, match);

        MatchBet matchBet = matchBetsService.load(currentUser, match);
        if (matchBet == null) {
            throw new UnprocessableEntityException(String.format("Match %s: bet does not exists for user %s", currentUser, match));
        }

        if (!matchBetsService.delete(currentUser.getId(), matchBet.getId())) {
            throw new UnprocessableEntityException(String.format("Cannot delete bet: %d", matchBet.getId()));
        }
        return true;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{matchId}/bets/count/")
    public int matchBetsCount(@PathVariable("matchId") final int matchId) {
        return matchBetsService.betsCount(matchId);
    }

    private void assertBetIsModifiable(final User user, final Match match) {
        final ValidationResult validationResult = matchBetsService.validateBettingAllowed(match, user);
        if (!validationResult.isPassed()) {
            throw new BusinessException(validationResult.getErrorCode(), validationResult.getErrorCode(), validationResult.getTranslatedMessage());
        }
    }

    private void populateEntity(final MatchBet matchBet, final int score2, final int score1) {
        matchBet.setBetScore1(score1);
        matchBet.setBetScore2(score2);
        matchBet.setBetTime(dateTimeService.getNow());
    }
}

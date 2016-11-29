package betmen.web.controllers.rest.admin;

import betmen.core.entity.Cup;
import betmen.core.entity.CupWinner;
import betmen.core.entity.Team;
import betmen.core.exception.BadRequestException;
import betmen.core.exception.UnprocessableEntityException;
import betmen.core.service.CategoryService;
import betmen.core.service.CupBetsService;
import betmen.core.service.CupService;
import betmen.core.service.CupTeamService;
import betmen.core.service.LogoService;
import betmen.core.service.PointsCalculationStrategyService;
import betmen.core.service.TeamService;
import betmen.core.service.UserService;
import betmen.core.service.matches.MatchService;
import betmen.dto.dto.admin.CupEditDTO;
import betmen.dto.dto.admin.CupWinnerEditDTO;
import betmen.web.converters.DTOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/rest/cups/edit")
public class AdminCupsEditRestController {

    private static final String CUP_LOGO_NAME_TEMPLATE = "cup_logo_%d";

    @Autowired
    private UserService userService;
    @Autowired
    private CupService cupService;
    @Autowired
    private MatchService matchService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CupBetsService cupBetsService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private LogoService logoService;
    @Autowired
    private PointsCalculationStrategyService pointsCalculationStrategyService;
    @Autowired
    private CupTeamService cupTeamService;
    @Autowired
    private DTOService dtoService;

    @RequestMapping(method = RequestMethod.GET, value = "/{cupId}")
    public CupEditDTO getItem(@PathVariable("cupId") final int cupId) {
        return convertToEditDto(cupService.loadAndAssertExists(cupId));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/")
    public List<CupEditDTO> getAllItems() {
        return cupService.loadAll().stream().map(cup -> convertToEditDto(cup)).collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/0")
    public CupEditDTO createCup(@Validated @RequestBody final CupEditDTO dto) {
        validateDto(dto.getWinnersCount(), dto.getCupWinners(), dto.getCategoryId());

        final Cup cup = new Cup();
        populateEntityFromDto(cup, dto);
        final Cup saved = cupService.save(cup);

        final List<Team> teams = teamService.loadAll(saved.getCategory().getId());
        for (final Team team : teams) {
            cupTeamService.saveCupTeam(saved.getId(), team.getId(), true);
        }

        return convertToEditDto(saved);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{cupId}")
    public CupEditDTO updateItem(@PathVariable("cupId") final int cupId, @Validated @RequestBody final CupEditDTO dto) {
        validateDto(dto.getWinnersCount(), dto.getCupWinners(), dto.getCategoryId());
        Cup cup = cupService.loadAndAssertExists(dto.getCupId());
        populateEntityFromDto(cup, dto);
        return convertToEditDto(cupService.save(cup));
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{cupId}")
    public boolean delete(@PathVariable("cupId") final int cupId) {
        if (cupId == 0) {
            return true;
        }
        if (matchService.getMatchCount(cupId) > 0) {
            throw new UnprocessableEntityException("Cup is assigned to at least one match.");
        }
        Cup cup = cupService.load(cupId);
        if (cup == null) {
            throw new UnprocessableEntityException("Cup does not exist");
        }
        cupService.delete(cupId);
        return true;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{cupId}/logo/")
    public void uploadLogo(@PathVariable("cupId") final int cupId, final MultipartHttpServletRequest request) throws IOException {
        final Iterator<String> itr = request.getFileNames();
        if (!itr.hasNext()) {
            return;
        }

        // TODO: rewrite circular dependencies cup <=> logo
        final MultipartFile logoFile = request.getFile(itr.next());
        final Cup cup = cupService.load(cupId);
        cup.setLogoFileName(String.format(CUP_LOGO_NAME_TEMPLATE, cup.getId()));
        cupService.save(cup);
        logoFile.transferTo(logoService.getLogoFile(cup));
    }

    private List<CupWinner> convertToCupWinners(final Cup cup, final List<CupWinnerEditDTO> cupWinners) {
        if (CollectionUtils.isEmpty(cupWinners)) {
            return Collections.emptyList();
        }
        return cupWinners.stream()
                .map(cupWinnerDto -> convertToCupWinnerEntity(cup, cupWinnerDto))
                .collect(Collectors.toList());
    }

    private CupWinner convertToCupWinnerEntity(final Cup cup, final CupWinnerEditDTO cupWinnerDto) {
        final CupWinner winner = new CupWinner();
        winner.setCup(cup);
        winner.setCupPosition(cupWinnerDto.getCupPosition());
        winner.setTeam(teamService.load(cupWinnerDto.getTeamId()));
        return winner;
    }

    private CupEditDTO convertToEditDto(final Cup cup) {
        final CupEditDTO cupEditDTO = new CupEditDTO();

        cupEditDTO.setCupId(cup.getId());
        cupEditDTO.setCupName(cup.getCupName());
        cupEditDTO.setCategoryId(cup.getCategory().getId());
        cupEditDTO.setCupPointsCalculationStrategyId(cup.getPointsCalculationStrategy().getId());

        cupEditDTO.setCupStartDate(cup.getCupStartTime());
        cupEditDTO.setWinnersCount(cup.getWinnersCount());
        cupEditDTO.setCupWinners(convertToCupWinnersDtos(cup.getCupWinners()));
        cupEditDTO.setPublicCup(cup.isPublicCup());
        cupEditDTO.setCupImportId(cup.getCupImportId());

        cupEditDTO.setLogoUrl(logoService.getLogoURL(cup));
        cupEditDTO.setReadyForCupBets(!cupBetsService.isCupBettingFinished(cup));
        cupEditDTO.setReadyForMatchBets(!cupBetsService.isMatchBettingFinished(cup));
        cupEditDTO.setFinished(cupService.isCupFinished(cup));

        return cupEditDTO;
    }

    private List<CupWinnerEditDTO> convertToCupWinnersDtos(final List<CupWinner> cupWinners) {
        return cupWinners.stream().map(this::convertCupWinnerEntityToEditDTO).collect(Collectors.toList());
    }

    private void populateEntityFromDto(final Cup cup, final CupEditDTO dto) {
        cup.setCupName(dto.getCupName());
        cup.setCategory(categoryService.loadAndAssertExists(dto.getCategoryId()));
        cup.setPointsCalculationStrategy(pointsCalculationStrategyService.loadAndAssertExists(dto.getCupPointsCalculationStrategyId()));
        cup.setCupStartTime(dto.getCupStartDate());
        cup.setWinnersCount(dto.getWinnersCount());
        cup.setPublicCup(dto.isPublicCup());
        cup.setCupImportId(dto.getCupImportId());
        cup.setCupWinners(convertToCupWinners(cup, dto.getCupWinners()));
    }

    private CupWinnerEditDTO convertCupWinnerEntityToEditDTO(final CupWinner cupWinner) {
        final CupWinnerEditDTO cupWinnerEditDTO = new CupWinnerEditDTO();
        cupWinnerEditDTO.setCupId(cupWinner.getCup().getId());
        cupWinnerEditDTO.setCupPosition(cupWinner.getCupPosition());
        cupWinnerEditDTO.setTeamId(cupWinner.getTeam().getId());
        cupWinnerEditDTO.setTeam(dtoService.transformTeam(cupWinner.getTeam()));
        return cupWinnerEditDTO;
    }

    private void validateDto(final int winnersCount, final List<CupWinnerEditDTO> cupWinners, final int categoryId) {
        if (CollectionUtils.isEmpty(cupWinners)) {
            return;
        }

        if (cupWinners.size() != winnersCount) {
            throw new BadRequestException("Winners count does not match cup's champion count");
        }

        cupWinners.stream().forEach(winner -> {
            Team team = teamService.loadAndAssertExists(winner.getTeamId());
            if (team.getCategory().getId() != categoryId) {
                throw new BadRequestException(String.format("Cup winner %s and have different categories (Team: %d; cup: %d)", team, team.getCategory().getId(), categoryId));
            }
        });

        int uniquePositionsCount = cupWinners.stream()
                .map(CupWinnerEditDTO::getCupPosition)
                .distinct()
                .collect(Collectors.toList())
                .size();
        if (cupWinners.size() != uniquePositionsCount) {
            throw new BadRequestException("Winners positions should be unique");
        }

        int uniqueTeamsCount = cupWinners.stream()
                .map(CupWinnerEditDTO::getTeamId)
                .distinct()
                .collect(Collectors.toList())
                .size();
        if (cupWinners.size() != uniqueTeamsCount) {
            throw new BadRequestException("Winners teams should be unique");
        }
    }
}

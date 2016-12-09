package betmen.web.controllers.rest.admin;

import betmen.core.entity.Cup;
import betmen.core.entity.Team;
import betmen.core.service.CategoryService;
import betmen.core.service.CupTeamService;
import betmen.core.service.LogoService;
import betmen.core.service.TeamService;
import betmen.core.service.matches.MatchService;
import betmen.dto.dto.admin.TeamEditDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin/rest/teams")
public class AdminTeamEditRestController {

    @Autowired
    private TeamService teamService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CupTeamService cupTeamService;
    @Autowired
    private MatchService matchService;
    @Autowired
    private LogoService logoService;

    @RequestMapping(method = RequestMethod.GET, value = "/{teamId}/")
    public TeamEditDTO getItem(@PathVariable("teamId") final int teamId) {
        return convertToEditDto(teamService.loadAndAssertExists(teamId), null);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/categories/{categoryId}/")
    public List<TeamEditDTO> categoryTeams(@PathVariable(value = "categoryId") final Integer categoryId) {
        return teamService.loadAll(categoryId).stream().map(team -> convertToEditDto(team, null)).collect(Collectors.toList());
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/0")
    public TeamEditDTO createItem(@Validated @RequestBody final TeamEditDTO dto, @RequestParam(value = "cupId", required = false) final Integer cupId) {
        final Team team = new Team();
        populateEntity(team, dto);

        final Team saved = teamService.save(team);
        if (cupId != null && cupId > 0) {
            teamActivityInCup(saved.getId(), cupId, true);
        }

        return convertToEditDto(saved, null);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{teamId}")
    public TeamEditDTO updateItem(@PathVariable("teamId") final int teamId, @Validated @RequestBody final TeamEditDTO dto) {
        final Team team = teamService.loadAndAssertExists(dto.getTeamId());
        populateEntity(team, dto);
        return convertToEditDto(teamService.save(team), null);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{teamId}")
    public boolean delete(@PathVariable("teamId") final int teamId) {
        if (teamId == 0) {
            return true;
        }
        teamService.delete(teamId);
        return true;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{teamId}/cups/{cupId}/active/{isActive}/")
    public void teamActivityInCup(@PathVariable("teamId") final int teamId, @PathVariable("cupId") final int cupId, @PathVariable("isActive") final boolean isActive) {
        cupTeamService.saveCupTeam(cupId, teamId, isActive);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{teamId}/logo/")
    public void uploadLogo(@PathVariable("teamId") final int teamId, final MultipartHttpServletRequest request) throws IOException {

        final Team team = teamService.load(teamId);

        final Iterator<String> itr = request.getFileNames();
        if (!itr.hasNext()) {
            return;
        }

        final MultipartFile logoFile = request.getFile(itr.next());

        team.setLogoFileName(String.format("team_logo_%d", team.getId()));
        Team saved = teamService.save(team);

        logoFile.transferTo(logoService.getLogoFile(saved));
    }

    private void populateEntity(final Team team, final TeamEditDTO dto) {
        team.setTeamName(dto.getTeamName());
        team.setCategory(categoryService.loadAndAssertExists(dto.getCategoryId()));
        team.setImportId(dto.getTeamImportId());
    }

    private TeamEditDTO convertToEditDto(final Team team, final Cup cup) {
        final TeamEditDTO dto = new TeamEditDTO();
        dto.setTeamId(team.getId());
        dto.setTeamName(team.getTeamName());
        dto.setCategoryId(team.getCategory().getId());
        dto.setTeamImportId(team.getImportId());

        dto.setTeamLogo(logoService.getLogoURL(team));
        dto.setTeamChecked(cup != null && cupTeamService.exists(cup, team));
        dto.setMatchCount(matchService.getMatchCount(team.getId()));

        return dto;
    }
}

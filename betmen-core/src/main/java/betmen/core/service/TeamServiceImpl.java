package betmen.core.service;

import betmen.core.entity.Team;
import betmen.core.exception.UnprocessableEntityException;
import betmen.core.repository.TeamDao;
import betmen.core.repository.jpa.TeamJpaRepository;
import betmen.core.service.matches.MatchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
public class TeamServiceImpl implements TeamService {

    @Autowired
    private TeamDao teamRepository;
    @Autowired
    private TeamJpaRepository teamJpaRepository;
    @Autowired
    private CupTeamService cupTeamService;
    @Autowired
    private MatchService matchService;
    @Autowired
    private CupWinnerService cupWinnerService;
    @Autowired
    private LogoService logoService;

    @Override
    public List<Team> loadAll(final int categoryId) {
        return teamJpaRepository.findAllByCategoryIdOrderByTeamNameAsc(categoryId);
    }

    @Override
    public Team loadAndAssertExists(final int teamId) {
        Team team = load(teamId);
        if (team == null) {
            LOGGER.warn(String.format("Cannot get team with ID: %d", teamId));
            throw new UnprocessableEntityException("errors.team_does_not_exist");
        }
        return team;
    }

    @Override
    @Transactional
    public Team save(final Team entry) {
        return teamRepository.save(entry);
    }

    @Override
    @Transactional(readOnly = true)
    public Team load(final int teamId) {
        return teamRepository.load(teamId);
    }

    @Override
    @Transactional
    public void delete(final int teamId) {
        Team team = loadAndAssertExists(teamId);

        if (matchService.getMatchCount(teamId) > 0) {
            throw new UnprocessableEntityException(String.format("Team #%s is assigned to at least one match", teamId));
        }
        if (!cupWinnerService.loadAll(team).isEmpty()) {
            throw new UnprocessableEntityException(String.format("Team #%s presents as a winner of at least one cup", teamId));
        }

        LOGGER.debug(String.format("About to delete team #%d", teamId));
        cupTeamService.clearFor(teamId);
        teamRepository.delete(teamId);
        try {
            logoService.deleteLogo(team);
        } catch (IOException e) {
            LOGGER.error(String.format("Team #%s logo does not exist", team), e);
        }
        LOGGER.info(String.format("Team %s has been deleted", team));
    }

    @Override
    @Transactional(readOnly = true)
    public Team findByName(final int categoryId, final String teamName) {
        return teamJpaRepository.findByCategoryIdAndTeamName(categoryId, teamName);
    }

    @Override
    public Team findByImportId(final int categoryId, final String importId) {
        return teamJpaRepository.findByCategoryIdAndImportId(categoryId, importId);
    }

    @Override
    public boolean exists(final int teamId) {
        return teamJpaRepository.exists(teamId);
    }
}

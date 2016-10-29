package betmen.core.service;

import betmen.core.entity.Team;
import betmen.core.exception.UnprocessableEntityException;
import betmen.core.repository.TeamDao;
import betmen.core.repository.jpa.TeamJpaRepository;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {

    private static final Logger LOGGER = Logger.getLogger(TeamServiceImpl.class);

    @Autowired
    private TeamDao teamRepository;

    @Autowired
    private TeamJpaRepository teamJpaRepository;

    @Autowired
    private CupTeamService cupTeamService;

    @Override
    @Transactional(readOnly = true)
    public List<Team> loadAll() {
        throw new RuntimeException("Loading all teams makes performance very slow. Limit team list by category");
//		return newArrayList( teamRepository.loadAll() );
    }

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
        cupTeamService.clearFor(teamId);
        teamRepository.delete(teamId);
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

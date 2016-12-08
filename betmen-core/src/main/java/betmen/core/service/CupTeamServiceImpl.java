package betmen.core.service;

import betmen.core.entity.Cup;
import betmen.core.entity.CupTeam;
import betmen.core.entity.Team;
import betmen.core.repository.CupTeamDao;
import betmen.core.repository.jpa.CupTeamJpaRepository;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CupTeamServiceImpl implements CupTeamService {

    @Autowired
    private CupTeamDao cupTeamRepository;
    @Autowired
    private CupTeamJpaRepository cupTeamJpaRepository;
    @Autowired
    private CupService cupService;
    @Autowired
    private TeamService teamService;

    @Override
    public List<CupTeam> loadAll(final int cupId) {
        return Lists.newArrayList(cupTeamRepository.loadAll(cupId));
    }

    @Override
    @Transactional
    public void saveCupTeam(final int cupId, final int teamId, final boolean isActive) {
        final CupTeam cupTeam = cupTeamRepository.load(cupId, teamId);
        final boolean exists = cupTeam != null;
        if (isActive && !exists) {
            createNewEntry(cupId, teamId);
            return;
        }
        if (!isActive && exists) {
            deleteExistingEntry(cupTeam);
            return;
        }
    }

    @Override
    public boolean exists(final Cup cup, final Team team) {
        return exists(cup.getId(), team.getId());
    }

    @Override
    public boolean exists(final int cupId, final int teamId) {
        return cupTeamRepository.load(cupId, teamId) != null;
    }

    @Override
    public List<Team> loadActiveForCup(final int cupId) {
        return loadAll(cupId).stream()
                .sorted((o1, o2) -> o1.getTeam().getTeamName().compareToIgnoreCase(o2.getTeam().getTeamName()))
                .map(CupTeam::getTeam)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void clearFor(int teamId) {
        cupTeamJpaRepository.deleteAllByTeamId(teamId);
    }

    @Override
    @Transactional
    public void clearForCup(final int cupId) {
        cupTeamJpaRepository.delete(loadAll(cupId));
    }

    @Override
    public void clearFor(int cupId, int teamId) {
        saveCupTeam(cupId, teamId, false);
    }

    private void createNewEntry(final int cupId, final int teamId) {
        cupTeamRepository.save(new CupTeam(cupService.load(cupId), teamService.load(teamId)));
    }

    private void deleteExistingEntry(final CupTeam cupTeam) {
        cupTeamRepository.delete(cupTeam.getId());
    }
}

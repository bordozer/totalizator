package betmen.core.service;

import betmen.core.entity.Cup;
import betmen.core.entity.CupTeam;
import betmen.core.entity.Team;
import betmen.core.repository.CupTeamDao;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class CupTeamServiceImpl implements CupTeamService {

    @Autowired
    private CupTeamDao cupTeamRepository;

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
    @Transactional(readOnly = true)
    public boolean exists(final Cup cup, final Team team) {
        return exists(cup.getId(), team.getId());
    }

    @Override
    public boolean exists(final int cupId, final int teamId) {
        return cupTeamRepository.load(cupId, teamId) != null;
    }

    @Override
    public List<Team> loadActiveForCup(final int cupId) {

        final List<CupTeam> cupTeams = loadAll(cupId);
        Collections.sort(cupTeams, new Comparator<CupTeam>() {
            @Override
            public int compare(final CupTeam o1, final CupTeam o2) {
                return o1.getTeam().getTeamName().compareToIgnoreCase(o2.getTeam().getTeamName());
            }
        });

        return Lists.transform(cupTeams, new Function<CupTeam, Team>() {
            @Override
            public Team apply(final CupTeam cupTeam) {
                return cupTeam.getTeam();
            }
        });
    }

    @Override
    public void clearFor(int teamId) {
        cupService.loadAll().stream()
                .filter(cup -> exists(cup.getId(), teamId))
                .forEach(cup -> clearFor(cup.getId(), teamId));
    }

    @Override
    public void clearForCup(final int cupId) {
        loadAll(cupId).stream()
                .forEach(cupTeam -> {
                    clearFor(cupId, cupTeam.getTeam().getId());
                });
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

package betmen.core.service;

import betmen.core.repository.CupWinnerDao;
import betmen.core.entity.Cup;
import betmen.core.entity.CupWinner;
import betmen.core.entity.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Service
public class CupWinnerServiceImpl implements CupWinnerService {

    @Autowired
    private CupService cupService;

    @Autowired
    private CupWinnerDao cupWinnerRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CupWinner> loadAll() {
        return newArrayList(cupWinnerRepository.loadAll());
    }

    @Override
    @Transactional(readOnly = true)
    public CupWinner load(final int id) {
        return cupWinnerRepository.load(id);
    }

    @Override
    @Transactional
    public CupWinner save(final CupWinner entry) {
        return cupWinnerRepository.save(entry);
    }

    @Override
    @Transactional
    public void delete(final int id) {
        cupWinnerRepository.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CupWinner> loadAll(final Cup cup) {
        return newArrayList(cupWinnerRepository.loadAll(cup));
    }

    @Override
    @Transactional(readOnly = true)
    public CupWinner load(final Cup cup, final Team team) {
        return cupWinnerRepository.load(cup, team);
    }

    @Override
    public List<CupWinner> loadAll(final Team team) {

        final List<CupWinner> result = newArrayList();

        for (final Cup cup : cupService.loadPublicFinished()) {
            result.add(load(cup, team));
        }

        return result;
    }

    @Override
    @Transactional
    public void saveAll(final List<CupWinner> winners) {
        cupWinnerRepository.saveAll(winners);
    }

    @Override
    @Transactional
    public void deleteAllWinners(final Cup cup) {
        for (final CupWinner cupWinner : cupWinnerRepository.loadAll(cup)) {
            cupWinnerRepository.delete(cupWinner.getId());
        }
    }

    @Override
    public boolean hasChampions(final Cup cup) {
        return loadAll(cup).size() > 0;
    }
}

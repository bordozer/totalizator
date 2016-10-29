package betmen.core.service;

import betmen.core.exception.UnprocessableEntityException;
import betmen.core.repository.SportKindDao;
import betmen.core.entity.SportKind;
import betmen.core.repository.jpa.SportKindJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SportKindServiceImpl implements SportKindService {

    @Autowired
    private SportKindDao sportKindRepository;

    @Autowired
    private SportKindJpaRepository sportKindJpaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<SportKind> loadAll() {
        return sportKindRepository.loadAll();
    }

    @Override
    @Transactional(readOnly = true)
    public SportKind load(final int id) {
        return sportKindRepository.load(id);
    }

    @Override
    @Transactional
    public SportKind save(final SportKind entry) {
        return sportKindRepository.save(entry);
    }

    @Override
    @Transactional
    public void delete(final int id) {
        if (!sportKindJpaRepository.exists(id)) {
            throw new UnprocessableEntityException("Sport does not exist");
        }
        sportKindJpaRepository.delete(id);
    }

    @Override
    public SportKind findBySportKindName(final String name) {
        return sportKindJpaRepository.findBySportKindName(name);
    }

    @Override
    public SportKind loadAndAssertExists(final int sportKindId) {
        SportKind sportKind = load(sportKindId);
        if (sportKind == null) {
            throw new UnprocessableEntityException("Item does not exist");
        }
        return sportKind;
    }
}

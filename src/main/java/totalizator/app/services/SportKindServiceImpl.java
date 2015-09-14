package totalizator.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import totalizator.app.dao.SportKindDao;
import totalizator.app.models.SportKind;

import java.util.List;

@Service
public class SportKindServiceImpl implements SportKindService {

	@Autowired
	private SportKindDao sportKindRepository;

	@Override
	@Transactional( readOnly = true )
	public List<SportKind> loadAll() {
		return sportKindRepository.loadAll();
	}

	@Override
	@Transactional( readOnly = true )
	public SportKind load( final int id ) {
		return sportKindRepository.load( id );
	}

	@Override
	@Transactional
	public SportKind save( final SportKind entry ) {
		return sportKindRepository.save( entry );
	}

	@Override
	@Transactional
	public void delete( final int id ) {
		sportKindRepository.delete( id );
	}
}

package totalizator.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import totalizator.app.dao.CupRepository;
import totalizator.app.models.Cup;

import java.util.List;

@Service
public class CupServiceImpl implements CupService {

	@Autowired
	private CupRepository cupRepository;

	@Override
	@Transactional( readOnly = true )
	public List<Cup> loadAll() {
		return cupRepository.loadAll();
	}

	@Override
	@Transactional
	public void save( final Cup entry ) {
		cupRepository.save( entry );
	}

	@Override
	@Transactional( readOnly = true )
	public Cup load( final int id ) {
		return cupRepository.load( id );
	}

	@Override
	@Transactional
	public void delete( final int id ) {
		cupRepository.delete( id );
	}

	@Override
	@Transactional( readOnly = true )
	public Cup findByName( final String name ) {
		return cupRepository.findByName( name );
	}
}

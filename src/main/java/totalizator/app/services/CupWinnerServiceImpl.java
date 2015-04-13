package totalizator.app.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import totalizator.app.dao.CupWinnerRepository;
import totalizator.app.models.Cup;
import totalizator.app.models.CupWinner;
import totalizator.app.models.Team;

import java.util.List;

@Service
public class CupWinnerServiceImpl implements CupWinnerService {

	@Autowired
	private CupWinnerRepository cupWinnerRepository;

	@Override
	@Transactional( readOnly = true )
	public List<CupWinner> loadAll() {
		return cupWinnerRepository.loadAll();
	}

	@Override
	@Transactional( readOnly = true )
	public CupWinner load( final int id ) {
		return cupWinnerRepository.load( id );
	}

	@Override
	@Transactional
	public CupWinner save( final CupWinner entry ) {
		return cupWinnerRepository.save( entry );
	}

	@Override
	@Transactional
	public void delete( final int id ) {
		cupWinnerRepository.delete( id );
	}

	@Override
	@Transactional( readOnly = true )
	public List<CupWinner> loadAll( final Cup cup ) {
		return cupWinnerRepository.loadAll( cup );
	}

	@Override
	@Transactional( readOnly = true )
	public List<CupWinner> loadAll( final Cup cup, final Team team ) {
		return cupWinnerRepository.loadAll( cup, team );
	}

	@Override
	@Transactional
	public void saveAll( final Cup cup, final List<CupWinner> winners ) {
		deleteAllWinners( cup );
		saveAll( winners );
	}

	@Override
	@Transactional
	public void saveAll( final List<CupWinner> winners ) {
		cupWinnerRepository.saveAll( winners );
	}

	@Override
	@Transactional
	public void deleteAllWinners( final Cup cup ) {
		cupWinnerRepository.deleteAllWinners( cup );
	}
}

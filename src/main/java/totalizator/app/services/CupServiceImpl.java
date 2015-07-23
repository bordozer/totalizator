package totalizator.app.services;

import org.apache.commons.collections15.CollectionUtils;
import org.apache.commons.collections15.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import totalizator.app.dao.CupDao;
import totalizator.app.models.Category;
import totalizator.app.models.Cup;
import totalizator.app.models.CupWinner;
import totalizator.app.services.utils.DateTimeService;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.Lists.newArrayList;

@Service
public class CupServiceImpl implements CupService {

	@Autowired
	private CupDao cupRepository;

	@Autowired
	private CupWinnerService cupWinnerService;

	@Autowired
	private DateTimeService dateTimeService;

	@Override
	@Transactional( readOnly = true )
	public List<Cup> loadAll() {

		final List<Cup> cups = newArrayList( cupRepository.loadAll() );

		sort( cups );

		return cups;
	}

	@Override
	public void sort( final List<Cup> cups ) {
		Collections.sort( cups, new Comparator<Cup>() {

			@Override
			public int compare( final Cup o1, final Cup o2 ) {
				return o2.getCupStartTime().compareTo( o1.getCupStartTime() );
			}
		} );
	}

	@Override
	@Transactional
	public Cup save( final Cup entry ) {
		return cupRepository.save( entry );
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

	@Override
	public List<Cup> loadAllCurrent() {

		final List<Cup> portalPageCups = loadAllPublic();

		CollectionUtils.filter( portalPageCups, new Predicate<Cup>() {

			@Override
			public boolean evaluate( final Cup cup ) {
				return !isCupFinished( cup );
			}
		} );

		return portalPageCups;
	}

	@Override
	public List<Cup> loadAllPublic() {

		final List<Cup> portalPageCups = loadAll();

		CollectionUtils.filter( portalPageCups, new Predicate<Cup>() {
			@Override
			public boolean evaluate( final Cup cup ) {
				return cup.isPublicCup();
			}
		} );

		return portalPageCups;
	}

	@Override
	public List<Cup> loadAllPublic( final Category category ) {

		return loadAllPublic().stream().filter( new java.util.function.Predicate<Cup>() {

			@Override
			public boolean test( final Cup cup ) {
				return cup.getCategory().equals( category );
			}
		} ).collect( Collectors.toList() );
	}

	@Override
	public List<Cup> loadAllPublicFinished() {
		final List<Cup> cups = loadAllPublic();

		CollectionUtils.filter( cups, new Predicate<Cup>() {

			@Override
			public boolean evaluate( final Cup cup ) {
				return isCupFinished( cup );
			}
		} );

		return cups;
	}

	@Override
	public List<Cup> loadAllPublicFinished( final Category category ) {
		final List<Cup> cups = loadAllPublicFinished();

		CollectionUtils.filter( cups, new Predicate<Cup>() {

			@Override
			public boolean evaluate( final Cup cup ) {
				return cup.getCategory().equals( category );
			}
		} );

		return cups;
	}

	@Override
	@Transactional
	public Cup save( final Cup cup, final List<CupWinner> winners ) {

		final Cup saved = save( cup );

		cupWinnerService.deleteAllWinners( cup );

		cupWinnerService.saveAll( winners );

		return saved;
	}

	@Override
	public boolean isCupStarted( final Cup cup ) {
		return dateTimeService.getNow().isAfter( cup.getCupStartTime() );
	}

	@Override
	public boolean isCupFinished( final Cup cup ) {
		return cupWinnerService.hasChampions( cup );
	}
}

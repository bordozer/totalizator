package betmen.core.user.points;

import betmen.core.entity.Category;
import betmen.core.entity.Cup;
import betmen.core.entity.CupWinner;
import betmen.core.entity.Team;
import betmen.core.entity.User;
import betmen.core.service.CupService;
import betmen.core.service.CupWinnerService;
import betmen.core.service.points.calculation.cup.UserCupWinnersBonusCalculationServiceImpl;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.junit.Assert.assertEquals;

public class UserCupWinnersBonusCalculationServiceTest {

	@Before
	public void setup() {

	}

	@Test
	public void cupWinnerAndItPositionGuessedRight() {

		final TestData testData = new TestData();

		final UserCupWinnersBonusCalculationServiceImpl userBetPointsCalculationService = new UserCupWinnersBonusCalculationServiceImpl();
		userBetPointsCalculationService.setCupService( cupService( testData ) );
		userBetPointsCalculationService.setCupWinnerService( cupWinnerService( testData ) );

		final int points = userBetPointsCalculationService.getUserCupWinnerPoints( testData.cup, testData.team1, testData.user, 1 );
		assertEquals( "User guessed right the cup winner and it's position, have to be 6 points", 6, points );
	}

	@Test
	public void cupWinnerGuessedRightButNotPosition() {

		final TestData testData = new TestData();

		final UserCupWinnersBonusCalculationServiceImpl userBetPointsCalculationService = new UserCupWinnersBonusCalculationServiceImpl();
		userBetPointsCalculationService.setCupService( cupService( testData ) );
		userBetPointsCalculationService.setCupWinnerService( cupWinnerService( testData ) );

		final int points = userBetPointsCalculationService.getUserCupWinnerPoints( testData.cup, testData.team1, testData.user, 2 );
		assertEquals( "User guessed right the cup winner but did not it's position, have to be 3 points", 3, points );
	}

	@Test
	public void WinnerGuessedWrong() {

		final TestData testData = new TestData();

		final UserCupWinnersBonusCalculationServiceImpl userBetPointsCalculationService = new UserCupWinnersBonusCalculationServiceImpl();
		userBetPointsCalculationService.setCupService( cupService( testData ) );
		userBetPointsCalculationService.setCupWinnerService( cupWinnerService( testData ) );

		final int points = userBetPointsCalculationService.getUserCupWinnerPoints( testData.cup, testData.team3, testData.user, 1 );
		assertEquals( "User's team is outsider, it have to be zero points", 0, points );
	}

	private CupWinnerService cupWinnerService(final TestData testData ) {

		final CupWinnerService cupWinnerService = EasyMock.createMock( CupWinnerService.class );

		EasyMock.expect( cupWinnerService.loadAll( testData.cup ) ).andReturn( testData.cupWinners ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( cupWinnerService );

		return cupWinnerService;
	}

	private CupService cupService(final TestData testData ) {

		final CupService cupService = EasyMock.createMock( CupService.class );

		EasyMock.expect( cupService.isCupFinished( testData.cup ) ).andReturn( true ).anyTimes();

		EasyMock.expectLastCall();
		EasyMock.replay( cupService );

		return cupService;
	}

	private class TestData {

		private final Cup cup;
		private final Category category;

		private final Team team1;
		private final Team team2;
		private final Team team3;

		private final User user;

		private final List<CupWinner> cupWinners = newArrayList();

		private TestData() {

			category = new Category( "NBA" );
			category.setId( 1 );

			cup = new Cup( "The Cup", category );
			cup.setId( 2 );

			team1 = new Team( "Cold", category );
			team1.setId( 100 );

			team2 = new Team( "Silver", category );
			team2.setId( 101 );

			team3 = new Team( "Outsider", category );
			team3.setId( 102 );

			user = new User();
			user.setId( 1000 );
			user.setUsername( "The User" );

			final CupWinner winner1 = new CupWinner();
			winner1.setId( 500 );
			winner1.setCup( cup );
			winner1.setTeam( team1 );
			winner1.setCupPosition( 1 );

			final CupWinner winner2 = new CupWinner();
			winner2.setId( 501 );
			winner2.setCup( cup );
			winner2.setTeam( team2 );
			winner2.setCupPosition( 2 );

			cupWinners.add( winner1 );
			cupWinners.add( winner2 );
		}
	}
}

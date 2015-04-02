package totalizator.app.init.initializers;

import totalizator.app.init.TestDataInitializer;
import totalizator.app.services.utils.DateTimeService;

import java.util.Calendar;
import java.util.Date;

public abstract class MatchDataGenerationStrategy {

	public abstract Date generateBeginningTime( final DateTimeService dateTimeService );

	public abstract int generateScore();

	public abstract boolean isFinished();

	public static MatchDataGenerationStrategy nbaPastStrategy() {
		return new MatchDataGenerationStrategy() {

			@Override
			public Date generateBeginningTime( final DateTimeService dateTimeService ) {
				return dateTimeService.offset( Calendar.HOUR, -getRandomInt( 1, 512 ) );
			}

			@Override
			public int generateScore() {
				return getRandomInt( 80, 115 );
			}

			@Override
			public boolean isFinished() {
				return true;
			}
		};
	}

	public static MatchDataGenerationStrategy nbaFutureStrategy() {
		return new MatchDataGenerationStrategy() {

			@Override
			public Date generateBeginningTime( final DateTimeService dateTimeService ) {
				return dateTimeService.offset( Calendar.HOUR, getRandomInt( 1, 168 ) );
			}

			@Override
			public int generateScore() {
				return 0;
			}

			@Override
			public boolean isFinished() {
				return false;
			}
		};
	}

	public static MatchDataGenerationStrategy ncaaPastStrategy() {
		return new MatchDataGenerationStrategy() {

			@Override
			public Date generateBeginningTime( final DateTimeService dateTimeService ) {
				return dateTimeService.offset( Calendar.HOUR, -getRandomInt( 1, 512 ) );
			}

			@Override
			public int generateScore() {
				return getRandomInt( 60, 85 );
			}

			@Override
			public boolean isFinished() {
				return true;
			}
		};
	}

	public static MatchDataGenerationStrategy ncaaFutureStrategy() {
		return new MatchDataGenerationStrategy() {

			@Override
			public Date generateBeginningTime( final DateTimeService dateTimeService ) {
				return dateTimeService.offset( Calendar.HOUR, getRandomInt( 1, 200 ) );
			}

			@Override
			public int generateScore() {
				return 0;
			}

			@Override
			public boolean isFinished() {
				return false;
			}
		};
	}

	public static MatchDataGenerationStrategy uefaPastStrategy() {

		return new MatchDataGenerationStrategy() {

			@Override
			public Date generateBeginningTime( final DateTimeService dateTimeService ) {
				return dateTimeService.offset( Calendar.HOUR, -getRandomInt( 1, 512 ) );
			}

			@Override
			public int generateScore() {
				return getRandomInt( 0, 8 );
			}

			@Override
			public boolean isFinished() {
				return true;
			}
		};
	}

	public static MatchDataGenerationStrategy uefaFutureStrategy() {
		return new MatchDataGenerationStrategy() {

			@Override
			public Date generateBeginningTime( final DateTimeService dateTimeService ) {
				return dateTimeService.offset( Calendar.HOUR, getRandomInt( 1, 256 ) );
			}

			@Override
			public int generateScore() {
				return 0;
			}

			@Override
			public boolean isFinished() {
				return false;
			}
		};
	}

	private static int getRandomInt( final int minValue, final int maxValue ) {
		return AbstractDataInitializer.getRandomInt( minValue, maxValue );
	}
}

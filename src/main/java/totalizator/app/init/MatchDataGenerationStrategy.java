package totalizator.app.init;

import totalizator.app.services.utils.DateTimeService;

import java.util.Calendar;
import java.util.Date;

abstract class MatchDataGenerationStrategy {

	abstract Date generateBeginningTime( final DateTimeService dateTimeService );

	abstract int generateScore();

	abstract boolean isFinished();

	static MatchDataGenerationStrategy nbaPastStrategy() {
		return new MatchDataGenerationStrategy() {

			@Override
			Date generateBeginningTime( final DateTimeService dateTimeService ) {
				return dateTimeService.offset( Calendar.HOUR, -getRandomInt( 1, 512 ) );
			}

			@Override
			int generateScore() {
				return getRandomInt( 80, 115 );
			}

			@Override
			boolean isFinished() {
				return true;
			}
		};
	}

	static MatchDataGenerationStrategy nbaFutureStrategy() {
		return new MatchDataGenerationStrategy() {

			@Override
			Date generateBeginningTime( final DateTimeService dateTimeService ) {
				return dateTimeService.offset( Calendar.HOUR, getRandomInt( 1, 168 ) );
			}

			@Override
			int generateScore() {
				return 0;
			}

			@Override
			boolean isFinished() {
				return false;
			}
		};
	}

	static MatchDataGenerationStrategy ncaaPastStrategy() {
		return new MatchDataGenerationStrategy() {

			@Override
			Date generateBeginningTime( final DateTimeService dateTimeService ) {
				return dateTimeService.offset( Calendar.HOUR, -getRandomInt( 1, 512 ) );
			}

			@Override
			int generateScore() {
				return getRandomInt( 60, 85 );
			}

			@Override
			boolean isFinished() {
				return true;
			}
		};
	}

	static MatchDataGenerationStrategy ncaaFutureStrategy() {
		return new MatchDataGenerationStrategy() {

			@Override
			Date generateBeginningTime( final DateTimeService dateTimeService ) {
				return dateTimeService.offset( Calendar.HOUR, getRandomInt( 1, 200 ) );
			}

			@Override
			int generateScore() {
				return 0;
			}

			@Override
			boolean isFinished() {
				return false;
			}
		};
	}

	static MatchDataGenerationStrategy uefaPastStrategy() {

		return new MatchDataGenerationStrategy() {

			@Override
			Date generateBeginningTime( final DateTimeService dateTimeService ) {
				return dateTimeService.offset( Calendar.HOUR, -getRandomInt( 1, 512 ) );
			}

			@Override
			int generateScore() {
				return getRandomInt( 0, 8 );
			}

			@Override
			boolean isFinished() {
				return true;
			}
		};
	}

	static MatchDataGenerationStrategy uefaFutureStrategy() {
		return new MatchDataGenerationStrategy() {

			@Override
			Date generateBeginningTime( final DateTimeService dateTimeService ) {
				return dateTimeService.offset( Calendar.HOUR, getRandomInt( 1, 256 ) );
			}

			@Override
			int generateScore() {
				return 0;
			}

			@Override
			boolean isFinished() {
				return false;
			}
		};
	}

	private static int getRandomInt( final int minValue, final int maxValue ) {
		return TestDataInitializer.getRandomInt( minValue, maxValue );
	}
}

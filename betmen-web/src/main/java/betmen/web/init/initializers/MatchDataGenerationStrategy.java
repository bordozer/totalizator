package betmen.web.init.initializers;

import betmen.core.service.utils.DateTimeService;

import java.time.LocalDateTime;

abstract class MatchDataGenerationStrategy {

    public abstract LocalDateTime generateBeginningTime(final DateTimeService dateTimeService);

    public abstract int generateScore();

    public abstract boolean isFinished();

    public static MatchDataGenerationStrategy nbaPastStrategy() {

        return new MatchDataGenerationStrategy() {

            @Override
            public LocalDateTime generateBeginningTime(final DateTimeService dateTimeService) {
                return dateTimeService.minusHours(rnd(1, 512));
            }

            @Override
            public int generateScore() {
                return rnd(80, 115);
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
            public LocalDateTime generateBeginningTime(final DateTimeService dateTimeService) {
                return dateTimeService.plusHours(rnd(1, 168));
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
            public LocalDateTime generateBeginningTime(final DateTimeService dateTimeService) {
                return dateTimeService.minusHours(rnd(1, 512));
            }

            @Override
            public int generateScore() {
                return rnd(60, 85);
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
            public LocalDateTime generateBeginningTime(final DateTimeService dateTimeService) {
                return dateTimeService.plusHours(rnd(1, 200));
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
            public LocalDateTime generateBeginningTime(final DateTimeService dateTimeService) {
                return dateTimeService.minusHours(rnd(1, 512));
            }

            @Override
            public int generateScore() {
                return rnd(0, 8);
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
            public LocalDateTime generateBeginningTime(final DateTimeService dateTimeService) {
                return dateTimeService.plusHours(rnd(1, 256));
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

    private static int rnd(final int minValue, final int maxValue) {
        return AbstractDataInitializer.rnd(minValue, maxValue);
    }
}

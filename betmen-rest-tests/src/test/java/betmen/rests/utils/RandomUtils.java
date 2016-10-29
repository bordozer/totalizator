package betmen.rests.utils;

import betmen.rests.common.UserRegData;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

public class RandomUtils {

    public static String generate() {
        return String.valueOf(System.nanoTime());
    }

    public static UserRegData randomUser() {
        String login = generate();
        String username = generate();
        String password = generate();

        return new UserRegData(login, username, password);
    }

    public static String UUID() {
        return UUID.randomUUID().toString();
    }

    public static boolean randomBoolean() {
        return new Random().nextBoolean();
    }

    public static int randomInt(final int max) {
        return randomInt(0, max);
    }

    public static int randomInt(final int min, final int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }

    public static LocalDateTime randomTime() {
        int year = randomInt(2010, 2020);
        int month = randomInt(1, 12);
        int day = randomInt(1, 28);
        int hour = randomInt(0, 23);
        int minute = randomInt(0, 59);
        return LocalDateTime.of(year, month, day, hour, minute);
    }

    public static String cupName() {
        return String.format("cup_%s", UUID());
    }

    public static String categoryName() {
        return String.format("cat_%s", generate());
    }

    public static String pointsStrategyName() {
        return String.format("pcs_%s", generate());
    }

    public static String sportName() {
        return String.format("sk_%s", generate());
    }

    public static String teamName() {
        return String.format("team_%s", generate());
    }
}

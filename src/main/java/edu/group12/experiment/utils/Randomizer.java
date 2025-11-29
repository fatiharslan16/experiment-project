package edu.group12.experiment.utils;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Randomizer {

    private static final Random RANDOM = new Random();

    public static String assignCondition() {
        boolean video = RANDOM.nextBoolean();
        return video ? "VIDEO" : "TEXT";
    }

    public static <T> void shuffleList(List<T> items) {
        Collections.shuffle(items, RANDOM);
    }

    public static int nextInt(int boundExclusive) {
        return RANDOM.nextInt(boundExclusive);
    }
}

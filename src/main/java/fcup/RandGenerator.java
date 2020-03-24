package fcup;

import java.util.Random;

public class RandGenerator {
    private final Random rand = new Random();

    public int randInt(int min, int max) {
        return this.rand.nextInt((max - min) + 1) + min;
    }
}

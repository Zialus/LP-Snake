package fcup;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.util.List;
import java.util.logging.Logger;

/**
 * Simulates a full headless game: the snake follows a deterministic serpentine path,
 * eating food placed along the way, while collisions are checked on every tick.
 */
@State(Scope.Thread)
public class GameSimulationBenchmark {

    private static final int COLUMNS = 120;
    private static final int ROWS = 60;
    private static final int FOOD_SPACING = 7;

    // Keep a strong reference so the logger configuration is not garbage collected.
    private static final Logger GAME_LOGGER = Logger.getLogger(GameLogic.class.getName());

    @Param({"200", "2000"})
    public int ticks;

    private int[] pathX;
    private int[] pathY;

    @Setup(Level.Trial)
    public void setUp() {
        GAME_LOGGER.setLevel(java.util.logging.Level.OFF);

        // Precompute a serpentine path that stays inside the borders and never crosses itself.
        pathX = new int[ticks + FOOD_SPACING];
        pathY = new int[ticks + FOOD_SPACING];
        int x = 10;
        int y = 10;
        int dx = 1;
        for (int i = 0; i < pathX.length; i++) {
            if ((dx > 0 && x == COLUMNS - 3) || (dx < 0 && x == 2)) {
                y++;
                dx = -dx;
            } else {
                x += dx;
            }
            if (y >= ROWS - 2) {
                throw new IllegalStateException("Path does not fit on the board");
            }
            pathX[i] = x;
            pathY[i] = y;
        }
    }

    @Benchmark
    public int playGame() {
        GameLogic game = new GameLogic(COLUMNS, ROWS);
        game.createSnake();
        List<Coordinates> food = game.getFoodPositions();
        food.add(new Coordinates(pathX[FOOD_SPACING], pathY[FOOD_SPACING]));

        int lastScore = 0;
        for (int i = 0; i < ticks; i++) {
            game.setCursorX(pathX[i]);
            game.setCursorY(pathY[i]);
            game.updateSnake();
            game.findCollisions();
            if (game.isHasHitBorder() || game.isHasHitItself()) {
                throw new IllegalStateException("Unexpected collision at tick " + i);
            }
            if (game.getScore() != lastScore) {
                // Replace the randomly placed food with a deterministic one further down the path.
                lastScore = game.getScore();
                food.clear();
                food.add(new Coordinates(pathX[i + FOOD_SPACING], pathY[i + FOOD_SPACING]));
            }
        }
        return game.getScore() + game.getSnakeBodyPositions().size();
    }
}

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
 * Benchmarks for the per-tick game logic: collision detection and snake movement
 * for snakes of different lengths.
 */
@State(Scope.Thread)
public class GameLogicBenchmark {

    private static final int BOARD_SIZE = 1000;

    // Keep a strong reference so the logger configuration is not garbage collected.
    private static final Logger GAME_LOGGER = Logger.getLogger(GameLogic.class.getName());

    @Param({"5", "100", "2000"})
    public int snakeLength;

    private GameLogic gameLogic;

    @Setup(Level.Trial)
    public void silenceLogs() {
        GAME_LOGGER.setLevel(java.util.logging.Level.OFF);
    }

    @Setup(Level.Iteration)
    public void setUp() {
        gameLogic = new GameLogic(BOARD_SIZE, BOARD_SIZE);
        // Lay the snake out in a serpentine shape so the head never overlaps the body.
        List<Coordinates> body = gameLogic.getSnakeBodyPositions();
        int width = BOARD_SIZE - 20;
        for (int i = 0; i < snakeLength; i++) {
            int row = i / width;
            int col = (row % 2 == 0) ? (width - (i % width)) : (i % width);
            body.add(new Coordinates(col + 10, row + 10));
        }
        Coordinates head = body.get(0);
        gameLogic.setCursorX(head.getX());
        gameLogic.setCursorY(head.getY());
        // Food far away from the snake so it is never eaten.
        gameLogic.getFoodPositions().add(new Coordinates(BOARD_SIZE - 2, BOARD_SIZE - 2));
    }

    @Benchmark
    public boolean findCollisions() {
        gameLogic.findCollisions();
        return gameLogic.isHasHitItself() || gameLogic.isHasHitBorder();
    }

    @Benchmark
    public int updateSnake() {
        // The cursor does not move, so the snake keeps the same length and shape.
        gameLogic.updateSnake();
        return gameLogic.getSnakeBodyPositions().size();
    }
}

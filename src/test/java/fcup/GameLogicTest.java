package fcup;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameLogicTest {
    @Test
    public void testNewSnake() {
        GameLogic game = new GameLogic();
        game.createSnake();
        assertEquals(game.snakeBodyPositions.size(), 5);
    }
}

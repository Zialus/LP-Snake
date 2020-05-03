package fcup;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameLogicTest {
    @Test
    public void testNewSnake() {
        GameLogic game = new GameLogic(20,20);
        game.createSnake();
        assertEquals(5, game.getSnakeBodyPositions().size(), "Snake body size should be 5");
    }
}

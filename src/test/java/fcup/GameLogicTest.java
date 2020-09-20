package fcup;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameLogicTest {
    @Test
    void testNewSnake() {
        GameLogic game = new GameLogic(20,20);
        game.createSnake();
        assertEquals(5, game.getSnakeBodyPositions().size(), "Snake body size should be 5");
    }
}

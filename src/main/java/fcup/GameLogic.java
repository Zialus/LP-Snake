package fcup;

import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.List;

@Log
public class GameLogic {
    private final ArrayList<Coordinates> snakeBodyPositions = new ArrayList<>();
    private final ArrayList<Coordinates> foodPositions = new ArrayList<>();

    private final int termColumns;
    private final int termRows;

    private int score = 0;
    // Snake's initial position
    private int cursorX = 10;
    private int cursorY = 10;

    private boolean hasHitBorder = false;
    private boolean hasHitItself = false;
    private boolean hasHitFood = false;

    private final RandGenerator randG = new RandGenerator();

    public GameLogic(int termColumns, int termRows) {
        this.termRows = termRows;
        this.termColumns = termColumns;
    }

    public List<Coordinates> getSnakeBodyPositions() {
        return snakeBodyPositions;
    }

    public List<Coordinates> getFoodPositions() {
        return foodPositions;
    }

    public int getScore() {
        return score;
    }

    public int getCursorX() {
        return cursorX;
    }

    public void setCursorX(int cursorX) {
        this.cursorX = cursorX;
    }

    public int getCursorY() {
        return cursorY;
    }

    public void setCursorY(int cursorY) {
        this.cursorY = cursorY;
    }

    public boolean isHasHitBorder() {
        return hasHitBorder;
    }

    public boolean isHasHitItself() {
        return hasHitItself;
    }

    public void createFood() {
        int foodColumn = randG.randInt(1, termColumns - 2);
        int foodRow = randG.randInt(1, termRows - 2);

        Coordinates foodCord = new Coordinates(foodColumn, foodRow);
        foodPositions.add(foodCord);
    }

    public void createSnake() {
        Coordinates cor1 = new Coordinates(cursorX, cursorY);
        Coordinates cor2 = new Coordinates(cursorX - 1, cursorY);
        Coordinates cor3 = new Coordinates(cursorX - 2, cursorY);
        Coordinates cor4 = new Coordinates(cursorX - 3, cursorY);
        Coordinates cor5 = new Coordinates(cursorX - 4, cursorY);
        snakeBodyPositions.add(cor1);
        snakeBodyPositions.add(cor2);
        snakeBodyPositions.add(cor3);
        snakeBodyPositions.add(cor4);
        snakeBodyPositions.add(cor5);
    }

    public void findCollisions() {

        Coordinates head = snakeBodyPositions.get(0);
        int headX = head.getX();
        int headY = head.getY();

        // Collisions with borders
        for (int i = 0; i < termRows; i++) {
            if ((headX == 0 || headX == termColumns - 1) && headY == i) {
                hasHitBorder = true;
                log.info("Snake has hit a column");
            }
        }

        for (int i = 0; i < termColumns; i++) {
            if ((headY == 0 || headY == termRows - 1) && headX == i) {
                hasHitBorder = true;
                log.info("Snake has hit a row");
            }
        }

        // Collisions with body
        int len = snakeBodyPositions.size();

        for (int i = 1; i < len; i++) {
            int bodyX = snakeBodyPositions.get(i).getX();
            int bodyY = snakeBodyPositions.get(i).getY();

            if (headX == bodyX && headY == bodyY) {
                hasHitItself = true;
                log.info("Snake hit itself");
            }
        }

    }

    public void updateSnake() {
        int len = snakeBodyPositions.size();
        Coordinates head = snakeBodyPositions.get(0);
        int headX = head.getX();
        int headY = head.getY();

        // Collisions with food
        Coordinates food = foodPositions.get(0);
        int foodX = food.getX();
        int foodY = food.getY();

        if (headX == foodX && headY == foodY) {
            hasHitFood = true;
            score += 10;
            log.info("Food has been eaten");
        }

        // Increase Snake Size
        if (hasHitFood) {

            int x = snakeBodyPositions.get(len - 1).getX();
            int y = snakeBodyPositions.get(len - 1).getY();

            Coordinates newTail = new Coordinates(x, y);

            snakeBodyPositions.add(newTail);

            foodPositions.remove(0);
            createFood();
            hasHitFood = false;

        }

        // Remove the tail
        snakeBodyPositions.remove(len - 1);

        int newX = cursorX;
        int newY = cursorY;
        Coordinates newHead = new Coordinates(newX, newY);

        // Add the head
        snakeBodyPositions.add(0, newHead);

    }

}

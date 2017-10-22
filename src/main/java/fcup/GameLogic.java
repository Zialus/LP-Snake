package fcup;

import com.googlecode.lanterna.TerminalSize;

import java.util.ArrayList;
import java.util.Random;

public class GameLogic {
    final ArrayList<Coordinates> snakeBodyPositions = new ArrayList<Coordinates>();
    final ArrayList<Coordinates> foodList = new ArrayList<Coordinates>();
    TerminalSize terminalSize;
    int termColumns;
    int termRows;
    int score = 0;
    // Snake's initial position
    int cursor_x = 10;
    int cursor_y = 10;
    boolean hasHitBorder = false;
    boolean hasHitItself = false;
    boolean hasHitFood = false;

    public GameLogic() {
    }

    public TerminalSize getTerminalSize() {
        return terminalSize;
    }

    public void setTerminalSize(TerminalSize terminalSize) {
        this.terminalSize = terminalSize;
    }

    public int getTermColumns() {
        return termColumns;
    }

    public void setTermColumns(int termColumns) {
        this.termColumns = termColumns;
    }

    public int getTermRows() {
        return termRows;
    }

    public void setTermRows(int termRows) {
        this.termRows = termRows;
    }

    public ArrayList<Coordinates> getSnakeBodyPositions() {
        return snakeBodyPositions;
    }

    public ArrayList<Coordinates> getFoodList() {
        return foodList;
    }

    public int getScore() {
        return score;
    }

    public int getCursor_x() {
        return cursor_x;
    }

    public void setCursor_x(int cursor_x) {
        this.cursor_x = cursor_x;
    }

    public int getCursor_y() {
        return cursor_y;
    }

    public void setCursor_y(int cursor_y) {
        this.cursor_y = cursor_y;
    }

    public boolean isHasHitBorder() {
        return hasHitBorder;
    }

    public boolean isHasHitItself() {
        return hasHitItself;
    }

    void createFood() {
        int foodColumn = randInt(1, termColumns - 2);
        int foodRow = randInt(1, termRows - 2);

        Coordinates foodCord = new Coordinates(foodColumn, foodRow);
        foodList.add(foodCord);
    }

    void createSnake() {
        Coordinates cor1 = new Coordinates(cursor_x, cursor_y);
        Coordinates cor2 = new Coordinates(cursor_x - 1, cursor_y);
        Coordinates cor3 = new Coordinates(cursor_x - 2, cursor_y);
        Coordinates cor4 = new Coordinates(cursor_x - 3, cursor_y);
        Coordinates cor5 = new Coordinates(cursor_x - 4, cursor_y);
        snakeBodyPositions.add(cor1);
        snakeBodyPositions.add(cor2);
        snakeBodyPositions.add(cor3);
        snakeBodyPositions.add(cor4);
        snakeBodyPositions.add(cor5);
    }

    void findCollisions() {

        Coordinates head = snakeBodyPositions.get(0);
        int head_X = (head.getX());
        int head_Y = (head.getY());

        // Collisions with borders
        for (int i = 0; i < termRows; i++) {
            if ((head_X == 0 || head_X == termColumns - 1) && head_Y == i) {
                hasHitBorder = true;
                System.out.println("Snake has hit a column");
            }
        }

        for (int i = 0; i < termColumns; i++) {
            if ((head_Y == 0 || head_Y == termRows - 1) && head_X == i) {
                hasHitBorder = true;
                System.out.println("Snake has hit a row");
            }
        }

        // Collisions with body
        int len = snakeBodyPositions.size();

        for (int i = 1; i < len; i++) {
            int body_X = snakeBodyPositions.get(i).getX();
            int body_Y = snakeBodyPositions.get(i).getY();

            if ((head_X == body_X && head_Y == body_Y)) {
                hasHitItself = true;
                System.out.println("Snake hit itself");
            }
        }

    }

    void updateSnake() {
        int len = snakeBodyPositions.size();
        Coordinates head = snakeBodyPositions.get(0);
        int head_X = (head.getX());
        int head_Y = (head.getY());

        // Collisions with food
        Coordinates food = foodList.get(0);
        int food_X = food.getX();
        int food_Y = food.getY();

        if ((head_X == food_X) && (head_Y == food_Y)) {
            hasHitFood = true;
            score += 10;
            System.out.println("Food has been eaten");
        }

        // Increase Snake Size
        if (hasHitFood) {

            int x = snakeBodyPositions.get(len - 1).getX();
            int y = snakeBodyPositions.get(len - 1).getY();

            Coordinates newTail = new Coordinates(x, y);

            snakeBodyPositions.add(newTail);

            foodList.remove(0);
            createFood();
            hasHitFood = false;

        }

        // Remove the tail
        snakeBodyPositions.remove(len - 1);

        int newX = cursor_x;
        int newY = cursor_y;
        Coordinates newHead = new Coordinates(newX, newY);

        // Add the head
        snakeBodyPositions.add(0, newHead);

    }

    int randInt(int min, int max) {
        Random rand = new Random();

        return rand.nextInt((max - min) + 1) + min;
    }
}
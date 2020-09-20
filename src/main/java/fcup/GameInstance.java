package fcup;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import lombok.extern.java.Log;

import java.io.IOException;

@Log
public class GameInstance {
    private final Terminal term;
    private final TerminalSize terminalSize = new TerminalSize(80, 40);
    private final GameLogic gameLogic = new GameLogic(terminalSize.getColumns(), terminalSize.getRows());

    public GameInstance() throws IOException {

        // Initialize the terminal
        term = new DefaultTerminalFactory().setInitialTerminalSize(terminalSize).createTerminal();

        term.enterPrivateMode();


        Directions direction = Directions.RIGHT;

        // Create game objects
        gameLogic.createSnake();
        gameLogic.createFood();

        // Deal with input
        while (true) {

            term.clearScreen();
            term.enableSGR(SGR.BOLD);

            showBorders();
            showFood();
            showSnake();
            term.setCursorVisible(false);

            term.flush();

            direction = readInput(direction);

            proccessDirection(direction);

            // Update game state

            gameLogic.updateSnake();
            gameLogic.findCollisions();
            dealWithCollisions();

            try {
                if (direction == Directions.RIGHT || direction == Directions.LEFT) {
                    Thread.sleep(60L);
                } else {
                    Thread.sleep(80L);
                }
            } catch (InterruptedException ie) {
                log.severe(ie.getMessage());
                Thread.currentThread().interrupt();
            }

        }
        //Input has been dealt with

    }

    private void proccessDirection(Directions direction) {
        switch (direction) {
            case LEFT:
                gameLogic.setCursorX(gameLogic.getCursorX() - 1);
                break;
            case RIGHT:
                gameLogic.setCursorX(gameLogic.getCursorX() + 1);
                break;
            case DOWN:
                gameLogic.setCursorY(gameLogic.getCursorY() + 1);
                break;
            case UP:
                gameLogic.setCursorY(gameLogic.getCursorY() - 1);
                break;
        }
    }

    private Directions readInput(Directions direction) throws IOException {
        KeyStroke ks = term.pollInput();

        if (ks != null) {
            log.info(ks.toString());
            switch (ks.getKeyType()) {
                case Escape:
                    term.exitPrivateMode();
                    Runtime.getRuntime().exit(0);
                    break;
                case ArrowLeft:
                    if (direction != Directions.RIGHT) {
                        direction = Directions.LEFT;
                    }
                    break;
                case ArrowRight:
                    if (direction != Directions.LEFT) {
                        direction = Directions.RIGHT;
                    }
                    break;
                case ArrowDown:
                    if (direction != Directions.UP) {
                        direction = Directions.DOWN;
                    }
                    break;
                case ArrowUp:
                    if (direction != Directions.DOWN) {
                        direction = Directions.UP;
                    }
                    break;
                default:
                    break;
            }
        }
        return direction;
    }


    private void showBorders() throws IOException {
        int columns = terminalSize.getColumns();
        int rows = terminalSize.getRows();

        term.setForegroundColor(TextColor.ANSI.RED);

        for (int i = 0; i < rows; i++) {
            term.setCursorPosition(0, i);
            term.putCharacter('#');
            term.setCursorPosition(columns, i);
            term.putCharacter('#');
        }

        for (int i = 1; i < columns - 1; i++) {
            term.setCursorPosition(i, 0);
            term.putCharacter('#');
            term.setCursorPosition(i, rows);
            term.putCharacter('#');
        }

    }

    private void showFood() throws IOException {

        term.setForegroundColor(TextColor.ANSI.YELLOW);

        for (Coordinates food : gameLogic.getFoodPositions()) {
            term.setCursorPosition(food.getX(), food.getY());
            term.putCharacter('X');
        }

    }

    private void showSnake() throws IOException {

        // Head Stuff
        term.setForegroundColor(TextColor.ANSI.BLUE);

        Coordinates head = gameLogic.getSnakeBodyPositions().get(0);
        term.setCursorPosition(head.getX(), head.getY());
        term.putCharacter('@');

        // Body and Tail Stuff

        term.setForegroundColor(TextColor.ANSI.GREEN);
        int len = gameLogic.getSnakeBodyPositions().size();

        for (int i = 1; i < len; i++) {
            Coordinates bodyPart = gameLogic.getSnakeBodyPositions().get(i);
            term.setCursorPosition(bodyPart.getX(), bodyPart.getY());
            term.putCharacter('O');
        }

        Coordinates tail = gameLogic.getSnakeBodyPositions().get(len - 1);
        term.setCursorPosition(tail.getX(), tail.getY());
        term.putCharacter('Q');

    }

    private void dealWithCollisions() throws IOException {
        if (gameLogic.isHasHitBorder() || gameLogic.isHasHitItself()) {
            term.clearScreen();
            showBorders();
            log.info("GAME OVER -- Cursor @[x=" + gameLogic.getCursorX() + "|y=" + gameLogic.getCursorY() + "]");

            show("GAME OVER", 45, 14);
            show("PRESS ESC to Exit or ENTER to start a NEW GAME", 28, 17);
            show("Score = " + gameLogic.getScore(), 45, 20);

            //Deal with Game Over and start the game again
            term.flush();

            KeyType exit = term.readInput().getKeyType();

            if (exit == KeyType.Escape) {
                Runtime.getRuntime().exit(0);
            }

            if (exit == KeyType.Enter) {
                term.exitPrivateMode();
                new GameInstance();
            }


        }
    }

    private void show(String message, int x, int y) throws IOException {
        term.setCursorPosition(x, y);

        for (char c : message.toCharArray()) {
            term.putCharacter(c);
        }
    }

}

package fcup;

import java.util.ArrayList;
import java.util.Random;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalSize;

public class GameInstance {
	private final Terminal term;
	private final ArrayList<Coordinates> snakeBodyPositions = new ArrayList<>();
	private final ArrayList<Coordinates> foodList = new ArrayList<>();
	private int score = 0; // Player score
	private int cursor_x=10, cursor_y=10; // Snake's initial position

	private boolean hasHitBorder = false;
	private boolean hasHitItself = false;
	private boolean hasHitFood = false;

	public enum Directions{UP,DOWN,LEFT,RIGHT}

	public GameInstance(){

		// Initialize the terminal
		term = TerminalFacade.createTerminal();
		term.enterPrivateMode();
		Directions direction = GameInstance.Directions.RIGHT;

		// Create game objects
		createSnake();
		createFood();

		// Deal with input
		while(true){

			term.clearScreen();
			term.applySGR(Terminal.SGR.ENTER_BOLD);

			showBorders();
			showFood();
			showSnake();
			term.setCursorVisible(false);

			term.flush();

			Key k = term.readInput();

			if (k != null) {
				System.out.println(k);
				switch (k.getKind()) {
					case Escape:
						term.exitPrivateMode();
						return;
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

			switch (direction) {
				case LEFT:
					cursor_x -= 1;
					break;
				case RIGHT:
					cursor_x += 1;
					break;
				case DOWN:
					cursor_y += 1;
					break;
				case UP:
					cursor_y -= 1;
					break;
				default:
					break;
			}


			// Update game state

			updateSnake();
			collisions();

			try
			{
				if (direction == Directions.RIGHT || direction == Directions.LEFT){
					Thread.sleep(60);
				}
				else{
					Thread.sleep(80);
				}
			}
			catch (InterruptedException ie)
			{
				ie.printStackTrace();
			}

		}
		//Input has been dealt with

	}

	private void createFood(){
		TerminalSize terminalSize = term.getTerminalSize();

		int termColumns = terminalSize.getColumns();
		int termRows = terminalSize.getRows();

		int foodColumns = randInt(1,termColumns-2);
		int foodRows = randInt(1,termRows-2);

		Coordinates foodCord = new Coordinates(foodColumns,foodRows);
		foodList.add(foodCord);

	}

	private void createSnake(){
		Coordinates cor1 = new Coordinates(cursor_x  , cursor_y);
		Coordinates cor2 = new Coordinates(cursor_x -1,cursor_y);
		Coordinates cor3 = new Coordinates(cursor_x -2,cursor_y);
		Coordinates cor4 = new Coordinates(cursor_x -3,cursor_y);
		Coordinates cor5 = new Coordinates(cursor_x -4,cursor_y);
		snakeBodyPositions.add(cor1);
		snakeBodyPositions.add(cor2);
		snakeBodyPositions.add(cor3);
		snakeBodyPositions.add(cor4);
		snakeBodyPositions.add(cor5);
	}

	private void showBorders(){
		TerminalSize terminalSize = term.getTerminalSize();
		term.applyForegroundColor(Terminal.Color.RED);

		int columns = terminalSize.getColumns();
		int rows = terminalSize.getRows();

		for(int i = 0; i<rows;i++){
			term.moveCursor(0,i);
			term.putCharacter('#');
			term.moveCursor(columns,i);
			term.putCharacter('#');
		}

		for(int i = 1; i<columns-1;i++){
			term.moveCursor(i,0);
			term.putCharacter('#');
			term.moveCursor(i,rows);
			term.putCharacter('#');
		}

	}

	private void showFood() {

		term.applyForegroundColor(Terminal.Color.YELLOW);

		for (Coordinates food : foodList) {
			term.moveCursor(food.getX(), food.getY());
			term.putCharacter('X');
		}

	}


	private void showSnake(){

		// Head Stuff
		term.applyForegroundColor(Terminal.Color.BLUE);

		Coordinates head = snakeBodyPositions.get(0);
		term.moveCursor(head.getX(),head.getY());
		term.putCharacter('@');

		// Body and Tail Stuff

		term.applyForegroundColor(Terminal.Color.GREEN);
		int len = snakeBodyPositions.size();

		for(int i =1; i<len;i++){
			Coordinates bodyPart = snakeBodyPositions.get(i);
			term.moveCursor(bodyPart.getX(),bodyPart.getY());
			term.putCharacter('O');
		}

		Coordinates tail = snakeBodyPositions.get(len-1);
		term.moveCursor(tail.getX(),tail.getY());
		term.putCharacter( 'Q' );

	}


	private void collisions() {

		TerminalSize terminalSize = term.getTerminalSize();
		int columns = terminalSize.getColumns();
		int rows = terminalSize.getRows();

		Coordinates head = snakeBodyPositions.get(0);
		int head_X = ( head.getX());
		int head_Y = ( head.getY());

		// Collisions with borders
		for(int i = 0; i<rows;i++){
			if ( (head_X==0 || head_X == columns-1) && head_Y == i){
				hasHitBorder = true;
				System.out.println("Snake has hit a column");
			}
		}

		for(int i = 0; i<columns;i++){
			if ( (head_Y==0 || head_Y == rows-1) && head_X == i){
				hasHitBorder = true;
				System.out.println("Snake has hit a row");
			}
		}

		// Collisions with body
		int len = snakeBodyPositions.size();

		for(int i=1;i<len; i++){
			int body_X = snakeBodyPositions.get(i).getX();
			int body_Y = snakeBodyPositions.get(i).getY();

			if ( (head_X==body_X && head_Y == body_Y) )
			{
				hasHitItself = true;
				System.out.println("Snake hit itself");
			}
		}

		if( hasHitBorder || hasHitItself ) {
			term.clearScreen();
			showBorders();
			System.out.println("GAME OVER");
			System.out.println("-----x="+cursor_x+"-----");
			System.out.println("-----y="+cursor_y+"-----");

			show("GAME OVER",45,14);

			show("PRESS ESC to Exit or ENTER to start a NEW GAME",28,17);

			show("Score = " + score,45,20);

			//Deal with Game Over and start the game again
			while(true)
			{
				Key exit = term.readInput();
				if (exit != null)
				{
					if (exit.getKind() == Key.Kind.Escape)
					{
						System.exit(0);
					}
					if (exit.getKind() == Key.Kind.Enter) {
						term.exitPrivateMode();
						new GameInstance();
					}
				}

			}

		}

	}

	private void updateSnake() {
		int len = snakeBodyPositions.size();
		Coordinates head = snakeBodyPositions.get(0);
		int head_X = ( head.getX());
		int head_Y = ( head.getY());

		// Collisions with food
		Coordinates food = foodList.get(0);
		int food_X = food.getX();
		int food_Y = food.getY();

		if ( (head_X == food_X) && (head_Y == food_Y)) {
			hasHitFood = true;
			score += 10;
			System.out.println("Food has been eaten");
		}

		// Increase Snake Size
		if (hasHitFood) {

			int x = snakeBodyPositions.get(len-1).getX();
			int y = snakeBodyPositions.get(len-1).getY();

			Coordinates newTail = new Coordinates(x,y);

			snakeBodyPositions.add(newTail);

			foodList.remove(0);
			createFood();
			hasHitFood = false;

		}

		// Remove the tail
		snakeBodyPositions.remove(len-1);

		int newX = cursor_x; int newY = cursor_y;
		Coordinates newHead = new Coordinates(newX,newY);

		// Add the head
		snakeBodyPositions.add(0,newHead);

	}

	private void show(String str, int x, int y){
		term.moveCursor(x, y);
		int len = str.length();
		for (int i = 0; i < len; i++){
			term.putCharacter(str.charAt(i));
		}
	}

	private int randInt(int min, int max){
		Random rand = new Random();

		return rand.nextInt((max - min ) + 1) + min;
	}

}

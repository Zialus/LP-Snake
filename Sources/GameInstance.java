import java.util.ArrayList;
import java.util.Random;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalSize;

public class GameInstance {
	private Terminal term;
	private ArrayList<Coordinates> snakeCompleta = new ArrayList<>();
	private ArrayList<Coordinates> comida = new ArrayList<>();
	private int score = 0; // Pontuação
	private int cursor_x=10, cursor_y=10; // Posição inicial da Snake

	private boolean hasHitBorder = false;
	private boolean hasHitItself = false;
	private boolean hasHitFood = false;

	public enum Directions{UP,DOWN,LEFT,RIGHT}

	public GameInstance(){
		//Inicializar o terminal
		term = TerminalFacade.createTerminal();
		term.enterPrivateMode();
		Directions direction = GameInstance.Directions.RIGHT;
		
		//Criar a Snake
		createSnake();
		createFood();

		//Dealing with input
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
						//cursor_x -= 1;
						direction = Directions.LEFT;
					}
					break;
				case ArrowRight:
					if (direction != Directions.LEFT) {
						//cursor_x += 1;
						direction = Directions.RIGHT;
					}
					break;
				case ArrowDown:
					if (direction != Directions.UP) {
						//cursor_y += 1;
						direction = Directions.DOWN;
					}
					break;
				case ArrowUp:
					if (direction != Directions.DOWN) {
						//cursor_y -= 1;
						direction = Directions.UP;
					}
					break;
				default:
					break;
				}
			}

			//if (k == null) {
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
			}
			//}

			//Actualização de estado

			actualizaSnake();
			collisons();

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
		comida.add(foodCord);

	}

	private void createSnake(){
		Coordinates cor1 = new Coordinates(cursor_x  , cursor_y);
		Coordinates cor2 = new Coordinates(cursor_x -1,cursor_y);
		Coordinates cor3 = new Coordinates(cursor_x -2,cursor_y);
		Coordinates cor4 = new Coordinates(cursor_x -3,cursor_y);
		Coordinates cor5 = new Coordinates(cursor_x -4,cursor_y);
		snakeCompleta.add(cor1);
		snakeCompleta.add(cor2);
		snakeCompleta.add(cor3);
		snakeCompleta.add(cor4);
		snakeCompleta.add(cor5);
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

		for (Coordinates food : comida) {
			term.moveCursor(food.getX(), food.getY());
			term.putCharacter('X');
		}

	}


	private void showSnake(){

		// Head Stuff
		term.applyForegroundColor(Terminal.Color.BLUE);

		Coordinates head = snakeCompleta.get(0);
		term.moveCursor(head.getX(),head.getY());
		term.putCharacter('@');

		// Body and Tail Stuff

		term.applyForegroundColor(Terminal.Color.GREEN);
		int len = snakeCompleta.size();

		for(int i =1; i<len;i++){
			Coordinates body = snakeCompleta.get(i);
			term.moveCursor(body.getX(),body.getY());
			term.putCharacter('O');
		}

		Coordinates tail = snakeCompleta.get(len-1);
		term.moveCursor(tail.getX(),tail.getY());
		term.putCharacter( 'Q' );

	}


	private void collisons() {

		TerminalSize terminalSize = term.getTerminalSize();
		int colunas = terminalSize.getColumns();
		int linhas = terminalSize.getRows();

		Coordinates head = snakeCompleta.get(0);
		int head_X = ( head.getX());
		int head_Y = ( head.getY());

		//Collisions with borders
		for(int i = 0; i<linhas;i++){
			if ( (head_X==0 || head_X == colunas-1) && head_Y == i){
				hasHitBorder = true;
				System.out.println("Bateu numa coluna");
			}
		}

		for(int i = 0; i<colunas;i++){
			if ( (head_Y==0 || head_Y == linhas-1) && head_X == i){
				hasHitBorder = true;
				System.out.println("Bateu numa linha");
			}
		}

		//Collisions with body
		int len = snakeCompleta.size();

		for(int i=1;i<len; i++){
			int Corpo_X = snakeCompleta.get(i).getX();
			int Corpo_Y = snakeCompleta.get(i).getY();

			if ( (head_X==Corpo_X && head_Y == Corpo_Y) )
			{
				hasHitItself = true;
				System.out.println("Bateu em si propria");
			}
		}

		if(hasHitBorder || hasHitItself){
			term.clearScreen();
			showBorders();
			System.out.println("GAME OVER");
			System.out.println("-----x="+cursor_x+"-----");
			System.out.println("-----y="+cursor_y+"-----");

			show("GAME OVER",45,14);

			show("PRESS ESC to Exit or ENTER to start a NEW GAME",28,17);

			show("Score = " + score,45,20);

			//Deal with Game Over and Start the Game again
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

	private void actualizaSnake(){
		int len = snakeCompleta.size();
		Coordinates head = snakeCompleta.get(0);
		int head_X = ( head.getX());
		int head_Y = ( head.getY());

		//Collisions with food
		Coordinates food = comida.get(0);
		int food_X = food.getX();
		int food_Y = food.getY();

		if ( (head_X == food_X) && (head_Y == food_Y)) {
			hasHitFood = true;
			score += 10;
			System.out.println("Comeu a food");
		}


		//Aumentar Snake
		if (hasHitFood){

			int x = snakeCompleta.get(len-1).getX();
			int y = snakeCompleta.get(len-1).getY();

			Coordinates newTail = new Coordinates(x,y);

			snakeCompleta.add(newTail);

			comida.remove(0);
			createFood();
			hasHitFood = false;

		}

		//remover cauda
		snakeCompleta.remove(len-1);

		int newX = cursor_x; int newY = cursor_y;
		Coordinates newHead = new Coordinates(newX,newY);

		//Adicionar cabeça
		snakeCompleta.add(0,newHead);


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
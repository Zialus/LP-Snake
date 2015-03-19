import java.util.ArrayList;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalSize;

public class StartGame {
	private Terminal term;
	private ArrayList<Cell> snakeCompleta = new ArrayList<Cell>();
	private ArrayList<Cell> Comida = new ArrayList<Cell>();
	private int score = 0; // Pontuação
	private int cursor_x=10, cursor_y=10; // Posição inicial da Snake
	private boolean hitborder = false;
	private boolean hitself = false;
	private boolean hitfood = false;
	public enum Directions{UP,DOWN,LEFT,RIGHT}
	public Cell food;

	public StartGame(){	
		//Inicializar o terminal
		term = TerminalFacade.createTerminal();
		term.enterPrivateMode();
		Directions direction = StartGame.Directions.RIGHT;


		TerminalSize terminalsize = term.getTerminalSize();

		//Criar a Snake
		createSnake();
		createFood(terminalsize);

		//Dealing with input
		while(true){
			Key k = term.readInput();

			if (k != null) {
				System.out.println(k);
				switch (k.getKind()) {
				case Escape:
					term.exitPrivateMode();
					return;
				case ArrowLeft:
					if (direction != Directions.RIGHT) {
						cursor_x -= 1;
						direction = Directions.LEFT;
					}
					break;
				case ArrowRight:
					if (direction != Directions.LEFT) {
						cursor_x += 1;
						direction = Directions.RIGHT;
					}
					break;
				case ArrowDown:
					if (direction != Directions.UP) {
						cursor_y += 1;
						direction = Directions.DOWN;
					}
					break;
				case ArrowUp:
					if (direction != Directions.DOWN) {
						cursor_y -= 1;
						direction = Directions.UP;
					}
					break;
				default:
					break;
				}

			}
			if (k == null) {
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
			}

			TerminalSize terminalSize = term.getTerminalSize();

			term.clearScreen();
			term.applySGR(Terminal.SGR.ENTER_BOLD);

			showBorders();
			showFood();

			
			collisons();
			dealwithcollisions();

			if(hitfood == true){
				Comida.remove(0);
				createFood(terminalSize);
			}
			
			actualizaSnake();
			showSnake();

			hitfood = false;



			term.flush();

			try
			{
				Thread.sleep(150);
			}
			catch (InterruptedException ie)
			{
				ie.printStackTrace();
			}
		}


	}

	private void createFood(TerminalSize terminalSize){
		int colunas = terminalSize.getColumns();
		int linhas = terminalSize.getRows();

		int foodColumns = 2 + (int)(Math.random() * ((colunas - 2) ));        	   	    	
		int foodRows = 2 + (int)(Math.random() * ((linhas - 2) ));       	    	   
		Cordenadas foodCord = new Cordenadas(foodColumns,foodRows);

		Cell food = new Cell('', foodCord);
		Comida.add(food);

	}

	private void showFood() {

		Cell food = Comida.get(0);

		int food_X = food.getCord().getX();
		int food_Y = food.getCord().getY();

		term.applyForegroundColor(Terminal.Color.YELLOW);

		term.moveCursor(food_X, food_Y);
		term.putCharacter(food.getCorpo());

	}


	private void showBorders(){

		TerminalSize terminalSize = term.getTerminalSize();
		term.applyForegroundColor(Terminal.Color.RED);

		int colunas = terminalSize.getColumns();
		int linhas = terminalSize.getRows();

		for(int i = 0; i<linhas;i++){
			term.moveCursor(0,i);
			term.putCharacter('#');
			term.moveCursor(colunas,i);
			term.putCharacter('#');
		}

		for(int i = 0; i<colunas;i++){
			term.moveCursor(i,0);
			term.putCharacter('#');
			term.moveCursor(i,linhas);
			term.putCharacter('#');
		}

	}

	private void showSnake(){

		//Cabeça
		Cell head = snakeCompleta.get(0);
		term.applyForegroundColor(Terminal.Color.BLUE);
		term.moveCursor(cursor_x, cursor_y);
		term.putCharacter(head.getCorpo());

		//Resto do corpo
		term.applyForegroundColor(Terminal.Color.GREEN);
		int corX, corY;
		int len = snakeCompleta.size();

		for(int i =1; i<len;i++){
			corX = ( snakeCompleta.get(i)).getCord().getX();
			corY = ( snakeCompleta.get(i)).getCord().getY();
			Cell tail = snakeCompleta.get(i);
			term.moveCursor(corX,corY);
			term.putCharacter( tail.getCorpo() );
		}

	}

	private void createSnake(){
		Cordenadas cor1 = new Cordenadas(cursor_x,cursor_y);
		Cordenadas cor2 = new Cordenadas(cursor_x -1,cursor_y);
		Cordenadas cor3 = new Cordenadas(cursor_x -2,cursor_y);
		Cordenadas cor4 = new Cordenadas(cursor_x -3,cursor_y);
		Cordenadas cor5 = new Cordenadas(cursor_x -4,cursor_y);

		Cell cel1 = new Cell('@', cor1);
		Cell cel2 = new Cell('0', cor2);
		Cell cel3 = new Cell('0', cor3);
		Cell cel4 = new Cell('0', cor4);
		Cell cel5 = new Cell('Q', cor5);

		snakeCompleta.add(cel1);
		snakeCompleta.add(cel2);
		snakeCompleta.add(cel3);
		snakeCompleta.add(cel4);
		snakeCompleta.add(cel5);
	}

	private void actualizaSnake(){
		int x,y;
		int len = snakeCompleta.size();

		int newX = cursor_x;
		int	newY = cursor_y;

		if (hitfood == true){
			
			x = snakeCompleta.get(len-1).getCord().getX();
			y = snakeCompleta.get(len-1).getCord().getY();

			Cordenadas coord = new Cordenadas(x,y);
			Cell nova_cauda = new Cell('0', coord);

			snakeCompleta.add(nova_cauda);


		}

		//
		for(int i=len-1;i>0; i--){
			x = snakeCompleta.get(i-1).getCord().getX();
			y = snakeCompleta.get(i-1).getCord().getY();
			snakeCompleta.get(i).getCord().setX(x);
			snakeCompleta.get(i).getCord().setY(y);
		}

		snakeCompleta.get(0).getCord().setX(newX);
		snakeCompleta.get(0).getCord().setY(newY);




	}

	private void collisons() {
		
		TerminalSize terminalSize = term.getTerminalSize();
		int colunas = terminalSize.getColumns();
		int linhas = terminalSize.getRows();

		Cell head = snakeCompleta.get(0);
		int head_X = ( head.getCord().getX());
		int head_Y = ( head.getCord().getY());

		//Collisions with borders
		for(int i = 0; i<linhas;i++){
			if ( (head_X==0 || head_X == colunas) && head_Y == i){
				hitborder = true;
				System.out.println("Bateu numa coluna");
			}
		}

		for(int i = 0; i<colunas;i++){
			if ( (head_Y==0 || head_Y == linhas) && head_X == i){
				hitborder = true;
				System.out.println("Bateu numa linha");
			}
		}

		//Collisions with body
		int len = snakeCompleta.size();

		for(int i=1;i<len; i++){
			int Corpo_X = snakeCompleta.get(i).getCord().getX();
			int Corpo_Y = snakeCompleta.get(i).getCord().getY();

			if ( (head_X==Corpo_X && head_Y == Corpo_Y) )
			{
				hitself = true;
				System.out.println("Bateu em si propria");
			}
		}

		//Collisions with food

		Cell food = Comida.get(0);

		int food_X = food.getCord().getX();
		int food_Y = food.getCord().getY();

		if ( (head_X == food_X) && (head_Y == food_Y)) {
			hitfood = true;
			score += 10;
			System.out.println("Comeu a food");
		}



	}


	private void dealwithcollisions(){

		if(hitborder == true || hitself == true){

			System.out.println("GAME OVER");
			System.out.println("-----x="+cursor_x+"-----");
			System.out.println("-----y="+cursor_y+"-----");

			show("GAME OVER",45,15);
			
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
						new StartGame();
					}
				}

			}

		}

	}

	private void show(String str, int x, int y){
		term.moveCursor(x, y);
		int len = str.length();
		for (int i = 0; i < len; i++){
			term.putCharacter(str.charAt(i));
		}
	}
}
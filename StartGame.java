import java.util.ArrayList;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalSize;


public class StartGame {
	private Terminal term;
	private ArrayList<Cell> snakeCompleta = new ArrayList<Cell>();
	private int cursor_x=10, cursor_y=10;
	
	public enum Directions{UP,DOWN,LEFT,RIGHT}
		
	public StartGame(){
		term = TerminalFacade.createTerminal();
		term.enterPrivateMode();
		Directions direction = StartGame.Directions.RIGHT;
		
		Cordenadas cor1 = new Cordenadas(cursor_x,cursor_y);
		Cordenadas cor2 = new Cordenadas(cursor_x -1,cursor_y);
		Cordenadas cor3 = new Cordenadas(cursor_x -2,cursor_y);
		Cordenadas cor4 = new Cordenadas(cursor_x -3,cursor_y);
		
		Cell cel1 = new Cell('@', cor1);
		Cell cel2 = new Cell('0', cor2);
		Cell cel3 = new Cell('0', cor3);
		Cell cel4 = new Cell('Q', cor4);
		
		snakeCompleta.add(cel1);
		snakeCompleta.add(cel2);
		snakeCompleta.add(cel3);
		snakeCompleta.add(cel4);
		
		while(true){
			Key k = term.readInput();
			if (k != null) {

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
			term.clearScreen();
			
			term.applySGR(Terminal.SGR.ENTER_BOLD);
	        term.applyForegroundColor(Terminal.Color.BLUE);
	        
	        showSnake();
	        term.flush();

	           try
	           {
	               Thread.sleep(300);
	           }
	           catch (InterruptedException ie)
	           {
	               ie.printStackTrace();
	           }
		}
		
		
	}
	
	private void showSnake(){
		term.moveCursor(cursor_x, cursor_y);
		int corX, corY;
		
		int len = snakeCompleta.size();
		
		
		
		//actualizaSnake(cursor_x,cursor_y);
		Cell cel = (Cell) snakeCompleta.get(0);
		
		
		term.putCharacter(cel.getCorpo());
		TerminalSize terminalSize = term.getTerminalSize();
		
		
		int colunas = terminalSize.getColumns();
		int linhas = terminalSize.getRows();

		for(int i = 0; i<terminalSize.getRows();i++){
			
			term.moveCursor(0,i);
			term.putCharacter('#');
			//bug a ser corrigido
			term.moveCursor(linhas+60,i);
			term.putCharacter('#');

		}
		
		for(int i = 0; i<terminalSize.getColumns();i++){
		
			term.moveCursor(i,0);
			term.putCharacter('#');
			term.moveCursor(i,colunas);
			term.putCharacter('#');

		}
		
		
		for(int i =1; i<len;i++){
			corX =((Cell) snakeCompleta.get(i)).getCord().getX();
			corY =((Cell) snakeCompleta.get(i)).getCord().getY();
			
			term.moveCursor(corX,corY);
			term.putCharacter(((Cell) snakeCompleta.get(i)).getCorpo());
			
		}
		actualizaSnake(cursor_x,cursor_y);
	}
	
	
	private void actualizaSnake(int newX,int newY){
		int len = snakeCompleta.size();
		int x,y;
		
		for(int i=len-1;i>0; i--){
			x= snakeCompleta.get(i-1).getCord().getX();
			System.out.println("-----x="+x);
			y= snakeCompleta.get(i-1).getCord().getY();
			System.out.println("-----y"+y);
			snakeCompleta.get(i).getCord().setX(x);
			snakeCompleta.get(i).getCord().setY(y);
		}
		
		snakeCompleta.get(0).getCord().setX(newX);
		snakeCompleta.get(0).getCord().setY(newY);
		
	}
}




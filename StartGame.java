import java.util.ArrayList;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.TerminalSize;


public class StartGame {
	private Terminal term;
	private ArrayList snakeCompleta = new ArrayList<Cell>();
	private int cursor_x=10, cursor_y=10;
	
	public StartGame(){
		term = TerminalFacade.createTerminal();
		term.enterPrivateMode();
		
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
			    	   cursor_x -= 1;
					   break;
				   case ArrowRight:
					   cursor_x += 1;
					   break;
				   case ArrowDown: 
					   cursor_y += 1;
					   break;
				   case ArrowUp:
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
	               Thread.sleep(10);
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
		
		Cell cel = (Cell) snakeCompleta.get(0);
		
		
		term.putCharacter(cel.getCorpo());
		TerminalSize terminalSize = term.getTerminalSize();
		
		for(int i =1; i<len;i++){
			corX =((Cell) snakeCompleta.get(i)).getCord().getX();
			corY =((Cell) snakeCompleta.get(i)).getCord().getY();
			
			term.moveCursor(corX,corY);
			term.putCharacter(((Cell) snakeCompleta.get(i)).getCorpo());
			
		}
	}
}

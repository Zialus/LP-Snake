


public class Cell {
	private char corpo;
	private Cordenadas cord;
	
	public Cell(){}

	public Cell(char corpo, Cordenadas cord){
		this.corpo = corpo;
		this.cord = cord;
	}
	
	public char getCorpo() {
		return corpo;
	}

	public void setCorpo(char corpo) {
		this.corpo = corpo;
	}

	public Cordenadas getCord() {
		return cord;
	}

	public void setCord(Cordenadas cord) {
		this.cord = cord;
	}
	
	
}

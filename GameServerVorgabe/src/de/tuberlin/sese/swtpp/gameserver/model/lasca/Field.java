package de.tuberlin.sese.swtpp.gameserver.model.lasca;

public class Field {
	
	private int row;
	private int column;
	private boolean ownedByWhite;
	private boolean ownedByBlack;
	
	public Field(int row, int column) {
		this.setRow(row);
		this.setColumn(column);
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public boolean isOwnedByWhite() {
		return ownedByWhite;
	}

	public void setOwnedByWhite(boolean ownedByWhite) {
		this.ownedByWhite = ownedByWhite;
		this.ownedByBlack = !ownedByWhite;
	}

	public boolean isOwnedByBlack() {
		return ownedByBlack;
	}

	public void setOwnedByBlack(boolean ownedByBlack) {
		this.ownedByBlack = ownedByBlack;
		this.ownedByWhite = !ownedByWhite;
	}
}

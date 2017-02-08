package de.tuberlin.sese.swtpp.gameserver.model.lasca;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {

	/* x - forbidden field
	 * e - empty field
	 * b - black on this field
	 * w - white on this field
	 */
	//		          	      A   B   C   D   E   F   G
	private String[] row7 = {"b","x","b","x","b","x","b"};
	private String[] row6 = {"x","b","x","b","x","b","x"};
	private String[] row5 = {"b","x","b","x","b","x","b"};
	private String[] row4 = {"x","" ,"x","" ,"x","" ,"x"};
	private String[] row3 = {"w","x","w","x","w","x","w"};
	private String[] row2 = {"x","w","x","w","x","w","x"};
	private String[] row1 = {"w","x","w","x","w","x","w"};
	
	private String[][] boardState = {row7, row6, row5, row4, row3, row2, row1};
	
	private int startRow;
	private int startColumn;
	private int targetRow;
	private int targetColumn;
	
	private boolean isJump;
	private boolean itsWhiteTurn;
	
	public Board(String state) {
		setBoard(state);
	}
	
	public Board() {
		// setBoard("b,b,b,b/b,b,b/b,b,b,b/,,/w,w,w,w/w,w,w/w,w,w,w");
	}
	
	public boolean executeMove() {
		if (isJump) {
			
			System.out.println("set targetRow " + targetRow + ", targetColumn " + targetColumn + " to " + boardState[startRow][startColumn] + boardState[(targetRow+startRow)/2][(targetColumn+startColumn)/2].charAt(0));
			boardState[targetRow][targetColumn] = boardState[startRow][startColumn] + boardState[(targetRow+startRow)/2][(targetColumn+startColumn)/2].charAt(0);
			
			System.out.println("set startRow " + startRow + ", startColumn " + startColumn + " from " + boardState[startRow][startColumn] + " to \"\"");
			boardState[startRow][startColumn] = "";
	
			System.out.println("field jumped over; row " + ((targetRow+startRow)/2) + ", column " + ((targetColumn+startColumn)/2));			
			if (boardState[(targetRow+startRow)/2][(targetColumn+startColumn)/2].length() >= 1) {
				boardState[(targetRow+startRow)/2][(targetColumn+startColumn)/2] = boardState[(targetRow+startRow)/2][(targetColumn+startColumn)/2].substring(1);			
			} else {
				boardState[(targetRow+startRow)/2][(targetColumn+startColumn)/2] = "";
			}
			System.out.println("set to " + boardState[(targetRow+startRow)/2][(targetColumn+startColumn)/2]);
			
			return true;
		}
					
		return false;
	}
	
	private boolean isOfficer(int row, int column) {
		return boardState[row][column].charAt(0) == 'W' || boardState[row][column].charAt(0) == 'B' ? true : false;
	}
	
/*	public String remapCoordinatesToMoveString(Field startField, Field targetField) {
		String move = "";
		int c = startField.getColumn();
		char charColumn = c == 0? 'a' : c == 1? 'b' : c == 2? 'c' : c == 3? 'd' : c == 4? 'e' : c == 5? 'f' : 'g';
		move = move + charColumn + (7-startField.getRow()) + "-";
		c = targetField.getColumn();
		charColumn = c == 0? 'a' : c == 1? 'b' : c == 2? 'c' : c == 3? 'd' : c == 4? 'e' : c == 5? 'f' : 'g';
		move = move + charColumn + (7-targetField.getRow());
		return move;
	} */
	
	public String remapCoordinatesToMoveString(int startRow, int startColumn, int targetRow, int targetColumn) {
		System.out.println("set startRow " + startRow + ", startColumn " + startColumn + ", targetRow " + targetRow + ", targetCloumn " + targetColumn);
		String move = "";
		int c = startColumn;
		char charColumn = c == 0? 'a' : c == 1? 'b' : c == 2? 'c' : c == 3? 'd' : c == 4? 'e' : c == 5? 'f' : 'g';
		System.out.println("set startColumn " + startColumn + " to " + charColumn + "and set startRow " + startRow + " to " + (7-startRow));
		move = move + charColumn + (7-startRow) + "-";
		c = targetColumn;
		charColumn = c == 0? 'a' : c == 1? 'b' : c == 2? 'c' : c == 3? 'd' : c == 4? 'e' : c == 5? 'f' : 'g';
		System.out.println("set targetColumn " + targetColumn + " to " + charColumn + " and set targetRow " + targetRow + " to " + (7-targetRow));
		move = move + charColumn + (7-targetRow);
		return move;
	}
	
	private ArrayList<String> getAllJumpsWhitePlayer() {
		ArrayList<Field> fieldsOwnedByWhite = getAllFieldsOwnedByPlayer(true);
		System.out.println("white ownes " + fieldsOwnedByWhite.size() + " fields");
		ArrayList<String> jumpMoves = new ArrayList<String>();
		int row, column;
		for (Field field:fieldsOwnedByWhite) {
			row = field.getRow();
			column = field.getColumn();
			// left
			if (row-2 >= 0 && column-2 >= 0 && fieldOwnedByPlayer(row-1, column-1, false) && isFieldEmpty(row-2, column-2)) {
				jumpMoves.add(remapCoordinatesToMoveString(row, column, row-2, column-2));
			} // right
			if (row-2 >= 0 && column+2 < 7 && fieldOwnedByPlayer(row-1, column+1, false) && isFieldEmpty(row-2, column+2)) {
				jumpMoves.add(remapCoordinatesToMoveString(row, column, row-2, column+2));
			}
			if (isOfficer(row, column)) {
				if (row+2 < 7 && column-2 >= 0 && fieldOwnedByPlayer(row+1, column-1, false) && isFieldEmpty(row+2, column-2)) {
					jumpMoves.add(remapCoordinatesToMoveString(row, column, row+2, column-2));
				} // right
				if (row+2 < 7 && column+2 < 7 && fieldOwnedByPlayer(row+1, column+1, false) && isFieldEmpty(row+2, column+2)) {
					jumpMoves.add(remapCoordinatesToMoveString(row, column, row+2, column+2));
				}
			}
		}
		System.out.println("white can do " + jumpMoves.size() + " jumps");
		return jumpMoves;	
	}
	
	private ArrayList<String> getAllJumpsBlackPlayer() {
		ArrayList<Field> fieldsOwnedByBlack = getAllFieldsOwnedByPlayer(false);
		System.out.println("black ownes " + fieldsOwnedByBlack.size() + " fields");
		ArrayList<String> jumpMoves = new ArrayList<String>();
		int row, column;
		for (Field field:fieldsOwnedByBlack) {
			row = field.getRow();
			column = field.getColumn();
			// left
			if (row+2 < 7 && column-2 >= 0 && fieldOwnedByPlayer(row+1, column-1, true) && isFieldEmpty(row+2, column-2)) {
				jumpMoves.add(remapCoordinatesToMoveString(row, column, row+2, column-2));
				System.out.println("set move from " + row + ", " + column + " to " + (row+2) + ", " + (column-2));
			} // right
			if (row+2 < 7 && column+2 < 7 && fieldOwnedByPlayer(row+1, column+1, true) && isFieldEmpty(row+2, column+2)) {
				jumpMoves.add(remapCoordinatesToMoveString(row, column, row+2, column+2));
				System.out.println("set move from " + row + ", " + column + " to " + (row+2) + ", " + (column+2));
			}
			if (isOfficer(row, column)) {
				if (row-2 >= 0 && column-2 >= 0 && fieldOwnedByPlayer(row-1, column-1, true) && isFieldEmpty(row-2, column-2)) {
					jumpMoves.add(remapCoordinatesToMoveString(row, column, row-2, column-2));
				} // right
				if (row-2 >= 0 && column+2 < 7 && fieldOwnedByPlayer(row-1, column+1, true) && isFieldEmpty(row-2, column+2)) {
					jumpMoves.add(remapCoordinatesToMoveString(row, column, row-2, column+2));
				}
			}
		}
		System.out.println("black can do " + jumpMoves.size() + " jumps");
		return jumpMoves;	
	}
	
	private ArrayList<String> getAllJumps() {
		if (itsWhiteTurn) {
			return getAllJumpsWhitePlayer();	
		} else {
			return getAllJumpsBlackPlayer();	
		}
	}
	
	private void printMoves(ArrayList<String> moves) {
		System.out.println("start move list");
		for (String move:moves) {
			System.out.println(move);
		}
		System.out.println("end move list");
	}
	
	public boolean isMoveJump() {
		ArrayList<String> jumpMoves = getAllJumps();
		printMoves(jumpMoves);
		isJump = jumpMoves.contains(remapCoordinatesToMoveString(startRow, startColumn, targetRow, targetColumn));
		return isJump;
	}

	public boolean startOwnedByPlayer() {
		System.out.println("start ownes by player " + fieldOwnedByPlayer(startRow, startColumn, itsWhiteTurn));
		return fieldOwnedByPlayer(startRow, startColumn, itsWhiteTurn);
	}
	
	private boolean fieldOwnedByPlayer(int row, int column, boolean isWhite) {
		if (boardState[row][column].length() == 0) return false;
		char c = boardState[row][column].charAt(0);
		if (isWhite) {
			return c == 'w' || c == 'W' ? true : false;
		} else {
			return c == 'b' || c == 'B' ? true : false;
		}
	}
	
	private void printField(Field field) {
		System.out.println(field.getRow() + ", " + field.getColumn() + " " + boardState[field.getRow()][field.getColumn()]);
	}
	
	private void printFields(ArrayList<Field> fields) {
		System.out.println("Fields owned by player");
		for (Field field:fields) {
			printField(field);
		}
		System.out.println("end Fields owned by player");
	}
	
	private ArrayList<Field> getAllFieldsOwnedByPlayer(boolean isWhite) {
		ArrayList<Field> fields = new ArrayList<Field>();
		Field field;
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				if (isWhite && fieldOwnedByPlayer(i, j, isWhite)) {
					field = new Field(i,j);
					fields.add(field);
				}
				if (!isWhite && fieldOwnedByPlayer(i, j, isWhite)) {
					field = new Field(i,j);
					fields.add(field);
				}
				
			}
		}
		printFields(fields);
		return fields;
	}
	
	public boolean isTargetEmpty() {
		return isFieldEmpty(targetRow, targetColumn);
	}
	
	private boolean isFieldEmpty(int row, int column) {
		return boardState[row][column].compareTo("") == 0 ? true : false;
	}
	
	public void setRequiredMove(String move, boolean isWhite) {
		startRow = mapCooradinatesToBoard(move.substring(0, 2))[0];
		startColumn = mapCooradinatesToBoard(move.substring(0, 2))[1];
		targetRow = mapCooradinatesToBoard(move.substring(3))[0];
		targetColumn = mapCooradinatesToBoard(move.substring(3))[1];
		this.itsWhiteTurn = isWhite;
		isJump = false; // because we don't know so far
	}
	
	private int[] mapCooradinatesToBoard(String field) {
		int coordinates[] = new int[2];
		char c = field.charAt(0);
		coordinates[1] = c == 'a'? 0 : c == 'b'? 1 : c == 'c'? 2 : c == 'd'? 3 : c == 'e'? 4 : c == 'f'? 5 : 6;
		c = field.charAt(1);
		coordinates[0] = c == '1'? 6 : c == '2'? 5 : c == '3'? 4 : c == '4'? 3 : c == '5'? 2 : c == '6'? 1 : 0;
		return coordinates; 
		
	}
	
	
	/*******************************
	 * Board State
	 ******************************/
	
	public void setBoard(String state) {
		System.out.println(state);
		String[] newBoardStateRows = state.split("/");
		
		for (int i = 0; i < 7; i++) {
			System.out.println(newBoardStateRows[i]);
			List<String> newRow = new ArrayList<String>(Arrays.asList(newBoardStateRows[i].split(",")));
			for (int j = 0; j < 7; j++) {
				if (newRow.size() == 0) continue;
				System.out.println("left length " + newRow.size());
				System.out.println("can I set " + newRow.get(0).toString());
				if (i % 2 == 0 && j % 2 == 0) {    // rows with 4 fields (7, 5, 3, 1) && field A, C, E, G
					System.out.println("set " + newRow.get(0).toString() + " in " + i + "," + j);
					boardState[i][j] = newRow.get(0).toString();
					newRow.remove(0);
				}
				if (i % 2 != 0 && j % 2 != 0) {   // rows with 3 fields (6, 4, 2) && field B, D, F
					System.out.println("set " + newRow.get(0).toString() + " in " + i + "," + j);
					boardState[i][j] = newRow.get(0).toString();
					newRow.remove(0);
				}
			}
		}		
	}
	
	public String getBoard() {
//		System.out.println("print board");
		String boardStateString = "";
		for (int i = 0; i < 7 ; i++) {
			for (int j = 0; j < 7; j++) {
				if (boardState[i][j].compareTo("x") != 0) {
					boardStateString = boardStateString + boardState[i][j];
					boardStateString = boardStateString + ",";
				}
			}
			if (boardStateString.charAt(boardStateString.length()-1)==',') {
				boardStateString = boardStateString.substring(0, boardStateString.length()-1);	
			}
			boardStateString = boardStateString + "/";	
		}
		boardStateString = boardStateString.substring(0, boardStateString.length()-1);
		return boardStateString;
	}
}

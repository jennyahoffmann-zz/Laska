package de.tuberlin.sese.swtpp.gameserver.model.lasca;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {

	/* x - forbidden field
	 */
	//		          	      A   B   C   D   E   F   G
	private String[] row7 = {"" ,"x","" ,"x","" ,"x","" };
	private String[] row6 = {"x","" ,"x","" ,"x","" ,"x"};
	private String[] row5 = {"" ,"x","" ,"x","" ,"x","" };
	private String[] row4 = {"x","" ,"x","" ,"x","" ,"x"};
	private String[] row3 = {"" ,"x","" ,"x","" ,"x","" };
	private String[] row2 = {"x","" ,"x","" ,"x","" ,"x"};
	private String[] row1 = {"" ,"x","" ,"x","" ,"x","" };
	
	private String[][] boardState = {row7, row6, row5, row4, row3, row2, row1};
	
	private int startRow;
	private int startColumn;
	private int targetRow;
	private int targetColumn;
	
	private boolean isJump;
	private boolean itsWhiteTurn;
	
	public Board() {
	}
	
	public boolean tryMove(String moveString, boolean isWhite) {
		setRequiredMove(moveString, isWhite);
		if (isTargetEmpty() && isStartOwnedByPlayer() && isMoveValidJump()) return executeMove();
		if (isTargetEmpty() && isStartOwnedByPlayer() && getAllJumps().size() == 0 && isValidNormalMove()) return executeMove();
		return false;
	}

	public void setRequiredMove(String move, boolean isWhite) {
		startRow = mapCooradinatesToBoard(move.substring(0, 2))[0];
		startColumn = mapCooradinatesToBoard(move.substring(0, 2))[1];
		targetRow = mapCooradinatesToBoard(move.substring(3))[0];
		targetColumn = mapCooradinatesToBoard(move.substring(3))[1];
		itsWhiteTurn = isWhite;
		isJump = false; // because we don't know so far
	}
	
	public boolean executeMove() {
		if (isJump) {
			boardState[targetRow][targetColumn] = boardState[startRow][startColumn] + boardState[(targetRow+startRow)/2][(targetColumn+startColumn)/2].charAt(0);
			boardState[startRow][startColumn] = "";
			boardState[(targetRow+startRow)/2][(targetColumn+startColumn)/2] = boardState[(targetRow+startRow)/2][(targetColumn+startColumn)/2].substring(1);			
			mayPromoteToOfficer();
		} else {
				boardState[targetRow][targetColumn] = boardState[startRow][startColumn];
				boardState[startRow][startColumn] = "";
				mayPromoteToOfficer();
		}
		return true;		
	}
	
	private void mayPromoteToOfficer() {
		if (itsWhiteTurn && targetRow == 0) boardState[targetRow][targetColumn] = "W" + boardState[targetRow][targetColumn].substring(1);
		if (!itsWhiteTurn && targetRow == 6) boardState[targetRow][targetColumn] = "B" + boardState[targetRow][targetColumn].substring(1);
	}
	
	public boolean isValidNormalMove() {
		if (itsWhiteTurn) {
			if (targetRow == startRow-1 && (targetColumn ==  startColumn+1 || targetColumn ==  startColumn-1 )) return true;
			if(isOfficer(startRow, startColumn) && targetRow == startRow+1 && (targetColumn ==  startColumn+1 || targetColumn ==  startColumn-1)) return true;
		} else {
			if (targetRow == startRow+1 && (targetColumn ==  startColumn+1 || targetColumn ==  startColumn-1 )) return true;
			if(isOfficer(startRow, startColumn) && targetRow == startRow-1 && (targetColumn ==  startColumn+1 || targetColumn ==  startColumn-1)) return true;
		}
		return false;
	}
	
	public boolean isGameFinished() {
		return getAllJumps(!itsWhiteTurn).size() == 0 && !canDoNormalMove(!itsWhiteTurn);
	}
	
	
	/*******************************
	 * Field Analysis
	 ******************************/
	
	public boolean isStartOwnedByPlayer() {
		return fieldOwnedByPlayer(startRow, startColumn, itsWhiteTurn);
	}
	
	public boolean isTargetEmpty() {
		return isFieldEmpty(targetRow, targetColumn);
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
	
	private boolean isFieldEmpty(int row, int column) {
		return boardState[row][column].compareTo("") == 0 ? true : false;
	}
	
	private ArrayList<Field> getAllFieldsOwnedByPlayer(boolean isWhite) {
		ArrayList<Field> fields = new ArrayList<Field>();
		for (int i = 0; i < 7; i++) {
			for (int j = 0; j < 7; j++) {
				if (fieldOwnedByPlayer(i, j, isWhite)) fields.add(new Field(i,j));
			}
		}
		return fields;
	}
	
	private boolean isOfficer(int row, int column) {
		return boardState[row][column].charAt(0) == 'W' || boardState[row][column].charAt(0) == 'B' ? true : false;
	}
	
	
	/*******************************
	 * get Normal Moves
	 ******************************/
	
	private boolean canDoNormalMove(boolean isWhite) {
		int row, column;
		for (Field field:getAllFieldsOwnedByPlayer(isWhite)) {
			row = field.getRow();
			column = field.getColumn();
			if (isWhite) {
				if (row-1 >= 0 && ((column-1 >= 0 && isFieldEmpty(row-1, column-1)) || (column+1 < 7 && isFieldEmpty(row-1, column+1)))) return true;
				if (isOfficer(row, column) && row+1 < 7 && ((column-1 >= 0 && isFieldEmpty(row+1, column-1)) || (column+1 < 7 && isFieldEmpty(row+1, column+1)))) return true;
			} else {
				if (row+1 < 7 && ((column-1 >= 0 && isFieldEmpty(row+1, column-1)) || (column+1 < 7 && isFieldEmpty(row+1, column+1)))) return true;
				if (isOfficer(row, column) && row-1 >= 0 && ((column-1 >= 0 && isFieldEmpty(row-1, column-1)) || (column+1 < 7 && isFieldEmpty(row-1, column+1)))) return true;
			}
		}
		return false;
	}
	
	
	/*******************************
	 * get Jump Moves
	 ******************************/
	
	public boolean isMoveValidJump() {
		ArrayList<String> jumpMoves = getAllJumps();
		return isJump = jumpMoves.contains(remapCoordinatesToMoveString(startRow, startColumn, targetRow, targetColumn));
	}
	
	private ArrayList<String> getAllJumps(boolean isWhite) {
		return isWhite ? getAllJumpsWhitePlayer() : getAllJumpsBlackPlayer();	
	}
	
	private ArrayList<String> getAllJumps() {
		return itsWhiteTurn ? getAllJumpsWhitePlayer() : getAllJumpsBlackPlayer();
	}
	
	private ArrayList<String> getAllJumpsBlackPlayer() {
		ArrayList<String> jumpMoves = new ArrayList<String>();
		int row, column;
		for (Field field:getAllFieldsOwnedByPlayer(false)) {
			row = field.getRow();
			column = field.getColumn();
			// left
			if (row+2 < 7 && column-2 >= 0 && fieldOwnedByPlayer(row+1, column-1, true) && isFieldEmpty(row+2, column-2)) {
				jumpMoves.add(remapCoordinatesToMoveString(row, column, row+2, column-2));
			} // right
			if (row+2 < 7 && column+2 < 7 && fieldOwnedByPlayer(row+1, column+1, true) && isFieldEmpty(row+2, column+2)) {
				jumpMoves.add(remapCoordinatesToMoveString(row, column, row+2, column+2));
			}
			if (isOfficer(row, column)) {
				// left
				if (row-2 >= 0 && column-2 >= 0 && fieldOwnedByPlayer(row-1, column-1, true) && isFieldEmpty(row-2, column-2)) {
					jumpMoves.add(remapCoordinatesToMoveString(row, column, row-2, column-2));
				} // right
				if (row-2 >= 0 && column+2 < 7 && fieldOwnedByPlayer(row-1, column+1, true) && isFieldEmpty(row-2, column+2)) {
					jumpMoves.add(remapCoordinatesToMoveString(row, column, row-2, column+2));
				}
			}
		}
		return jumpMoves;	
	}
	
	private ArrayList<String> getAllJumpsWhitePlayer() {
		ArrayList<String> jumpMoves = new ArrayList<String>();
		int row, column;
		for (Field field:getAllFieldsOwnedByPlayer(true)) {
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
				// left
				if (row+2 < 7 && column-2 >= 0 && fieldOwnedByPlayer(row+1, column-1, false) && isFieldEmpty(row+2, column-2)) {
					jumpMoves.add(remapCoordinatesToMoveString(row, column, row+2, column-2));
				} // right
				if (row+2 < 7 && column+2 < 7 && fieldOwnedByPlayer(row+1, column+1, false) && isFieldEmpty(row+2, column+2)) {
					jumpMoves.add(remapCoordinatesToMoveString(row, column, row+2, column+2));
				}
			}
		}
		return jumpMoves;	
	}
	
	public boolean canPlayerDoAnotherJump() {
		return isJump && getAllJumps().size() > 0;
	}
	
	
	/*******************************
	 * Coordinate Mapping
	 ******************************/

	public String remapCoordinatesToMoveString(int startRow, int startColumn, int targetRow, int targetColumn) {
		String move = "";
		int c = startColumn;
		char charColumn = c == 0? 'a' : c == 1? 'b' : c == 2? 'c' : c == 3? 'd' : c == 4? 'e' : c == 5? 'f' : 'g';
		move = move + charColumn + (7-startRow) + "-";
		c = targetColumn;
		charColumn = c == 0? 'a' : c == 1? 'b' : c == 2? 'c' : c == 3? 'd' : c == 4? 'e' : c == 5? 'f' : 'g';
		move = move + charColumn + (7-targetRow);
		return move;
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
		String[] newBoardStateRows = state.split("/");
		for (int i = 0; i < 7; i++) {
			List<String> newRow = new ArrayList<String>(Arrays.asList(newBoardStateRows[i].split(",")));
			for (int j = 0; j < 7; j++) {
				if (newRow.size() == 0) continue;
				if (i % 2 == 0 && j % 2 == 0) boardState[i][j] = newRow.remove(0).toString();   // rows with 4 fields (7, 5, 3, 1) && field A, C, E, G
				if (i % 2 != 0 && j % 2 != 0) boardState[i][j] = newRow.remove(0).toString();   // rows with 3 fields (6, 4, 2) && field B, D, F
			}
		}		
	}
	
	public String getBoard() {
		String boardStateString = "";
		for (int i = 0; i < 7 ; i++) {
			for (int j = 0; j < 7; j++) {
				if (boardState[i][j].compareTo("x") != 0) boardStateString = boardStateString + boardState[i][j] + ",";
			}
			boardStateString = boardStateString.substring(0, boardStateString.length()-1) + "/";		
		}
		return boardStateString.substring(0, boardStateString.length()-1);
	}
}

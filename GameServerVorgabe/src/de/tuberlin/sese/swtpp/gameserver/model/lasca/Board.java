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
	
	public Board(String state) {
		setBoard(state);
	}
	
	public Board() {
		// setBoard("b,b,b,b/b,b,b/b,b,b,b/,,/w,w,w,w/w,w,w/w,w,w,w");
	}
	
	public void setBoard(String state) {
		String[] newBoardStateRows = state.split("/");
		
		for (int i = 0; i < 7; i++) {
			List newRow = new ArrayList<String>(Arrays.asList(newBoardStateRows[i].split(",")));
			for (int j = 0; j < 7; j++) {
				if (i % 2 == 0 && j % 2 == 0) {    // rows with 4 fields (7, 5, 3, 1) && field A, C, E, G
					boardState[i][j] = newRow.get(0).toString();
					newRow.remove(0);
				}
				if (i % 2 != 0 && j % 2 != 0) {   // rows with 3 fields (6, 4, 2) && field B, D, F
					boardState[i][j] = newRow.get(0).toString();
					newRow.remove(0);
				}
			}
		}		
	}
	
	public String getBoard() {
		System.out.println("print board");
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

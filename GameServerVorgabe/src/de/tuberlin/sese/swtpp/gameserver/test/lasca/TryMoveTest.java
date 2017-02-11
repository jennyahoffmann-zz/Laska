package de.tuberlin.sese.swtpp.gameserver.test.lasca;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.tuberlin.sese.swtpp.gameserver.control.GameController;
import de.tuberlin.sese.swtpp.gameserver.model.Player;
import de.tuberlin.sese.swtpp.gameserver.model.User;
import de.tuberlin.sese.swtpp.gameserver.model.lasca.LascaGame;

public class TryMoveTest {

	User user1 = new User("Alice", "alice");
	User user2 = new User("Bob", "bob");
	
	Player whitePlayer = null;
	Player blackPlayer = null;
	LascaGame game = null;
	GameController controller;
	
	@Before
	public void setUp() throws Exception {
		controller = GameController.getInstance();
		controller.clear();
		
		int gameID = controller.startGame(user1, "");
		
		game = (LascaGame) controller.getGame(gameID);
		whitePlayer = game.getPlayer(user1);

	}
	
	
	public void startGame(String initialBoard, boolean whiteNext) {
		controller.joinGame(user2);		
		blackPlayer = game.getPlayer(user2);
	
		game.setState(initialBoard);
		game.setNextPlayer(whiteNext? whitePlayer:blackPlayer);
	}
	
	public void assertMove(String move, boolean white, boolean expectedResult) {
		if (white)
			assertEquals(game.tryMove(move, whitePlayer), expectedResult);
		else 
			assertEquals(game.tryMove(move, blackPlayer), expectedResult);
	}
	
	public void assertGameState(String expectedBoard, boolean whiteNext, boolean finished, boolean whiteWon) {
		assertEquals(game.getState(), expectedBoard);
		assertEquals(game.isWhiteNext(), whiteNext);

		assertEquals(game.isFinished(), finished);
		if (game.isFinished()) {
			assertEquals(whitePlayer.isWinner(), whiteWon);
			assertEquals(blackPlayer.isWinner(), !whiteWon);
		}
	}
	
	/*******************************************
	 * !!!!!!!!! To be implemented !!!!!!!!!!!!
	 *******************************************/
	
	@Test
	public void exampleTest() {
		startGame("b,b,b,b/b,b,b/b,b,b,b/,,/w,w,w,w/w,w,w/w,w,w,w", true);
		assertMove("a3-b4", true, true);
		assertGameState("b,b,b,b/b,b,b/b,b,b,b/w,,/,w,w,w/w,w,w/w,w,w,w", false, false, false);
	} 

	//TODO: implement test cases of same kind as example here
	
	@Test
	public void setState() {
		startGame("b,b,,/,,b/b,w,,b/,,/,,B,/,W,w/w,,,", false);
		assertGameState("b,b,,/,,b/b,w,,b/,,/,,B,/,W,w/w,,,", false, false, false);
	}
	
	/*******************************
	 * Wrong player moving
	 ******************************/
	
	@Test
	public void whiteTriesToMoveBlacksTurn() {
		startGame("b,b,b,b/b,b,b/b,b,b,b/,,/w,w,w,w/w,w,w/w,w,w,w", false);
		assertMove("a3-b4", true, false);
		assertGameState("b,b,b,b/b,b,b/b,b,b,b/,,/w,w,w,w/w,w,w/w,w,w,w", false, false, false);
	}
	
	@Test
	public void blackTriesToMoveWhitesTurn() {
		startGame("b,b,b,b/b,b,b/b,b,b,b/,,/w,w,w,w/w,w,w/w,w,w,w", true);
		assertMove("a5-b4", false, false);
		assertGameState("b,b,b,b/b,b,b/b,b,b,b/,,/w,w,w,w/w,w,w/w,w,w,w", true, false, false);
	}
	
	
	/*******************************
	 * Moving stone of opponent
	 ******************************/
	
	@Test
	public void blackTriesToMoveWhitesSoldier() {
		startGame("b,b,b,b/b,b,b/b,b,b,b/,,/w,w,w,w/w,w,w/w,w,w,w", false);
		assertMove("a3-b4", false, false);
		assertGameState("b,b,b,b/b,b,b/b,b,b,b/,,/w,w,w,w/w,w,w/w,w,w,w", false, false, false);
	}
	
	@Test
	public void whiteTriesToMoveblackSoldier() {
		startGame("b,b,b,b/b,b,b/b,b,b,b/,,/w,w,w,w/w,w,w/w,w,w,w", true);
		assertMove("a5-b4", true, false);
		assertGameState("b,b,b,b/b,b,b/b,b,b,b/,,/w,w,w,w/w,w,w/w,w,w,w", true, false, false);
	}
	
	@Test
	public void blackTriesToMoveWhitesOfficer() {
		startGame("b,b,b,b/b,b,b/b,b,b,b/,,/W,w,w,w/w,w,w/w,w,w,w", false);
		assertMove("a3-b4", false, false);
		assertGameState("b,b,b,b/b,b,b/b,b,b,b/,,/W,w,w,w/w,w,w/w,w,w,w", false, false, false);
	}
	
	@Test
	public void whiteTriesToMoveblackOfficer() {
		startGame("b,b,b,b/b,b,b/B,b,b,b/,,/w,w,w,w/w,w,w/w,w,w,w", true);
		assertMove("a5-b4", true, false);
		assertGameState("b,b,b,b/b,b,b/B,b,b,b/,,/w,w,w,w/w,w,w/w,w,w,w", true, false, false);
	}
	
	
	/*******************************
	 * jump over empty field
	 ******************************/
	
	@Test
	public void blackTriesToJumpOverEmptyField() {
		startGame("b,b,b,b/b,b,b/b,b,b,b/,,/,,,/w,w,w/w,w,w,w", false);
		assertMove("e5-c3", false, false);
		assertGameState("b,b,b,b/b,b,b/b,b,b,b/,,/,,,/w,w,w/w,w,w,w", false, false, false);
	}
	
	@Test
	public void whiteTriesToJumpOverEmptyField() {
		startGame("b,b,b,b/b,b,b/,,,/,,/w,w,w,w/w,w,w/w,w,w,w", true);
		assertMove("e3-c5", true, false);
		assertGameState("b,b,b,b/b,b,b/,,,/,,/w,w,w,w/w,w,w/w,w,w,w", true, false, false);
	}
	
	@Test
	public void blackTriesToJumpOverEmptyFieldWithOfficer() {
		startGame("b,b,b,b/b,b,b/b,b,B,b/,,/,,,/w,w,w/w,w,w,w", false);
		assertMove("e5-c3", false, false);
		assertGameState("b,b,b,b/b,b,b/b,b,B,b/,,/,,,/w,w,w/w,w,w,w", false, false, false);
	}
	
	@Test
	public void whiteTriesToJumpOverEmptyFieldWithOfficer() {
		startGame("b,b,b,b/b,b,b/,,,/,,/w,w,W,w/w,w,w/w,w,w,w", true);
		assertMove("e3-c5", true, false);
		assertGameState("b,b,b,b/b,b,b/,,,/,,/w,w,W,w/w,w,w/w,w,w,w", true, false, false);
	}
	
	/*******************************
	 * jump over own stones
	 ******************************/
	
	@Test
	public void blackTriesToJumpOverBlackStone() {
		startGame("b,b,b,b/b,b,b/b,b,b,b/,b,/,,,/w,w,w/w,w,w,w", false);
		assertMove("e5-c3", false, false);
		assertGameState("b,b,b,b/b,b,b/b,b,b,b/,b,/,,,/w,w,w/w,w,w,w", false, false, false);
	}
	
	@Test
	public void whiteTriesToJumpOverWhiteStone() {
		startGame("b,b,b,b/b,b,b/,,,/,w,/w,w,w,w/w,w,w/w,w,w,w", true);
		assertMove("e3-c5", true, false);
		assertGameState("b,b,b,b/b,b,b/,,,/,w,/w,w,w,w/w,w,w/w,w,w,w", true, false, false);
	}
	
	@Test
	public void blackTriesToJumpOverBlackStoneWithOfficer() {
		startGame("b,b,b,b/b,b,b/,,,/,b,/,,B,/w,w,w/w,w,w,w", false);
		assertMove("e3-c5", false, false);
		assertGameState("b,b,b,b/b,b,b/,,,/,b,/,,B,/w,w,w/w,w,w,w", false, false, false);
	}
	
	@Test
	public void whiteTriesToJumpOverWhiteStoneWithOfficer() {
		startGame("b,b,b,b/b,b,b/,,W,/,w,/,,,/w,w,w/w,w,w,w", true);
		assertMove("e5-c3", true, false);
		assertGameState("b,b,b,b/b,b,b/,,W,/,w,/,,,/w,w,w/w,w,w,w", true, false, false);
	}
	
	/*******************************
	 * move on non-empty fields
	 ******************************/
	
	@Test
	public void blackTriesToJumpOnFieldOwnedByWhite() {
		startGame("b,b,b,b/b,b,b/b,b,b,b/,w,/w,w,w,w/w,w,/w,w,w,w", false);
		assertMove("e5-c3", false, false);
		assertGameState("b,b,b,b/b,b,b/b,b,b,b/,w,/w,w,w,w/w,w,/w,w,w,w", false, false, false);
	}
	
	@Test
	public void whiteTriesToJumpOnFieldOwnedByBlack() {
		startGame("b,b,b,b/b,b,/b,b,b,b/,b,/w,w,w,w/w,w,w/w,w,w,w", true);
		assertMove("e3-c5", true, false);
		assertGameState("b,b,b,b/b,b,/b,b,b,b/,b,/w,w,w,w/w,w,w/w,w,w,w", true, false, false);
	}
	
	@Test
	public void blackTriesToJumpOnFieldOwnedByBlack() {
		startGame("b,b,b,b/b,b,b/b,b,b,b/,w,/w,b,w,w/w,w,/w,w,w,w", false);
		assertMove("e5-c3", false, false);
		assertGameState("b,b,b,b/b,b,b/b,b,b,b/,w,/w,b,w,w/w,w,/w,w,w,w", false, false, false);
	}
	
	@Test
	public void whiteTriesToJumpOnFieldOwnedByWhite() {
		startGame("b,b,b,b/b,b,/b,w,b,b/,b,/w,w,w,w/w,w,w/w,w,w,w", true);
		assertMove("e3-c5", true, false);
		assertGameState("b,b,b,b/b,b,/b,w,b,b/,b,/w,w,w,w/w,w,w/w,w,w,w", true, false, false);
	}
	
	@Test
	public void blackTriesToJumpOnFieldOwnedByWhiteWithOfficer() {
		startGame("b,b,b,b/b,b,b/b,b,B,b/,w,/w,w,w,w/,w,w/w,w,w,w", false);
		assertMove("e5-c3", false, false);
		assertGameState("b,b,b,b/b,b,b/b,b,B,b/,w,/w,w,w,w/,w,w/w,w,w,w", false, false, false);
	}
	
	@Test
	public void whiteTriesToJumpOnFieldOwnedByBlackWithOfficer() {
		startGame("b,b,b,b/b,b,/b,b,b,b/,b,/w,w,W,w/w,w,w/w,w,w,w", true);
		assertMove("e3-c5", true, false);
		assertGameState("b,b,b,b/b,b,/b,b,b,b/,b,/w,w,W,w/w,w,w/w,w,w,w", true, false, false);
	}
	
	@Test
	public void blackTriesToJumpOnFieldOwnedByBlackWithOfficer() {
		startGame("b,b,b,b/b,b,b/b,b,B,b/,w,/w,b,w,w/w,w,/w,w,w,w", false);
		assertMove("e5-c3", false, false);
		assertGameState("b,b,b,b/b,b,b/b,b,B,b/,w,/w,b,w,w/w,w,/w,w,w,w", false, false, false);
	}
	
	@Test
	public void whiteTriesToJumpOnFieldOwnedByWhiteWithOfficer() {
		startGame("b,b,b,b/b,b,/b,w,b,b/,b,/w,W,w,w/w,w,w/w,w,w,w", true);
		assertMove("e3-c5", true, false);
		assertGameState("b,b,b,b/b,b,/b,w,b,b/,b,/w,W,w,w/w,w,w/w,w,w,w", true, false, false);
	}
	
	@Test
	public void blackTriesToNormalMoveOnFieldOwnedByWhite() {
		startGame("b,b,b,b/b,b,b/b,b,b,b/,,w/w,w,w,/w,w,w/w,w,w,w", false);
		assertMove("e5-f4", false, false);
		assertGameState("b,b,b,b/b,b,b/b,b,b,b/,,w/w,w,w,/w,w,w/w,w,w,w", false, false, false);
	}
	
	@Test
	public void whiteTriesToNormalMoveOnFieldOwnedByBlack() {
		startGame("b,b,b,b/b,b,b/b,b,b,/,,b/w,w,w,w/w,w,w/w,w,w,w", true);
		assertMove("e3-f4", true, false);
		assertGameState("b,b,b,b/b,b,b/b,b,b,/,,b/w,w,w,w/w,w,w/w,w,w,w", true, false, false);
	}
	
	@Test
	public void blackTriesToNormalMoveOnFieldOwnedByBlack() {
		startGame("b,b,b,b/b,b,b/b,b,b,/,,b/w,w,w,w/w,w,w/w,w,w,w", false);
		assertMove("e5-f4", false, false);
		assertGameState("b,b,b,b/b,b,b/b,b,b,/,,b/w,w,w,w/w,w,w/w,w,w,w", false, false, false);
	}
	
	@Test
	public void whiteTriesToNormalMoveOnFieldOwnedByWhite() {
		startGame("b,b,b,b/b,b,b/b,b,b,b/,,w/w,w,w,/w,w,w/w,w,w,w", true);
		assertMove("e3-f4", true, false);
		assertGameState("b,b,b,b/b,b,b/b,b,b,b/,,w/w,w,w,/w,w,w/w,w,w,w", true, false, false);
	}
	
	@Test
	public void blackTriesTNormalMoveOnFieldOwnedByWhiteWithOfficer() {
		startGame("b,b,b,b/b,b,b/b,b,B,b/,,w/w,w,w,/w,w,w/w,w,w,w", false);
		assertMove("e5-c3", false, false);
		assertGameState("b,b,b,b/b,b,b/b,b,B,b/,,w/w,w,w,/w,w,w/w,w,w,w", false, false, false);
	}
	
	@Test
	public void whiteTriesToNormalMoveOnFieldOwnedByBlackWithOfficer() {
		startGame("b,b,b,b/b,b,b/b,b,b,/,,b/w,w,W,w/w,w,w/w,w,w,w", true);
		assertMove("e3-c5", true, false);
		assertGameState("b,b,b,b/b,b,b/b,b,b,/,,b/w,w,W,w/w,w,w/w,w,w,w", true, false, false);
	}
	
	@Test
	public void blackTriesTNormalMoveOnFieldOwnedByBlackWithOfficer() {
		startGame("b,b,b,b/b,b,b/b,b,B,b/,,b/w,w,w,/w,w,w/w,w,w,w", false);
		assertMove("e5-c3", false, false);
		assertGameState("b,b,b,b/b,b,b/b,b,B,b/,,b/w,w,w,/w,w,w/w,w,w,w", false, false, false);
	}
	
	@Test
	public void whiteTriesToNormalMoveOnFieldOwnedByWhiteWithOfficer() {
		startGame("b,b,b,b/b,b,b/b,b,b,/,,w/w,w,W,w/w,w,w/w,w,w,w", true);
		assertMove("e3-c5", true, false);
		assertGameState("b,b,b,b/b,b,b/b,b,b,/,,w/w,w,W,w/w,w,w/w,w,w,w", true, false, false);
	}
	
	/*******************************
	 * non-diagonal move
	 ******************************/
	
	@Test
	public void upwards() {
		startGame("b,b,b,b/b,b,b/b,b,,/,,/,,w,/w,w,w/w,w,w,w", true);
		assertMove("e3-e5", true, false);
		assertGameState("b,b,b,b/b,b,b/b,b,,/,,/,,w,/w,w,w/w,w,w,w", true, false, false);
	}
	
	@Test
	public void sidewards() {
		startGame("b,b,b,b/b,b,b/,,b,/,,w/,,,/w,w,w/w,w,w,w", false);
		assertMove("e5-c5", false, false);
		assertGameState("b,b,b,b/b,b,b/,,b,/,,w/,,,/w,w,w/w,w,w,w", false, false, false);
	}
	
	@Test
	public void upwardsSidewardsWhite() {
		startGame("b,b,b,b/b,b,b/b,b,,/,,/,,w,/w,w,w/w,w,w,w", true);
		assertMove("e3-b4", true, false);
		assertGameState("b,b,b,b/b,b,b/b,b,,/,,/,,w,/w,w,w/w,w,w,w", true, false, false);
	}
	
	@Test
	public void upwardsSidewardsBlack() {
		startGame("b,b,b,b/b,b,b/b,b,b,b/,,/,,w,/w,w,w/w,w,w,w", false);
		assertMove("e5-b4", false, false);
		assertGameState("b,b,b,b/b,b,b/b,b,b,b/,,/,,w,/w,w,w/w,w,w,w", false, false, false);
	}
	
	@Test
	public void downwardsSidewardsWhiteWithOfficer() {
		startGame("b,b,b,b/b,b,b/b,b,W,/,b,b/w,w,w,w/w,w,w/w,w,w,w", true);
		assertMove("e5-b4", true, false);
		assertGameState("b,b,b,b/b,b,b/b,b,W,/,b,b/w,w,w,w/w,w,w/w,w,w,w", true, false, false);
	}
	
	@Test
	public void downwardsSidewardsWhiteWithOfficerWithoutPossibleJumps() {
		startGame("b,b,b,b/b,b,b/b,b,W,/,,/w,w,w,w/w,w,w/w,w,w,w", true);
		assertMove("e5-b4", true, false);
		assertGameState("b,b,b,b/b,b,b/b,b,W,/,,/w,w,w,w/w,w,w/w,w,w,w", true, false, false);
	}
	
	@Test
	public void upwardsSidewardsBlackWithOfficer() {
		startGame("b,b,b,b/b,b,b/b,b,b,b/w,w,/,B,w,/w,w,w/w,w,w,w", false);
		assertMove("c3-f4", false, false);
		assertGameState("b,b,b,b/b,b,b/b,b,b,b/w,w,/,B,w,/w,w,w/w,w,w,w", false, false, false);
	}
	
	@Test
	public void upwardsSidewardsBlackWithOfficerWithoutPossibleJumps() {
		startGame("b,b,b,b/b,b,b/b,b,b,b/,,/,B,w,/w,w,w/w,w,w,w", false);
		assertMove("c3-f4", false, false);
		assertGameState("b,b,b,b/b,b,b/b,b,b,b/,,/,B,w,/w,w,w/w,w,w,w", false, false, false);
	}
	
	/*******************************
	 * move in wrong direction
	 ******************************/
	
	@Test
	public void whiteNormalMovesBackwardsRight() {
		startGame("b,b,b,b/b,b,b/b,b,b,/,,w/,,,/w,w,w/w,w,w,w", true);
		assertMove("f4-e3", true, false);
		assertGameState("b,b,b,b/b,b,b/b,b,b,/,,w/,,,/w,w,w/w,w,w,w", true, false, false);
	}
	
	@Test
	public void whiteNormalMovesBackwardsLeft() {
		startGame("b,b,b,b/b,b,b/b,b,b,/,,w/,,,/w,w,w/w,w,w,w", true);
		assertMove("f4-g3", true, false);
		assertGameState("b,b,b,b/b,b,b/b,b,b,/,,w/,,,/w,w,w/w,w,w,w", true, false, false);
	}
	
	@Test
	public void blackNormalMovesBackwards() {
		startGame("b,b,b,b/b,b,b/,,,/,,b/,,,/w,w,w/w,w,w,w", false);
		assertMove("f4-e5", false, false);
		assertGameState("b,b,b,b/b,b,b/,,,/,,b/,,,/w,w,w/w,w,w,w", false, false, false);
	}
	
	@Test
	public void whiteJumpsBackwards() {
		startGame("b,b,b,b/b,b,b/b,b,b,/,,w/,,b,/,,/w,w,w,w", true);
		assertMove("f4-d2", true, false);
		assertGameState("b,b,b,b/b,b,b/b,b,b,/,,w/,,b,/,,/w,w,w,w", true, false, false);
	}
	
	@Test
	public void blackJumpsBackwards() {
		startGame("b,b,b,b/,,/,,w,/,,b/,,,/w,w,w/w,w,w,w", false);
		assertMove("f4-d6", false, false);
		assertGameState("b,b,b,b/,,/,,w,/,,b/,,,/w,w,w/w,w,w,w", false, false, false);
	}
	
	/*******************************
	 * try normal move while jump possible
	 ******************************/
	
	@Test
	public void whiteTriesNormalMoveWhileJumpPossible() {
		startGame("b,b,b,b/b,b,b/b,,,/b,,/w,w,w,w/w,w,w/w,w,w,w", true);
		assertMove("e3-f4", true, false);
		assertGameState("b,b,b,b/b,b,b/b,,,/b,,/w,w,w,w/w,w,w/w,w,w,w", true, false, false);
	}
	
	@Test
	public void blackTriesNormalMoveWhileJumpPossible() {
		startGame("b,b,b,b/b,b,b/b,b,b,b/w,,/,,,/w,w,w/w,w,w,w", false);
		assertMove("e5-f4", false, false);
		assertGameState("b,b,b,b/b,b,b/b,b,b,b/w,,/,,,/w,w,w/w,w,w,w", false, false, false);
	}
	
	@Test
	public void whiteTriesNormalMoveWithOfficerWhileJumpPossible() {
		startGame("b,b,b,b/b,b,b/b,,,/b,,/w,w,W,w/w,w,w/w,w,w,w", true);
		assertMove("e3-f4", true, false);
		assertGameState("b,b,b,b/b,b,b/b,,,/b,,/w,w,W,w/w,w,w/w,w,w,w", true, false, false);
	}
	
	@Test
	public void blackTriesNormalMoveWithOfficerWhileJumpPossible() {
		startGame("b,b,b,b/b,b,b/b,b,B,b/w,,/,,,/w,w,w/w,w,w,w", false);
		assertMove("e5-f4", false, false);
		assertGameState("b,b,b,b/b,b,b/b,b,B,b/w,,/,,,/w,w,w/w,w,w,w", false, false, false);
	}
	
	/*******************************
	 * try to move from empty field
	 ******************************/
	
	@Test
	public void whiteNormalMoveFormEmptyField() {
		startGame("b,b,b,b/b,b,b/,,,/b,,/,,,/w,w,w/w,w,w,w", true);
		assertMove("e3-f4", true, false);
		assertGameState("b,b,b,b/b,b,b/,,,/b,,/,,,/w,w,w/w,w,w,w", true, false, false);
	}
	
	@Test
	public void blackNormalMoveFormEmptyFiel() {
		startGame("b,b,b,b/b,b,b/,,,/w,,/,,,/w,w,w/w,w,w,w", false);
		assertMove("e5-f4", false, false);
		assertGameState("b,b,b,b/b,b,b/,,,/w,,/,,,/w,w,w/w,w,w,w", false, false, false);
	}
	
	@Test
	public void whiteJumpFormEmptyField() {
		startGame("b,b,b,b/b,b,b/,,,/,b,/,,,/w,w,w/w,w,w,w", true);
		assertMove("e3-c5", true, false);
		assertGameState("b,b,b,b/b,b,b/,,,/,b,/,,,/w,w,w/w,w,w,w", true, false, false);
	}
	
	@Test
	public void blackJumpFormEmptyField() {
		startGame("b,b,b,b/b,b,b/,,,/,w,/,,,/w,w,w/w,w,w,w", false);
		assertMove("e5-c3", false, false);
		assertGameState("b,b,b,b/b,b,b/,,,/,w,/,,,/w,w,w/w,w,w,w", false, false, false);
	}
	
	/*******************************
	 * valid normal move
	 ******************************/
	
	@Test
	public void whiteNormalMoveRight() {
		startGame("b,b,b,b/b,b,b/b,b,b,b/,,/w,w,w,w/w,w,w/w,w,w,w", true);
		assertMove("e3-f4", true, true);
		assertGameState("b,b,b,b/b,b,b/b,b,b,b/,,w/w,w,,w/w,w,w/w,w,w,w", false, false, false);
	}
	
	@Test
	public void whiteNormalMoveLeft() {
		startGame("b,b,b,b/b,b,b/b,b,b,b/,,/w,w,w,w/w,w,w/w,w,w,w", true);
		assertMove("c3-b4", true, true);
		assertGameState("b,b,b,b/b,b,b/b,b,b,b/w,,/w,,w,w/w,w,w/w,w,w,w", false, false, false);
	}
	
	@Test
	public void blackNormalMoveRight() {
		startGame("b,b,b,b/b,b,b/b,b,b,b/,,/w,w,w,w/w,w,w/w,w,w,w", false);
		assertMove("e5-f4", false, true);
		assertGameState("b,b,b,b/b,b,b/b,b,,b/,,b/w,w,w,w/w,w,w/w,w,w,w", true, false, false);
	}
	
	@Test
	public void blackNormalMoveLeft() {
		startGame("b,b,b,b/b,b,b/b,b,b,b/,,/w,w,w,w/w,w,w/w,w,w,w", false);
		assertMove("c5-b4", false, true);
		assertGameState("b,b,b,b/b,b,b/b,,b,b/b,,/w,w,w,w/w,w,w/w,w,w,w", true, false, false);
	}
	
	@Test
	public void whiteNormalMoveBackwardsWithOfficerLeft() {
		startGame("b,b,b,b/b,b,b/b,b,b,b/,W,/,,,/w,w,w/w,w,w,w", true);
		assertMove("d4-c3", true, true);
		assertGameState("b,b,b,b/b,b,b/b,b,b,b/,,/,W,,/w,w,w/w,w,w,w", false, false, false);
	}
	
	@Test
	public void whiteNormalMoveBackwardsWithOfficerRight() {
		startGame("b,b,b,b/b,b,b/b,b,b,b/,W,/,,,/w,w,w/w,w,w,w", true);
		assertMove("d4-e3", true, true);
		assertGameState("b,b,b,b/b,b,b/b,b,b,b/,,/,,W,/w,w,w/w,w,w,w", false, false, false);
	}
	
	@Test
	public void blackNormalMoveBackwardsWithOfficerLeft() {
		startGame("b,b,b,b/b,b,b/,,,/,,B/w,w,w,w/w,w,w/w,w,w,w", false);
		assertMove("f4-e5", false, true);
		assertGameState("b,b,b,b/b,b,b/,,B,/,,/w,w,w,w/w,w,w/w,w,w,w", true, false, false);
	}
	
	@Test
	public void blackNormalMoveBackwardsWithOfficerRight() {
		startGame("W,w,w,W/w,b,w/,,,/B,,/w,w,w,w/w,w,w/W,w,w,W", false);
		assertMove("b4-c5", false, true);
		assertGameState("W,w,w,W/w,b,w/,B,,/,,/w,w,w,w/w,w,w/W,w,w,W", true, false, false);
	}
	
	@Test
	public void whiteMovesBlacksOfficerCanMovesLeftUpwards() {
		startGame("b,b,B,b/w,w,w/w,w,w,w/,,/,,b,w/,w,/,,w,B", true);
		assertMove("d2-f4", true, true);
		assertGameState("b,b,B,b/w,w,w/w,w,w,w/,,wb/,,,w/,,/,,w,B", false, false, true);
	}
	
	/*******************************
	 * valid jump
	 ******************************/
	
	@Test
	public void whiteJumpRight() {
		startGame("b,b,b,b/b,b,b/,,,/,,/,,,/b,,/w,w,w,w", true);
		assertMove("a1-c3", true, true);
		assertGameState("b,b,b,b/b,b,b/,,,/,,/,wb,,/,,/,w,w,w", false, false, false);
	}

	@Test
	public void whiteJumpLeft() {
		startGame("b,b,b,b/b,b,b/,,,/,,/,,,/,,b/w,w,w,w", true);
		assertMove("g1-e3", true, true);
		assertGameState("b,b,b,b/b,b,b/,,,/,,/,,wb,/,,/w,w,w,", false, false, false);
	}
	
	@Test
	public void blackJumpRight() {
		startGame("b,b,b,b/w,,/,,,/,,/,,,/w,w,w/w,w,w,w", false);
		assertMove("a7-c5", false, true);
		assertGameState(",b,b,b/,,/,bw,,/,,/,,,/w,w,w/w,w,w,w", true, false, false);
	}
	
	@Test
	public void blackJumpLeft() {
		startGame("b,b,b,b/,,w/,,,/,,/,,,/w,w,w/w,w,w,w", false);
		assertMove("g7-e5", false, true);
		assertGameState("b,b,b,/,,/,,bw,/,,/,,,/w,w,w/w,w,w,w", true, false, false);
	}
	
	@Test
	public void whiteJumpBackwardsWithOfficerRight() {
		startGame("b,b,b,b/W,,/,b,,/,,/,,,/w,w,w/w,w,w,w", true);
		assertMove("b6-d4", true, true);
		assertGameState("b,b,b,b/,,/,,,/,Wb,/,,,/w,w,w/w,w,w,w", false, false, false);
	}
	
	@Test
	public void whiteJumpBackwardsWithOfficerLeft() {
		startGame("b,b,b,b/,,W/,,b,/,,/,,,/w,w,w/w,w,w,w", true);
		assertMove("f6-d4", true, true);
		assertGameState("b,b,b,b/,,/,,,/,Wb,/,,,/w,w,w/w,w,w,w", false, false, false);
	}
	
	@Test
	public void blackJumpBackwardsWithOfficerRight() {
		startGame("b,b,b,b/b,b,b/,,,/,,/,w,,/B,,/w,w,w,w", false);
		assertMove("b2-d4", false, true);
		assertGameState("b,b,b,b/b,b,b/,,,/,Bw,/,,,/,,/w,w,w,w", true, false, false);
	}
	
	@Test
	public void blackJumpBackwardsWithOfficerLeft() {
		startGame("b,b,b,b/b,b,b/,,,/,,/,,w,/,,B/w,w,w,w", false);
		assertMove("f2-d4", false, true);
		assertGameState("b,b,b,b/b,b,b/,,,/,Bw,/,,,/,,/w,w,w,w", true, false, false);
	}

	/*******************************
	 * player jumps and can do another jump
	 ******************************/
	
	@Test
	public void whiteJumpCombi() {
		startGame("b,b,b,b/b,b,b/,,,/b,,/,,,/b,,/w,w,w,w", true);
		assertMove("a1-c3", true, true);
		assertGameState("b,b,b,b/b,b,b/,,,/b,,/,wb,,/,,/,w,w,w", true, false, false);
		assertMove("c3-a5", true, true);
		assertGameState("b,b,b,b/b,b,b/wbb,,,/,,/,,,/,,/,w,w,w", false, false, false);
	}
	
	@Test
	public void whiteJumpblackTriesJump() {
		startGame("b,b,b,b/b,b,b/,,,/b,,/,,,/b,,/w,w,w,w", true);
		assertMove("a1-c3", true, true);
		assertGameState("b,b,b,b/b,b,b/,,,/b,,/,wb,,/,,/,w,w,w", true, false, false);
		assertMove("b4-d2", false, false);
		assertGameState("b,b,b,b/b,b,b/,,,/b,,/,wb,,/,,/,w,w,w", true, false, false);
	}

	@Test
	public void blackJumpCombi() {
		startGame("b,b,b,b/,,w/,,,/,w,/,,,/w,w,w/w,w,w,w", false);
		assertMove("g7-e5", false, true);
		assertGameState("b,b,b,/,,/,,bw,/,w,/,,,/w,w,w/w,w,w,w", false, false, false);
		assertMove("e5-c3", false, true);
		assertGameState("b,b,b,/,,/,,,/,,/,bww,,/w,w,w/w,w,w,w", true, false, false);
	}
	
	@Test
	public void blackJumpWhiteTriesJump() {
		startGame("b,b,b,b/,,w/,,,/,w,/,,,/w,w,w/w,w,w,w", false);
		assertMove("g7-e5", false, true);
		assertGameState("b,b,b,/,,/,,bw,/,w,/,,,/w,w,w/w,w,w,w", false, false, false);
		assertMove("d4-f6", true, false);
		assertGameState("b,b,b,/,,/,,bw,/,w,/,,,/w,w,w/w,w,w,w", false, false, false);
	}
	
	@Test
	public void whiteJumpCombiWithOfficer() {
		startGame("b,b,b,b/b,b,b/,,,/,,/,,,/B,b,/W,w,,w", true);
		assertMove("a1-c3", true, true);
		assertGameState("b,b,b,b/b,b,b/,,,/,,/,WB,,/,b,/,w,,w", true, false, false);
		assertMove("c3-e1", true, true);
		assertGameState("b,b,b,b/b,b,b/,,,/,,/,,,/,,/,w,WBb,w", false, false, false);
	}
	
	@Test
	public void blackJumpCombiWithOfficer() {
		startGame("b,b,b,b/,,/w,w,w,w/,,/w,w,w,w/,B,/w,w,w,w", false);
		assertMove("d2-f4", false, true);
		assertGameState("b,b,b,b/,,/w,w,w,w/,,Bw/w,w,,w/,,/w,w,w,w", false, false, false);
		assertMove("f4-d6", false, true);
		assertGameState("b,b,b,b/,Bww,/w,w,,w/,,/w,w,,w/,,/w,w,w,w", false, false, false);
		assertMove("d6-b4", false, true);
		assertGameState("b,b,b,b/,,/w,,,w/Bwww,,/w,w,,w/,,/w,w,w,w", false, false, false);
		assertMove("b4-d2", false, true);
		assertGameState("b,b,b,b/,,/w,,,w/,,/w,,,w/,Bwwww,/w,w,w,w", true, false, false);
		assertMove("c1-e3", true, true);
		assertGameState("b,b,b,b/,,/w,,,w/,,/w,,wB,w/,wwww,/w,,w,w", false, false, false);
	}
	
	/*******************************
	 * promotion & winning
	 ******************************/
	
	@Test
	public void whiteGetsPromotion() {
		startGame("b,,b,b/b,b,b/w,,,/b,,/,,,/b,,/w,w,w,w", true);
		assertMove("a5-c7", true, true);
		assertGameState("b,Wb,b,b/,b,b/,,,/b,,/,,,/b,,/w,w,w,w", true, false, false);
		assertMove("a1-c3", true, true);
		assertGameState("b,Wb,b,b/,b,b/,,,/b,,/,wb,,/,,/,w,w,w", true, false, false);
	}
	
	@Test
	public void blackGetsPromotionAndWins() {
		startGame("b,b,b,b/,,/,,,/,,/b,b,b,b/,w,/,,,", false);
		assertMove("c3-e1", false, true);
		assertGameState("b,b,b,b/,,/,,,/,,/b,,b,b/,,/,,Bw,", false, true, false);
	}
	
	@Test
	public void whiteWinsBlacksOfficerInLastLine() {
		startGame("b,b,B,b/w,w,w/w,w,w,w/,,/,,b,w/,w,b/,,w,B", true);
		assertMove("d2-f4", true, true);
		assertGameState("b,b,B,b/w,w,w/w,w,w,w/,,wb/,,,w/,,b/,,w,B", true, true, true);
	}
	
	@Test
	public void whiteOfficerPlacement1() {
		startGame("W,w,w,W/w,,w/,B,,/b,b,b/b,b,b,b/w,w,w/W,w,w,W", false);
		assertMove("c5-d6", false, true);
		assertGameState("W,w,w,W/w,B,w/,,,/b,b,b/b,b,b,b/w,w,w/W,w,w,W", false, true, false);
	}
	
	@Test
	public void whiteOfficerPlacement2() {
		startGame("W,w,w,W/,,/,B,,/b,b,b/b,b,b,b/w,w,w/W,w,w,W", false);
		assertMove("c5-d6", false, true);
		assertGameState("W,w,w,W/,B,/,,,/b,b,b/b,b,b,b/w,w,w/W,w,w,W", true, false, false);
	}
	
	@Test
	public void whiteOfficerPlacement3() {
		startGame("w,w,w,W/,,/,B,,/b,b,b/b,b,b,b/w,w,w/W,w,w,W", false);
		assertMove("c5-d6", false, true);
		assertGameState("w,w,w,W/,B,/,,,/b,b,b/b,b,b,b/w,w,w/W,w,w,W", true, false, false);
	}
	
	@Test
	public void blackOfficerPlacement1() {
		startGame(",,,/,W,/w,w,w,w/w,w,w/b,b,b,b/w,w,w/B,w,w,B", true);
		assertMove("d6-c7", true, true);
		assertGameState(",W,,/,,/w,w,w,w/w,w,w/b,b,b,b/w,w,w/B,w,w,B", true, true, true);
	}
	
	@Test
	public void blackOfficerPlacement2() {
		startGame(",,,/,W,/w,w,w,w/w,w,w/w,w,w,w/,w,w/B,w,w,B", true);
		assertMove("d6-c7", true, true);
		assertGameState(",W,,/,,/w,w,w,w/w,w,w/w,w,w,w/,w,w/B,w,w,B", false, false, true);
	}
	
	@Test
	public void exampleTest2() {
		startGame("b,b,b,b/b,b,b/b,b,W,b/,b,/w,,w,w/w,w,w/w,w,w,w", true);
		assertMove("e5-c3", true, true);
		assertGameState("b,b,b,b/b,b,b/b,b,,b/,,/w,Wb,w,w/w,w,w/w,w,w,w", false, false, false);
	}
	
	@Test
	public void exampleTest3() {
		startGame("b,b,b,b/b,b,b/b,,,b/,b,/w,w,w,w/w,w,w/w,w,w,w", true);
		assertMove("c3-e5", true, true);
		assertGameState("b,b,b,b/b,b,b/b,,wb,b/,,/w,,w,w/w,w,w/w,w,w,w", false, false, false);
	}
	
	@Test
	public void exampleTest4() {
		startGame("b,b,b,b/b,b,b/b,b,b,b/,w,/w,,,w/w,w,w/w,w,w,w", false);
		assertMove("c5-e3", false, true);
		assertGameState("b,b,b,b/b,b,b/b,,b,b/,,/w,,bw,w/w,w,w/w,w,w,w", true, false, false);
	}
}

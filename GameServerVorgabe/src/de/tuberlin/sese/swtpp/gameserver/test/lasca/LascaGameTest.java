package de.tuberlin.sese.swtpp.gameserver.test.lasca;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.tuberlin.sese.swtpp.gameserver.control.GameController;
import de.tuberlin.sese.swtpp.gameserver.model.Player;
import de.tuberlin.sese.swtpp.gameserver.model.User;
import de.tuberlin.sese.swtpp.gameserver.model.lasca.LascaGame;

public class LascaGameTest {
	
	User user1 = new User("Alice", "alice");
	User user2 = new User("Bob", "bob");
	User user3 = new User("Eve", "eve");
	
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
	
	public void startGame() {
		controller.joinGame(user2);
		blackPlayer = game.getPlayer(user2);
	}

	
	@Test
	public void testWaitingGame() {
		assertTrue(game.getStatus().equals("Wait"));
		assertTrue(game.gameInfo().equals(""));
	}
	
	@Test
	public void testGameStarted() {
		assertEquals(controller.joinGame(user2), game.getGameID());
		assertEquals(game.addPlayer(new Player(user3, game)), false); // no third player
		assertEquals(game.getStatus(), "Started");
		assertTrue(game.gameInfo().equals(""));
		assertTrue(game.isWhiteNext());
		assertFalse(game.didWhiteDraw());
		assertFalse(game.didBlackDraw());
		assertFalse(game.whiteGaveUp());
		assertFalse(game.blackGaveUp());
	}

	@Test
	public void testSetNextPlayer() {
		startGame();
		
		game.setNextPlayer(blackPlayer);
		
		assertFalse(game.isWhiteNext());
	}

	
	@Test
	public void testCallDraw() {	
		// call draw before start
		assertFalse(game.callDraw(whitePlayer));

		startGame();
		
		controller.callDraw(user1, game.getGameID());
		assertTrue(game.didWhiteDraw());
		assertFalse(game.didBlackDraw());
		assertEquals(game.gameInfo(), "white called draw");
		
		controller.callDraw(user2, game.getGameID());
		assertTrue(game.didBlackDraw());

		assertEquals(game.getStatus(), "Draw");
		assertEquals(game.gameInfo(), "draw game");
		
		// call draw after finish
		assertFalse(game.callDraw(whitePlayer));
	}
	
	@Test
	public void testCallDrawBlack() {
		startGame();
		
		controller.callDraw(user2, game.getGameID());
		assertFalse(game.didWhiteDraw());
		assertTrue(game.didBlackDraw());
		assertEquals(game.gameInfo(), "black called draw");
	}

	@Test
	public void testGiveUp() {
		// try before start 
		assertFalse(game.giveUp(whitePlayer));
		assertFalse(game.giveUp(blackPlayer));
		
		startGame();
		
		controller.giveUp(user1, game.getGameID());
		
		assertEquals(game.getStatus(), "Surrendered");
		assertEquals(game.gameInfo(), "white gave up");
		
		// try after finish
		assertFalse(game.giveUp(whitePlayer));
		assertFalse(game.giveUp(blackPlayer));

	}
	
	@Test
	public void testGiveUpBlack() {
		startGame();
		
		controller.giveUp(user2, game.getGameID());
		
		assertEquals(game.getStatus(), "Surrendered");
		assertEquals(game.gameInfo(), "black gave up");
	}

	@Test
	public void testGetMinPlayers() {
		assertEquals(game.getMinPlayers(), 2);
	}
	
	@Test
	public void testGetMaxPlayers() {
		assertEquals(game.getMaxPlayers(), 2);
	}

	@Test
	public void testFinish() {
		// test before start
		assertFalse(game.finish(whitePlayer));

		startGame();
		
		assertTrue(game.finish(whitePlayer));
		assertEquals(game.getStatus(), "Finished");
		assertEquals(game.gameInfo(), "white won");
		
		// test after finish
		assertFalse(game.finish(whitePlayer));
	}
	
	@Test
	public void testFinishBlack() {
		startGame();
		
		assertTrue(game.finish(blackPlayer));
		assertEquals(game.getStatus(), "Finished");
		assertEquals(game.gameInfo(), "black won");
	}

	@Test
	public void testError() {
		assertFalse(game.isError());
		game.setError(true);
		assertTrue(game.isError());
		assertEquals(game.getStatus(), "Error");
		game.setError(false);
		assertFalse(game.isError());
	}
}

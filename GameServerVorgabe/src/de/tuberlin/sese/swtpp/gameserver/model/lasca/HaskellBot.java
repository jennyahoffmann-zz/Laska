package de.tuberlin.sese.swtpp.gameserver.model.lasca;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import de.tuberlin.sese.swtpp.gameserver.model.Bot;
import de.tuberlin.sese.swtpp.gameserver.model.Move;

/**
 * Bot implementation that launches Haskell program to retrieve single next move.
 *
 */
public class HaskellBot extends Bot implements Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1371646871809169057L;


	private String path;    // path of bot executable
	private LascaGame game; // the game this bot plays
	
	public HaskellBot(LascaGame game, String path) {
		super("HaskellBot");
		this.game = game;
		this.path = path;
		
		// start a bot poll thread
		new Thread(this).start();
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public String getName() {
		return "HaskellBot";
	}
	
	@Override
	public void run() {
		// run until game is finished
		while (!game.isFinished()) {
			try {
				// check every second for changes
				Thread.sleep(1000);
				
				// do move when it's my turn
				if (game.isItMyTurn(this)) {
					executeMove();						
				}
				
			} catch (InterruptedException e) {
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	private void executeMove() throws IOException, InterruptedException {
		// last move was also done by this bot
		// pass last move as an additional parameter
		String lastComboStep = "";
		if (game.getHistory().size() > 0 ) {
			Move lastMove = game.getHistory().get(game.getHistory().size()-1);
			if (lastMove.getPlayer() == game.getPlayer(this)) {
				lastComboStep = " " + lastMove.getMove();
			}
		}
		
		// Execute command
		String command = path + " " + game.getState() + " " + (game.isWhiteNext()? "w": "b") + lastComboStep;
		Process child = Runtime.getRuntime().exec(command);

		// get command line response (wait for bot to finish)
		BufferedReader bri = new BufferedReader
		        (new InputStreamReader(child.getInputStream()));
		
		child.waitFor();
		
		// get result into single string
		String result = "";
		while (bri.ready()) result += bri.readLine();
		
		// give up when bot didn't find a move (but should have)
		if (result == "") game.giveUp(game.getPlayer(this));
		else {
			if (!game.tryMove(result, game.getPlayer(this))) {
				// give up when move was illegal
				game.giveUp(game.getPlayer(this));
			} 
		}
	}

}
